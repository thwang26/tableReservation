package com.zerobase.springmission.review.domain;

import com.zerobase.springmission.reservation.domain.Reservation;
import com.zerobase.springmission.store.domain.Store;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 매장정보 entity
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    /**
     * 예약번호
     */
    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    /**
     * 매장번호
     */
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    /**
     * 제목
     */
    private String title;

    /**
     * 내용
     */
    private String contents;

    /**
     * 별점
     */
    private double rating;

    /**
     * 매장주인 답글
     */
    private String reply;

    /**
     * 가입일자
     */
    @CreatedDate
    private LocalDateTime regDate;
}
