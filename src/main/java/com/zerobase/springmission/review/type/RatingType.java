package com.zerobase.springmission.review.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 별점을 분류하는 type
 * 매장등록 시 별점 초기값은 ZERO_STAR
 * 리뷰 등록 시 별점 계산
 */
@AllArgsConstructor
@Getter
public enum RatingType {
    ZERO_STAR(0.0),
    ONE_STAR(1.0),
    TWO_STARS(2.0),
    THREE_STARS(3.0),
    FOUR_STARS(4.0),
    FIVE_STARS(5.0);

    private final double rating;
}
