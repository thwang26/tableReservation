package com.zerobase.springmission.store.domain;

import com.zerobase.springmission.member.domain.Member;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Store {

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String storeName;
    private String address;
    private String storePhone;
    private double lnt;
    private double lat;
    private LocalTime openTime;
    private LocalTime closeTime;
    private double rating;

    @CreatedDate
    private LocalDateTime regDate;

    @LastModifiedDate
    private LocalDateTime modDate;
}
