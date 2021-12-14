package com.hope.projectrepository.domain.service.account;

import com.hope.projectrepository.domain.entity.User;
import com.hope.projectrepository.compatibility.dto.AccountDTO;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface AccountService {
    // 실 계정 생성
    public User createAccount(AccountDTO accountDTO);
    
    // 실 계정 삭제
    public void deleteCurrentAccount();
    public void deleteAccount(HttpSession session);
    
    // 이메일 인증 과정
    public void sendVerificationCode(String email, String key);
    public void verifyCode(String key, String code) throws Exception;
    
    // findID 과정
    public List<String> findAccountIdByEmail(String email);

    // findPW 과정
    public void checkAccount(String email, String loginId) throws Exception;
    public void putAccountToWaitingResetPwMap(String loginId);
    public void resetAndSendPassword(String loginId) throws Exception;

}
