package com.hope.projectrepository.domain.service.verification;

import com.hope.projectrepository.domain.service.mail.MailService;
import com.hope.projectrepository.util.RandomCode;
import com.hope.projectrepository.util.Result;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// 해당 기능을 전부 accountVerifier로 옮기는 게 맞는듯

@Service
public class VerificationService {
    @Value("${app.verify.email.code_length}")
    private int codeLength;

    @Value("${app.verify.email.limit_time}")
    private int limitTime;

    @Autowired
    MailService mailService;


    // 주기적으로 비우는 batch 추가
    // key: email or loginId, value: {createTime, code}
    private static Map<String, VerifyInfo> verifyCodeStorage = new HashMap<>();

    public String createRandomCode(String key){
        String randomCode = RandomCode.getRandomCode(codeLength);
        VerifyInfo info = new VerifyInfo(randomCode, LocalDateTime.now().plusMinutes(limitTime));

//        이미 해당 키에 따른 값이 존재한다면 대체
        verifyCodeStorage.put(key, info);

        return randomCode;
    }

    public String verifyRandomCode(String key, String randomCode){
        VerifyInfo info = verifyCodeStorage.get(key);
        
        // 미존재 에러
        if(info == null){
            return Result.FAIL;
        }

        // 검증코드 불일치 에러
        if(!info.getCode().equals(randomCode)){
            return Result.FAIL;
        }

        // 검증시간 초과 에러
        if(LocalDateTime.now().isAfter(info.getValidity())){
            verifyCodeStorage.remove(key);
            return Result.FAIL;
        }

        // 만약 모두 옳다면
        if(info.getCode().equals(randomCode)){
            verifyCodeStorage.remove(key);
            return Result.SUCCESS;
        }

        return Result.FAIL;
    }

    public void printStorage(){
        Set<String> keys = verifyCodeStorage.keySet();

        for(String key: keys){
            System.out.println(key + ": " + verifyCodeStorage.get(key).getCode());
        }
    }
}

@AllArgsConstructor
@Setter
@Getter
class VerifyInfo{
    String code;
    LocalDateTime validity;
}
