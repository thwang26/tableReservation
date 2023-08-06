package com.zerobase.springmission.reservation.dto;

import com.zerobase.springmission.reservation.domain.Reservation;
import com.zerobase.springmission.reservation.type.ReservationType;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 나의 예약기록 객체
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationInfo {
    private String reservationId;
    private String storeName;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private ReservationType reservationType;
    private String cancelDescription;

    public static ReservationInfo fromEntity(Reservation reservation) {
        return ReservationInfo.builder()
                .reservationId(reservation.getReservationId())
                .storeName(reservation.getStore().getStoreName())
                .reservationDate(reservation.getReservationDate())
                .reservationTime(reservation.getReservationTime())
                .reservationType(reservation.getReservationType())
                .cancelDescription(reservation.getCancelDescription())
                .build();
    }
}
