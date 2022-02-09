package com.hope.projectrepository.config.service;

import com.hope.projectrepository.domain.service.account.AccountService;
import com.hope.projectrepository.domain.service.account.implementation.AccountServiceImpl;
import com.hope.projectrepository.domain.service.account.manager.AccountManager;
import com.hope.projectrepository.domain.service.account.manager.implementation.AccountManagerImpl;
import com.hope.projectrepository.domain.service.account.manager.implementation.WaitingResetPwMap;
import com.hope.projectrepository.domain.service.account.verifier.AccountVerifier;
import com.hope.projectrepository.domain.service.account.verifier.implementation.AccountVerifierImpl;
import com.hope.projectrepository.domain.service.account.verifier.implementation.WaitingVerificationMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountServiceConfig {
    @Bean
    public AccountManager getAccountManager(){
        return new AccountManagerImpl();
    }

    @Bean
    public AccountVerifier getAccountVerifier(){
        return new AccountVerifierImpl();
    }

    @Bean
    public AccountService getAccountService(){
        return new AccountServiceImpl();
    }

    @Bean
    public WaitingVerificationMap getWaitingVerificationMap(){return new WaitingVerificationMap();}

    @Bean
    public WaitingResetPwMap getWaitingResetPwMap(){return new WaitingResetPwMap();}
}
