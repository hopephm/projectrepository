package com.hope.projectrepository.controller.mvc;

import com.hope.projectrepository.domain.service.login.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String unitedLoginPage(Model model){
        return "login/login";
    }

    @GetMapping("/login/fail")
    public String loginFailedPage(Model model, HttpServletRequest rs, HttpServletResponse res){
        return "login/login_fail";
    }

    @GetMapping("/login/success")
    public String loginSuccessPage(Model model){
        return "login/login_success";
    }

    @GetMapping("/logout/success")
    public String logoutPage(Model model){
        return "login/logout_success";
    }
}
