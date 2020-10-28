package com.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailSender {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;


    public void send(String emailTo, String subject, String message) {
        MimeMessage message_obj = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message_obj, true);
            helper.setSubject(subject);
            helper.setTo(emailTo);
            helper.setText(message, true);
            mailSender.send(message_obj);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
