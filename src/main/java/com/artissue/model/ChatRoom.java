package com.artissue.model;

import com.artissue.ArtissueApplication;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
        String adminRoomId = ArtissueApplication.getAdminRoomId();

        if(adminRoomId.equals(this.roomId)) {
            if (type == ChatMessage.MessageType.JOIN) {
                sessions.add(session);

                Map<String, String> messageMap = new HashMap<String, String>();

                messageMap.put("type", "join");
                messageMap.put("sender", message.getSender());

                ObjectMapper om = new ObjectMapper();
                String msg = "";

                try {
                    msg = om.writeValueAsString(messageMap);
                }catch (Exception e){}
                message.setMessage( msg );
            }
        }else {
            if (type == ChatMessage.MessageType.JOIN) {
                sessions.add(session);
                message.setMessage(message.getSender() + " 님이 입장 하셨습니다.");
            } else if (type == ChatMessage.MessageType.LEAVE) {
                sessions.remove(session);
                message.setMessage(message.getSender() + " 님이 나갔습니다.");
            }
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

    @JsonProperty("roomId")
    public String getRoomId() {
        return roomId;
    }

}