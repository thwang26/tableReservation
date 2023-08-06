package com.zerobase.springmission.member.service;

import com.zerobase.springmission.global.exception.MemberException;
import com.zerobase.springmission.member.domain.Member;
import com.zerobase.springmission.member.dto.SignInRequest;
import com.zerobase.springmission.member.dto.SignUp;
import com.zerobase.springmission.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.zerobase.springmission.global.exception.ErrorCode.*;

@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        return memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("couldn't find user -> " + memberId));
    }

    /**
     * 회원가입 요청 시 계정을 생성하는 로직
     * 이미 가입되어있다면 에러처리
     * 가입 시 db에 비밀번호 암호화 하여 저장
     */
    @Transactional
    public SignUp.Response createAccount(SignUp.Request signUpRequest) {
        boolean exists = memberRepository.existsByMemberId(signUpRequest.getMemberId());
        if (exists) {
            throw new MemberException(ACCOUNT_ALREADY_REGISTERED);
        }

        signUpRequest.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        Member member = memberRepository.save(signUpRequest.toEntity());
        return SignUp.Response.fromEntity(member);
    }

    /**
     * 로그인 로직
     * db의 비밀번호와 요청의 비밀번호를 대조
     */
    public Member authenticate(SignInRequest signInRequest) {
        Member member = memberRepository.findByMemberId(signInRequest.getMemberId())
                .orElseThrow(() -> new MemberException(ACCOUNT_DOES_NOT_EXIST));

        if (!passwordEncoder.matches(signInRequest.getPassword(), member.getPassword())) {
            throw new MemberException(PASSWORD_DOES_NOT_MATCH);
        }

        return member;
    }
}
// TODO : "추가로 할 수 있는 것들 : 이메일 중복확인, 비밀번호 정규식"