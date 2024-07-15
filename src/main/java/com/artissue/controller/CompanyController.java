package com.artissue.controller;

import com.artissue.model.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    private ReservationMapper reservationMapper;
    @Autowired
    private ExhibitionMapper exhibitionMapper;

    @GetMapping("/exList")
    public String getMyExhibitionsList(Model model, HttpSession session) {
        int member_key = ((MemberDTO)session.getAttribute("cDTO")).getMember_key();
        model.addAttribute("list", this.exhibitionMapper.getMyExhibitionsList(member_key));
        return "company/exhiList";
    }

    @GetMapping("/reList")
    public String getMyExhibitionsReservationList(Model model, HttpSession session) {
        int member_key = ((MemberDTO)session.getAttribute("cDTO")).getMember_key();
        model.addAttribute("list", this.reservationMapper.getMyExhibitionsReservationList(member_key));
        return "company/reserList";
    }


}
