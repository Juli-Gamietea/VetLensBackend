package com.api.vetlens.service;

import com.api.vetlens.exceptions.EmailNotSendException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

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
            } catch (Exception e) {
                throw new EmailNotSendException("No se pudo enviar el correo");
            }
        }).start();
    }

    public void sendEmailWithAttachment(String email, String subject, String textMessage, File attachment) {
        new Thread(() -> {
            try {
                MimeMessage message = sender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(email);
                helper.setText(textMessage, true);
                helper.setSubject(subject);
                helper.addAttachment(attachment.getName(), attachment);
                sender.send(message);
            } catch (Exception e) {
                throw new EmailNotSendException("No se pudo enviar el correo");
            }
        }).start();
    }
}
