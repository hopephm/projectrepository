package com.hope.projectrepository.domain.service.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService{
    @Value("${app.mail.sender}")
    private String FROM;

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleMail(String to, String subject, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public void sendRandomCode(String to, String randomCode){
        String subject = "Project Repository: 인증코드";
        String text = "인증코드: ";
        text += randomCode;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public void sendNewPassword(String loginId, String to, String newPw){
        String subject = "Project Repository: 임시 비밀번호";
        String text = "아이디: " + loginId + "\n";
        text += "임시 비밀번호: " + newPw + "\n";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
