package com.zerobase.springmission.dto;

import com.zerobase.springmission.type.UserType;
import lombok.*;

import java.time.LocalDateTime;

public class SignUp {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {

        private Long userId;
        private String userName;
        private String password;
        private String phone;
        private LocalDateTime regDate;
        private UserType userType;
    }

//    @Getter
//    @Setter
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @Builder
//    public static class Response {
//
//    }
}
