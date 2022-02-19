package com.hope.projectrepository.domain.service.account.implementation;

import com.hope.projectrepository.domain.entity.User;
import com.hope.projectrepository.compatibility.dto.AccountDTO;
import com.hope.projectrepository.domain.service.account.AccountService;
import com.hope.projectrepository.domain.service.account.manager.AccountManager;
import com.hope.projectrepository.domain.service.account.verifier.AccountVerifier;
import com.hope.projectrepository.exception.service.account.AccountDoesNotExistException;
import com.hope.projectrepository.util.global.ContextManager;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

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
        HttpSession session = ContextManager.getCurrentSession();
        deleteAccount(session);
    }

    public void deleteAccount(HttpSession session){
        User user = (User)session.getAttribute("user");
        accountManager.deleteAccount(user);
    }

    public void logoutCurrentAccount(HttpServletRequest request, HttpServletResponse response){
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
    }

    public void sendVerificationCode(String email, String key){
        accountVerifier.sendVerificationCode(email, key);
    }

    public void verifyCode(String key, String code){
        accountVerifier.verifyCode(key, code);
    }

    public List<String> findLoginIdsByEmail(String email){
        List<String> ids = accountManager.findAccountIdByEmail(email);
        return ids;
    }

    public void changeAccountStateAndSendResetEmail(String email, String loginId){
        User user = accountManager.checkAccount(email, loginId);
        if(user == null)
            throw new AccountDoesNotExistException();

        String resetUrl = accountManager.changeAccountStateToResetPw(user);

        accountVerifier.sendResetPwEmail(email, loginId, resetUrl);
    }

    public String resetPw(String resetKey){
        String newPw = accountManager.resetPw(resetKey);

        return newPw;
    }

    public Boolean isExistLoginId(String loginId){
        User user = accountManager.getUserByLoginId(loginId);
        if(user == null)
            return false;
        return true;
    }

    public Boolean isExistNickname(String nickname){
        User user = accountManager.getUserByNickname(nickname);
        if(user == null)
            return false;
        return true;
    }

    public User getUserById(Long userId){
        User user = accountManager.getUserById(userId);
        if(user == null)
            throw new AccountDoesNotExistException();
        return user;
    }

}
