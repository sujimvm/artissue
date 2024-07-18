package com.artissue.controller;

import com.artissue.model.*;
import com.google.zxing.WriterException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    MemberMapper memberMapper;
    @Autowired
    private ReservationMapper reservationMapper;
    @Autowired
    private ExhibitionMapper exhibitionMapper;

    @GetMapping("/")
    public String home(HttpSession session,Model model){

        // 현재 인증된 사용자의 정보 가져오기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // 사용자의 첫 번째 권한 가져오기
        String role = auth.getAuthorities().iterator().next().getAuthority();
        String id = auth.getName();

        System.out.println("role>>> " + role);
        System.out.println("id>>> " + id);

        if (role.equals("ROLE_USER")) {
            session.setAttribute("role", role);

            MemberDTO memberDTO = memberMapper.findUsername(id);
            session.setAttribute("mDTO", memberDTO);

        } else if (role.equals("ROLE_COMPANY")) {
            session.setAttribute("role", role);

            MemberDTO companyDTO = memberMapper.findUsername(id);
            session.setAttribute("cDTO", companyDTO);

        }

        return "index";
    }

    @GetMapping("/ticket")
    public String reserveSuccess(@RequestParam("T") String reservation_id, Model model) throws IOException, WriterException {

        List<ReservationDTO> reserveList= this.reservationMapper.getReserveList(reservation_id);
        ExhibitionDTO exhiDTO = this.exhibitionMapper.getExhibitionCont(reserveList.get(0).getExhibition_key());

        //예매 확인 페이지 구현 및 페이지로 이동
        model.addAttribute("exhiDTO", exhiDTO)
                .addAttribute("reserveList", reserveList);

        return "reservation/ticket";
    }



    @GetMapping("/event")
    public String event(){
        return "exhibition/event";
    }
    @GetMapping("/elements")
    public String elements(){
        return "elements";
    }
    @GetMapping("/contact")
    public String contact(){
        return "contact";
    }
    @GetMapping("/blog")
    public String blog(){
        return "blog";
    }
    @GetMapping("/albums-store")
    public String albumsstore(){
        return "albums-store";
    }

}
