package com.artissue.controller;

import com.artissue.model.PaymentRequestDTO;
import com.artissue.model.PaymentResponseDTO;
import com.artissue.model.ReservationMapper;
import com.artissue.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ReservationMapper reservationMapper;

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
            return "redirect:/reserve/success?id="+params.get("orderId") + "&paymentKey="+paymentResponse.getPaymentKey();
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

    @PostMapping("/payment/cancel")
    public String cancelPayment(@RequestParam String paymentKey, @RequestParam String cancelReason, Model model) {
        System.out.println("결제 취소 요청: paymentKey=" + paymentKey + ", cancelReason=" + cancelReason);

        if (paymentKey == null || paymentKey.isEmpty() || cancelReason == null || cancelReason.isEmpty()) {
            model.addAttribute("errorMessage", "필수 파라미터가 누락되었습니다.");
            return "tosspay/paymentFail";
        }

        String errorMessage = null;
        boolean cancelResult = false;
        try {
            cancelResult = paymentService.cancelPayment(paymentKey, cancelReason);
        } catch (Exception e) {
            errorMessage = e.getMessage();
            System.err.println("결제 취소 중 예외 발생: " + errorMessage);
        }

        if (cancelResult) {
            System.out.println("결제 취소 성공");
            return "redirect:/tosspay/paymentCancel";
        } else {
            if (errorMessage != null) {
                model.addAttribute("errorMessage", "결제 취소가 실패했습니다. 상세 오류: " + errorMessage);
                System.err.println("결제 취소 실패: " + errorMessage);
            } else {
                model.addAttribute("errorMessage", "결제 취소가 실패했습니다.");
                System.err.println("결제 취소 실패: 이유 불명");
            }
            return "tosspay/paymentFail";
        }
    }



}
