package com.artissue.model;

import lombok.Data;

@Data
public class NoticeDTO {

    private int notice_key;
    private String notice_title;
    private String notice_cont;
    private int notice_hit;
    private String notice_date;
}
