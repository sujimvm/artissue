package com.artissue.controller;

import com.artissue.model.MemberDTO;
import com.artissue.model.MemberMapper;
import com.artissue.service.JoinService;
import com.artissue.service.MessageService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

@Controller
public class MemberController {


    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private JoinService joinService;


    @GetMapping("/login")
    public String login() {

        return "login";
    }

    @GetMapping("/join")
    public String join() {
        return "memberJoin";
    }

    @PostMapping("/joinProc")
    public void joinProc(MemberDTO memberDTO, @RequestParam("userType") String userType, HttpServletResponse response) throws IOException {
        System.out.println("userType: " + userType);
        if ("company".equals(userType)) {
            memberDTO.setRole("ROLE_COMPANY");
        } else if ("individual".equals(userType)) {
            memberDTO.setRole("ROLE_USER");
        }

        String phoneNumber = memberDTO.getMember_phone().replaceAll("[^0-9]", "");
        if (phoneNumber.length() == 8) {
            phoneNumber = phoneNumber.substring(0, 4) + "-" + phoneNumber.substring(4);
        } else if (phoneNumber.length() == 9) {
            phoneNumber = phoneNumber.substring(0, 2) + "-" + phoneNumber.substring(2, 5) + "-" + phoneNumber.substring(5);
        } else if (phoneNumber.length() == 10) {
            if (phoneNumber.startsWith("02")) {
                phoneNumber = phoneNumber.substring(0, 2) + "-" + phoneNumber.substring(2, 6) + "-" + phoneNumber.substring(6);
            } else {
                phoneNumber = phoneNumber.substring(0, 3) + "-" + phoneNumber.substring(3, 6) + "-" + phoneNumber.substring(6);
            }
        } else if (phoneNumber.length() == 11) {
            phoneNumber = phoneNumber.substring(0, 3) + "-" + phoneNumber.substring(3, 7) + "-" + phoneNumber.substring(7);
        }
        memberDTO.setMember_phone(phoneNumber);

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();


        int check = joinService.joinProcess(memberDTO, userType);

        if (check > 0) {
            out.println("<script>");
            out.println("alert('회원가입을 완료하였습니다.')");
            out.println("location.href='/login'");
            out.println("</script>");
        } else {
            out.println("<script>");
            out.println("alert('회원가입을 실패하였습니다.')");
            out.println("history.back()");
            out.println("</script>");
        }

    }

    @GetMapping("/findbyId")
    public String findId() {
        return "findbyId";
    }

    @PostMapping("/findIdProc")
    public String findIdProc(@RequestParam("member_name") String member_name, @RequestParam("member_email") String member_email,
                             Model model,HttpServletResponse response) throws IOException {

       MemberDTO dto = memberMapper.findMemberId(member_name,member_email);

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        if (dto == null || !member_email.equals(dto.getMember_email()) || !member_name.equals(dto.getMember_name())) {
            out.println("<script>");
            out.println("alert('정보가 일치하지 않습니다.입력한 정보를 다시 확인해주세요.')");
            out.println("history.back()");
            out.println("</script>");
            out.flush();
            return null;

        }

       String mId = dto.getMember_id();

       model.addAttribute("memberId",mId);

       return "findIdOk";


    }


}