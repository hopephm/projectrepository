package com.hope.projectrepository.domain.service.account;

import com.hope.projectrepository.domain.entity.User;
import com.hope.projectrepository.compatibility.dto.AccountDTO;
import com.hope.projectrepository.domain.service.account.manager.AccountManager;
import com.hope.projectrepository.domain.service.account.verifier.AccountVerifier;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;

public class AccountServiceImpl implements AccountService{
    @Autowired
    AccountManager accountManager;
    @Autowired
    AccountVerifier accountVerifier;

    public User createAccount(AccountDTO accountDTO){
        User user = null;
        try{
            user = User.builder()
                    .loginId(accountDTO.getLoginId())
                    .password(accountDTO.getPasswd())
                    .email(accountDTO.getEmail())
                    .nickname(accountDTO.getNickname())
                    .build();

            user = accountManager.createAccount(user);
        }catch(Exception e){
            // 같은 계정 존재 예외
            // 올바르지 않은 포맷 예외
        }
        return user;
    }

    public void deleteCurrentAccount(HttpSession session){
        try{
            User user = (User)session.getAttribute("user");
            accountManager.deleteAccount(user);
        }catch(Exception e){
            // 존재하지 않는 계정 예외
            // 현재 로그인 상태 아님 예외
        }
    }


    public void sendVerificationCode(String email){
        // checkAccount 내용이 이리로 와야함.


    }

    public void verifyCode(String email, String code){

    }
}
