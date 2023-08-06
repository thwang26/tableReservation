package com.zerobase.springmission.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 로그인시 사용되는 객체
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest {

    private String memberId;
    private String password;
}