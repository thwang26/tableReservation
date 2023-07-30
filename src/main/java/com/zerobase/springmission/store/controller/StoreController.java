//package com.zerobase.springmission.store.controller;
//
//import com.zerobase.springmission.store.service.StoreService;
//import lombok.AllArgsConstructor;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/store")
//@AllArgsConstructor
//public class StoreController {
//
//    private StoreService storeService;
//
//    @PostMapping("/register-store")
//    public void registerStore(
//            @RequestBody RegisterStore.Request registerStoreRequest) {
//        return storeService.registerStore(registerStoreRequest);
//    }
//
//}
