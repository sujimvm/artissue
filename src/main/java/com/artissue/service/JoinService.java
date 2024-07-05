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

    public int joinProcess(MemberDTO memberDTO, String userType){

        boolean isMember = memberMapper.existsByMembername(memberDTO.getMember_id());

        if(isMember){
            return 0;
        }


        String encodedPassword = bCryptPasswordEncoder.encode(memberDTO.getMember_pwd());
        memberDTO.setMember_pwd(encodedPassword);


        if ("company".equals(userType)) {

            memberDTO.setRole("ROLE_COMPANY");
        } else if ("individual".equals(userType)) {

            memberDTO.setRole("ROLE_USER");
        }


        memberMapper.insertMember(memberDTO);

        return 1;
    }
}
