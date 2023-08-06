package com.zerobase.springmission.reservation.service;

import com.zerobase.springmission.global.exception.MemberException;
import com.zerobase.springmission.global.exception.ReservationException;
import com.zerobase.springmission.global.exception.StoreException;
import com.zerobase.springmission.member.domain.Member;
import com.zerobase.springmission.member.repository.MemberRepository;
import com.zerobase.springmission.reservation.domain.Reservation;
import com.zerobase.springmission.reservation.dto.*;
import com.zerobase.springmission.reservation.repository.ReservationRepository;
import com.zerobase.springmission.reservation.type.ReservationType;
import com.zerobase.springmission.store.domain.Store;
import com.zerobase.springmission.store.repository.StoreRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.zerobase.springmission.global.exception.ErrorCode.*;
import static com.zerobase.springmission.reservation.type.ReservationType.*;

@Service
@AllArgsConstructor
public class ReservationService {

    ReservationRepository reservationRepository;
    MemberRepository memberRepository;
    StoreRepository storeRepository;

    public ReservationTimeResponse getAvailableReservationTime(String storeName) {
        boolean exist = storeRepository.existsByStoreName(storeName);

        if (!exist) {
            throw new StoreException(STORE_NOT_FOUND);
        }

        Store store = storeRepository.findByStoreName(storeName);
        LocalTime openTime = store.getOpenTime();
        LocalTime closeTime = store.getCloseTime();

        List<LocalTime> timeList = getTimeRangeList(openTime, closeTime);

        return ReservationTimeResponse.builder()
                .storeName(storeName)
                .timeList(timeList)
                .build();
    }

    private List<LocalTime> getTimeRangeList(LocalTime openTime, LocalTime closeTime) {
        List<LocalTime> timeList = new ArrayList<>();
        LocalTime current = openTime;

        while (current.isBefore(closeTime)) {
            timeList.add(current);
            current = current.plus(1, ChronoUnit.HOURS);
        }

        return timeList;
    }

    @Transactional
    public ReservationResponse requestReservation(ReservationRequest reservationRequest) {
        String reservationId = generateReservationId(reservationRequest);
        Member member = memberRepository.findByMemberId(reservationRequest
                .getMemberId()).orElseThrow(() -> new MemberException(USER_NOT_FOUND));
        Store store = storeRepository.findByStoreName(reservationRequest.getStoreName());

        LocalDate date = reservationRequest.getReservationDate();
        if (LocalDate.now().isAfter(date.minusDays(1))) {
            throw new ReservationException(CANNOT_BE_RESERVED);
        }

        return ReservationResponse.fromEntity(reservationRepository
                .save(Reservation.builder()
                        .reservationId(reservationId)
                        .member(member)
                        .store(store)
                        .reservationDate(reservationRequest.getReservationDate())
                        .reservationTime(reservationRequest.getReservationTime())
                        .reservationType(REQUESTED)
                        .build()));
    }

    private String generateReservationId(ReservationRequest reservationRequest) {
        String date = reservationRequest.getReservationDate()
                .toString().substring(2).replaceAll("-", "");
        String time = reservationRequest.getReservationTime()
                .toString().substring(0, 2).replaceAll(":", "");
        String storeId = storeRepository.findByStoreName(
                reservationRequest.getStoreName()).getStoreId().toString();

        StringBuilder reservationIdBuilder = new StringBuilder();
        reservationIdBuilder.append(date).append(time).append(storeId);

        int maxStoreIdLength = 3 - storeId.length();
        while (maxStoreIdLength-- > 0) {
            reservationIdBuilder.append(0);
        }

        reservationIdBuilder.append((char) ((int) (Math.random() * 26) + 65));

        return reservationIdBuilder.toString();
    }

    @Transactional
    public ReservationResponse acceptReservation(String reservationId, String memberId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException(RESERVATION_NOT_FOUND));

        Long storeId = reservation.getStore().getStoreId();
        Store store = storeRepository.findByStoreId(storeId);

        if (!store.getMember().getMemberId().equals(memberId)) {
            throw new ReservationException(NO_CHANGE_PERMISSION);
        }

        ReservationType reservationType = reservation.getReservationType();
        checkReservation(reservationType);

        reservation.setReservationType(ACCEPTED);

        return ReservationResponse.fromEntity(reservationRepository
                .save(reservation));
    }

    @Transactional
    public ReservationResponse cancelReservation(CancelReservationRequest cancelReservationRequest,
                                                 String memberId) {
        Reservation reservation = reservationRepository.findById(cancelReservationRequest
                        .getReservationId())
                .orElseThrow(() -> new ReservationException(RESERVATION_NOT_FOUND));

        Long storeId = reservation.getStore().getStoreId();
        Store store = storeRepository.findByStoreId(storeId);

        if (!store.getMember().getMemberId().equals(memberId)) {
            throw new ReservationException(NO_CHANGE_PERMISSION);
        }

        ReservationType reservationType = reservation.getReservationType();
        checkReservation(reservationType);

        reservation.setReservationType(CANCELLED);
        reservation.setCancelDescription(cancelReservationRequest.getCancelDescription());

        return ReservationResponse.fromEntity(reservationRepository
                .save(reservation));
    }

    @Transactional
    public ReservationResponse useReservation(String reservationId, String memberId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException(RESERVATION_NOT_FOUND));

        Long storeId = reservation.getStore().getStoreId();
        Store store = storeRepository.findByStoreId(storeId);

        if (!store.getMember().getMemberId().equals(memberId)) {
            throw new ReservationException(NO_CHANGE_PERMISSION);
        }

        if (reservation.getReservationType() != ACCEPTED) {
            throw new ReservationException(UN_ACCEPTED_RESERVATION);
        }

        LocalDate date = reservation.getReservationDate();
        LocalTime time = reservation.getReservationTime();
        if (LocalDate.now().isAfter(date) ||
                (LocalDate.now().equals(date) && LocalTime.now()
                        .isAfter(time.minusMinutes(10)))) {
            throw new ReservationException(RESERVATION_TIME_EXCEEDED);
        }

        reservation.setReservationType(USED);

        return ReservationResponse.fromEntity(reservationRepository
                .save(reservation));
    }

    private void checkReservation(ReservationType reservationType) {
        switch (reservationType) {
            case ACCEPTED:
                throw new ReservationException(ALREADY_ACCEPTED);
            case CANCELLED:
                throw new ReservationException(ALREADY_CANCELLED);
            case USED:
                throw new ReservationException(ALREADY_COMPLETED);
            case EXPIRED:
                throw new ReservationException(ALREADY_EXPIRED);
            default:
                break;
        }
    }

    @Transactional
    @Scheduled(cron = "0 * * * * *") // 매일 12시 반복
    public void updateReservationTypeIfDatePassed() {
        List<Reservation> reservations = reservationRepository.findAll();

        LocalDate now = LocalDate.now();
        List<Reservation> cancelledReservations = new ArrayList<>();

        for (Reservation reservation : reservations) {
            if (reservation.getReservationDate().isBefore(now) &&
                    reservation.getReservationType() != USED &&
                    reservation.getReservationType() != CANCELLED) {
                reservation.setReservationType(EXPIRED);
                cancelledReservations.add(reservation);
            }
        }

        reservationRepository.saveAll(cancelledReservations);
    }

    public List<ReservationInfo> getReservation(String memberId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberException(USER_NOT_FOUND));
        List<Reservation> reservations = reservationRepository
                .findByMember(member);

        List<ReservationInfo> reservationInfos = new ArrayList<>();
        for (Reservation reservation: reservations) {
            reservationInfos.add(ReservationInfo.fromEntity(reservation));
        }

        if (reservationInfos.size() == 0) {
            throw new ReservationException(RESERVATION_NOT_FOUND);
        }

        return reservationInfos;
    }
}
