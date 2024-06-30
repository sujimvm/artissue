package com.artissue.controller;

import com.artissue.model.MemberDTO;
import com.artissue.model.MemberMapper;
import com.artissue.service.JoinService;
import com.artissue.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Random;

@Controller
public class MemberController {

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private MessageService messageService;

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
    public String joinProc(MemberDTO memberDTO) {

        joinService.joinProcess(memberDTO);

        return "login";
    }

    @GetMapping("/findbyId")
    public String findId() {
        return "findbyId";
    }

    @PostMapping("/sendVerificationCode")
    @ResponseBody
    public ResponseEntity<String> sendVerificationCode(@RequestParam String phone) {
        try {

            String verificationCode = generateVerificationCode();
            messageService.sendOne(phone, verificationCode);


            return ResponseEntity.ok("{\"success\": true}");
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"success\": false}");
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000);
        return String.valueOf(code);
    }

}