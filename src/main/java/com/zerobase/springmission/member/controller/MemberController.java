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

@Slf4j
@RestController
@RequestMapping("/member")
@AllArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @PostMapping("/sign-up")
    public SignUp.Response signUp(@RequestBody SignUp.Request signUpRequest) {
        log.info("detected new sign-up attempts -> " + signUpRequest.getMemberName());
        return memberService.createAccount(signUpRequest);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest signInRequest) {
        Member member = memberService.authenticate(signInRequest);
        String token = tokenProvider.generateToken(member.getMemberId(), member.getRoles());
        log.info("user login -> " + signInRequest.getMemberName());
        return ResponseEntity.ok(token);
    }
}
