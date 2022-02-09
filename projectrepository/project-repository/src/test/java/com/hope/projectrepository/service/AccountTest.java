package com.hope.projectrepository.service;

import com.hope.projectrepository.domain.entity.User;
import com.hope.projectrepository.compatibility.dto.AccountDTO;
import com.hope.projectrepository.domain.repository.UserRepository;
import com.hope.projectrepository.domain.service.account.AccountService;
import com.hope.projectrepository.domain.service.account.manager.AccountManager;
import com.hope.projectrepository.domain.service.account.verifier.implementation.AccountVerifierImpl;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class AccountTest {
    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private AccountVerifierImpl accountVerifier;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder pwEncoder;

    private static User user1;
    private static User user2;
    private static AccountDTO accountDTO;
    private static MockHttpSession session;

    @BeforeAll
    public static void createMockObjs(){
        accountDTO = new AccountDTO("hopephm1", "1", "hopephm@naver.com", "감자골청년");

        user1 = User.builder()
                .loginId("hopephm2")
                .password("1")
                .email("hopephm@naver.com")
                .nickname("감자골청년2")
                .build();

        user2 = User.builder()
                .loginId("hopephm3")
                .password("1")
                .email("hopephm@naver.com")
                .nickname("감자골청년3")
                .build();
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
//        userRepository.save(user1);
//
//        session = new MockHttpSession();
//        session.setAttribute("user", user1);
//
////        accountService.deleteAccount(session);
//        User user1 = userRepository.findByLoginId("hopephm2");
//        MatcherAssert.assertThat(null, Matchers.is(user1));
    }

    @Test
    public void sendVerificationCodeTest() throws Exception{
        accountService.sendVerificationCode("hopephm@naver.com", "hopephm@naver.com");
    }

    @Test
    public void verifyCodeSuccessTest() throws Exception{
        String code = accountVerifier.createVerifiactionCode("1");
        accountService.verifyCode("1",code);
    }

    @Test
    @Transactional
    public void findAccountIdTest() throws Exception{
        userRepository.save(user1);
        userRepository.save(user2);

        List<String> ids = accountService.findLoginIdsByEmail("hopephm@naver.com");

        MatcherAssert.assertThat(ids, Matchers.hasItem("hopephm2"));
        MatcherAssert.assertThat(ids, Matchers.hasItem("hopephm3"));
    }

    @Test
    @Transactional
    public void checkAccountTest() throws Exception{
        User user = userRepository.findByEmailAndLoginId("hopephm4", "hopephm@naver.com");
        MatcherAssert.assertThat(user, Matchers.is(null));
    }

    @Test
    @Transactional
    public void resetPwTest(){

    }
}
