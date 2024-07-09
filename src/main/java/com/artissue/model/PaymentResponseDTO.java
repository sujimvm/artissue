package com.artissue.model;

import lombok.Data;

@Data
public class PaymentResponseDTO {

    private String paymentKey;
    private String orderId;
    private int amount;
    private String status;
}
