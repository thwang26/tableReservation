package com.zerobase.springmission.reservation.type;

/**
 * 예약상태 type
 */
public enum ReservationType {
    /**
     * 요청됨
     */
    REQUESTED,

    /**
     * 승인
     */
    ACCEPTED,

    /**
     * 거부
     */
    CANCELLED,

    /**
     * 사용
     */
    USED,

    /**
     * 만료
     */
    EXPIRED
}
