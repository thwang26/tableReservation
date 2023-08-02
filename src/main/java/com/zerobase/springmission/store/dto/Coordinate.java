package com.zerobase.springmission.store.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Coordinate {
    private double lnt;
    private double lat;
}
