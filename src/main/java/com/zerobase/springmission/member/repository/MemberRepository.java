package com.zerobase.springmission.member.repository;

import com.zerobase.springmission.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberId(String memberId);
    boolean existsByMemberId(String memberId);
}
