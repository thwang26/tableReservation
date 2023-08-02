package com.zerobase.springmission.member.service;

import com.zerobase.springmission.global.exception.MemberException;
import com.zerobase.springmission.member.domain.Member;
import com.zerobase.springmission.member.dto.SignUp;
import com.zerobase.springmission.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("회원가입 성공")
    public void testCreateAccount_Success() {
        // Given
        SignUp.Request signUpRequest = new SignUp.Request();
        signUpRequest.setMemberId("testuser");
        signUpRequest.setPassword("testpassword");

        when(memberRepository.existsByMemberId("testuser")).thenReturn(false);

        String encodedPassword = "encoded_password";
        when(passwordEncoder.encode("testpassword")).thenReturn(encodedPassword);

        Member savedMember = new Member();
        savedMember.setMemberId("testuser");
        savedMember.setPassword(encodedPassword);
        when(memberRepository.save(any(Member.class))).thenReturn(savedMember);

        // When
        SignUp.Response response = memberService.createAccount(signUpRequest);

        // Then
        assertNotNull(response);
        assertEquals("testuser", response.getMemberId());

        verify(memberRepository, times(1))
                .existsByMemberId("testuser");
        verify(passwordEncoder, times(1))
                .encode("testpassword");
        verify(memberRepository, times(1))
                .save(any(Member.class));
    }

    @Test
    @DisplayName("회원가입 실패")
    public void testCreateAccount_AccountAlreadyRegistered() {
        // given
        SignUp.Request signUpRequest = new SignUp.Request();
        signUpRequest.setMemberId("testuser");
        signUpRequest.setPassword("testpassword");

        // when
        when(memberRepository.existsByMemberId("testuser"))
                .thenReturn(true);

        // then
        assertThrows(MemberException.class,
                () -> memberService.createAccount(signUpRequest));
    }

}