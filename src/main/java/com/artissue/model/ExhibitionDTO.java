package com.artissue.model;

import lombok.Data;

import java.util.Date;

@Data
public class ExhibitionDTO {
    private int exhibition_key;
    private int member_key;
    private String company_number;
    private String exhibition_thumnail;
    private String exhibition_title;
    private Date exhibition_start_date;
    private Date exhibition_end_date;
    private String exhibition_place;
    private String exhibition_addr;
    private String exhibition_view_time;
    private String exhibition_view_age;
    private String exhibition_notice;
    private String exhibition_detail_info;
    private String exhibition_seller_info;
    private String exhibition_product_info;
    private String exhibition_date;
}
