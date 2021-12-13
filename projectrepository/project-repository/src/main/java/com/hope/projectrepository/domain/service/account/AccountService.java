package com.hope.projectrepository.domain.service.account;

import com.hope.projectrepository.domain.entity.User;
import com.hope.projectrepository.compatibility.dto.AccountDTO;

import javax.servlet.http.HttpSession;

public interface AccountService {
    public User createAccount(AccountDTO accountDTO);
    public void deleteCurrentAccount(HttpSession session);

    public void sendVerificationCode(String email);
    public void verifyCode(String email, String code);
    // 메일 인증 요청
    // 메일 인증 검사
    // 유저 id 검색
    // 유저 pw 변경
}
