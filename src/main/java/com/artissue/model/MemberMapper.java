package com.artissue.model;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface MemberMapper {

    //로그인
    MemberDTO findUsername(String member_id);

    //동일한 ID 있는지 확인
    boolean existsByMembername(String member_id);

    //회원가입
    void insertMember(MemberDTO memberDTO);

    //중복 아이디
    int sameMemberName(String member_id);

    // 비밀번호 찾기
    MemberDTO findMemberPwd(@Param("member_id") String member_id,
                            @Param("member_name") String member_name,
                            @Param("member_email") String member_email);

    //아이디 찾기
    MemberDTO findMemberId(@Param("member_name") String member_name, @Param("member_email") String member_email);


    int updatePassword(@Param("member_id") String member_id,
                       @Param("encodedPwd") String encodedPwd);

    MemberDTO findbyId(String member_id);

    int oAuth2insert(MemberDTO dto);

    int oAuth2update(MemberDTO dto);

    MemberDTO findUserId(String member_id);

    List<ReservationDTO> userReserveList(int member_key);

    List<ReservationDTO> userReserveCont(String reservation_id);

    ExhibitionDTO userExhibition(int exhibition_key);

    List<ZzimDTO> userZzimList(int member_key);

    int memberUpdate(MemberDTO dto);

    int updatePwd(String member_id, String member_pwd);

    //사업자 번호 조회
    MemberMapper companyInfoByNo(String company_number);

    String getMemberNickname(int member_key);

}
