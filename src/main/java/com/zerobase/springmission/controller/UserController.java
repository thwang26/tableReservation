package com.zerobase.springmission.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/sign-up")
    public void signUp() {

    }

    @PostMapping("/sign-in")
    public void signIn() {

    }

}
