package com.zerobase.springmission.reservation.domain;

import com.zerobase.springmission.member.domain.Member;
import com.zerobase.springmission.reservation.type.ReservationType;
import com.zerobase.springmission.store.domain.Store;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Reservation {

    @Id
    private String reservationId;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    @Enumerated(EnumType.STRING)
    private ReservationType reservationType;
    private String cancelDescription;

}
