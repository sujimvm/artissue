package com.artissue.service;

import com.artissue.model.ChatMessage;
import com.artissue.model.ChatRoom;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {

    private final ChatService chatService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Connected to WebSocket server.");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("Disconnected from WebSocket server.");
        String roomId = getRoomId(session);
        ChatRoom room = chatService.findRoomById(roomId);
        String sender = (String) session.getAttributes().get("sender");
        if (sender != null) {
            room.handleActions(session, ChatMessage.builder()
                    .type(ChatMessage.MessageType.LEAVE)
                    .roomId(roomId)
                    .sender(sender)
                    .message(sender + " left the room")
                    .build(), ChatMessage.MessageType.LEAVE);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String roomId = getRoomId(session);
        ChatRoom room = chatService.findRoomById(roomId);
        ChatMessage chatMessage = objectMapper.readValue(message.getPayload(), ChatMessage.class);

        if (chatMessage.getType() == ChatMessage.MessageType.JOIN) {
            session.getAttributes().put("sender", chatMessage.getSender());
        }

        room.handleActions(session, chatMessage, chatMessage.getType());
    }

    private String getRoomId(WebSocketSession session) {
        return session.getUri().getPath().split("/")[3];
    }
}
