package com.artissue.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {


    private final MemberDTO memberDTO;

    public CustomUserDetails(MemberDTO memberDTO){

        this.memberDTO = memberDTO;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {


        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() { // user role 데이터를 넣어줄거임

            @Override
            public String getAuthority() {

                return memberDTO.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getPassword() {
        return memberDTO.getUser_pwd();
    }

    @Override
    public String getUsername() {
        return memberDTO.getUser_id();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
