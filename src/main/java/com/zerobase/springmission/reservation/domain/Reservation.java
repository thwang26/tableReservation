package com.zerobase.springmission.reservation.domain;

import com.zerobase.springmission.member.domain.Member;
import com.zerobase.springmission.reservation.type.ReservationType;
import com.zerobase.springmission.store.domain.Store;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 예약 entity
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Reservation {

    /**
     * 에약번호
     */
    @Id
    private String reservationId;

    /**
     * 예약한 회원의 id FK
     */
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * 예약한 매장 id FK
     */
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    /**
     * 예약한 날짜
     */
    private LocalDate reservationDate;

    /**
     * 예약한 시간
     */
    private LocalTime reservationTime;

    /**
     * 해당 예약의 상태(요청, 승인, 취소, 사용됨, 시간만료)
     */
    @Enumerated(EnumType.STRING)
    private ReservationType reservationType;

    /**
     * 예약 취소 사유
     */
    private String cancelDescription;

}
