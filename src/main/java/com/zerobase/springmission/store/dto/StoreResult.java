package com.zerobase.springmission.store.dto;

import com.zerobase.springmission.store.domain.Store;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StoreResult {
    private Store store;
    private double distance;
}
