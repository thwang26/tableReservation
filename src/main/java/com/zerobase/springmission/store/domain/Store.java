package com.zerobase.springmission.store.domain;

import com.zerobase.springmission.member.domain.Member;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 매장정보 entity
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Store {

    /**
     * 파트너회원의 아이디 FK
     */
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * 매장 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    /**
     * 매장이름
     */
    private String storeName;

    /**
     * 도로명주소
     */
    private String address;

    /**
     * 전화번호
     */
    private String storePhone;

    /**
     * 경도
     */
    private double lnt;

    /**
     * 위도
     */
    private double lat;

    /**
     * 매장 오픈시간
     */
    private LocalTime openTime;

    /**
     * 매장 마감시간
     */
    private LocalTime closeTime;

    /**
     * 별점
     */
    private double rating;

    /**
     * 가입일자
     */
    @CreatedDate
    private LocalDateTime regDate;

    /**
     * 수정일자
     */
    @LastModifiedDate
    private LocalDateTime modDate;
}
