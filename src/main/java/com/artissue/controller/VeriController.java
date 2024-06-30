package com.artissue.controller;

import com.artissue.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;


@RestController
@RequestMapping("/veri")
public class VeriController {

    @Autowired
    private MessageService messageService;
    private static final Logger logger = LoggerFactory.getLogger(VeriController.class);


    @PostMapping("/sendSms")
    @ResponseBody
    public ResponseEntity<?> sendSms(@RequestParam("memberPhone") String memberPhone) {
        System.out.println("넘어오나 보자");

        String phone = memberPhone;

        String verificationCode = String.format("%06d", (int) (Math.random() * 1000000));

        messageService.saveSMS(phone, verificationCode);

        SingleMessageSentResponse response = messageService.sendOne(phone, verificationCode);

        if (response.getStatusCode().equals("2000")) { // assuming 2000 means success
            return ResponseEntity.ok().body("{\"status\": 200, \"message\": \"인증번호가 전화번호로 전송되었습니다.\"}");
        } else {
            return ResponseEntity.status(500).body("{\"status\": 500, \"message\": \"인증번호 전송에 실패했습니다.\"}");
        }
    }

}
