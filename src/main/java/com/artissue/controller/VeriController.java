package com.artissue.controller;

import com.artissue.model.MemberMapper;
import com.artissue.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/veri")
public class VeriController {

    @Autowired
    private MessageService massageService;

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

 /*   @GetMapping("/joinId")
    public ResponseEntity<String> sameId(@RequestParam("id") String id) {
        int count = memberMapper.sameUsername(id);

        if (count > 0) {
            return ResponseEntity.ok("exists");
        } else {
            return ResponseEntity.ok("available");
        }
    }*/
}
