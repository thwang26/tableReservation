package com.zerobase.springmission.global.exception;

import lombok.*;

/**
 * 도로명주소 -> 위도, 경도 값을 가져오는 api 사용 시 exception
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewException extends RuntimeException {
    private ErrorCode errorCode;
    private String errorMessage;

    public ReviewException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
