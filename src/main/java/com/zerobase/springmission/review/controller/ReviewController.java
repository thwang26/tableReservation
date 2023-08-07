package com.zerobase.springmission.review.controller;

import com.zerobase.springmission.member.domain.Member;
import com.zerobase.springmission.review.dto.ReplyRequest;
import com.zerobase.springmission.review.dto.ReplyResponse;
import com.zerobase.springmission.review.dto.ReviewRequest;
import com.zerobase.springmission.review.dto.ReviewResponse;
import com.zerobase.springmission.review.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 리뷰작성 기능
 */
@RestController
@RequestMapping("/review")
@AllArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /*
    * 예약 사용 후 리뷰작성
    */
    @PostMapping("/write-review")
    @PreAuthorize("hasRole('USER')")
    public ReviewResponse writeReview(
            @RequestBody ReviewRequest reviewRequest,
            Authentication authentication) {
        String memberId = ((Member) authentication.getPrincipal()).getMemberId();
        return reviewService.writeReview(reviewRequest, memberId);
    }

    /**
     * 리뷰에 답글작성
     */
    @PostMapping("/write-reply")
    @PreAuthorize("hasRole('PARTNER')")
    public ReplyResponse writeReply(
            @RequestBody ReplyRequest replyRequest,
            Authentication authentication) {
        String memberId = ((Member) authentication.getPrincipal()).getMemberId();
        return reviewService.writeReply(replyRequest, memberId);
    }

    /**
     * 매장에 작성된 리뷰 가져오기
     */
    @GetMapping("/get-review/{storeId}")
    public List<ReviewResponse> getReview(
            @PathVariable Long storeId) {
        return reviewService.getReview(storeId);
    }
}
