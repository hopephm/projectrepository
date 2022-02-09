package com.hope.projectrepository.config.service;

import com.hope.projectrepository.domain.service.mail.MailService;
import com.hope.projectrepository.domain.service.mail.implementation.MailServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailServiceConfig {
    @Bean
    public MailService getMailService(){
        return new MailServiceImpl();
    }
}
