package com.hope.projectrepository.config.domain;

import com.hope.projectrepository.domain.service.account.AccountService;
import com.hope.projectrepository.domain.service.account.implementation.AccountServiceImpl;
import com.hope.projectrepository.domain.service.account.manager.AccountManager;
import com.hope.projectrepository.domain.service.account.manager.implementation.AccountManagerImpl;
import com.hope.projectrepository.domain.service.account.verifier.AccountVerifier;
import com.hope.projectrepository.domain.service.account.verifier.implementation.AccountVerifierImpl;
import com.hope.projectrepository.domain.service.account.verifier.implementation.WaitingVerificationMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImplConfig {
    @Bean
    public AccountManager accountManager(){
        return new AccountManagerImpl();
    }

    @Bean
    public AccountVerifier accountVerifier(){
        return new AccountVerifierImpl();
    }

    @Bean
    public AccountService accountService(){
        return new AccountServiceImpl();
    }

    @Bean
    public WaitingVerificationMap waitingVerificationMap(){return new WaitingVerificationMap();}

}
