package com.hope.projectrepository.domain.service.account.verifier;

public interface AccountVerifier {
    public void sendVerificationCode(String email);
    public void checkVerifyCode(String email, String code) throws Exception;
}
