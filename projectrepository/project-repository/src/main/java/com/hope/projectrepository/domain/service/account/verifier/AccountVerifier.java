package com.hope.projectrepository.domain.service.account.verifier;

public interface AccountVerifier {
    public void sendVerificationCode(String email, String key);
    public void verifyCode(String key, String code);
    public void sendResetPwEmail(String email, String loginId, String resetUrl);
}
