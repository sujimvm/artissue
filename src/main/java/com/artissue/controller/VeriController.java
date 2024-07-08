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

        // 여기서는 간단히 성공 상태를 반환하고, 실제로는 SMS 발송 로직을 구현해야 합니다.
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", 200);
        responseMap.put("verificationCode", verificationCode);
        responseMap.put("response", response);

        return ResponseEntity.ok(responseMap);
    }

    private String generateVerificationCode() {

        Random random = new Random();
        int code = 1000 + random.nextInt(9000);
        return String.valueOf(code);
    }

    @GetMapping("/test_send")
    public void test_send() {

        String memberPhone = "01036181007";

        String verificationCode = "안녕하세요\n 예매내역 안내드립니다\n " +
                "날짜 : 2024-07-08\n 예약날짜 : 2024-07-08\n" +
                "전시회 이름 : 어쩌구 저쩌구\n 예매 갯수 : 4매\n  " +
                "이렇게 하면 장문 문자가 가는 건가요 ???\n" +
                "내용을 더더더더더더더더더 길게 \n 써보겠습니다\n 하하하하하하하하하";

        SingleMessageSentResponse response = massageService.sendOne(memberPhone, verificationCode);

        System.out.println(response);
        System.out.println("문자발송");
    }
}
