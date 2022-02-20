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

@RestController
@RequestMapping("/rest/account")
public class AccountRestController {
    @Autowired
    AccountService accountService;

    @GetMapping("/users/{userId}")
    @ExceptionHandling
    public String findUserByUserId(@PathVariable Long userId){
        User user = accountService.getUserById(userId);

        JsonResponseWrapper jr = new JsonResponseWrapper();
        jr.addData("user", user);

        return jr.getResponse();
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

        JsonResponseWrapper jr = new JsonResponseWrapper();

        return jr.getResponse();
    }

    @DeleteMapping("/users")
    @ExceptionHandling
    public String deleteAccount(HttpServletRequest request, HttpServletResponse response){
        accountService.deleteCurrentAccount();
        accountService.logoutCurrentAccount(request,response);

        JsonResponseWrapper jr = new JsonResponseWrapper();

        return jr.getResponse();
    }

    @GetMapping("/nicknames/exists/{nickname}")
    @ExceptionHandling
    public String isDuplicateNickname(@PathVariable String nickname){
        Boolean result = accountService.isExistNickname(nickname);

        JsonResponseWrapper jr = new JsonResponseWrapper();
        jr.addData("result", result);

        return jr.getResponse();

    }

    @GetMapping("/ids/exists/{loginId}")
    @ExceptionHandling
    public String isDuplicateLoginId(@PathVariable String loginId){
        Boolean result = accountService.isExistLoginId(loginId);

        JsonResponseWrapper jr = new JsonResponseWrapper();
        jr.addData("result", result);

        return jr.getResponse();
    }

    @PostMapping("/emails/send")
    @ExceptionHandling
    public String sendEmailVerificationCode(Model model,
                                                          @RequestParam("user_email") String email){
        accountService.sendVerificationCode(email, email);

        JsonResponseWrapper jr = new JsonResponseWrapper();

        return jr.getResponse();
    }

    @PostMapping("/emails/verify")
    @ExceptionHandling
    public String verifyCode(Model model,
                                           @RequestParam("user_email") String email,
                                           @RequestParam("verify_code") String code){
        accountService.verifyCode(email, code);

        JsonResponseWrapper jr = new JsonResponseWrapper();

        return jr.getResponse();
    }

    @GetMapping("/users/ids")
    @ExceptionHandling
    public String findLoginIdByEmail(@RequestParam("user_email") String email){
        List<String> idList = accountService.findLoginIdsByEmail(email);

        JsonResponseWrapper jr = new JsonResponseWrapper();
        jr.addData("id_list", idList);

        return jr.getResponse();
    }

    @PostMapping("/users/passwords/find")
    @ExceptionHandling
    public String sendResetPasswordEmail(Model model,
                                             @RequestParam("user_id") String loginId,
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
