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

    /**
     * 해당매장의 예약 가능시간 가져오기
     */
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

    /**
     * 해당매장의 오픈시간, 마감시간을 1시간 단위로 예약가능시간 list return
     */
    private List<LocalTime> getTimeRangeList(LocalTime openTime, LocalTime closeTime) {
        List<LocalTime> timeList = new ArrayList<>();
        LocalTime current = openTime;

        while (current.isBefore(closeTime)) {
            timeList.add(current);
            current = current.plus(1, ChronoUnit.HOURS);
        }

        return timeList;
    }

    /**
     * 예약요청
     * 예약은 최소 하루 전, 운영시간 사이에 예약할 수 있다.
     * 예약정보 저장 후 예약번호 return
     */
    @Transactional
    public ReservationResponse requestReservation(ReservationRequest reservationRequest) {
        String reservationId = generateReservationId(reservationRequest);
        Member member = memberRepository.findByMemberId(reservationRequest
                .getMemberId()).orElseThrow(() -> new MemberException(USER_NOT_FOUND));
        Store store = storeRepository.findByStoreName(reservationRequest.getStoreName());

        LocalDate date = reservationRequest.getReservationDate();
        LocalTime time = reservationRequest.getReservationTime();
        LocalTime openTime = store.getOpenTime();
        LocalTime closeTime = store.getCloseTime();

        if (LocalDate.now().isAfter(date.minusDays(1)) ||
        time.isBefore(openTime) || time.isAfter(closeTime.minusHours(1))) {
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

    /**
     * 예약번호 생성
     * yyMMddHH + 매장id(3글자) + 임의의 대문자 한 글자 (12글자)
     */
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

    /**
     * 매장 예약 수락
     */
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

    /**
     * 매장 예약 취소
     */
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

    /**
     * 매장예약 사용
     * 날짜가 지났거나 10분 전에 도착하지 못한경우 사용불가
     */
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

    /**
     * 예약상태 확인
     */
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

    /**
     * 매일 자정에 전날 예약상태를 취소상태로 변경
     * 취소된 예약(취소사유 확인목적), 사용된 예약은 기록을 위해 변경하지 않음
     */
    @Transactional
    @Scheduled(cron = "0 0 0 * * *") // 매일 12시 반복
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

    /**
     * 나의 예약기록 return
     */
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
