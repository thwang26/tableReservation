package com.zerobase.springmission.reservation.dto;

import lombok.*;

import java.time.LocalTime;
import java.util.List;

/**
 * 해당 매장의 예약가능 시간 객체
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationTimeResponse {
    private String storeName;
    private List<LocalTime> timeList;
}
