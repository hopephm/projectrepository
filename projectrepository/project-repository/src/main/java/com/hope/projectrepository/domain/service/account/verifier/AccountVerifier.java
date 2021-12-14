package com.hope.projectrepository.domain.service.account.verifier;

import com.hope.projectrepository.domain.entity.User;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServer;

public interface AccountVerifier {
    public void sendVerificationCode(String email);
    public void sendVerificationCode(String email, String key);
    public void verifyCode(String key, String code) throws Exception;
    public void sendPassword(User user, String pw);
}
