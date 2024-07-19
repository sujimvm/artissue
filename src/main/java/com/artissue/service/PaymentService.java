package com.artissue.service;

import com.artissue.model.PaymentResponseDTO;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
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

    public boolean approvePayment(PaymentResponseDTO paymentResponse) {
        RestTemplate restTemplate = new RestTemplate();
        String url = baseUrl + "/payments/confirm";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(secretKey, ""); // 시크릿 키 설정

        Map<String, Object> request = Map.of(
                "paymentKey", paymentResponse.getPaymentKey(),
                "orderId", paymentResponse.getOrderId(),
                "amount", paymentResponse.getAmount()
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            System.out.println("결제 승인 성공: " + response.getBody());
            return true;
        } catch (HttpClientErrorException e) {
            System.err.println("결제 승인 오류: " + e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cancelPayment(String paymentKey, String cancelReason) {
        RestTemplate restTemplate = new RestTemplate();
        String url = baseUrl + "/v1/payments/" + paymentKey + "/cancel";

        HttpHeaders headers = getHeaders();
        JSONObject params = new JSONObject();
        params.put("cancelReason", cancelReason);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, new HttpEntity<>(params.toString(), headers), Map.class);
            System.out.println("결제 취소 성공: " + response.getBody());
            return true;
        } catch (HttpClientErrorException e) {
            System.err.println("결제 취소 오류: " + e.getResponseBodyAsString());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String encodedAuthKey = new String(Base64.getEncoder().encode((secretKey + ":").getBytes(StandardCharsets.UTF_8)));

        headers.setBasicAuth(encodedAuthKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }
}
