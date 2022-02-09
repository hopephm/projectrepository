package com.hope.projectrepository.domain.service.mail.implementation;

import com.hope.projectrepository.domain.service.mail.MailService;
import com.hope.projectrepository.exception.service.mail.EmailSendException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class MailServiceImpl implements MailService {
    @Value("${app.mail.sender}")
    private String FROM;

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationMail(String to, String verificationCode){
        String title = getVerificationMailTitle();
        String text = getVerificationMailText(verificationCode);
        sendMail(title, text, to);
    }

    private String getVerificationMailTitle(){
        return "Project Repository: 인증코드";
    }

    private String getVerificationMailText(String verificationCode){
        String text = "인증코드: ";
        text += verificationCode;
        return text;
    }

    public void sendResetPwEmail(String to, String loginId, String resetUrl){
        String title = getResetPwMailTitle();
        String text = getResetPwMailText(loginId, resetUrl);
        sendMail(title, text, to);
    }

    private String getResetPwMailTitle(){
        return "Project Repository: 비밀번호 변경";
    }

    private String getResetPwMailText(String loginId, String resetUrl){
        String text = "아이디: " + loginId + "\n";
        text += "비밀번호 변경 URL: " + resetUrl + "\n";
        return text;
    }

    private void sendMail(String title, String text, String to){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM);
        message.setTo(to);
        message.setSubject(title);
        message.setText(text);

        try{
            mailSender.send(message);
        }catch(Exception e){
            // MailAuthenticationException, MailSendException, MailException
            e.printStackTrace();
//            throw new EmailSendException();
        }

    }
}
