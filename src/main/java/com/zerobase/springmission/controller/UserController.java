package com.zerobase.springmission.controller;

import com.zerobase.springmission.dto.SignUp;
import com.zerobase.springmission.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public void signUp(@RequestBody SignUp.Request signUpRequest) {
//        SignUp.Response signUpResponse = userService.signUp(signUpRequest);
        return;
    }

    @PostMapping("/sign-in")
    public void signIn() {

    }

}
