package com.hope.projectrepository.domain.service.account.verifier.implementation.ver1;

import com.hope.projectrepository.domain.entity.User;
import com.hope.projectrepository.domain.service.account.verifier.AccountVerifier;
import com.hope.projectrepository.domain.service.mail.MailService;
import com.hope.projectrepository.util.RandomCode;
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
    private int limitTime;

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
        VerifyInfo info = new VerifyInfo(verificationCode, LocalDateTime.now().plusMinutes(limitTime));
        waitingVerificationMap.put(key, info);
        return verificationCode;
    }

    public void verifyCode(String key, String code) throws Exception{
        VerifyInfo info = waitingVerificationMap.get(key);
        waitingVerificationMap.remove(key);

        if(isNotExist(info))
            throw new Exception();      // 이메일 정보 예외
        if(isTimeout(info.getValidity()))
            throw new Exception();      // 시간초과 예외 
        if(!isCorrectCode(info.getCode(),code))
            throw new Exception();      // 코드 불일치 예외
    }  

    public boolean isNotExist(VerifyInfo info){
        if(info == null)
            return true;
        return false;
    }

    public boolean isTimeout(LocalDateTime limitTime){
        if(LocalDateTime.now().isAfter(limitTime))
            return true;
        return false;
    }

    public boolean isCorrectCode(String realCode, String givenCode){
        if(givenCode.equals(realCode))
            return true;
        return false;
    }

    public void sendPassword(User user, String pw){
        mailService.sendNewPassword(user.getLoginId(), user.getEmail(), pw);
    }
}

