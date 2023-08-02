package com.zerobase.springmission.store.dto;

import com.zerobase.springmission.store.domain.Store;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreResponse {

    private String storeName;
    private String storePhone;
    private String address;
    private double rating;
    private double distance;

    public static Page<StoreResponse> fromEntities(Page<Store> page) {
        List<Store> stores = page.getContent();
        return new PageImpl<>(stores.stream()
                .map(store -> StoreResponse.builder()
                        .storeName(store.getStoreName())
                        .storePhone(store.getStorePhone())
                        .address(store.getAddress())
                        .rating(store.getRating())
                        .build()).collect(Collectors.toList()));
    }

    public static Page<StoreResponse> fromResults(Page<StoreResult> page) {
        List<StoreResult> stores = page.getContent();
        return new PageImpl<>(stores.stream()
                .map(storeResult -> StoreResponse.builder()
                        .storeName(storeResult.getStore().getStoreName())
                        .storePhone(storeResult.getStore().getStorePhone())
                        .address(storeResult.getStore().getAddress())
                        .rating(storeResult.getStore().getRating())
                        .distance(storeResult.getDistance())
                        .build()).collect(Collectors.toList()));
    }

    public static StoreResponse fromEntity(Store store) {
        return StoreResponse.builder()
                .storeName(store.getStoreName())
                .storePhone(store.getStorePhone())
                .address(store.getAddress())
                .rating(store.getRating())
                .build();
    }
}
