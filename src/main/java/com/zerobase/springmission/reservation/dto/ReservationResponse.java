package com.zerobase.springmission.reservation.dto;

import com.zerobase.springmission.reservation.domain.Reservation;
import com.zerobase.springmission.reservation.type.ReservationType;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponse {
    private String reservationId;
    private String memberId;
    private String storeName;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private ReservationType reservationType;

    public static ReservationResponse fromEntity(Reservation reservation) {
        return ReservationResponse.builder()
                .reservationId(reservation.getReservationId())
                .memberId(reservation.getMember().getMemberId())
                .storeName(reservation.getStore().getStoreName())
                .reservationDate(reservation.getReservationDate())
                .reservationTime(reservation.getReservationTime())
                .reservationType(reservation.getReservationType())
                .build();
    }
}
