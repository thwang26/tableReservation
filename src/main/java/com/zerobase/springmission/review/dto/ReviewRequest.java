package com.zerobase.springmission.review.dto;

import com.zerobase.springmission.review.type.RatingType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 리뷰 작성 요청 객체
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {
    private String reservationId;
    private String title;
    private String contents;
    private RatingType ratingType;
}
