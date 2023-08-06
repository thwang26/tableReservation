package com.zerobase.springmission.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 예약취소 요청 객체
 * 예약 취소 사유 포함
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CancelReservationRequest {
    private String reservationId;
    private String cancelDescription;
}