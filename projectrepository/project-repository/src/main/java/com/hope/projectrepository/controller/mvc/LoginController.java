package com.hope.projectrepository.controller.mvc;

import com.hope.projectrepository.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {
    @Autowired
    private LoginService loginService;

    @GetMapping("/login")
    public String unitedLoginPage(Model model){
//        미존재 아이디 핸들링
//        계정정보 불일치 핸들링
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
