package com.zerobase.springmission.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
    STORE_NOT_FOUND("매장정보가 없습니다.");
//    ACCOUNT_TRANSACTION_LOCK("해당 계좌는 사용 중입니다."),
//    TRANSACTION_NOT_FOUND("해당 거래가 없습니다."),
//    AMOUNT_EXCEED_BALANCE("거래 금액이 계좌 잔액보다 큽니다."),
//    TRANSACTION_ACCOUNT_UN_MATCH("이 거래는 해당 계좌에서 발생한 거래가 아닙니다."),
//    CANCEL_MUST_FULLY("부분 취소는 허용되지 않습니다."),
//    TOO_OLD_ORDER_TO_CANCEL("1년이 지난 거래는 취소가 불가능합니다."),
//    USER_ACCOUNT_UN_MATCH("사용자와 계좌의 소유주가 다릅니다."),
//    ACCOUNT_ALREADY_UNREGISTERED("계좌가 이미 해지되었습니다."),
//    BALANCE_NOT_EMPTY("잔액이 있는 계좌는 해지할 수 없습니다."),
//    MAX_ACCOUNT_PER_USER_10("사용자 최대 계좌는 10개입니다.");

    private String description;
}
