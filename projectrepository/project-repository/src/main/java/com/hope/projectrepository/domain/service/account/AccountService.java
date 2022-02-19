package com.hope.projectrepository.domain.service.account;

import com.hope.projectrepository.domain.entity.User;
import com.hope.projectrepository.compatibility.dto.AccountDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public interface AccountService {
    public User createAccount(AccountDTO accountDTO);
    public void deleteCurrentAccount();
    public void logoutCurrentAccount(HttpServletRequest request, HttpServletResponse response);
    public void sendVerificationCode(String email, String key);
    public void verifyCode(String key, String code);
    public List<String> findLoginIdsByEmail(String email);
    public void changeAccountStateAndSendResetEmail(String email, String loginId);
    public String resetPw(String resetKey);
    public User getUserById(Long userId);
    public Boolean isExistLoginId(String loginId);
    public Boolean isExistNickname(String nickname);
}
