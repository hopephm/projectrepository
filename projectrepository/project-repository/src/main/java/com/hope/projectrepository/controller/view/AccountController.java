package com.hope.projectrepository.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AccountController {
    @GetMapping("/create_account")
    public String joinPage(){
        return "account/create_account";
    }

    @GetMapping("/find/id")
    public String findId(){
        return "account/find_id";
    }

    @GetMapping("/delete_account")
    public String deleteAccountPage(){ return "account/delete_account"; }

    @GetMapping("/find/pw")
    public String findPw(Model model){
        return "account/find_pw";
    }

    @GetMapping("/find/pw/reset")
    public String findPwResult(@RequestParam("user_id") String loginId,
                               @RequestParam("key") String resetKey) {
        return "account/find_pw_result";
    }
}
