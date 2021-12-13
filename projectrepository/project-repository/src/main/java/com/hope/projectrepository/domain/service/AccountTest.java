package com.hope.projectrepository.domain.service;

import com.hope.projectrepository.domain.entity.User;
import com.hope.projectrepository.compatibility.dto.AccountDTO;
import com.hope.projectrepository.domain.repository.UserRepository;
import com.hope.projectrepository.domain.service.account.AccountService;
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
    private UserRepository userRepository;

    private static User user;
    private static AccountDTO accountDTO;
    private static MockHttpSession session;

    @BeforeAll
    public static void createMockObjs(){
        accountDTO = new AccountDTO("hopephm1", "1", "hopephm@naver.com", "감자골청년");

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
    public void createAccountTest(){
        User user1 = accountService.createAccount(accountDTO);
        User user2 = userRepository.findByLoginId(accountDTO.getLoginId());
        MatcherAssert.assertThat(user1, Matchers.is(user2));
    }

    @Test
    @Transactional
    public void deleteCurrentAccountTest(){
        userRepository.save(user);
        accountService.deleteCurrentAccount(session);
        User user1 = userRepository.findByLoginId("hopephm2");
        MatcherAssert.assertThat(null, Matchers.is(user1));
    }
}
