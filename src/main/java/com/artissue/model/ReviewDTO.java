package com.artissue.model;

import lombok.Data;

@Data
public class ReviewDTO {
    private int review_key;
    private int exhibition_key;
    private int member_key;
    private int review_score;
    private String review_title;
    private String review_cont;
    private int review_recommend;
    private String review_date;
}
