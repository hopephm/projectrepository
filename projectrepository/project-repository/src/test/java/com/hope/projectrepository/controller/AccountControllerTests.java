package com.hope.projectrepository.controller;

import com.hope.projectrepository.util.dto.AccountDTO;
import com.hope.projectrepository.domain.entity.User;
import com.hope.projectrepository.domain.repository.UserRepository;
import com.hope.projectrepository.domain.service.account.AccountService;
import com.hope.projectrepository.domain.service.account.manager.implementation.WaitingResetPwMap;
import com.hope.projectrepository.domain.service.account.verifier.implementation.VerifyInfo;
import com.hope.projectrepository.domain.service.account.verifier.implementation.WaitingVerificationMap;
import com.hope.projectrepository.exception.service.account.AccountDoesNotExistException;
import com.hope.projectrepository.exception.service.login.LoginIdAlreadyExistException;
import com.hope.projectrepository.exception.service.account.NicknameAlreadyExistException;
import com.hope.projectrepository.util.global.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AccountControllerTests {
    private final String ID = "newTestId";
    private final String PW = "newTestPw";
    private final String EMAIL = "hopephm@naver.com";
    private final String NICKNAME = "newTestNickname";
    private final String ID2 = "newTestId2";
    private final String NICKNAME2 = "newTestNickname2";

    @Autowired
    UserRepository userRepository;
    @Autowired
    AccountService accountService;
    @Autowired
    WaitingVerificationMap waitingVerificationMap;
    @Autowired
    WaitingResetPwMap waitingResetPwMap;

    @Autowired
    private WebApplicationContext webApplicationContext;
    private static MockMvc mvc;

    @BeforeEach
    public void setup(){
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @Transactional
    public void createAccountSuccessTest() throws Exception{
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("user_id", ID);
        params.add("user_password", PW);
        params.add("user_email", EMAIL);
        params.add("user_nickname", NICKNAME);

        mvc.perform(MockMvcRequestBuilders.post("/create_account").params(params))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Result.OK.getCode()));
    }

    @Test
    @Transactional
    public void createAccountIdDuplicateFailureTest() throws Exception{
        AccountDTO accountDTO = new AccountDTO(ID,PW,EMAIL,NICKNAME);
        accountService.createAccount(accountDTO);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("user_id", ID);
        params.add("user_password", PW);
        params.add("user_email", EMAIL);
        params.add("user_nickname", NICKNAME2);

        mvc.perform(MockMvcRequestBuilders.post("/create_account").params(params))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(new LoginIdAlreadyExistException().getErrorCode()));
    }

    @Test
    @Transactional
    public void createAccountNicknameDuplicateFailureTest() throws Exception{
        AccountDTO accountDTO = new AccountDTO(ID,PW,EMAIL,NICKNAME);
        accountService.createAccount(accountDTO);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("user_id", ID2);
        params.add("user_password", PW);
        params.add("user_email", EMAIL);
        params.add("user_nickname", NICKNAME);

        mvc.perform(MockMvcRequestBuilders.post("/create_account").params(params))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(new NicknameAlreadyExistException().getErrorCode()));
    }

    @Test
    @Transactional
    public void deleteAccountSuccessTest() throws Exception{
        AccountDTO accountDTO = new AccountDTO(ID,PW,EMAIL,NICKNAME);
        accountService.createAccount(accountDTO);

        User user = userRepository.findByLoginId(ID);

        mvc.perform(MockMvcRequestBuilders.post("/delete_account")
                .sessionAttr("user", user))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Result.OK.getCode()));
    }

    @Test
    @Transactional
    public void deleteAccountAccountDoesNotExistExceptionFailureTest() throws Exception{
        User user = User.builder()
                .loginId(ID)
                .password(PW)
                .email(EMAIL)
                .nickname(NICKNAME)
                .build();

        mvc.perform(MockMvcRequestBuilders.post("/delete_account")
                .sessionAttr("user", user))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(new AccountDoesNotExistException().getErrorCode()));
    }

    @Test
    @Transactional
    public void EmailVerificationSuccessTest() throws Exception{
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("user_email", EMAIL);

        mvc.perform(MockMvcRequestBuilders.post("/verify/email/send").params(params))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Result.OK.getCode()));

        VerifyInfo verifyInfo = waitingVerificationMap.get(EMAIL);

        MultiValueMap<String, String> params2 = new LinkedMultiValueMap<>();
        params2.add("user_email", EMAIL);
        params2.add("verify_code", verifyInfo.getCode());

        mvc.perform(MockMvcRequestBuilders.post("/verify/email/verify").params(params2))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Result.OK.getCode()));
    }
}
