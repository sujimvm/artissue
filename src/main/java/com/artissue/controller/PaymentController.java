package com.artissue.controller;

import com.artissue.model.PaymentRequestDTO;
import com.artissue.model.PaymentResponseDTO;
import com.artissue.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Value("${toss.api.client-key}")
    private String clientKey;

    @GetMapping("/payment")
    public String paymentForm(Model model) {
        model.addAttribute("paymentRequest", new PaymentRequestDTO());
        model.addAttribute("clientKey", clientKey);
        return "tosspay/paymentForm";
    }

    @PostMapping("/success")
    public String requestPayment(@RequestBody PaymentRequestDTO paymentRequest, Model model) {
        PaymentResponseDTO paymentResponse = paymentService.requestPayment(paymentRequest);
        model.addAttribute("paymentResponse", paymentResponse);
        return "tosspay/paymentResult";
    }
}
