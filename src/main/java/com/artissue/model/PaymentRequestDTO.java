package com.artissue.model;

import lombok.Data;

@Data
public class PaymentRequestDTO {
    private String orderId;
    private int amount;
    private String orderName;
    private String successUrl;
    private String failUrl;
}
