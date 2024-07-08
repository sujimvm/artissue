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

}
