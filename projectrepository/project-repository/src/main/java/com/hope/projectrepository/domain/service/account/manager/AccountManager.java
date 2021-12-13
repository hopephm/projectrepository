package com.hope.projectrepository.domain.service.account.manager;

import com.hope.projectrepository.domain.entity.User;

public interface AccountManager {
    // 패스워드 인코딩 후 만들기
    public User createAccount(User user);
    public void deleteAccount(User user);
}
