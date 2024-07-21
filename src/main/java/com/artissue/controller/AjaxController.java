package com.artissue.controller;

import com.artissue.model.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ajax")
public class AjaxController {

    @Autowired
    private ExhibitionMapper exhibitionMapper;
    @Autowired
    private ReviewMapper reviewMapper;
    @Autowired
    private ReservationMapper reservationMapper;
    @Autowired
    private MemberMapper memberMapper;

    @GetMapping("/exhibition")
    public List<ExhibitionDTO> exhibitionList(@RequestParam("offset") int offset,
                                              @RequestParam("limit") int limit,
                                              @RequestParam(value = "keyword", required = false) String keyword,
                                              @RequestParam(value = "sellCodes", required = false) List<String> sellCodes,
                                              @RequestParam(value = "locCodes", required = false) List<String> locCodes) {

        List<ExhibitionDTO> exhibition_list = this.exhibitionMapper.getExhibitionsList(offset, limit, keyword, sellCodes, locCodes);

        return exhibition_list;
    }

    @GetMapping("/zzimCheck")
    public int checkZzim(@RequestParam("exhibition_key") int exhibition_key,
                         HttpSession session){
        MemberDTO member = (MemberDTO) session.getAttribute("mDTO");

        int memberKey = member.getMember_key();

        ZzimDTO checkZzim = this.exhibitionMapper.checkZzim(memberKey, exhibition_key);

        int result = 0;

        if(checkZzim != null){
            result = -1;
        }else{
            result = 1;
        }
        return result;
    }

    @PostMapping("/zzimDelete")
    public void deleteZzim(@RequestParam("exhibition_key") int exhibition_key,
                       HttpSession session) {
        MemberDTO member = (MemberDTO) session.getAttribute("mDTO");

        int memberKey = member.getMember_key();

        this.exhibitionMapper.deleteZzim(memberKey, exhibition_key);

    }

    @PostMapping("/zzimAdd")
    public void addZzim(@RequestParam("exhibition_key") int exhibition_key,
                           HttpSession session) {
        MemberDTO member = (MemberDTO) session.getAttribute("mDTO");

        int memberKey = member.getMember_key();

        this.exhibitionMapper.insertZzim(memberKey, exhibition_key);

    }

    @PostMapping("/writeReview")
    public int writeReview(@RequestParam("title") String title,
                            @RequestParam("cont") String cont,
                            @RequestParam("score") int score,
                            @RequestParam("exhibitionKey") int exhibition_key,
                            HttpSession session) {

        MemberDTO member = (MemberDTO) session.getAttribute("mDTO");

        int memberKey = member.getMember_key();

        List<ReservationDTO> reservationCheck = this.reservationMapper.checkReservation(memberKey, exhibition_key);

        System.out.println(memberKey +","+ exhibition_key);

        int result = 0;

        if(reservationCheck.isEmpty()){
            result = -1;
        }else {
            ReviewDTO reviewCheck = this.reviewMapper.checkReview(memberKey, exhibition_key);

            if(reviewCheck == null){
                ReviewDTO review = new ReviewDTO();

                review.setMember_key(memberKey);
                review.setExhibition_key(exhibition_key);
                review.setReview_title(title);
                review.setReview_score(score);
                review.setReview_cont(cont);

                result = this.reviewMapper.writeReview(review);
            }else{
                result = 2;
            }

        }

        return result;

    }

    @PostMapping("/reWriteReview")
    public int reWriteReview(@RequestParam("review_title") String review_title,
                             @RequestParam("review_cont") String review_cont,
                             @RequestParam("review_score") int review_score,
                             @RequestParam("exhibition_key") int exhibition_key,
                             HttpSession session){
        MemberDTO member = (MemberDTO) session.getAttribute("mDTO");

        int memberKey = member.getMember_key();

        ReviewDTO review = this.reviewMapper.checkReview(memberKey, exhibition_key);

        int review_key = review.getReview_key();

        int result = this.reviewMapper.updateReview(review_key, review_title, review_cont, review_score);

        return result;
    }

    @PostMapping("/deleteReview")
    public int deleteReview(@RequestParam("review_key") int review_key){

        int result = this.reviewMapper.deleteReview(review_key);

        return result;
    }

    @PostMapping("/updateReservation")
    public ResponseEntity<Map<String, Object>> updateReservation(@RequestParam("reservation_id") String reservation_id) {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("Received reservation_id: " + reservation_id);

            int result = reservationMapper.updateReservationBV(reservation_id);

            if(result > 0){
                response.put("success", true);
            } else {
                response.put("success", false);
                response.put("message", "삭제 실패");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "서버 오류: " + e.getMessage());
            e.printStackTrace();
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/companyNumberCheck")
    public String companyNoCheck(@RequestParam("company_no") String company_no, HttpServletRequest request,
                                 HttpServletResponse response) {
        String res = "available"; //사용가능
        System.out.println("company_no"+company_no);
        response.setContentType("text/html; charset=UTF-8");

        String str1 = company_no.substring(0, 3);
        String str2 = company_no.substring(3, 5);
        String str3 = company_no.substring(5);
        String newCompanyNo = str1 + "-" + str2 + "-" + str3;

        MemberDTO idCheck = (MemberDTO) this.memberMapper.companyInfoByNo(newCompanyNo);

        if (idCheck != null) {
            res = "unavailable";
        }
        return res;
    }
}

