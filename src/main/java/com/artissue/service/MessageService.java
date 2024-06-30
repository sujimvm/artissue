package com.artissue.service;

import com.artissue.model.MessageDTO;
import com.artissue.model.MessageMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import net.nurigo.sdk.message.model.Message;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Value("${coolsms.apiKey}")
    private String apiKey;

    @Value("${coolsms.apiSecret}")
    private String apiSecret;

    @Value("${coolsms.senderNumber}")
    private String senderNumber;

    private DefaultMessageService messageService;



    @PostConstruct
    public void init(){
        messageService = NurigoApp.INSTANCE.initialize(
                apiKey,
                apiSecret,
                "https://api.coolsms.co.kr"
        );
    }


    public SingleMessageSentResponse sendOne(String to, String verificationCode) {
        Message message = new Message();


        message.setFrom(senderNumber);
        message.setTo(to);
        message.setText("[WorkWave] 아래의 인증번호를 입력해주세요\n" + verificationCode);


        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));

        return response;
    }

    public void saveSMS(String phone, String verify_code) {

        MessageDTO messageDTO = new MessageDTO(phone, verify_code);

        messageDTO.setId(UUID.randomUUID().toString());
        messageDTO.setPhone(phone);
        messageDTO.setVerify_code(verify_code);

        messageMapper.saveSMS(messageDTO);

    }

    public String getVerifyCode(String phone) {

        return messageMapper.getVerifyCode(phone);

    }

    public void removeVerifyCode(String phone) {
        messageMapper.removeVerifyCode(phone);
    }
}
