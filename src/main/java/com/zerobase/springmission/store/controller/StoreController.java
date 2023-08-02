package com.zerobase.springmission.store.controller;

import com.zerobase.springmission.store.dto.RegisterStore;
import com.zerobase.springmission.store.dto.StoreResponse;
import com.zerobase.springmission.store.service.StoreService;
import com.zerobase.springmission.store.type.SortingType;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/store")
@AllArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping("/register-store")
    @PreAuthorize("hasRole('PARTNER')")
    public RegisterStore.Response registerStore(
            @RequestBody RegisterStore.Request registerStoreRequest) throws IOException {
        return storeService.registerStore(registerStoreRequest);
    }

    @GetMapping("/get-stores")
    public Page<StoreResponse> getStores(@RequestParam(required = false, defaultValue = "DISTANCE")
                                     SortingType sortingType, Pageable pageable,
                                         @RequestParam(required = false, defaultValue = "0.0") Double lat,
                                         @RequestParam(required = false, defaultValue = "0.0") Double lnt) {
        return storeService.getStores(sortingType, pageable, lat, lnt);
    }

    @GetMapping("/get-store")
    public StoreResponse getStore(@RequestParam String storeName) {
        return storeService.getStore(storeName);
    }
}
