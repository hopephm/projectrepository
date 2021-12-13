package com.hope.projectrepository.controller.rest;

import com.hope.projectrepository.domain.entity.User;
import com.hope.projectrepository.domain.repository.UserRepository;
import com.hope.projectrepository.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/user")
    public User getUserByLoginId(@RequestParam("login_id") String loginId){
        return userRepository.findByLoginId(loginId);
    }

    // Rest 원칙 위배 : id를 rest에서 조회하도록 하고 조회되면 프론트에서 중복처리
    @GetMapping("/user/duplicated/id")
    public String isDuplicatedLoginId(@RequestParam("value") String loginId){
        User user = userRepository.findByLoginId(loginId);

        if(user == null) return Result.SUCCESS;
        else return Result.FAIL;
    }

    // Rest 원칙 위배 : id를 rest에서 조회하도록 하고 조회되면 프론트에서 중복처리
    @GetMapping("/user/duplicated/nickname")
    public String isDuplicatedNickname(@RequestParam("value") String nickname) {
        User user = userRepository.findByNickname(nickname);

        if(user == null) return Result.SUCCESS;
        else return Result.FAIL;
    }
}
