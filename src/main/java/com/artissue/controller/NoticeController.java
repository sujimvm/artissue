package com.artissue.controller;

import com.artissue.model.NoticeDTO;
import com.artissue.model.NoticeMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeMapper noticeMapper;

    @GetMapping("")
    public String noticeList(Model model) {

        List<NoticeDTO> noticeList = this.noticeMapper.noticeList();

        model.addAttribute("noticeList", noticeList);

        return "notice/noticeList";
    }

    @GetMapping("/insert")
    public String noticeInsert() {

        return "notice/noticeInsert";
    }

    @PostMapping("/insertOk")
    public void noticeInsertOk(NoticeDTO noticeDTO, HttpServletResponse response) throws IOException {

        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();

        int result = this.noticeMapper.noticeInsert(noticeDTO);

        if(result > 0) {
            out.println("<script>");
            out.println("alert('공지사항 추가 성공')");
            out.println("location.href='/notice'");
            out.println("</script>");
        }else {
            out.println("<script>");
            out.println("alert('공지사항 추가 실패')");
            out.println("history.back()");
            out.println("</script>");
        }

    }

    @GetMapping("/modify")
    public String noticeModify(@RequestParam("no") int notice_key, Model model) {

        NoticeDTO noticeCont = this.noticeMapper.noticeCont(notice_key);

        model.addAttribute("noticeCont", noticeCont);

        return "notice/noticeModify";
    }

    @PostMapping("/modifyOk")
    public void noticeModifyOk(NoticeDTO noticeDTO, HttpServletResponse response) throws IOException {

        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();

        int result = this.noticeMapper.noticeModify(noticeDTO);

        if(result > 0) {
            out.println("<script>");
            out.println("alert('공지사항 수정 성공')");
            out.println("location.href='/notice'");
            out.println("</script>");
        }else {
            out.println("<script>");
            out.println("alert('공지사항 수정 실패')");
            out.println("history.back()");
            out.println("</script>");
        }

    }

    @GetMapping("/delete")
    public void noticeDelete(@RequestParam("no") int notice_key, HttpServletResponse response) throws IOException {

        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();

        int result = this.noticeMapper.noticeDelete(notice_key);

        if(result > 0) {
            out.println("<script>");
            out.println("alert('공지사항 삭제 성공')");
            out.println("location.href='/notice'");
            out.println("</script>");
        }else {
            out.println("<script>");
            out.println("alert('공지사항 삭제 실패')");
            out.println("history.back()");
            out.println("</script>");
        }

    }

}
