package com.artissue.controller;

import com.artissue.model.*;
import com.artissue.service.JoinService;
import com.artissue.service.MessageService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

    @PostMapping("/joinProc")
    public void joinProc(MemberDTO memberDTO, @RequestParam("userType") String userType, HttpServletResponse response) throws IOException {
        System.out.println("userType: " + userType);
        if ("company".equals(userType)) {
            memberDTO.setRole("ROLE_COMPANY");
        } else if ("individual".equals(userType)) {
            memberDTO.setRole("ROLE_USER");
        }

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

    @GetMapping("/user")
    public String userPage(HttpSession session, Model model) {

        return "my-page/user";
    }

    @GetMapping("/userReserveList")
    public String userReserveList(HttpSession session, Model model) {

        MemberDTO memberInfo = (MemberDTO)session.getAttribute("mDTO");

        List<ReservationDTO> userReserveList = this.memberMapper.userReserveList(memberInfo.getMember_key());

        List<ExhibitionDTO> exhibitionList = new ArrayList<>();
        for (ReservationDTO reservation : userReserveList) {
            ExhibitionDTO exhibition = this.memberMapper.userExhibitionList(reservation.getExhibition_key());
            exhibitionList.add(exhibition);
        }

        model.addAttribute("userReserveList", userReserveList)
                .addAttribute("exhibitionList", exhibitionList);

        return "my-page/userReserveList";
    }

    @GetMapping("/userZZimList")
    public String userZZimList(HttpSession session, Model model) {

        MemberDTO memberInfo = (MemberDTO)session.getAttribute("mDTO");

        List<ZzimDTO> zzimList = this.memberMapper.userZzimList(memberInfo.getMember_key());

        List<ExhibitionDTO> zexhibitionList = new ArrayList<>();
        for(ZzimDTO zzim : zzimList) {
            ExhibitionDTO exhibition = this.memberMapper.userExhibitionList(zzim.getExhibition_key());
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

  @GetMapping("/my-page/user")
    public String showUserProfile(Model model,HttpSession session) {

      MemberDTO memberInfo = (MemberDTO) session.getAttribute("mDTO");

      return "my-page/user";

  }

    @GetMapping("/my-page/userModify")
    public String modify(HttpSession session, Model model){

        return "my-page/userModify";
    }


    @PostMapping("/mModify")
    public String modifyOk(@RequestParam("member_pwd") String pwd,
                           HttpSession session,
                           HttpServletResponse response) throws IOException {

        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();

        MemberDTO memberInfo = (MemberDTO) session.getAttribute("mDTO");

        if (passwordEncoder.matches(pwd, memberInfo.getMember_pwd())) {
            out.println("<script>");
            out.println("alert('비밀번호가 일치합니다.')");
            out.println("</script>");
            return "my-page/userModify";
        } else {
            out.println("<script>");
            out.println("alert('비밀번호가 틀렸습니다')");
            out.println("</script>");
            return "my-page/user";

        }
    }

    @PostMapping("/mModifyOk")
    public String modifyOk(HttpServletResponse response,MemberDTO dto) throws IOException {

        System.out.println("dto>>"+dto);
       int result = this.memberMapper.memberUpdate(dto);

       response.setContentType("text/html; charset=UTF-8");
       PrintWriter out = response.getWriter();

       if(result > 0){
           out.println("<script>");
           out.println("alert('정보 수정을 성공하였습니다.')");
           out.println("</script>");
           return "my-page/userModify";
       }else{
           out.println("<script>");
           out.println("alert('정보수정을 실패하였습니다.')");
           out.println("</script>");
           return "my-page/userModify";
       }

    }
}