package com.artissue.model;

import lombok.Data;

@Data
public class ReservationDTO {
    private int reservation_key;
    private int exhibition_key;
    private int member_key;
    private String reservation_date;
    private String reservation_option;
    private String reservation_count;
    private String reservation_price;
    private String reservation_buyer_view;
    private String reservation_seller_view;

    private String exhibition_title;

}
