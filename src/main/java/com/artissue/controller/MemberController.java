package com.artissue.controller;

import com.artissue.model.*;
import com.artissue.service.JoinService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MemberController {


    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private JoinService joinService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {

        return "login";
    }

    @GetMapping("/join")
    public String join() {
        return "memberJoin";
    }

    //회원가입
    @PostMapping("/joinProc")
    public void joinProc(MemberDTO memberDTO, @RequestParam("userType") String userType, HttpServletResponse response) throws IOException {
        System.out.println("userType: " + userType);
        if ("company".equals(userType)) {
            memberDTO.setRole("ROLE_COMPANY");
            //사업자번호
            String str1 = memberDTO.getCompany_number().substring(0, 3);
            String str2 = memberDTO.getCompany_number().substring(3, 5);
            String str3 = memberDTO.getCompany_number().substring(5);

            memberDTO.setCompany_number(str1 + "-" + str2 + "-" + str3);

        } else if ("individual".equals(userType)) {
            memberDTO.setRole("ROLE_USER");

            memberDTO.setCompany_number(null);
        }

        

        //전화번호
        String phoneNumber = memberDTO.getMember_phone().replaceAll("[^0-9]", "");
        if (phoneNumber.length() == 8) {
            phoneNumber = phoneNumber.substring(0, 4) + "-" + phoneNumber.substring(4);
        } else if (phoneNumber.length() == 9) {
            phoneNumber = phoneNumber.substring(0, 2) + "-" + phoneNumber.substring(2, 5) + "-" + phoneNumber.substring(5);
        } else if (phoneNumber.length() == 10) {
            if (phoneNumber.startsWith("02")) {
                phoneNumber = phoneNumber.substring(0, 2) + "-" + phoneNumber.substring(2, 6) + "-" + phoneNumber.substring(6);
            } else {
                phoneNumber = phoneNumber.substring(0, 3) + "-" + phoneNumber.substring(3, 6) + "-" + phoneNumber.substring(6);
            }
        } else if (phoneNumber.length() == 11) {
            phoneNumber = phoneNumber.substring(0, 3) + "-" + phoneNumber.substring(3, 7) + "-" + phoneNumber.substring(7);
        }
        memberDTO.setMember_phone(phoneNumber);

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();


        int check = joinService.joinProcess(memberDTO, userType);

        if (check > 0) {
            out.println("<script>");
            out.println("alert('회원가입을 완료하였습니다.')");
            out.println("location.href='/login'");
            out.println("</script>");
        } else {
            out.println("<script>");
            out.println("alert('회원가입을 실패하였습니다.')");
            out.println("history.back()");
            out.println("</script>");
        }

    }


    @GetMapping("/findbyId")
    public String findId() {
        return "findbyId";
    }

    //아이디 찾기
    @PostMapping("/findIdProc")
    public String findIdProc(@RequestParam("member_name") String member_name, @RequestParam("member_email") String member_email,
                             Model model,HttpServletResponse response) throws IOException {

       MemberDTO dto = memberMapper.findMemberId(member_name,member_email);

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        if (dto == null || !member_email.equals(dto.getMember_email()) || !member_name.equals(dto.getMember_name())) {
            out.println("<script>");
            out.println("alert('정보가 일치하지 않습니다.입력한 정보를 다시 확인해주세요.')");
            out.println("history.back()");
            out.println("</script>");
            out.flush();
            return null;

        }

       String mId = dto.getMember_id();

       model.addAttribute("memberId",mId);

       return "findIdOk";


    }
    @GetMapping("/findPwdProc")
    public String findPwdProc(@RequestParam("member_id") String member_id,
                              @RequestParam("member_name") String member_name,
                              @RequestParam("member_email") String member_email,
                              Model model,
                              HttpServletResponse response) throws IOException, IOException {

        response.setContentType("text/html; charset=UTF-8");

        MemberDTO member_pwd = this.memberMapper.findMemberPwd(member_id, member_name, member_email);

        System.out.println(member_pwd);

        if(member_pwd == null) {
            PrintWriter out = response.getWriter();
            out.println("<script>");
            out.println("alert('입력하신 정보에 해당하는 회원이 없습니다.')");
            out.println("location.href='/findbyId'");
            out.println("</script>");
            out.flush();
            out.close();
            return null;
        }else {
            model.addAttribute("member_id", member_id);
            return "updatePwd";
        }
    }

    @PostMapping("/updatePassword")
    public void updatePassword(@RequestParam("member_id") String member_id,
                               @RequestParam("member_pwd") String member_pwd,
                               HttpServletResponse response) throws IOException {
        System.out.println(member_id);
        System.out.println(member_pwd);

        String encodedPwd = passwordEncoder.encode(member_pwd);

        System.out.println(encodedPwd);

        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();

        MemberDTO memberInfo = this.memberMapper.findUsername(member_id);

        if(passwordEncoder.matches(member_pwd, memberInfo.getMember_pwd())){
            out.println("<script>");
            out.println("alert('기존 비밀번호와 새로 입력하신 비밀번호가 같습니다.')");
            out.println("history.back()");
            out.println("</script>");
        }else{
            int result = this.memberMapper.updatePassword(member_id, encodedPwd);

            if(result == 1) {
                out.println("<script>");
                out.println("alert('비밀번호 수정에 성공했습니다.')");
                out.println("location.href='/login'");
                out.println("</script>");
            }else {
                out.println("<script>");
                out.println("alert('비밀번호 수정에 실패했습니다.')");
                out.println("history.back()");
                out.println("</script>");
            }

        }
    }



    @GetMapping("/userPwdUpdate")
    public String userPwdUpdate(HttpSession session, Model model) {

        return "my-page/userPwdUpdate";
    }

    //비밀번호 변경
    @PostMapping("/pwdUpdate")
    public String pwdUpdate(@RequestParam("oriPwd") String oriPwd,@RequestParam("newPassword") String newPassword,
                            HttpSession session, HttpServletResponse response) throws IOException {

        MemberDTO memberInfo =null;

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        if(session.getAttribute("mDTO") != null){
            memberInfo =(MemberDTO) session.getAttribute("mDTO");
        }else if(session.getAttribute("cDTO") != null){
            memberInfo =(MemberDTO) session.getAttribute("cDTO");
        }
        if(passwordEncoder.matches(oriPwd, memberInfo.getMember_pwd())) {
            //변경비번과 기존과 동일 할 경우
            if(passwordEncoder.matches(newPassword,memberInfo.getMember_pwd())){
                out.println("<script>");
                out.println("alert('기존 비밀번호와 새 비밀번호가 같습니다.')");
                out.println("</script>");
                return "my-page/userPwdUpdate";
                //변경비번과 기존과 동일 하지 않을 경우
            }else{
                String newPwdEncoded = this.passwordEncoder.encode(newPassword);

                int result = this.memberMapper.updatePwd(memberInfo.getMember_id(),newPwdEncoded);

                if (result > 0) {
                    memberInfo.setMember_pwd(newPwdEncoded); // 업데이트된 비밀번호로 설정
                    session.setAttribute("mDTO", memberInfo); // 세션에 저장
                }

                out.println("<script>");
                out.println("alert('비밀번호 수정에 성공했습니다.')");
                out.println("</script>");
                 return "my-page/userContent";

            }
        }else{
        //입력된 비밀번호와 기존 비밀번호가 다를때
        out.println("<script>");
        out.println("alert('입력하신 비밀번호와 기존 비밀번호가 다릅니다.')");
        out.println("</script>");
            return "my-page/userPwdUpdate";
        }
    }

    @GetMapping("/user")
    public String userPage(HttpSession session, Model model) {

        return "my-page/user";
    }

    //소셜로그인,일반 로그인 이동
    @GetMapping("/userMove")
    public String updateUserMove(HttpSession session, Model model) {

        MemberDTO memberInfo=null;

       if(session.getAttribute("mDTO") != null) {
             memberInfo = (MemberDTO) session.getAttribute("mDTO");

        } else if (session.getAttribute("cDTO") != null) {
             memberInfo = (MemberDTO) session.getAttribute("cDTO");
        }
        if (memberInfo.getMember_pwd().equals("-")) {
                return "my-page/userModify";
            } else {
                return "my-page/user";
            }
    }

    //비밀번호 변경 경로
    @GetMapping("userPwdUpdateMove")
    public String userPwdUpdateMove(HttpSession session, Model model) {

        MemberDTO memberInfo = null;

        if (session.getAttribute("mDTO") != null) {
             memberInfo = (MemberDTO) session.getAttribute("mDTO");

        } else if(session.getAttribute("cDTO") != null) {
             memberInfo = (MemberDTO) session.getAttribute("cDTO");

        }

        String memberId = memberInfo.getMember_id();

        MemberDTO dto = this.memberMapper.findbyId(memberId);
        System.out.println("dto: " + dto);

        if (dto == null || dto.getMember_pwd() == null || dto.getMember_pwd().equals("-")) {
            return "my-page/socialPwd";
        } else {
            model.addAttribute("memberId", memberId);
            return "my-page/userPwdUpdate";
        }
    }




    @GetMapping("/userReserveList")
    public String userReserveList(HttpSession session, Model model) {

        MemberDTO memberInfo = (MemberDTO)session.getAttribute("mDTO");

        List<ReservationDTO> userReserveList = this.memberMapper.userReserveList(memberInfo.getMember_key());
        System.out.println(userReserveList);
        model.addAttribute("userReserveList", userReserveList);

        return "my-page/userReserveList";
    }

    @GetMapping("/userReserveCont")
    public String userReserveCont(HttpSession session, Model model,
                                  @RequestParam("reservation_id") String reservation_id,
                                  @RequestParam("exhibition_key") int exhibition_key) {

        List<ReservationDTO> userReserveCont = this.memberMapper.userReserveCont(reservation_id);
        ExhibitionDTO exhibitionDTO = this.memberMapper.userExhibition(exhibition_key);

        int totalPrice = userReserveCont.stream()
                .mapToInt(ReservationDTO::getReservation_price)
                .sum();

        model.addAttribute("userReserveCont", userReserveCont)
                .addAttribute("exhiDTO", exhibitionDTO)
                .addAttribute("totalPrice", totalPrice);

        return "my-page/userReserveCont";
    }

    @GetMapping("/userZZimList")
    public String userZZimList(HttpSession session, Model model) {

        MemberDTO memberInfo = (MemberDTO)session.getAttribute("mDTO");

        List<ZzimDTO> zzimList = this.memberMapper.userZzimList(memberInfo.getMember_key());

        List<ExhibitionDTO> zexhibitionList = new ArrayList<>();
        for(ZzimDTO zzim : zzimList) {
            ExhibitionDTO exhibition = this.memberMapper.userExhibition(zzim.getExhibition_key());
            zexhibitionList.add(exhibition);
        }

        System.out.println(zzimList);
        System.out.println(zexhibitionList);

        model.addAttribute("zzimList", zzimList)
                .addAttribute("exhibitionList", zexhibitionList);

        return "my-page/userZZimList";
    }

    @GetMapping("/userResign")
    public String userResign(HttpSession session, Model model) {

        return "my-page/userResign";
    }

    @PostMapping("/deleteMember")
    public void userResignOk(@RequestParam("member_pwd") String member_pwd, HttpSession session, HttpServletResponse response) throws IOException {

        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();

        int result = 0;

        System.out.println(result);

        if(session.getAttribute("mDTO") != null) {
            MemberDTO memberInfo = (MemberDTO) session.getAttribute("mDTO");
            if(passwordEncoder.matches(member_pwd, memberInfo.getMember_pwd())) {
                result = this.memberMapper.memberDelete(memberInfo.getMember_key());
            }
        }else if(session.getAttribute("cDTO") != null) {
            MemberDTO memberInfo = (MemberDTO) session.getAttribute("cDTO");
            if(passwordEncoder.matches(member_pwd, memberInfo.getMember_pwd())) {
                result = this.memberMapper.memberDelete(memberInfo.getMember_key());
            }
        }

        if(result == 1) {
            session.invalidate();

            out.println("<script>");
            out.println("alert('회원삭제에 성공했습니다.')");
            out.println("location.href='/'");
            out.println("</script>");
        }else {
            out.println("<script>");
            out.println("alert('등록된 회원정보와 입력하신 정보가 다릅니다.')");
            out.println("history.back()");
            out.println("</script>");
        }
    }

    @PostMapping("/deleteSocialMember")
    public void deleteSocialMember(@RequestParam("member_email") String member_email, HttpSession session, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();

        int result = 0;

        MemberDTO memberInfo = (MemberDTO) session.getAttribute("mDTO");

        if(member_email.equals(memberInfo.getMember_email())) {
            result = this.memberMapper.memberDelete(memberInfo.getMember_key());
        }

        if(result == 1) {
            session.invalidate();

            out.println("<script>");
            out.println("alert('회원삭제에 성공했습니다.')");
            out.println("location.href='/'");
            out.println("</script>");
        }else {
            out.println("<script>");
            out.println("alert('등록된 회원정보와 입력하신 정보가 다릅니다.')");
            out.println("history.back()");
            out.println("</script>");
        }
    }

  @GetMapping("/my-page/user")
    public String showUserProfile(Model model,HttpSession session) {

      MemberDTO memberInfo = (MemberDTO) session.getAttribute("mDTO");

      return "my-page/user";

  }

    @GetMapping("/my-page/userModify")
    public String modify(HttpSession session, Model model){

        return "my-page/userModify";
    }

    //비밀번호 일치 시 수정폼 이동
    @PostMapping("/mModify")
    public String modifyOk(@RequestParam("member_pwd") String pwd,
                           HttpSession session,
                           HttpServletResponse response) throws IOException {


        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();


        if (session.getAttribute("mDTO") != null) {
            MemberDTO memberInfo = (MemberDTO) session.getAttribute("mDTO");
            if (passwordEncoder.matches(pwd, memberInfo.getMember_pwd())) {

                return "my-page/userModify";
            }
        }else if (session.getAttribute("cDTO") != null) {
            MemberDTO companyInfo = (MemberDTO) session.getAttribute("cDTO");

            if (passwordEncoder.matches(pwd, companyInfo.getMember_pwd())) {

                return "my-page/userModify";
            }
        }

        // 비밀번호가 틀린 경우
        out.println("<script>");
        out.println("alert('비밀번호가 틀렸습니다');");
        out.println("location.href='/my-page/user';");
        out.println("</script>");
        out.flush();
        return null;
    }

    //정보수정완료
    @PostMapping("/mModifyOk")
    public String modifyOk(HttpServletResponse response, MemberDTO dto, HttpSession session) throws IOException {

        MemberDTO originalDto = null;

        if (session.getAttribute("mDTO") != null) {
            originalDto = (MemberDTO) session.getAttribute("mDTO");
        } else if (session.getAttribute("cDTO") != null) {
            originalDto = (MemberDTO) session.getAttribute("cDTO");
        }

        // 전화번호 포맷팅
        String phoneNumber = dto.getMember_phone().replaceAll("[^0-9]", "");
        if (phoneNumber.length() == 8) {
            phoneNumber = phoneNumber.substring(0, 4) + "-" + phoneNumber.substring(4);
        } else if (phoneNumber.length() == 9) {
            phoneNumber = phoneNumber.substring(0, 2) + "-" + phoneNumber.substring(2, 5) + "-" + phoneNumber.substring(5);
        } else if (phoneNumber.length() == 10) {
            if (phoneNumber.startsWith("02")) {
                phoneNumber = phoneNumber.substring(0, 2) + "-" + phoneNumber.substring(2, 6) + "-" + phoneNumber.substring(6);
            } else {
                phoneNumber = phoneNumber.substring(0, 3) + "-" + phoneNumber.substring(3, 6) + "-" + phoneNumber.substring(6);
            }
        } else if (phoneNumber.length() == 11) {
            phoneNumber = phoneNumber.substring(0, 3) + "-" + phoneNumber.substring(3, 7) + "-" + phoneNumber.substring(7);
        }
        dto.setMember_phone(phoneNumber);

        // 기존 비밀번호 설정
        if (originalDto != null) {
            dto.setMember_pwd(originalDto.getMember_pwd());
        }

        int result = this.memberMapper.memberUpdate(dto);

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        if (result > 0) {
            out.println("<script>");
            out.println("alert('정보를 수정했습니다.')");
            out.println("</script>");

            // 수정된 dto를 세션에 저장
            if (session.getAttribute("mDTO") != null) {
                session.setAttribute("mDTO", dto);
            } else if (session.getAttribute("cDTO") != null) {
                session.setAttribute("cDTO", dto);
            }

            return "my-page/userContent";
        } else {
            out.println("<script>");
            out.println("alert('정보를 수정하지 못했습니다.')");
            out.println("</script>");

            return "my-page/userModify";
        }
    }
}