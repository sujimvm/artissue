package com.artissue.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO {

        public enum MessageType {
                ENTER, TALK, OUT
        }

        private MessageType type;
        private String roomId;
        private String sender;
        private String message;
        private String time;

}
