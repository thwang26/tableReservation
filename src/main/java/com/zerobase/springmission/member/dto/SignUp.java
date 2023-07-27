package com.zerobase.springmission.member.dto;

import com.zerobase.springmission.global.config.type.UserType;
import lombok.*;

import java.time.LocalDateTime;

public class SignUp {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {

        private String userName;
        private String password;
        private String phone;
        private LocalDateTime regDate;
        private UserType userType;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String userName;
        private String password;
        private String phone;
        private LocalDateTime regDate;
        private UserType userType;
    }
}
