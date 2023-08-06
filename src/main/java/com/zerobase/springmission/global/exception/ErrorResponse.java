package com.zerobase.springmission.global.exception;

import lombok.*;

/**
 * 발생한 error에 대한 정보를 return 할 때 사용하는 객체
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {
    private ErrorCode errorCode;
    private String errorMessage;
}
