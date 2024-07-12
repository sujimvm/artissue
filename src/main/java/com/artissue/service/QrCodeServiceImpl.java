package com.artissue.service;

import com.artissue.model.ReservationMapper;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class QrCodeServiceImpl implements QrCodeService {
    private final ReservationMapper reservationMapper; // QrCodeMapper 빈 객체 주입을 위한 final 필드

    // 매개변수로 받은 문자열 링크를 이용해 QR 코드를 생성하고 DB에 삽입한 후, 생성된 QR 코드 바이트 배열을 반환하는 메서드
    public byte[] generateQrCode(String link, String reservation_id) throws IOException, WriterException {

        // QR 코드 생성
        byte[] qrCodeBytes = QRCodeGenerator.generateQRCodeImage(link);

        String qrCode = Base64.getEncoder().encodeToString(qrCodeBytes);
        // DB에 QR 코드 삽입
        reservationMapper.insertQrCode(reservation_id, qrCode);

        return qrCodeBytes; // 생성된 QR 코드 바이트 배열을 반환
    }
}