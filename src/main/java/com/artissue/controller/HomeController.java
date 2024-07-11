package com.artissue.controller;

import com.artissue.model.CustomOAuth2User;
import com.artissue.model.MemberDTO;
import com.artissue.model.MemberMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    MemberMapper memberMapper;

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

    @GetMapping("/qr")
    public String qrTest(){
        return "reservation/qrTest";
    }



    @GetMapping("/company")
    public String admin(Model model,HttpSession session){

        MemberDTO userInfo = (MemberDTO)session.getAttribute("cDTO");

        System.out.println("info>>>"+userInfo);

        return "admin";
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
