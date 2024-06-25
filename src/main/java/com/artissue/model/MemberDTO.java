package com.artissue.model;

import lombok.Data;

@Data
public class MemberDTO {
    private int user_key;
    private String user_id;
    private String user_pwd;
    private String role;
    private int enabled;
}
