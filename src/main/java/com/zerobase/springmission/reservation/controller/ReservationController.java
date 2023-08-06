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

/**
 * 매장 예약기능
 */
@Slf4j
@RestController
@RequestMapping("/reservation")
@AllArgsConstructor
public class ReservationController {
    ReservationService reservationService;

    /**
     * 해당매장의 예약가능시간 return
     */
    @GetMapping("/available-reservation-time")
    @PreAuthorize("hasRole('USER')")
    public ReservationTimeResponse getAvailableReservationTime(
            @RequestParam String storeName) {
        return reservationService.getAvailableReservationTime(storeName);
    }

    /**
     * 매장 예약 요청(user)
     * 매장 예약번호 발급, 매장예약상태는 enum type으로 관리
     * request type
     */
    @PostMapping("/request-reservation")
    @PreAuthorize("hasRole('USER')")
    public ReservationResponse requestReservation(
            @RequestBody ReservationRequest reservationRequest,
            Authentication authentication) {
        String memberId = ((Member) authentication.getPrincipal()).getMemberId();
        reservationRequest.setMemberId(memberId);
        return reservationService.requestReservation(reservationRequest);
    }

    /**
     * 매장 예약 요청을 수락(partner)
     * accept type
     */
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

    /**
     * 매장 예약 요청을 거부(partner)
     * 거부사유 또한 포함
     * cancelled type
     */
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

    /**
     * 예약된 매장예약을 사용
     * 매장안의 키오스크에서 사용하므로 parter 권한으로 요청
     * used type
     */
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

    /**
     * 나의 예약기록 확인
     */
    @GetMapping("/get-reservation")
    @PreAuthorize("hasRole('USER')")
    public List<ReservationInfo> getReservation(
            Authentication authentication
    ) {
        String memberId = ((Member) authentication.getPrincipal()).getMemberId();
        return reservationService.getReservation(memberId);
    }
}
