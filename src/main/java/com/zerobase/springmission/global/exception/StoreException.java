package com.zerobase.springmission.global.exception;

import lombok.*;

/**
 * store 기능 구현 시 발생하는 exception
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreException extends RuntimeException {
    private ErrorCode errorCode;
    private String errorMessage;

    public StoreException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
