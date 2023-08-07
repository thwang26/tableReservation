package com.zerobase.springmission.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 에러발생 시의 에러코드
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {
    ACCOUNT_ALREADY_REGISTERED("이미 가입된 회원입니다."),
    ACCOUNT_DOES_NOT_EXIST("회원정보가 존재하지 않습니다."),
    PASSWORD_DOES_NOT_MATCH("비밀번호가 일치하지 않습니다."),
    USER_NOT_FOUND("사용자가 없습니다."),
    NOT_A_PARTNER("매장등록은 파트너 회원만 사용 가능합니다."),
    STORE_ALREADY_EXIST("이미 등록된 점포입니다."),
    WRONG_RESPONSE("잘못된 응답입니다."),
    RESULT_NOT_FOUND("검색결과가 없습니다."),
    STORE_EMPTY("등록된 매장이 없습니다."),
    WRONG_SORTINGTYPE("정렬조건이 잘못되었습니다."),
    STORE_NOT_FOUND("매장정보가 없습니다."),
    RESERVATION_NOT_FOUND("예약정보가 없습니다."),
    ALREADY_ACCEPTED("이미 승인된 예약입니다."),
    ALREADY_CANCELLED("이미 취소된 예약입니다."),
    ALREADY_COMPLETED("이미 사용된 예약입니다."),
    ALREADY_EXPIRED("이미 예약시간이 지난 예약입니다."),
    NO_CHANGE_PERMISSION("예약변경 권한이 없습니다."),
    UN_ACCEPTED_RESERVATION("사용할 수 없는 예약입니다."),
    NO_USE_PERMISSION("예약사용 권한이 없습니다."),
    RESERVATION_TIME_EXCEEDED("예약시간을 초과하였습니다."),
    CANNOT_BE_RESERVED("예약할 수 없는 날짜입니다."),
    NO_REVIEW_PERMISSION("리뷰 작성 권한이 없습니다."),
    CANNOT_WRITE_REVIEW("리뷰를 작성하려면 예약을 사용해야합니다."),
    ALREADY_WRITTEN_REVIEW("이미 리뷰가 작성되었습니다."),
    REVIEW_NOT_FOUND("리뷰가 없습니다."),
    ALREADY_WRITTEN_REPLY("이미 답글이 작성되었습니다."),
    NO_REPLY_PERMISSION("답글 작성 권한이 없습니다.");
    private String description;
}
