package com.artissue.controller;

import com.artissue.model.MemberDTO;
import com.artissue.model.MemberMapper;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    MemberMapper memberMapper;

    @GetMapping("/")
    public String main(){
        return "index";
    }

    @GetMapping("/index")
    public String index(HttpSession session) {
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
        }

        System.out.println("Role in session: " + session.getAttribute("role"));
        System.out.println("Member DTO in session: " + session.getAttribute("mDTO"));

        // index 페이지로 이동
        return "index";
    }




    @GetMapping("/event")
    public String event(){
        return "event";
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
