package com.artissue.controller;

import com.artissue.model.*;
import com.artissue.service.MessageService;
import jakarta.servlet.http.HttpSession;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
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
    public String reserveSuccess(@RequestParam("id") String exhibition_id, HttpSession session, Model model) {

        this.reservationMapper.updateReservePay(exhibition_id);
        List<ReservationDTO> reserveList= this.reservationMapper.getReserveList(exhibition_id);
        int exhibition_key = reserveList.get(0).getExhibition_key();
        int totalPrice = 0;

        ExhibitionDTO exhiDTO = this.exhibitionMapper.getExhibitionCont(exhibition_key);
        MemberDTO memberDTO = (MemberDTO)session.getAttribute("mDTO");
        for(int i=0;i<reserveList.size();i++){
            totalPrice += reserveList.get(i).getReservation_price();
        }
        // QR 생성

        // 예매내역 및 예약 확인 페이지 링크 문자 발송

        String memberPhone = memberDTO.getMember_phone();
        String link = "http://localhost:8181/";

        String verificationCode = "[Art Issue]\n\n" +
                memberDTO.getMember_name()+"고객님 예매가 완료되었어요\n" +
                "예약번호 : "+reserveList.get(0).getReservation_id()+"\n" +
                "전시회명 : "+exhiDTO.getExhibition_title()+"\n" +
                "일시 : "+exhiDTO.getExhibition_start_date()+"~"+exhiDTO.getExhibition_end_date()+"\n" +
                "결제금액 : "+totalPrice+"\n\n" +
                "나의 예매내역보기\n" + link;
/*
        SingleMessageSentResponse response = massageService.sendReserve(memberPhone, verificationCode);

        System.out.println(response);*/
        System.out.println("문자발송");

        //예매 확인 페이지 구현 및 페이지로 이동
        model.addAttribute("exhiDTO", exhiDTO)
            .addAttribute("totalPrice", totalPrice)
            .addAttribute("reserveList", reserveList);

        return "tosspay/paymentSuccess";
    }
}
