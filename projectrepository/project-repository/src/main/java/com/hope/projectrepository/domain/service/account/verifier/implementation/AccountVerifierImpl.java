package com.hope.projectrepository.domain.service.account.verifier.implementation;

import com.hope.projectrepository.domain.service.account.verifier.AccountVerifier;
import com.hope.projectrepository.domain.service.mail.MailService;
import com.hope.projectrepository.exception.service.account.EmailVerificationDoesNotExistException;
import com.hope.projectrepository.exception.service.account.ResetTimeOutException;
import com.hope.projectrepository.exception.service.account.VerificationCodeDoesNotMatchException;
import com.hope.projectrepository.util.global.RandomCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

public class AccountVerifierImpl implements AccountVerifier {
    @Autowired
    MailService mailService;
    @Autowired
    WaitingVerificationMap waitingVerificationMap;

    @Value("${app.verify.email.code_length}")
    private int codeLength;

    @Value("${app.verify.email.limit_time}")
    private int limitMinute;

    public void sendVerificationCode(String email){
        String key = email;
        String verificationCode = createVerifiactionCode(key);
        mailService.sendVerificationMail(email, verificationCode);
    }

    public void sendVerificationCode(String email, String loginId){
        String key = loginId;
        String verificationCode = createVerifiactionCode(key);
        mailService.sendVerificationMail(email, verificationCode);
    }

    public String createVerifiactionCode(String key){
        String verificationCode = RandomCode.getRandomCode(codeLength);
        VerifyInfo info = new VerifyInfo(verificationCode, LocalDateTime.now().plusMinutes(limitMinute));
        waitingVerificationMap.put(key, info);
        return verificationCode;
    }

    public void verifyCode(String key, String code){
        VerifyInfo info = waitingVerificationMap.get(key);
        waitingVerificationMap.remove(key);

        if(isNotExist(info))
            throw new EmailVerificationDoesNotExistException();      // 이메일 정보 예외
        if(isTimeout(info.getLimit()))
            throw new ResetTimeOutException();      // 시간초과 예외
        if(!isCorrectCode(info.getCode(),code))
            throw new VerificationCodeDoesNotMatchException();      // 코드 불일치 예외
    }  

    public boolean isNotExist(VerifyInfo info){
        if(info == null)
            return true;
        return false;
    }

    public boolean isTimeout(LocalDateTime limit){
        if(LocalDateTime.now().isAfter(limit))
            return true;
        return false;
    }

    public boolean isCorrectCode(String realCode, String givenCode){
        if(givenCode.equals(realCode))
            return true;
        return false;
    }

    public void sendResetPwEmail(String email, String loginId, String resetUrl){
        mailService.sendResetPwEmail(email, loginId, resetUrl);
    }
}

