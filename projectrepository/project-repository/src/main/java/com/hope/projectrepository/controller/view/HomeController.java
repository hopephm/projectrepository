package com.hope.projectrepository.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping({"", "/"})
    public String homePage(Model model){
        return "home";
    }
    @GetMapping("/denied")
    public String deniedPage(Model model){
        return "denied";
    }
}
