package com.artissue.controller;

import com.artissue.model.PaymentRequestDTO;
import com.artissue.model.PaymentResponseDTO;
import com.artissue.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Value("${toss.api.client-key}")
    private String clientKey;

    @GetMapping("/payment/success-redirect")
    public String paymentSuccess(@RequestParam Map<String, String> params, Model model) {
        System.out.println("결제 성공 리디렉트 호출됨: " + params);
        PaymentResponseDTO paymentResponse = paymentService.handleSuccess(params);
        model.addAttribute("paymentResponse", paymentResponse);

        // 추가: 결제 승인 로직
        boolean approvalResult = paymentService.approvePayment(paymentResponse);
        if (approvalResult) {
            return "redirect:/reserve/success";
        } else {
            model.addAttribute("errorMessage", "결제 승인이 실패했습니다.");
            return "tosspay/paymentFail";
        }
    }

    @GetMapping("/payment/fail")
    public String paymentFail(@RequestParam Map<String, String> params, Model model) {
        System.out.println("결제 실패 리디렉트 호출됨: " + params);
        model.addAttribute("errorMessage", params.get("message"));
        return "tosspay/paymentFail";
    }





}
