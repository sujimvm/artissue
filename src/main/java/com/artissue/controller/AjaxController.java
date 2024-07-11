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

    @PostMapping("/writeReview")
    public int writeReview(@RequestParam("title") String title,
                            @RequestParam("cont") String cont,
                            @RequestParam("score") int score,
                            @RequestParam("exhibitionKey") int exhibition_key,
                            HttpSession session) {

        MemberDTO member = (MemberDTO) session.getAttribute("mDTO");

        int memberKey = member.getMember_key();

        ReviewDTO review = new ReviewDTO();

        review.setMember_key(memberKey);
        review.setExhibition_key(exhibition_key);
        review.setReview_title(title);
        review.setReview_score(score);
        review.setReview_cont(cont);

        int result = this.reviewMapper.writeReview(review);

        return result;

    }

}
