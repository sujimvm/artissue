package com.artissue.controller;

import com.artissue.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/exhi")
public class ExhibitionController {

    @Autowired
    private ExhibitionMapper exhibitionMapper;
    @Autowired
    private ReviewMapper reviewMapper;
    @Autowired
    private MemberMapper memberMapper;

    @GetMapping("/list")
    public String exhibitionList(){
        return "/exhibition/event";
    }

    @GetMapping("/content")
    public String exhibitionCont(@RequestParam("no") int no,
                                 Model model) {

        ExhibitionDTO exhibitionCont = this.exhibitionMapper.getExhibitionCont(no);
        List<PriceDTO> exhibitionPrice = this.exhibitionMapper.getExhibitionPrice(no);
        List<ReviewDTO> exhibitionReview = this.reviewMapper.getExhibitionReview(no);

        List<String> nicknames = new ArrayList<>();
        for(ReviewDTO review : exhibitionReview){
            String member_nickname = this.memberMapper.getMemberNickname(review.getMember_key());

            nicknames.add(member_nickname);
        }

        model.addAttribute("Exhibition", exhibitionCont);
        model.addAttribute("Price", exhibitionPrice);
        model.addAttribute("Review", exhibitionReview);
        model.addAttribute("Nicknames", nicknames);

        return "exhibition/content";
    }

}
