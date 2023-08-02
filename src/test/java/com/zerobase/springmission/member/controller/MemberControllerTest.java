package com.zerobase.springmission.member.controller;

import com.zerobase.springmission.global.exception.ErrorCode;
import com.zerobase.springmission.global.exception.MemberException;
import com.zerobase.springmission.global.security.TokenProvider;
import com.zerobase.springmission.member.domain.Member;
import com.zerobase.springmission.member.dto.SignInRequest;
import com.zerobase.springmission.member.dto.SignUp;
import com.zerobase.springmission.member.service.MemberService;
import com.zerobase.springmission.member.type.MemberType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MemberControllerTest {

    @Mock
    private MemberService memberService;

    @Mock
    private TokenProvider tokenProvider;

    @InjectMocks
    private MemberController memberController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSignUp_Success() {
        // Given
        SignUp.Request signUpRequest = new SignUp.Request();
        signUpRequest.setMemberId("testuser");
        signUpRequest.setPassword("testpassword");

        SignUp.Response signUpResponse = new SignUp.Response();
        signUpResponse.setMemberId("testuser");

        when(memberService.createAccount(signUpRequest)).thenReturn(signUpResponse);

        // When
        SignUp.Response response = memberController.signUp(signUpRequest);

        // Then
        assertNotNull(response);
        assertEquals("testuser", response.getMemberId());

        verify(memberService, times(1)).createAccount(signUpRequest);
    }

    @Test
    public void testSignIn_Success() {
        // Given
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setMemberId("testuser");
        signInRequest.setPassword("testpassword");

        Member member = new Member();
        member.setMemberId("testuser");
        member.setMemberType(MemberType.USER);

        when(memberService.authenticate(signInRequest)).thenReturn(member);
        when(tokenProvider.generateToken(member.getUsername(), member.getRoles()))
                .thenReturn("token");

        // When
        ResponseEntity<?> responseEntity = memberController.signIn(signInRequest);

        // Then
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertTrue(responseEntity.hasBody());

        String token = (String) responseEntity.getBody();
        assertNotNull(token);

        verify(memberService, times(1)).authenticate(signInRequest);
    }

    @Test
    public void testSignUp_AccountAlreadyRegistered() {
        // Given
        SignUp.Request signUpRequest = new SignUp.Request();
        signUpRequest.setMemberId("existinguser");
        signUpRequest.setPassword("testpassword");

        when(memberService.createAccount(signUpRequest)).thenThrow(new MemberException(ErrorCode.ACCOUNT_ALREADY_REGISTERED));

        // When, Then
        assertThrows(MemberException.class, () -> memberController.signUp(signUpRequest));
        verify(memberService, times(1)).createAccount(signUpRequest);
    }

    @Test
    public void testSignIn_AccountDoesNotExist() {
        // Given
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setMemberId("nonexistentuser");
        signInRequest.setPassword("testpassword");

        when(memberService.authenticate(signInRequest)).thenThrow(new MemberException(ErrorCode.ACCOUNT_DOES_NOT_EXIST));

        // When, Then
        assertThrows(MemberException.class, () -> memberController.signIn(signInRequest));
        verify(memberService, times(1)).authenticate(signInRequest);
    }

    @Test
    public void testSignIn_IncorrectPassword() {
        // Given
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setMemberId("testuser");
        signInRequest.setPassword("incorrectpassword");

        when(memberService.authenticate(signInRequest)).thenThrow(new MemberException(ErrorCode.PASSWORD_DOES_NOT_MATCH));

        // When, Then
        assertThrows(MemberException.class, () -> memberController.signIn(signInRequest));
        verify(memberService, times(1)).authenticate(signInRequest);
    }
}
