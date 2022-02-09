package com.hope.projectrepository.controller.rest;

import com.google.gson.JsonObject;
import com.hope.projectrepository.compatibility.dto.AccountDTO;
import com.hope.projectrepository.domain.entity.User;
import com.hope.projectrepository.domain.service.account.AccountService;
import com.hope.projectrepository.exception.ExceptionHandling;
import com.hope.projectrepository.util.global.Result;
import com.hope.projectrepository.util.response.data.RestResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/rest/account")
public class AccountRestController {
    @Autowired
    AccountService accountService;

    // @GetMapping("/find/id/result")
    @GetMapping("/users/ids")
    public List<String> findLoginIdByEmail(@RequestParam("user_email") String email){
        List<String> idList = accountService.findLoginIdsByEmail(email);

        return idList;
    }

    @GetMapping("/users/nicknames/{nickname}")
    public String isDuplicatedNickname(@RequestParam("value") String nickname) {
        User user = accountService.checkUserByNickname(nickname);

        return Result.OK.getCode();
    }

    @GetMapping("/users")
    public String isDuplicatedLoginId(@RequestParam("user_id") String loginId){
        User user = accountService.checkUserByLoginId(loginId);
        return Result.OK.getCode();
    }

    @PostMapping("/users")
    @ExceptionHandling
    public String createAccount(Model model,
                         @RequestParam("user_id") String loginId,
                         @RequestParam("user_password") String password,
                         @RequestParam("user_email") String email,
                         @RequestParam("user_nickname") String nickname){

        AccountDTO accountDTO = new AccountDTO(loginId, password, email, nickname);
        accountService.createAccount(accountDTO);

        return Result.OK.getCode();
    }

    @DeleteMapping("/users")
    @ExceptionHandling
    public String deleteAccount(HttpServletRequest request, HttpServletResponse response){
        accountService.deleteCurrentAccount();
        accountService.logoutCurrentAccount(request,response);

        return Result.OK.getCode();
    }

    @PostMapping("/emails/send")
    @ExceptionHandling
    public String sendEmailVerificationCode(Model model,
                                                          @RequestParam("user_email") String email){
        accountService.sendVerificationCode(email, email);

        return Result.OK.getCode();
    }

    @PostMapping("/emails/verify")
    @ExceptionHandling
    public String verifyCode(Model model,
                                           @RequestParam("user_email") String email,
                                           @RequestParam("verify_code") String code){
        accountService.verifyCode(email, code);

        return Result.OK.getCode();
    }

    @PostMapping("/passwords/find")
    @ExceptionHandling
    public String sendResetPasswordEmail(Model model,
                                             @RequestParam("user_id") String loginId,
                                             @RequestParam("user_email") String email){
        accountService.changeAccountStateAndSendResetEmail(email, loginId);
        return Result.OK.getCode();
    }

    // @GetMapping("/find/pw/{loginId}/{resetKey}") 의 동작
    @PutMapping("/passwords/reset")
    public String resetPassword(@RequestParam("user_id") String loginId,
                                @RequestParam("reset_key") String resetKey){

        String newPw = accountService.resetPw(resetKey);
        return loginId + newPw;
    }
}
