package com.zerobase.springmission.store.dto;

import com.zerobase.springmission.store.domain.Store;
import lombok.*;

/**
 * 거리순으로 정렬 시 사용되는 객체, Review 객체 + 거리
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StoreResult {
    private Store store;
    private double distance;
}
