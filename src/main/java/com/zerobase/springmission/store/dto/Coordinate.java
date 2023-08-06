package com.zerobase.springmission.store.dto;

import lombok.*;

/**
 * 매장 좌표 return 시 사용되는 객체
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Coordinate {
    private double lnt;
    private double lat;
}
