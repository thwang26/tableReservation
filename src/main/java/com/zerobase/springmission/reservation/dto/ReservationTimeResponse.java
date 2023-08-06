package com.zerobase.springmission.reservation.dto;

import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationTimeResponse {
    private String storeName;
    private List<LocalTime> timeList;
}
