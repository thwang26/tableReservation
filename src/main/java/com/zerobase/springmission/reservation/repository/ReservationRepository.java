package com.zerobase.springmission.reservation.repository;

import com.zerobase.springmission.member.domain.Member;
import com.zerobase.springmission.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String> {
    List<Reservation> findByMember(Member member);
}
