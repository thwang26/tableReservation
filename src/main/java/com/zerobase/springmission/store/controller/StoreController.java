package com.zerobase.springmission.store.controller;

import com.zerobase.springmission.member.domain.Member;
import com.zerobase.springmission.store.dto.RegisterStore;
import com.zerobase.springmission.store.dto.StoreResponse;
import com.zerobase.springmission.store.service.StoreService;
import com.zerobase.springmission.store.type.SortingType;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * 매장관리 기능
 */
@RestController
@RequestMapping("/store")
@AllArgsConstructor
public class StoreController {

    private final StoreService storeService;

    /**
     * 매장등록기능
     * 파트너회원만 가능하며
     * 토큰에서 회원아이디를 추출 하여 사용
     */
    @PostMapping("/register-store")
    @PreAuthorize("hasRole('PARTNER')")
    public RegisterStore.Response registerStore(
            @RequestBody RegisterStore.Request registerStoreRequest,
            Authentication authentication) throws IOException {
        String memberId = ((Member) authentication.getPrincipal()).getMemberId();
        registerStoreRequest.setMemberId(memberId);
        return storeService.registerStore(registerStoreRequest);
    }

    /**
     * 매장정보 얻기 기능
     * page 값, 정렬타입, 나의 위치(경도, 위도)값을 통해 정렬값 가져옴
     */
    @GetMapping("/get-stores")
    public Page<StoreResponse> getStores(@RequestParam(required = false, defaultValue = "DISTANCE")
                                         SortingType sortingType, Pageable pageable,
                                         @RequestParam(required = false, defaultValue = "0.0") Double lat,
                                         @RequestParam(required = false, defaultValue = "0.0") Double lnt) {
        return storeService.getStores(sortingType, pageable, lat, lnt);
    }

    /**
     * 매장 상세정보 얻기 기능
     */
    @GetMapping("/get-store")
    public StoreResponse getStore(@RequestParam String storeName) {
        return storeService.getStore(storeName);
    }
}
