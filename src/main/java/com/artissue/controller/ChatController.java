package com.artissue.controller;

import com.artissue.ArtissueApplication;
import com.artissue.model.ChatRoom;
import com.artissue.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/rooms")
    public String rooms(Model model) {
        model.addAttribute("rooms", chatService.findAllRooms());
        return "chat";
    }

    @GetMapping("/create")
    public String createRoomForm() {

        System.out.println(ArtissueApplication.getAdminRoomId());

        return "chatroom";
    }

    @PostMapping("/create")
    @ResponseBody
    public ChatRoom createRoom(@RequestParam String name) {
        return chatService.createRoom(name);
    }
}