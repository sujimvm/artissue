package com.artissue.controller;

import com.artissue.model.ExhibitionDTO;
import com.artissue.model.ExhibitionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ajax")
public class AjaxController {

    @Autowired
    private ExhibitionMapper exhibitionMapper;

    @GetMapping("/exhibition")
    public List<ExhibitionDTO> exhibitionList(@RequestParam("offset") int offset, @RequestParam("limit") int limit) {

        System.out.println(offset);
        System.out.println(limit);

        List<ExhibitionDTO> exhibition_list = this.exhibitionMapper.getExhibitionsList(offset, limit);

        return exhibition_list;
    }

}
