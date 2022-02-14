package com.hope.projectrepository.domain.service.login;

import com.hope.projectrepository.domain.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface LoginService {
    public UserDetailsService getUserDetailService();
    public User registerNormalUser();
    public User registerSocialUser();
}
