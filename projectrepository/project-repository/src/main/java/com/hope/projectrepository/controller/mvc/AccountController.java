package com.hope.projectrepository.controller.mvc;

import com.hope.projectrepository.domain.service.account.implementation.LagacyAccountServiceImpl;
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


// Service 객체에서 반환하는 Exception에 따라 반환결과 차이

@Controller
public class AccountController {
    @Autowired
    LagacyAccountServiceImpl accountService;

    ///*                *///
    //         Get        //
    ///*                *///
    @GetMapping("/create_account")
    public String joinPage(Model model){
        return "account/create_account";
    }

    @GetMapping("/delete_account")
    public String deleteAccountPage(Model model){ return "account/delete_account"; }

    @GetMapping("/find/id")
    public String findId(Model model){
        return "account/find_id";
    }

    @GetMapping("/find/id/result")
    public String findIdResult(Model model,
                               @RequestParam("user_email") String email){

        List<String> idList = accountService.findAccountIdByEmail(email);

        model.addAttribute("idList", idList);
        return "account/find_id_result";
    }

    @GetMapping("/find/pw")
    public String findPw(Model model){
        return "account/find_pw";
    }

    @GetMapping("/find/pw/result")
    public String findPwResult(Model model){
        return "account/find_pw_result";
    }


    ///*                *///
    //        Post        //
    ///*                *///
    @PostMapping("/delete_account")
    public @ResponseBody String deleteAccount(HttpServletRequest request, HttpServletResponse response){
        accountService.deleteAccount(request, response);
        return Result.SUCCESS;      // 핸들링
    }

    @PostMapping("/create_account")
    public @ResponseBody String createAccount(Model model,
                                              @RequestParam("user_id") String loginId,
                                              @RequestParam("user_password") String password,
                                              @RequestParam("user_email") String email,
                                              @RequestParam("user_nickname") String nickname){
        // DTO로 파라미터 변경 (현재 컨트롤러 자체 + createAccount 호출 둘 다)
        accountService.createAccount(loginId, password, email, nickname);
        return Result.SUCCESS;      // 핸들링
    }

    // 이걸 왜 프론트에서 요청받아서 하지
    @PostMapping("/verify/email/send")
    public @ResponseBody String sendVerificationCode(Model model,
                                                     @RequestParam("user_email") String email){
        accountService.sendVerificationCode(email, email);
        return Result.SUCCESS;  // 핸들링
    }

    @PostMapping("/verify/email/verify")
    public @ResponseBody String verifyCode(Model model,
                                           @RequestParam("user_email") String email,
                                           @RequestParam("verify_code") String code){
        accountService.verifyCode(email, code);

        // 결과 보내는 곳부터 핸들링
        return Result.SUCCESS;
    }

    @PostMapping("/verify/find/pw")
    public @ResponseBody String sendAccountVerificationCode(Model model,
                                                            @RequestParam("user_id") String loginId,
                                                            @RequestParam("user_email") String email){
//        accountService.startFindAccountPw(loginId, email);

//        // 추상화 수준이 안맞음
//        String result = accountService.checkAccount(loginId, email);
//
//        // 핸들링
//        if(result == Result.SUCCESS){
//            accountService.sendVerificationCode(email, loginId);
//        }

        return Result.SUCCESS;
    }

    @PostMapping("/verify/find/pw/verify")
    public @ResponseBody String verifyAccount(Model model,
                                              @RequestParam("user_id") String loginId,
                                              @RequestParam("verify_code") String code){
//        accountService.finishFindAccountPw(loginId, code);

//        // 추상화 수준이 안맞음
//        String result = accountService.verifyCode(loginId, code);
//
//        if(result == Result.SUCCESS) {
//            accountService.addResetAccount(loginId);
//        }

        return Result.SUCCESS;
    }

    @PostMapping("/verify/find/pw/reset")
    public @ResponseBody String resetPw(Model model,
                                        @RequestParam("user_id") String loginId,
                                        @RequestParam("user_email") String email){
        String result = accountService.resetPw(loginId, email);

        // 핸들링
        return Result.SUCCESS;
    }
}
