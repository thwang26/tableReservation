package com.zerobase.springmission.member.controller;

import com.zerobase.springmission.member.dto.SignUp;
import com.zerobase.springmission.member.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    UserService userService;


    @PostMapping("/sign-up")
    public SignUp.Response signUp(@RequestBody SignUp.Request signUpRequest) {
        return userService.signUp(signUpRequest);
    }

    @PostMapping("/sign-in")
    public void signIn() {

    }

}
