package com.api.vetlens.service;

import com.api.vetlens.exceptions.EmailNotSendException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender sender;

    public void sendEmail(String email, String subject, String textMessage) {
        new Thread(() -> {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            try {
                helper.setTo(email);
                helper.setText(textMessage, true);
                helper.setSubject(subject);
                sender.send(message);
            } catch (MessagingException e) {
                throw new EmailNotSendException("No se pudo enviar el correo");
            }
        }).start();
    }
}
