package com.hope.projectrepository.domain.service.account.implementation;

import com.hope.projectrepository.domain.entity.User;
import com.hope.projectrepository.compatibility.dto.AccountDTO;
import com.hope.projectrepository.domain.service.account.AccountService;
import com.hope.projectrepository.domain.service.account.manager.AccountManager;
import com.hope.projectrepository.domain.service.account.verifier.AccountVerifier;
import com.hope.projectrepository.exception.ExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import sun.reflect.annotation.ExceptionProxy;

import javax.servlet.http.HttpSession;

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

    public void deleteCurrentAccount(HttpSession session){
        User user = (User)session.getAttribute("user");
        accountManager.deleteAccount(user);
    }


    public void sendVerificationCodeForCheckEmail(String email){
        // checkAccount 내용이 이리로 와야함.
        accountVerifier.sendVerificationCode(email);
    }

    public void verifyCodeForCheckEmail(String email, String code) throws Exception{
        accountVerifier.checkVerifyCode(email, code);
    }

    public void findAccountIdByEmail(String email) throws Exception{

    }

    public void startFindAccountPw(String loginId, String email) throws Exception{
//    public User checkAccount(String loginId, String email) throws Exception;
//    public void sendVerificationCodeForCheckAccountOwner(User user) throws Exception;
    }

    public void finishFindAccountPw(String loginId, String code) throws Exception{
//    public User verifyCodeForCheckAccountOwner(User user, String code) throws Exception;
//    public void resetPassword(User user);
    }
}
