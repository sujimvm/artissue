package com.artissue;

import com.artissue.model.ChatRoom;
import com.artissue.service.ChatService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ArtissueApplication {

    private static String adminRoomId;

    public static void main(String[] args) {
        SpringApplication.run(ArtissueApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            ChatService chatService = ctx.getBean(ChatService.class);

            String name = "admin_noti";

            ChatRoom chatRoom = chatService.createRoom(name);

            System.out.println(chatRoom.getRoomId());

            adminRoomId = chatRoom.getRoomId();

        };
    }

    public static String getAdminRoomId() {
        return adminRoomId;
    }

}
