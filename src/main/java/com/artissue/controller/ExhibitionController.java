package com.artissue.controller;

import com.artissue.model.ExhibitionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExhibitionController {

    @Autowired
    private ExhibitionMapper exhibitionMapper;

    @GetMapping("/list")
    public String exhibitionList(){
        return "event";
    }

}
