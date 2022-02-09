package com.hope.projectrepository.domain.service.account;

import com.hope.projectrepository.domain.entity.User;
import com.hope.projectrepository.compatibility.dto.AccountDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public interface AccountService {
    // 실 계정 생성
    public User createAccount(AccountDTO accountDTO);
    
    // 실 계정 삭제
    public void deleteCurrentAccount();
    public void logoutCurrentAccount(HttpServletRequest request, HttpServletResponse response);
    
    // 이메일 인증 과정
    public void sendVerificationCode(String email, String key);
    public void verifyCode(String key, String code);
    
    // findID 과정
    public List<String> findLoginIdsByEmail(String email);

    // findPW 과정
    public void changeAccountStateAndSendResetEmail(String email, String loginId);
    public String resetPw(String resetKey);
    
    // 리소스 검사
    public User checkUserByLoginId(String loginId);
    public User checkUserByNickname(String nickname);
}
