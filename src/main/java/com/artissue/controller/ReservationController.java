package com.artissue.controller;

import com.artissue.model.*;
import com.artissue.service.MessageService;
import com.artissue.service.QrCodeService;
import com.google.zxing.WriterException;
import jakarta.servlet.http.HttpSession;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/reserve")
public class ReservationController {

    @Value("${toss.api.client-key}")
    private String clientKey;

    @Autowired
    private ExhibitionMapper exhibitionMapper;
    @Autowired
    private ReservationMapper reservationMapper;
    @Autowired
    private MessageService massageService;
    @Autowired
    private QrCodeService qrCodeService;


    @GetMapping("/open")
    public String openReserve(@RequestParam("No") int exhibition_key, Model model){

        model.addAttribute("ExhiDTO", this.exhibitionMapper.getExhibitionCont(exhibition_key))
                .addAttribute("PriceList", this.reservationMapper.getPriceList(exhibition_key));

        return "reservation/popup";
    }

    @PostMapping("/sendReserve")
    public String reservation(ReservationDTO reserveDTO, HttpSession session, Model model) {
        int addResult = 0;
        int totalPrice = 0;

        // 값 insert
        String[] optionStr = reserveDTO.getReservation_option().split(",");
        String[] countStr = reserveDTO.getReservation_count_str().split(",");
        String[] priceStr = reserveDTO.getReservation_price_str().split(",");

        ReservationDTO insertDTO = new ReservationDTO();
        MemberDTO memberDTO = (MemberDTO)session.getAttribute("mDTO");

        Date today = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");

        String Reservation_id = memberDTO.getMember_id().substring(0,2).toUpperCase()+dateFormat.format(today);

        insertDTO.setExhibition_key(reserveDTO.getExhibition_key());
        insertDTO.setMember_key(memberDTO.getMember_key());
        insertDTO.setReservation_id(Reservation_id);

        for (int i = 0; i < optionStr.length; i++) {
            if(Integer.parseInt(priceStr[i]) > 0){
                insertDTO.setReservation_option(optionStr[i]);
                insertDTO.setReservation_count(Integer.parseInt(countStr[i].split("/")[0]));
                insertDTO.setReservation_price(Integer.parseInt(priceStr[i]));
                totalPrice += Integer.parseInt(priceStr[i]);

                addResult = reservationMapper.addReservation(insertDTO);
            }
        }

        // 결제 페이지 이동 -> 결제
        model.addAttribute("exhiDTO", this.exhibitionMapper.getExhibitionCont(insertDTO.getExhibition_key()))
                .addAttribute("reserveList", this.reservationMapper.getReserveList(insertDTO.getReservation_id()))
                .addAttribute("paymentRequest", new PaymentRequestDTO())
                .addAttribute("clientKey", clientKey)
                .addAttribute("totalPrice", totalPrice);

        return "tosspay/paymentForm";
    }

    @GetMapping("/success")
    public String reserveSuccess(@RequestParam("id") String reservation_id, HttpSession session, Model model) throws IOException, WriterException {
        MemberDTO memberDTO = (MemberDTO)session.getAttribute("mDTO");

        HashMap<String,String> map = new HashMap<>();
        map.put("member_key", String.valueOf(memberDTO.getMember_key()));
        map.put("reservation_id", reservation_id);

        this.reservationMapper.deleteReserveFail(map);
        this.reservationMapper.updateReservePay(reservation_id);

        List<ReservationDTO> reserveList= this.reservationMapper.getReserveList(reservation_id);
        int exhibition_key = reserveList.get(0).getExhibition_key();
        int totalPrice = 0;
        String massage = "";

        ExhibitionDTO exhiDTO = this.exhibitionMapper.getExhibitionCont(exhibition_key);

        for(int i=0;i<reserveList.size();i++){
            totalPrice += reserveList.get(i).getReservation_price();

            //옵션 문구 출력
            massage += reserveList.get(i).getReservation_option()+" "+
                    reserveList.get(i).getReservation_count()+"매 "+
                    reserveList.get(i).getReservation_price()+"원\n";

        }

        // QR 코드 생성 및 모델에 추가
        String link = "http://localhost:8181/ticket?T="+reservation_id; //링크주소 변경 예정
        // 변수에 생성된 QR 코드 이미지 데이터가 저장
        byte[] qrCodeBytes = qrCodeService.generateQrCode(link, reservation_id);
        // byte 배열 형태로 저장된 QR 코드 이미지 데이터를 Base64 인코딩하여 문자열 형태로 변환
        String qrCode = Base64.getEncoder().encodeToString(qrCodeBytes);
        // qrCode 변숫값을 qrCode라는 이름으로 Model 객체에 추가. Model 객체에 데이터를 추가하면 Spring MVC는 해당 데이터를 View로 전달하여 화면에 렌더링

        // 예매내역 및 예약 확인 페이지 링크 문자 발송
        String memberPhone = memberDTO.getMember_phone();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //원하는 데이터 포맷 지정
        String start_date = simpleDateFormat.format(exhiDTO.getExhibition_start_date());
        String end_date = simpleDateFormat.format(exhiDTO.getExhibition_end_date());
        //지정한 포맷으로 변환

        String price = String.format("%,d", totalPrice);

        String verificationCode = "\n[ART ISSUE]\n" +
                memberDTO.getMember_name()+"고객님 예매가 완료되었어요\n\n" +
                "예약번호 : "+reserveList.get(0).getReservation_id()+"\n\n" +
                exhiDTO.getExhibition_title()+"\n\n" +
                massage+"\n" +
                "일시 : "+start_date+" ~ "+end_date+"\n" +
                "장소 : "+exhiDTO.getExhibition_place()+"\n" +
                "총 결제금액 : "+price+"원\n\n\n" +
                "나의 예매티켓보기\n" + link;

        //SingleMessageSentResponse response = massageService.sendReserve(memberPhone, verificationCode);
        System.out.println("문자발송");

        //예매 확인 페이지 구현 및 페이지로 이동
        model.addAttribute("exhiDTO", exhiDTO)
            .addAttribute("totalPrice", totalPrice)
            .addAttribute("reserveList", reserveList);

        return "tosspay/paymentSuccess";
    }

}
