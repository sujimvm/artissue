package com.artissue.service;

import com.artissue.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    MemberMapper memberMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("info>>"+oAuth2User.getAttributes());

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); //OAuth2 제공자를 통해 로그인

        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("naver")) {

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("kakao")){

            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());


        }else{
            return null;
        }

        String member_id = oAuth2Response.getEmail();

        int atIndex = member_id.indexOf('@');

        String memberEmailTrim = atIndex != -1 ? member_id.substring(0, atIndex) : member_id;


        MemberDTO memberData = memberMapper.findbyId(memberEmailTrim);

        String role = "ROLE_USER";
        if (memberData == null) {

            MemberDTO dto = new MemberDTO();

            dto.setMember_id(memberEmailTrim);
            dto.setMember_pwd("-");
            dto.setMember_name(oAuth2Response.getName());
            dto.setMember_email(oAuth2Response.getEmail());
            dto.setMember_gender(oAuth2Response.getGender());
            dto.setMember_birth(null);
            dto.setMember_phone(oAuth2Response.getMobile());
            dto.setRole(role);

            memberMapper.oAuth2insert(dto);
        }
        else {

            memberData.setMember_pwd("-");
            memberData.setMember_name(oAuth2Response.getName());
            memberData.setMember_email(oAuth2Response.getEmail());
            memberData.setMember_gender(oAuth2Response.getGender());
            memberData.setMember_birth(null);
            memberData.setMember_phone(oAuth2Response.getMobile());


            role = memberData.getRole();

            memberMapper.oAuth2update(memberData);
        }



        return new CustomOAuth2User(oAuth2Response, role);
     }

    private String extractMemberId(String providerId) {
        return providerId;
    }

}

