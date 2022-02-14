package com.hope.projectrepository.config.service;

import com.hope.projectrepository.domain.service.login.LoginService;
import com.hope.projectrepository.domain.service.login.implementation.LoginServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoginServiceConfig {
    @Bean
    public LoginService getLoginService(){
        return new LoginServiceImpl();
    }
}
