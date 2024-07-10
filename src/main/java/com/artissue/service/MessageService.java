package com.artissue.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import net.nurigo.sdk.message.model.Message;

@Service
@RequiredArgsConstructor
public class MessageService {



    private DefaultMessageService messageService;

    @Value("${coolsms.apiKey}")
    private String apiKey;

    @Value("${coolsms.apiSecret}")
    private String apiSecret;

    @Value("${coolsms.senderNumber}")
    private String senderNumber;


    @PostConstruct
    public void init(){
        messageService = NurigoApp.INSTANCE.initialize(
                apiKey,
                apiSecret,
                "https://api.coolsms.co.kr"
        );
    }

    public SingleMessageSentResponse sendOne(String memberPhone, String verificationCode) {
        Message message = new Message();

        message.setFrom(senderNumber);
        message.setTo(memberPhone);
        message.setText("[artissue] 아래의 인증번호를 입력해주세요\n" + verificationCode);

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));

        return response;
    }

    public SingleMessageSentResponse sendReserve(String memberPhone, String verificationCode) {
        Message message = new Message();

        message.setFrom(senderNumber);
        message.setTo(memberPhone);
        message.setText(verificationCode);

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));

        return response;
    }

}
