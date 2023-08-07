package com.zerobase.springmission.review.dto;

import com.zerobase.springmission.review.domain.Review;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 리뷰 작성 응답 객체
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {
    private Long reviewId;
    private String reservationId;
    private String storeName;
    private String title;
    private String contents;
    private double rating;
    private String reply;
    private LocalDateTime regDate;
    public static ReviewResponse fromEntity(Review review) {
        return ReviewResponse.builder()
                .reviewId(review.getReviewId())
                .reservationId(review.getReservation().getReservationId())
                .storeName(review.getStore().getStoreName())
                .title(review.getTitle())
                .contents(review.getContents())
                .rating(review.getRating())
                .reply(review.getReply())
                .regDate(review.getRegDate())
                .build();
    }
}
