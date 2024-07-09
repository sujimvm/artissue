package com.artissue.controller;

import com.artissue.model.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/reserve")
public class ReservationController {

    @Value("${toss.api.client-key}")
    private String clientKey;

    @Autowired
    private ExhibitionMapper exhibitionMapper;
    @Autowired
    private ReservationMapper reservationMapper;

    @GetMapping("/open")
    public String openReserve(@RequestParam("No") int exhibition_key, Model model){

        model.addAttribute("ExhiDTO", this.exhibitionMapper.getExhibitionCont(exhibition_key))
                .addAttribute("PriceList", this.reservationMapper.getPriceList(exhibition_key));

        return "reservation/popup";
    }

    @PostMapping("/sendReserve")
    public String reservation(ReservationDTO reserveDTO, HttpSession session, Model model) {
        int addResult = 0;

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

                addResult = reservationMapper.addReservation(insertDTO);
            }
        }

        // 결제 페이지 이동 -> 결제
        model.addAttribute("exhiDTO", this.exhibitionMapper.getExhibitionCont(insertDTO.getExhibition_key()))
                .addAttribute("reserveList", this.reservationMapper.getReserveList(insertDTO.getReservation_id()))
                .addAttribute("paymentRequest", new PaymentRequestDTO())
                .addAttribute("clientKey", clientKey);

        return "tosspay/paymentForm";

        // QR 생성

        // 예매내역 및 예약 확인 페이지 링크 문자 발송

        //예매 확인 페이지 구현 및 페이지로 이동

    }
}
