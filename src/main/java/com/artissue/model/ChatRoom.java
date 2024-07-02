package com.artissue.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Data
public class ChatRoom {

    private String roomId;
    private String name;
    private Set<WebSocketSession> sessions;

    @Builder
    public ChatRoom(String roomId, String name, Set<WebSocketSession> sessions) {
        this.roomId = roomId;
        this.name = name;
        this.sessions = sessions != null ? sessions : new HashSet<>();
    }

    public void handleActions(WebSocketSession session, ChatMessage message, ChatMessage.MessageType type) {
        if (type == ChatMessage.MessageType.JOIN) {
            sessions.add(session);
            message.setMessage(message.getSender() + " joined the room.");
        } else if (type == ChatMessage.MessageType.LEAVE) {
            sessions.remove(session);
            message.setMessage(message.getSender() + " left the room.");
        }

        broadcast(message);
    }

    private void broadcast(ChatMessage message) {
        sessions.forEach(session -> {
            try {
                session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(message)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
