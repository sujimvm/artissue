package com.artissue.service;

import com.artissue.model.PaymentResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class PaymentService {

    @Value("${toss.api.base-url}")
    private String baseUrl;

    @Value("${toss.api.secret-key}")
    private String secretKey;

    public PaymentResponseDTO handleSuccess(Map<String, String> params) {
        PaymentResponseDTO response = new PaymentResponseDTO();
        response.setPaymentKey(params.get("paymentKey"));
        response.setOrderId(params.get("orderId"));
        response.setAmount(Integer.parseInt(params.get("amount")));
        response.setStatus(params.get("status"));
        return response;
    }

}
