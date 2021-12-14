package com.hope.projectrepository.controller.mvc;

import com.hope.projectrepository.compatibility.dto.AccountDTO;
import com.hope.projectrepository.domain.service.account.AccountService;
import com.hope.projectrepository.util.Result;
import com.hope.projectrepository.util.Util;
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
    AccountService accountService;
//    LagacyAccountServiceImpl accountService;

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
        accountService.deleteCurrentAccount();
        Util.logout(request, response);
        return Result.SUCCESS;      // 핸들링
    }

    @PostMapping("/create_account")
    public @ResponseBody String createAccount(Model model,
                                              @RequestParam("user_id") String loginId,
                                              @RequestParam("user_password") String password,
                                              @RequestParam("user_email") String email,
                                              @RequestParam("user_nickname") String nickname){
//        파라미터를 DTO로 변경

        AccountDTO accountDTO = new AccountDTO(loginId, password, email, nickname);
        accountService.createAccount(accountDTO);
        return Result.SUCCESS;      // 핸들링
    }

    @PostMapping("/verify/email/send")
    public @ResponseBody String sendEmailVerificationCode(Model model,
                                                     @RequestParam("user_email") String email){
        accountService.sendVerificationCode(email, email);
        return Result.SUCCESS;
    }

    @PostMapping("/verify/email/verify")
    public @ResponseBody String verifyCode(Model model,
                                           @RequestParam("user_email") String email,
                                           @RequestParam("verify_code") String code){
        try{
            accountService.verifyCode(email, code);
        }catch(Exception e){
            // 실패시 return a
            // 실패시 return b

        }

        return Result.SUCCESS;
    }


//    pw 찾기는 과정 변경 필요
//    1. 계정 존재여부 확인 (loginId, email);
//    2. 이메일 인증 코드 전송(email)
//    2. 이메일 코드 확인 (email, code) -> email 검증과 같은 함수 및 같은 과정
//    3. pw 변경 및 변경 내용 전송 (loginId, email)


    @PostMapping("/verify/find/pw")
    public @ResponseBody String checkAccount(Model model,
                                                            @RequestParam("user_id") String loginId,
                                                            @RequestParam("user_email") String email){
        // accountService.checkAccount(loginId, email);
        return Result.SUCCESS;
    }

    /*
        프론트 :
            pw 찾기 클릭 시 check Account
            존재시 >> 이메일 인증 발송 버튼 생성 + 인증 코드 입력창 생성
            인증코드 일치시 >> pw 초기화 및 전송 버튼 생성
            pw 초기화 클릭시 >> 초기화된 번호 전송

        현재 프론트 부터 하면 됨
     */


    /*
            위에 있는
            accountService.sendVerificationCode(email, loginId);

            accountService.verifyCode(loginId, code);
            예외 없는 경우 실행 >> accountService.putAccountToWaitingResetPwMap(loginId);

            accountService.resetPwAndSendNewPw(loginId);
            사용 
     */

    @PostMapping("/verify/find/pw/reset")
    public @ResponseBody String resetPw(Model model,
                                        @RequestParam("user_id") String loginId,
                                        @RequestParam("user_email") String email){
        // accountService.resetPwAndSendNewPw(loginId);
            // 내부적으로 reset PW, send PW로 구성
        return Result.SUCCESS;
    }

    
    // 추후 아래 맵핑정보 삭제
    @PostMapping("/verify/find/pw/verify")
    public @ResponseBody String verifyAccount(Model model,
                                              @RequestParam("user_id") String loginId,
                                              @RequestParam("verify_code") String code){

        return Result.SUCCESS;
    }
}
