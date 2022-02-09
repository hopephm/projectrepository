package com.hope.projectrepository.controller.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginModelController {
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
