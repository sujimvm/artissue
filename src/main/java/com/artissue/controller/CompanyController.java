package com.artissue.controller;

import com.artissue.model.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Controller
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private ExhibitionMapper exhibitionMapper;

    @GetMapping("/list")
    public String exhibitionCont(Model model, HttpSession session) {
        int member_key = ((MemberDTO)session.getAttribute("cDTO")).getMember_key();
        model.addAttribute("list", this.exhibitionMapper.getMyExhibitionsList(member_key));
        return "company/list";
    }


}
