package com.artissue.controller;

import com.artissue.model.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
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

        System.out.println(offset);
        System.out.println(limit);
        System.out.println(keyword);
        System.out.println(sellCodes);
        System.out.println(locCodes);

        List<ExhibitionDTO> exhibition_list = this.exhibitionMapper.getExhibitionsList(offset, limit, keyword, sellCodes, locCodes);

        return exhibition_list;
    }

    @PostMapping("/zzim")
    public int addZzim(@RequestParam("exhibitionKey") int exhibition_key,
                       HttpSession session, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");

        PrintWriter out = response.getWriter();

        MemberDTO member = (MemberDTO) session.getAttribute("mDTO");

        int memberKey = member.getMember_key();

        ZzimDTO checkZzim = this.exhibitionMapper.checkZzim(memberKey, exhibition_key);

        int result = 0;

        if (checkZzim == null) {
            out.println("<script>");
            out.println("<alert('찜리스트에 추가하였습니다')>");
            out.println("</script>");

            this.exhibitionMapper.insertZzim(memberKey, exhibition_key);

            result = -1;
        }else{
            out.println("<script>");
            out.println("<alert('찜리스트에서 삭제하였습니다')>");
            out.println("</script>");

            this.exhibitionMapper.deleteZzim(memberKey, exhibition_key);

            result = -1;
        }

        return result;
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
        }else{
            ReviewDTO review = new ReviewDTO();

            review.setMember_key(memberKey);
            review.setExhibition_key(exhibition_key);
            review.setReview_title(title);
            review.setReview_score(score);
            review.setReview_cont(cont);

            result = this.reviewMapper.writeReview(review);
        }

        System.out.println(result);

        return result;

    }

}
