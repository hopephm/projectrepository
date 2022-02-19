package com.hope.projectrepository.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
//              "login processing"
//    GET   /oauth2/authorization/{socialName}
//    POST  /login?username={userName}&password={passWord}

    @GetMapping("/login")
    public String unitedLoginPage(){
        return "login/login";
    }

    @GetMapping("/login/fail")
    public String loginFailedPage(){ return "login/login_fail"; }

    @GetMapping("/login/success")
    public String loginSuccessPage(){
        return "login/login_success";
    }

    @GetMapping("/logout/success")
    public String logoutPage(){
        return "login/logout_success";
    }
}
