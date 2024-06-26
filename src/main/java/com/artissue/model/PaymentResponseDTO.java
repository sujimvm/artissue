package com.artissue.model;

import lombok.Data;

@Data
public class PaymentResponseDTO {

    private String paymentKey;
    private String orderId;
    private String amount;
    private String status;
}
