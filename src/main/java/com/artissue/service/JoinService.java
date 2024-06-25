package com.artissue.service;

import com.artissue.model.MemberDTO;
import com.artissue.model.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    @Autowired
    MemberMapper memberMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinProcess(MemberDTO memberDTO){

       boolean isMember =  memberMapper.existsByUsername(memberDTO.getUser_id());

       if(isMember){
           return;
       }


        // 비밀번호 암호화
        String encodedPassword = bCryptPasswordEncoder.encode(memberDTO.getUser_pwd());
        memberDTO.setUser_pwd(encodedPassword);

        memberDTO.setRole("ROLE_USER");
        // 사용자 정보 저장
        memberMapper.insertMember(memberDTO);



    }

}
