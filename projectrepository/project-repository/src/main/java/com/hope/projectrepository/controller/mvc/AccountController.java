package com.hope.projectrepository.controller.mvc;

import com.hope.projectrepository.service.AccountService;
import com.hope.projectrepository.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class AccountController {
    @Autowired
    AccountService accountService;

    @GetMapping("/create_account")
    public String joinPage(Model model){
        return "account/create_account";
    }

    @PostMapping("/create_account")
    public @ResponseBody String createAccount(Model model,
                    @RequestParam("user_id") String loginId,
                    @RequestParam("user_password") String password,
                    @RequestParam("user_email") String email,
                    @RequestParam("user_nickname") String nickname){

        accountService.createAccount(loginId, password, email, nickname);
        
        return Result.SUCCESS;      // 핸들링
    }

    @GetMapping("/delete_account")
    public String deleteAccountPage(Model model){ return "account/delete_account"; }

    @PostMapping("/delete_account")
    public @ResponseBody String deleteAccount(HttpServletRequest request, HttpServletResponse response){

        accountService.deleteAccount(request, response);

        return Result.SUCCESS;      // 핸들링
    }

    @GetMapping("/find/id")
    public String findId(Model model){
        return "account/find_id";
    }

    @GetMapping("/find/id/result")
    public String findIdResult(Model model,
                               @RequestParam("user_email") String email){
        List<String> idList = accountService.findId(email);
        
//        아이디 미존재 예외
        model.addAttribute("idList", idList);
        return "account/find_id_result";
    }
    
    @PostMapping("/verify/email/send")
    public @ResponseBody String sendVerificationCode(Model model,
                                               @RequestParam("user_email") String email){
        String randomCode = accountService.sendVerificationCode(email, email);
        return Result.SUCCESS;  // 핸들링
    }

    @PostMapping("/verify/email/verify")
    public @ResponseBody String verifyCode(Model model,
                                           @RequestParam("user_email") String email,
                                           @RequestParam("verify_code") String code){
        String result = accountService.verifyCode(email, code);
        
        // 결과 보내는 곳부터 핸들링
        if(result == Result.SUCCESS) return Result.SUCCESS;
        else return Result.FAIL;
    }

    @PostMapping("/verify/find/pw")
    public @ResponseBody String sendAccountVerificationCode(Model model,
                                                      @RequestParam("user_id") String loginId,
                                                      @RequestParam("user_email") String email){
        String result = accountService.checkAccount(loginId, email);

        // 핸들링
        if(result == Result.SUCCESS){
            accountService.sendVerificationCode(email, loginId);
            return Result.SUCCESS;
        }

        return Result.FAIL;
    }

    @PostMapping("/verify/find/pw/verify")
    public @ResponseBody String verifyAccount(Model model,
                                              @RequestParam("user_id") String loginId,
                                              @RequestParam("verify_code") String code){
        String result = accountService.verifyCode(loginId, code);
        
        // 핸들링
        if(result == Result.SUCCESS) {
            accountService.addResetAccount(loginId);
            return Result.SUCCESS;
        }
        else return Result.FAIL;
    }

    @PostMapping("/verify/find/pw/reset")
    public @ResponseBody String resetPw(Model model,
                                                        @RequestParam("user_id") String loginId,
                                                        @RequestParam("user_email") String email){
        String result = accountService.resetPw(loginId, email);
        
        // 핸들링
        if(result == Result.SUCCESS) return Result.SUCCESS;
        else return Result.FAIL;
    }

    @GetMapping("/find/pw")
    public String findPw(Model model){
        return "account/find_pw";
    }

    @GetMapping("/find/pw/result")
    public String findPwResult(Model model){
        return "account/find_pw_result";
    }
}
