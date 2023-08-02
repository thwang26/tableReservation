package com.zerobase.springmission.store.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RatingType {
    ZERO_STAR(0.0),
    ONE_STAR(1.0),
    TWO_STARS(2.0),
    THREE_STARS(3.0),
    FOUR_STARS(4.0),
    FIVE_STARS(5.0);

    private final Double rating;
}
