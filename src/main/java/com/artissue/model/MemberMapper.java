package com.artissue.model;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

    //로그인
    MemberDTO findUsername(String user_id);

    //동일한 ID 있는지 확인
    boolean existsByUsername(String user_id);

    //회원가입
    void insertMember(MemberDTO memberDTO);
}
