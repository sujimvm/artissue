package com.artissue.controller;

import com.artissue.model.NoticeDTO;
import com.artissue.model.NoticeMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class NoticeController {

    @Autowired
    private NoticeMapper noticeMapper;

    @GetMapping("/notice")
    public String noticeList(Model model) {

        List<NoticeDTO> noticeList = this.noticeMapper.noticeList();

        model.addAttribute("noticeList", noticeList);

        return "blog";
    }
}
