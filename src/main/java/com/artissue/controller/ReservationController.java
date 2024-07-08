package com.artissue.controller;

import com.artissue.model.ExhibitionDTO;
import com.artissue.model.ExhibitionMapper;
import com.artissue.model.ReservationDTO;
import com.artissue.model.ReservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reserve")
public class ReservationController {

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
    public void reservation(ReservationDTO reserveDTO) {
        // 값 insert
        System.out.println(reserveDTO);


        // QR 생성 및 예매 확인 페이지 구현

        // 예매내역 및 예약 확인 페이지 링크 문자 발송

        //예약확인 페이지로 이동

    }
}
