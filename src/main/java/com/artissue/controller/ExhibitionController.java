package com.artissue.controller;

import com.artissue.model.ExhibitionDTO;
import com.artissue.model.ExhibitionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ExhibitionController {

    @Autowired
    private ExhibitionMapper exhibitionMapper;

    @GetMapping("/list")
    public String exhibitionList(){
        return "/exhibition/event";
    }

    @GetMapping("/exhi/content")
    public String exhibitionCont(@RequestParam("no") int no,
                                 Model model) {

        ExhibitionDTO exhibitionCont = this.exhibitionMapper.getExhibitionCont(no);

        model.addAttribute("Exhibition", exhibitionCont);

        return "exhibition/content";
    }

}
