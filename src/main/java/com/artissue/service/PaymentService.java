package com.artissue.service;

import com.artissue.model.PaymentRequestDTO;
import com.artissue.model.PaymentResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class PaymentService {

    @Value("${toss.api.base-url}")
    private String baseUrl;

    @Autowired
    private RestTemplate restTemplate;

    public PaymentResponseDTO requestPayment(PaymentRequestDTO paymentRequest) {
        String url = baseUrl + "/payments";

        return restTemplate.postForObject(url, paymentRequest, PaymentResponseDTO.class);
    }

}
