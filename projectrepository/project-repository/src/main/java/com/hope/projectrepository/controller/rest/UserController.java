package com.hope.projectrepository.controller.rest;

import com.hope.projectrepository.domain.User;
import com.hope.projectrepository.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.mail.search.SearchTerm;
import javax.servlet.http.HttpSession;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.*;

@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/user")
    public User getUserByLoginId(@RequestParam("login_id") String loginId){
        return userRepository.findByLoginId(loginId);
    }

    @GetMapping("/user/duplicated/id")
    public String isDuplicatedLoginId(@RequestParam("value") String loginId){
        User user = userRepository.findByLoginId(loginId);

        if(user == null) return "success";
        else return "fail";
    }

    @GetMapping("/user/duplicated/nickname")
    public String isDuplicatedNickname(@RequestParam("value") String nickname) {
        User user = userRepository.findByNickname(nickname);

        if(user == null) return "success";
        else return "fail";
    }
}
