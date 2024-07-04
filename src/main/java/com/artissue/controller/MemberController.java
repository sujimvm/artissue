package com.artissue.controller;

import com.artissue.model.MemberDTO;
import com.artissue.model.MemberMapper;
import com.artissue.service.JoinService;
import com.artissue.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    public String joinProc(MemberDTO memberDTO, @RequestParam("userType") String userType) {
        System.out.println("userType: " + userType);
        if ("company".equals(userType)) {
            memberDTO.setRole("ROLE_COMPANY");
        } else if ("individual".equals(userType)) {
            memberDTO.setRole("ROLE_USER");
        }

        joinService.joinProcess(memberDTO,userType);

        return "login";
    }

    @GetMapping("/findbyId")
    public String findId() {
        return "findbyId";
    }



}