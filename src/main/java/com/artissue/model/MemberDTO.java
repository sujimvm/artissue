package com.artissue.model;

import lombok.Data;

@Data
public class MemberDTO {
    private int member_key;
    private String member_id;
    private String member_pwd;
    private String member_name;
    private String member_email;
    private String member_phone;
    private String member_birth;
    private String member_gender;
    private String member_join_date;
    private String member_nickname;
    private String company_number;
    private String role;
    private int enabled;

}
