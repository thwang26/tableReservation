package com.zerobase.springmission.member.domain;

import com.zerobase.springmission.member.type.MemberType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 회원정보 entity
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Member implements UserDetails {

    /**
     * 회원 아이디
     */
    @Id
    private String memberId;

    /**
     * 비밀번호
     */
    private String password;

    /**
     * 전화번호
     */
    private String phone;

    /**
     * 가입일자
     */
    @CreatedDate
    private LocalDateTime regDate;

    /**
     * 수정일자
     */
    @LastModifiedDate
    private LocalDateTime modDate;

    /**
     * 회원종류, 일반회원 or 파트너회원
     */
    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + memberType.toString()));
        return authorities;
    }

    @Override
    public String getUsername() {
        return memberId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public List<String> getRoles() {
        List<String> list = new ArrayList<>();
        list.add(memberType.toString());
        return list;
    }
}
