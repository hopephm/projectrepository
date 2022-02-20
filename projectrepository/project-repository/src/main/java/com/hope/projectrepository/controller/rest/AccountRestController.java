package com.hope.projectrepository.controller.rest;

import com.hope.projectrepository.util.dto.AccountDTO;
import com.hope.projectrepository.domain.entity.User;
import com.hope.projectrepository.domain.service.account.AccountService;
import com.hope.projectrepository.exception.handle.ExceptionHandling;
import com.hope.projectrepository.util.response.json.JsonResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

// 여유될 때, RestFul하게 CRUD 맞춰서 추가하자
@RestController
@RequestMapping("/rest/account")
public class AccountRestController {
    @Autowired
    AccountService accountService;

    @PostMapping("/users")
    @ExceptionHandling
    public String createUser(@RequestParam("user_id") String loginId,
                             @RequestParam("user_password") String password,
                             @RequestParam("user_email") String email,
                             @RequestParam("user_nickname") String nickname){

        AccountDTO accountDTO = new AccountDTO(loginId, password, email, nickname);
        accountService.createAccount(accountDTO);

        JsonResponseWrapper jr = new JsonResponseWrapper();

        return jr.getResponse();
    }

    @GetMapping("/users/{userId}")
    @ExceptionHandling
    public String getUser(@PathVariable Long userId){
        User user = accountService.getUserById(userId);

        JsonResponseWrapper jr = new JsonResponseWrapper();
        jr.addData("user", user);

        return jr.getResponse();
    }

    @DeleteMapping("/users")
    @ExceptionHandling
    public String deleteUser(HttpServletRequest request, HttpServletResponse response){
        accountService.deleteCurrentAccount();
        accountService.logoutCurrentAccount(request, response);

        JsonResponseWrapper jr = new JsonResponseWrapper();

        return jr.getResponse();
    }

    @GetMapping("/nicknames/exists/{nickname}")
    @ExceptionHandling
    public String nicknameEixistCheck(@PathVariable String nickname){
        Boolean result = accountService.isExistNickname(nickname);

        JsonResponseWrapper jr = new JsonResponseWrapper();
        jr.addData("result", result);

        return jr.getResponse();

    }

    @GetMapping("/ids/exists/{loginId}")
    @ExceptionHandling
    public String loginIdExistCheck(@PathVariable String loginId){
        Boolean result = accountService.isExistLoginId(loginId);

        JsonResponseWrapper jr = new JsonResponseWrapper();
        jr.addData("result", result);

        return jr.getResponse();
    }

    @PostMapping("/emails/send")
    @ExceptionHandling
    public String sendJoinVerificationCodeEmail(@RequestParam("user_email") String email){
        accountService.sendVerificationCode(email, email);

        JsonResponseWrapper jr = new JsonResponseWrapper();

        return jr.getResponse();
    }

    @PostMapping("/emails/verify")
    @ExceptionHandling
    public String checkVerificationCode(@RequestParam("user_email") String email,
                                        @RequestParam("verify_code") String code){
        accountService.verifyCode(email, code);

        JsonResponseWrapper jr = new JsonResponseWrapper();

        return jr.getResponse();
    }

    @GetMapping("/users/ids")
    @ExceptionHandling
    public String getUsersByEmail(@RequestParam("user_email") String email){
        List<String> idList = accountService.findLoginIdsByEmail(email);

        JsonResponseWrapper jr = new JsonResponseWrapper();
        jr.addData("id_list", idList);

        return jr.getResponse();
    }

    @PostMapping("/users/passwords/find")
    @ExceptionHandling
    public String sendPasswordResetEmail(@RequestParam("user_id") String loginId,
                                         @RequestParam("user_email") String email){
        accountService.changeAccountStateAndSendResetEmail(email, loginId);

        JsonResponseWrapper jr = new JsonResponseWrapper();

        return jr.getResponse();
    }

    @PostMapping("/users/passwords/reset")
    @ExceptionHandling
    public String resetPassword(@RequestParam("user_id") String loginId,
                                @RequestParam("reset_key") String resetKey){

        String newPw = accountService.resetPw(resetKey);

        JsonResponseWrapper jr = new JsonResponseWrapper();
        jr.addData("id", loginId);
        jr.addData("password", newPw);

        return jr.getResponse();
    }
}
