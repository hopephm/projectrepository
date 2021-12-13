package com.hope.projectrepository.config.oauth2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OAuth2Config {
    private final String CLIENT_GOOGLE = "google";
    private final String CLIENT_FACEBOOK = "facebook";
    private final String CLIENT_NAVER = "naver";
    private final String CLIENT_KAKAO = "kakao";

    @Bean
    public ClientRegistrationRepository ClientRegistrationRepository(
            OAuth2ClientProperties oAuth2ClientProperties,
            @Value("${app.security.oauth2.client.registration.naver.client-id}") String naverClientId,
            @Value("${app.security.oauth2.client.registration.naver.client-secret}") String naverClientSecret,
            @Value("${app.security.oauth2.client.registration.kakao.client-id}") String kakaoClientId,
            @Value("${app.security.oauth2.client.registration.kakao.client-secret}") String kakaoClientSecret
    ){
        List<ClientRegistration> registrationList = new ArrayList<>();

        { // Google Registration
            OAuth2ClientProperties.Registration registration = oAuth2ClientProperties.getRegistration().get(CLIENT_GOOGLE);
            registrationList.add(
                    CommonOAuth2Provider.GOOGLE.getBuilder(CLIENT_GOOGLE)
                            .clientId(registration.getClientId())
                            .clientSecret(registration.getClientSecret())
                            .scope("email", "profile")
                            .build()
            );
        }

        {   // Facebook Registration
            OAuth2ClientProperties.Registration registration = oAuth2ClientProperties.getRegistration().get(CLIENT_FACEBOOK);
            registrationList.add(
                    CommonOAuth2Provider.FACEBOOK.getBuilder(CLIENT_FACEBOOK)
                            .clientId(registration.getClientId())
                            .clientSecret(registration.getClientSecret())
                            .userInfoUri("https://graph.facebook.com/me?fields=id,name,email,link")
                            .scope("email")
                            .build()
            );
        }

        // Naver Registration
        registrationList.add(
                CustomOAuth2Provider.NAVER.getBuilder(CLIENT_NAVER)
                .clientId(naverClientId)
                .clientSecret(naverClientSecret)
                .build()
        );

        // Kakao Registration
        registrationList.add(
                CustomOAuth2Provider.KAKAO.getBuilder(CLIENT_KAKAO)
                        .clientId(kakaoClientId)
                        .clientSecret(kakaoClientSecret)
                        .build()
        );

        return new InMemoryClientRegistrationRepository(registrationList);
    }
}