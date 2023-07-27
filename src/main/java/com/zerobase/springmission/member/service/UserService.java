package com.zerobase.springmission.member.service;

import com.zerobase.springmission.member.dto.SignUp;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public SignUp.Response signUp(SignUp.Request signUpRequest) {
        return SignUp.Response.builder()
                .userName(signUpRequest.getUserName())
                .password(signUpRequest.getPassword())
                .phone(signUpRequest.getPhone())
                .regDate(signUpRequest.getRegDate())
                .userType(signUpRequest.getUserType())
                .build();
    }
}
