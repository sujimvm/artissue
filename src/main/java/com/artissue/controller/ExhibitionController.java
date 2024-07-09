package com.artissue.controller;

import com.artissue.model.ExhibitionDTO;
import com.artissue.model.ExhibitionMapper;
import com.artissue.model.PriceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/exhi")
public class ExhibitionController {

    @Autowired
    private ExhibitionMapper exhibitionMapper;

    @GetMapping("/list")
    public String exhibitionList(){
        return "/exhibition/event";
    }

    @GetMapping("/content")
    public String exhibitionCont(@RequestParam("no") int no,
                                 Model model) {

        ExhibitionDTO exhibitionCont = this.exhibitionMapper.getExhibitionCont(no);
        List<PriceDTO> exhitionPrice = this.exhibitionMapper.getExhibitionPrice(no);

        model.addAttribute("Exhibition", exhibitionCont);
        model.addAttribute("Price", exhitionPrice);

        return "exhibition/content";
    }

}
