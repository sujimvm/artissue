package com.artissue.service;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.stereotype.Service;
import net.nurigo.sdk.message.model.Message;

@Service
public class MessageService {

    public MessageService() {

        this.messageService = NurigoApp.INSTANCE.initialize("NCS64AITTPW9KJ63", "EQFLJVJZI9QFNXNETNT9EE4U7NSJLGQ9", "https://api.coolsms.co.kr");
    }

    private DefaultMessageService messageService;


    public SingleMessageSentResponse sendOne(String memberPhone, String verificationCode) {
        Message message = new Message();

        message.setFrom("01066076380");
        message.setTo(memberPhone);
        message.setText("[artissue] 아래의 인증번호를 입력해주세요\n" + verificationCode);

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));

        return response;
    }

}
