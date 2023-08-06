package com.zerobase.springmission.member.controller;

import com.zerobase.springmission.global.security.TokenProvider;
import com.zerobase.springmission.member.domain.Member;
import com.zerobase.springmission.member.dto.SignInRequest;
import com.zerobase.springmission.member.dto.SignUp;
import com.zerobase.springmission.member.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * member 기능
 */
@Slf4j
@RestController
@RequestMapping("/member")
@AllArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    /**
     * 회원가입기능
     */
    @PostMapping("/sign-up")
    public SignUp.Response signUp(@RequestBody SignUp.Request signUpRequest) {
        log.info("detected new sign-up attempts -> " + signUpRequest.getMemberId());
        return memberService.createAccount(signUpRequest);
    }

    /**
     * 로그인 기능, 로그인 성공 시 토큰을 발급하여 return
     */
    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest signInRequest) {
        Member member = memberService.authenticate(signInRequest);
        String token = tokenProvider.generateToken(member.getMemberId(), member.getRoles());
        log.info("user login -> " + signInRequest.getMemberId());
        return ResponseEntity.ok(token);
    }
}
