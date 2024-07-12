package com.artissue.service;

import com.google.zxing.WriterException;

import java.io.IOException;

public interface QrCodeService {
    public byte[] generateQrCode(String link, String reservation_id) throws IOException, WriterException;
}
