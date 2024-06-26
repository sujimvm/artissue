package com.artissue.service;


import com.artissue.model.ChatDTO;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    public ChatDTO processMessage(ChatDTO chatMessage) {
        // 메시지 처리 로직을 추가할 수 있습니다.
        // 예: 메시지 검증, 사용자 관리, 데이터베이스 저장 등
        return chatMessage;
    }

    public ChatDTO addUser(ChatDTO chatMessage) {
        // 사용자 추가 로직을 추가할 수 있습니다.
        return chatMessage;

    }
}
