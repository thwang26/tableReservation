package com.zerobase.springmission.reservation.controller;

import com.zerobase.springmission.member.domain.Member;
import com.zerobase.springmission.reservation.dto.*;
import com.zerobase.springmission.reservation.service.ReservationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/reservation")
@AllArgsConstructor
public class ReservationController {
    ReservationService reservationService;

    @GetMapping("/available-reservation-time")
    @PreAuthorize("hasRole('USER')")
    public ReservationTimeResponse getAvailableReservationTime(
            @RequestParam String storeName) {
        return reservationService.getAvailableReservationTime(storeName);
    }

    @PostMapping("/request-reservation")
    @PreAuthorize("hasRole('USER')")
    public ReservationResponse requestReservation(
            @RequestBody ReservationRequest reservationRequest,
            Authentication authentication) {
        String memberId = ((Member) authentication.getPrincipal()).getMemberId();
        reservationRequest.setMemberId(memberId);
        return reservationService.requestReservation(reservationRequest);
    }

    @PostMapping("/accept-reservation")
    @PreAuthorize("hasRole('PARTNER')")
    public ReservationResponse acceptReservation(
            @RequestBody Map<String, String> reservationMap,
            Authentication authentication
    ) {
        String memberId = ((Member) authentication.getPrincipal()).getMemberId();
        return reservationService
                .acceptReservation(reservationMap.get("reservationId"), memberId);
    }

    @PostMapping("/cancel-reservation")
    @PreAuthorize("hasRole('PARTNER')")
    public ReservationResponse cancelReservation(
            @RequestBody CancelReservationRequest cancelReservationRequest,
            Authentication authentication
    ) {
        String memberId = ((Member) authentication.getPrincipal()).getMemberId();
        return reservationService
                .cancelReservation(cancelReservationRequest, memberId);
    }

    @PostMapping("/use-reservation")
    @PreAuthorize("hasRole('PARTNER')")
    public ReservationResponse useReservation(
            @RequestBody Map<String, String> reservationMap,
            Authentication authentication
    ) {
        String memberId = ((Member) authentication.getPrincipal()).getMemberId();
        return reservationService
                .useReservation(reservationMap.get("reservationId"), memberId);
    }

    @GetMapping("/get-reservation")
    @PreAuthorize("hasRole('USER')")
    public List<ReservationInfo> getReservation(
            Authentication authentication
    ) {
        String memberId = ((Member) authentication.getPrincipal()).getMemberId();
        return reservationService.getReservation(memberId);
    }
}
