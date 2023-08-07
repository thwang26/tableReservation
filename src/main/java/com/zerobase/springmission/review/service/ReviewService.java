package com.zerobase.springmission.review.service;

import com.zerobase.springmission.global.exception.ReservationException;
import com.zerobase.springmission.global.exception.ReviewException;
import com.zerobase.springmission.reservation.domain.Reservation;
import com.zerobase.springmission.reservation.repository.ReservationRepository;
import com.zerobase.springmission.review.domain.Review;
import com.zerobase.springmission.review.dto.ReplyRequest;
import com.zerobase.springmission.review.dto.ReplyResponse;
import com.zerobase.springmission.review.dto.ReviewRequest;
import com.zerobase.springmission.review.dto.ReviewResponse;
import com.zerobase.springmission.review.repository.ReviewRepository;
import com.zerobase.springmission.store.domain.Store;
import com.zerobase.springmission.store.repository.StoreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.zerobase.springmission.global.exception.ErrorCode.*;
import static com.zerobase.springmission.reservation.type.ReservationType.USED;

/**
 * 리뷰작성 서비스
 */
@Service
@AllArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;

    /**
     * 리뷰작성
     * 이미 작성된 리뷰인지, 작성 권한을 가지고있는지,
     * 리뷰작성이 가능한 예약인지(사용된 예약)
     * 확인 후 리뷰작성
     * 리뷰작성 후 해당상점의 별점평균 계산
     */
    @Transactional
    public ReviewResponse writeReview(ReviewRequest reviewRequest,
                                      String memberId) {
        Reservation reservation = reservationRepository
                .findById(reviewRequest.getReservationId())
                .orElseThrow(() -> new ReservationException(RESERVATION_NOT_FOUND));

        boolean exist = reviewRepository.existsByReservation(reservation);
        if (exist) {
            throw new ReviewException(ALREADY_WRITTEN_REVIEW);
        }

        String reservedMemberId = reservation.getMember().getMemberId();
        if (!reservedMemberId.equals(memberId)) {
            throw new ReviewException(NO_REVIEW_PERMISSION);
        }

        if (reservation.getReservationType() != USED) {
            throw new ReviewException(CANNOT_WRITE_REVIEW);
        }

        Review review = reviewRepository.save(Review.builder()
                .reservation(reservation)
                .store(reservation.getStore())
                .title(reviewRequest.getTitle())
                .contents(reviewRequest.getContents())
                .rating(reviewRequest.getRatingType().getRating())
                .regDate(LocalDateTime.now())
                .build());

        storeRepository.updateStoreRatingFromReviewRatings(review.getStore().getStoreId());

        return ReviewResponse.fromEntity(review);
    }

    /**
     * 작성된 리뷰에 매장주인 답글 작성
     */
    @Transactional
    public ReplyResponse writeReply(ReplyRequest replyRequest
            , String memberId) {
        Reservation reservation = reservationRepository.findById(replyRequest.getReservationId())
                .orElseThrow(() -> new ReservationException(RESERVATION_NOT_FOUND));

        Review review = reviewRepository.findByReservation(reservation)
                .orElseThrow(() -> new ReviewException(REVIEW_NOT_FOUND));
        if (!review.getStore().getMember().getMemberId().equals(memberId)) {
            throw new ReviewException(NO_REPLY_PERMISSION);
        }

        if (review.getReply() != null) {
            throw new ReviewException(ALREADY_WRITTEN_REPLY);
        }

        review.setReply(replyRequest.getReply());

        return ReplyResponse.fromEntity(reviewRepository.save(review));
    }

    /**
     * 해당매장에 작성된 리뷰 가져오기
     */
    public List<ReviewResponse> getReview(Long storeId) {
        Store store = storeRepository.findByStoreId(storeId);

        List<Review> reviews = reviewRepository.findAllByStore(store)
                .orElseThrow(() -> new ReviewException(REVIEW_NOT_FOUND));

        List<ReviewResponse> reviewResponses = new ArrayList<>();
        for (Review review: reviews) {
            reviewResponses.add(ReviewResponse.fromEntity(review));
        }

        Collections.reverse(reviewResponses);

        return reviewResponses;
    }
}
