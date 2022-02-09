package com.hope.projectrepository.controller.mvc;

import com.hope.projectrepository.domain.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AccountModelController {
    @GetMapping("/create_account")
    public String joinPage(){
        return "account/create_account";
    }

    @GetMapping("/find/id")
    public String findId(){
        return "account/find_id";
    }

    @GetMapping("/find/id/result")
    public String findIdResult(){ return "account/find_id_result"; }

    @GetMapping("/delete_account")
    public String deleteAccountPage(){ return "account/delete_account"; }

    @GetMapping("/find/pw")
    public String findPw(Model model){
        return "account/find_pw";
    }

    @GetMapping("/find/pw/{loginId}/{resetKey}")
    public String findPwResult(){
        return "account/find_pw_result";
    }


}
