package com.artissue.controller;

import com.artissue.model.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Controller
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    private ReservationMapper reservationMapper;
    @Autowired
    private ExhibitionMapper exhibitionMapper;
    @Autowired
    private CompanyMapper companyMapper;

    @GetMapping("/exList")
    public String getMyExhibitionsList(Model model, HttpSession session) {
        int member_key = ((MemberDTO)session.getAttribute("cDTO")).getMember_key();
        model.addAttribute("list", this.exhibitionMapper.getMyExhibitionsList(member_key));
        return "company/exhiList";
    }

    @GetMapping("/reList")
    public String getMyExhibitionsReservationList(@RequestParam("no") int exhibition_key, Model model, HttpSession session) {
        int member_key = ((MemberDTO)session.getAttribute("cDTO")).getMember_key();
        model.addAttribute("list", this.reservationMapper.getMyExhibitionsReservationList(member_key,exhibition_key));
        return "company/reserList";
    }

    @GetMapping("/insert")
    public String insertExhibition() {
        return "company/exhiInsert";
    }

    @PostMapping("/insertOk")
    public void insertOkExhibition(ExhibitionDTO exhibitionDTO,
                                   @RequestParam(value = "ticket_name", required = false) List<String> ticketNames,
                                   @RequestParam(value = "ticket_price", required = false) List<Integer> ticketPrices,
                                   HttpSession session, HttpServletResponse response) throws IOException {

        MemberDTO company = (MemberDTO) session.getAttribute("cDTO");

        exhibitionDTO.setCompany_number(company.getCompany_number());

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        int result = this.exhibitionMapper.insertExhibition(exhibitionDTO);

        System.out.println(result);

        if(result > 0){
            int exhibition_key = exhibitionDTO.getExhibition_key();

            System.out.println(exhibition_key);

            System.out.println(ticketNames);

            if(!ticketNames.isEmpty()){

                for (int i = 0; i < ticketNames.size(); i++) {
                    String price_option = ticketNames.get(i);
                    int price = ticketPrices.get(i);

                    this.exhibitionMapper.insertExhibitionPrice(exhibition_key, price_option, price);

                }
            }

            out.println("<script>");
            out.println("alert('전시회 등록에 성공하였습니다.')");
            out.println("location.href='/company/exList'");
            out.println("</script>");
        }else{
            out.println("<script>");
            out.println("alert('전시회 등록에 실패하였습니다.')");
            out.println("history.back()");
            out.println("</script>");
        }

    }

    @GetMapping("/modify")
    public String modifyExhibition(@RequestParam("no") int exhibition_key, Model model, HttpSession session) {

        MemberDTO company = (MemberDTO) session.getAttribute("cDTO");

        int member_key = company.getMember_key();

        ExhibitionDTO exhibitionDTO = this.exhibitionMapper.getExhibitionModifyCont(exhibition_key, member_key);
        List<PriceDTO> priceDTO = this.exhibitionMapper.getExhibitionPrice(exhibition_key);

        model.addAttribute("exhibition", exhibitionDTO);
        model.addAttribute("priceList", priceDTO);

        return "company/exhiModify";
    }

    @PostMapping("/modifyOk")
    public void modifyOkExhibition(ExhibitionDTO exhibitionDTO,
                                   @RequestParam(value = "price_key", required = false) List<Integer> priceKeys,
                                   @RequestParam(value = "ticket_name", required = false) List<String> ticketNames,
                                   @RequestParam(value = "ticket_price", required = false) List<Integer> ticketPrices,
                                   @RequestParam(value = "delete_price_key", required = false) List<Integer> deletePriceKeys,
                                   HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        int result = this.exhibitionMapper.updateExhibition(exhibitionDTO);

        if(result > 0){
            if(deletePriceKeys != null && !deletePriceKeys.isEmpty()){
                for(int  delete_key : deletePriceKeys){
                    this.exhibitionMapper.deletePrice(delete_key);
                }
            }

            for(int i = 0; i < priceKeys.size(); i++){
                int price_key = priceKeys.get(i);
                String price_option = ticketNames.get(i);
                int price = ticketPrices.get(i);

                if(price_key == 0) {
                    // 새로운 가격 추가
                    this.exhibitionMapper.insertExhibitionPrice(exhibitionDTO.getExhibition_key(), price_option, price);
                } else {
                    // 기존 가격 업데이트
                    this.exhibitionMapper.updateExhibitionPrice(price_key, exhibitionDTO.getExhibition_key(), price_option, price);
                }
            }

            out.println("<script>");
            out.println("alert('전시회 수정에 성공하였습니다.')");
            out.println("location.href='/company/exList'");
            out.println("</script>");
        }else{
            out.println("<script>");
            out.println("alert('전시회 수정에 실패하였습니다.')");
            out.println("history.back()");
            out.println("</script>");
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    public int deleteExhibition(@RequestParam("exhibition_key") int exhibition_key, HttpSession session) {

        ExhibitionDTO exhibitionDTO = this.exhibitionMapper.getExhibitionCont(exhibition_key);

        MemberDTO company = (MemberDTO) session.getAttribute("cDTO");

        int result = 0;
        if(exhibitionDTO.getMember_key() == company.getMember_key()){
            result = this.exhibitionMapper.deleteExhibition(exhibition_key);

            if(result > 0){
               result = 1;
            }else{
                result = 0;
            }
        }else{
           result = -1;
        }

        return result;
    }

}
