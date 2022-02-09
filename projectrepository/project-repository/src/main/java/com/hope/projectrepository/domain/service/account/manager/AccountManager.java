package com.hope.projectrepository.domain.service.account.manager;

import com.hope.projectrepository.domain.entity.User;

import java.util.List;

public interface AccountManager {
    // 패스워드 인코딩 후 만들기
    public User createAccount(User user);
    public void deleteAccount(User user);
    public List<String> findAccountIdByEmail(String email);
    public User findUserByLoginId(String loginId);
    public User checkAccount(String email, String loginId);
    public String resetPw(String resetKey);
    public String changeAccountStateToResetPw(User user);
    public User getUserByLoginId(String loginId);
    public User getUserByNickname(String nickname);
}
