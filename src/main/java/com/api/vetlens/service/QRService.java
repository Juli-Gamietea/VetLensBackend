package com.api.vetlens.service;

import com.api.vetlens.exceptions.ApiException;
import com.api.vetlens.exceptions.NotFoundException;
import com.api.vetlens.repository.DiagnosisRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class QRService {
    private final DiagnosisRepository diagnosisRepository;

    public byte[] getQr(Integer diagnosisId) {
        boolean exists = diagnosisRepository.existsById(diagnosisId);
        if (!exists) {
            throw new NotFoundException("Diagnosis no encontrada");
        }
        try {
            return generateQR(diagnosisId);
        } catch (Exception e) {
            throw new ApiException("Error generando el QR " + e.getMessage());
        }
    }

    private byte[] generateQR(Integer diagnosisId) throws WriterException, IOException {
        var qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(diagnosisId.toString(), BarcodeFormat.QR_CODE, 400, 400);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", baos);
        byte[] qrCodeBytes = baos.toByteArray();
        MultipartFile file = new MockMultipartFile(
                "qr.png",
                "qr.png",
                MediaType.IMAGE_PNG_VALUE,
                qrCodeBytes
        );

        return file.getBytes();
    }
}
