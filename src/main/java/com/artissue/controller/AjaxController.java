package com.artissue.controller;

import com.artissue.model.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ajax")
public class AjaxController {

    @Autowired
    private ExhibitionMapper exhibitionMapper;
    @Autowired
    private ReviewMapper reviewMapper;
    @Autowired
    private ReservationMapper reservationMapper;

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

        ReservationDTO reservationCheck = this.reservationMapper.checkReservation(memberKey, exhibition_key);

        int result = 0;

        if(reservationCheck == null){
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

        System.out.println();

        int memberKey = member.getMember_key();

        ReviewDTO review = this.reviewMapper.checkReview(memberKey, exhibition_key);

        int review_key = review.getReview_key();

        int result = this.reviewMapper.updateReview(review_key, review_title, review_cont, review_score);

        return result;
    }

}
