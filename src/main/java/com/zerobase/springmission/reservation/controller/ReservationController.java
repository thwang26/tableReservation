//package com.zerobase.springmission.reservation.controller;
//
//import com.zerobase.springmission.global.security.TokenProvider;
//import com.zerobase.springmission.member.dto.SignUp;
//import com.zerobase.springmission.member.service.MemberService;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@Slf4j
//@RestController
//@RequestMapping("/reservation")
//@AllArgsConstructor
//public class ReservationController {
//
//    private final MemberService memberService;
//    private final TokenProvider tokenProvider;
//
//    @PostMapping("/sign-up")
//    @PreAuthorize("hasRole('USER')")
//    public SignUp.Response signUp(@RequestBody SignUp.Request signUpRequest) {
//        log.info("detected new sign-up attempts -> " + signUpRequest.getMemberId());
//        return memberService.createAccount(signUpRequest);
//    }
//}
