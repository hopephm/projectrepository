package com.hope.projectrepository.domain.service.mail;

public interface MailService {
    public void sendVerificationMail(String to, String verificationCode);
    public void sendResetPwEmail(String to, String loginId, String resetUrl);
}
