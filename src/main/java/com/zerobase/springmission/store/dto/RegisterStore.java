package com.zerobase.springmission.store.dto;

import com.zerobase.springmission.store.domain.Store;
import lombok.*;

import java.time.LocalDateTime;

public class RegisterStore {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        private String memberId;
        private String storeName;
        private String address;
        private String storePhone;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String memberId;
        private String storeName;
        private String address;
        private String storePhone;
        private double lnt;
        private double lat;
        private LocalDateTime regDate;

        public static Response fromEntity(Store store) {
            return Response.builder()
                    .memberId(store.getMember().getMemberId())
                    .storeName(store.getStoreName())
                    .address(store.getAddress())
                    .storePhone(store.getStorePhone())
                    .lnt(store.getLnt())
                    .lat(store.getLat())
                    .regDate(store.getRegDate())
                    .build();
        }
    }
}

