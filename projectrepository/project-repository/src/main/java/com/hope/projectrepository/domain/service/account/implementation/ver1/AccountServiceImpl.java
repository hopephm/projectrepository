package com.hope.projectrepository.domain.service.account.implementation.ver1;

import com.hope.projectrepository.domain.entity.User;
import com.hope.projectrepository.compatibility.dto.AccountDTO;
import com.hope.projectrepository.domain.service.account.AccountService;
import com.hope.projectrepository.domain.service.account.manager.AccountManager;
import com.hope.projectrepository.domain.service.account.verifier.AccountVerifier;
import com.hope.projectrepository.exception.ExceptionHandler;
import com.hope.projectrepository.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import sun.reflect.annotation.ExceptionProxy;

import javax.servlet.http.HttpSession;
import java.util.List;


// 예외를 전부 던지고 Controller에서 예외에 맞게 응답을 보내자!
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountManager accountManager;
    @Autowired
    AccountVerifier accountVerifier;
    
    public User createAccount(AccountDTO accountDTO){
        User user = null;
        user = transformAccountDtoToUser(accountDTO);
        user = accountManager.createAccount(user);
        return user;
    }

    public User transformAccountDtoToUser(AccountDTO accountDTO){
        return User.builder()
                .loginId(accountDTO.getLoginId())
                .password(accountDTO.getPasswd())
                .email(accountDTO.getEmail())
                .nickname(accountDTO.getNickname())
                .build();
    }

    public void deleteCurrentAccount(){
        HttpSession session = Util.getCurrentSession();
        deleteAccount(session);
    }

    public void deleteAccount(HttpSession session){
        User user = (User)session.getAttribute("user");
        accountManager.deleteAccount(user);
        // account 처리에 대한.. 예외..
    }

    public void sendVerificationCode(String email, String key){
        accountVerifier.sendVerificationCode(email, key);
        // 메일 전송 실패 등에 대한 것을 던지자
        // 메일 전송 프론트 비동기처리 하자
    }

    public void verifyCode(String key, String code) throws Exception{
        accountVerifier.verifyCode(key, code);
        // verify코드 불일치에 대한 예외 등등을 던지자
    }

    public List<String> findAccountIdByEmail(String email){
        List<String> ids = accountManager.findAccountIdByEmail(email);
        // ids 미존재에 대한 예외를 날리자
        
        return ids;
    }

    public void checkAccount(String email, String loginId) throws Exception{
        User user = accountManager.checkAccount(email, loginId);
        
        // Account 존재결과를 예외로 던지자
        if(user == null) throw new Exception();
    }

    public void putAccountToWaitingResetPwMap(String loginId){
        User user = accountManager.findUserByLoginId(loginId);
        accountManager.putAccountToWaitingResetPwMap(user);
        
        // Account가 이미 대기중이라면 예외처리 하자(물론 Manager에서)
    }

    public void resetAndSendPassword(String loginId) throws Exception{
        User user = accountManager.findUserByLoginId(loginId);
        String newPw = accountManager.resetPassword(user);
        accountVerifier.sendPassword(user, newPw);
        
        // reset 실패에 대한 이유를 예외로 던지자 (대기 Set에 미존재 등)
    }
}
