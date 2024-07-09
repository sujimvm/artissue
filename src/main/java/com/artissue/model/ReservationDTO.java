package com.artissue.model;

import lombok.Data;

@Data
public class ReservationDTO {
    private int reservation_key;
    private int exhibition_key;
    private int member_key;
    private String reservation_date;
    private String reservation_id;
    private int member_pay;
    private String reservation_option;
    private String reservation_count_str;
    private int reservation_count;
    private String reservation_price_str;
    private int reservation_price;
    private int reservation_buyer_view;
    private int reservation_seller_view;

    private String exhibition_title;

}
