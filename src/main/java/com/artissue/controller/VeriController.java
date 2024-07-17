package com.artissue.controller;

import com.artissue.model.MemberMapper;
import com.artissue.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/veri")
public class VeriController {

    @Autowired
    private MessageService massageService;

    @Autowired
    private MemberMapper memberMapper;

    private String verificationCode = "";

    @PostMapping("/send-one")
    public ResponseEntity<Object> sendOne(@RequestParam("memberPhone") String memberPhone) {

        System.out.println(memberPhone);

        String verificationCode = String.format("%06d", (int) (Math.random() * 1000000));

        SingleMessageSentResponse response = massageService.sendOne(memberPhone, verificationCode);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("verificationCode", verificationCode);
        responseBody.put("response", response);

        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/checkId")
    public ResponseEntity<String> sameId(@RequestParam("id") String id) {
        System.out.println("id>>> " + id);
        int count = memberMapper.sameMemberName(id);

        if (count > 0) {
            return ResponseEntity.ok("exists");
        } else {
            return ResponseEntity.ok("available");
        }
    }


    @PostMapping("/reSendSms")
    public ResponseEntity<Map<String, Object>> reSendSms(@RequestParam("mgrPhone") String memberPhone) {

        System.out.println("memberPhone: " + memberPhone);
        verificationCode = generateVerificationCode();

        SingleMessageSentResponse response = massageService.sendOne(memberPhone, verificationCode);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", 200);
        responseMap.put("verificationCode", verificationCode);
        responseMap.put("response", response);

        return ResponseEntity.ok(responseMap);
    }

    private String generateVerificationCode() {

        Random random = new Random();
        String code = String.format("%06d", (int) (Math.random() * 1000000));
        return String.valueOf(code);
    }
}
