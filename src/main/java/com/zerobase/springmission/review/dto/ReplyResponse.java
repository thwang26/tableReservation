package com.zerobase.springmission.review.dto;

import com.zerobase.springmission.review.domain.Review;
import lombok.*;

/**
 * 답글 작성 응답 객체
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyResponse {
    private String reservationId;
    private String reply;

    public static ReplyResponse fromEntity(Review review) {
        return ReplyResponse.builder()
                .reservationId(review.getReservation().getReservationId())
                .reply(review.getReply())
                .build();
    }
}
