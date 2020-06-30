package org.base.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSender {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String emailUsername;

    public void sendEmailMessage(String toEmail, String subjectOfMessage, String message){
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(emailUsername);
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subjectOfMessage);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }
}
