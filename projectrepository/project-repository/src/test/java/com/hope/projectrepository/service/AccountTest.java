package com.hope.projectrepository.service;

import com.hope.projectrepository.domain.entity.User;
import com.hope.projectrepository.compatibility.dto.AccountDTO;
import com.hope.projectrepository.domain.repository.UserRepository;
import com.hope.projectrepository.domain.service.account.AccountService;
import com.hope.projectrepository.domain.service.account.verifier.AccountVerifier;
import com.hope.projectrepository.domain.service.account.verifier.implementation.AccountVerifierImpl;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class AccountTest {
    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountVerifierImpl accountVerifier;

    @Autowired
    private UserRepository userRepository;

    private static User user;
    private static AccountDTO accountDTO;
    private static MockHttpSession session;

    @BeforeAll
    public static void createMockObjs(){
        accountDTO = new AccountDTO("hopephm3", "1", "hopephm@naver.com", "감자골청년");

        user = User.builder()
                .loginId("hopephm2")
                .password("1")
                .email("hopephm@naver.com")
                .nickname("감자골청년2")
                .build();
        session = new MockHttpSession();
        session.setAttribute("user", user);
    }


    @Test
    @Transactional
    public void createAccountTest() throws Exception{
        User user1 = accountService.createAccount(accountDTO);
        User user2 = userRepository.findByLoginId(accountDTO.getLoginId());
        MatcherAssert.assertThat(user1, Matchers.is(user2));
    }

    @Test
    @Transactional
    public void deleteCurrentAccountTest() throws Exception{
        userRepository.save(user);
        accountService.deleteCurrentAccount(session);
        User user1 = userRepository.findByLoginId("hopephm2");
        MatcherAssert.assertThat(null, Matchers.is(user1));
    }

    @Test
    public void sendVerificationCodeTest() throws Exception{
        accountService.sendVerificationCodeForCheckEmail("hopephm@naver.com");
    }

    @Test
    public void verifyCodeTest() throws Exception{
        String code = accountVerifier.createVerifiactionCode("1");
        accountService.verifyCode("1",code);
    }
}
