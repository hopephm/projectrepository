package com.hope.projectrepository.domain.service.account;

import com.hope.projectrepository.domain.entity.User;
import com.hope.projectrepository.compatibility.dto.AccountDTO;

import javax.servlet.http.HttpSession;

public interface AccountService {
    // 실 계정 생성
    public User createAccount(AccountDTO accountDTO) throws Exception;
    
    // 실 계정 삭제
    public void deleteCurrentAccount(HttpSession session) throws Exception;
    
    // 이메일 인증 과정
    public void sendVerificationCodeForCheckEmail(String email) throws Exception;
    public void verifyCodeForCheckEmail(String email, String code) throws Exception;
    
    // findID 과정
    public void findAccountIdByEmail(String email) throws Exception;
    
    // findPW 과정
    public void startFindAccountPw(String loginId, String email) throws Exception;
    public void finishFindAccountPw(String loginId, String code) throws Exception;
}
