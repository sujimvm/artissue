package com.artissue.service;

import com.artissue.model.CustomUserDetails;
import com.artissue.model.MemberDTO;
import com.artissue.model.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String user_id) throws UsernameNotFoundException {

       MemberDTO memberdata = memberMapper.findUsername(user_id);

       if(memberdata != null){
           return new CustomUserDetails(memberdata);
       }
       return null;
    }
}
