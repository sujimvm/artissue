package com.artissue.model;

import lombok.Data;

@Data
public class PriceDTO {
    private int price_key;
    private int exhibition_key;
    private String price_option;
    private int price;

}
