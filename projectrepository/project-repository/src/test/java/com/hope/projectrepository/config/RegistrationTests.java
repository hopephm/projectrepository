package com.hope.projectrepository.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@SpringBootTest
public class RegistrationTests {
    @Autowired
    ClientRegistrationRepository clientRegistrationRepository;

    @Test
    public void contextLoadTest(){
    }
}
