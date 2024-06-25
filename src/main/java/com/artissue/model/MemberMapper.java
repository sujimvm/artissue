package com.artissue.model;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

    //로그인
    MemberDTO findUsername(String user_id);
}
