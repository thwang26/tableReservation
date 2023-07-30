package com.zerobase.springmission.store.domain;

import com.zerobase.springmission.member.domain.Member;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Store {

    @ManyToOne
    private Member partnerId;
    @Id
    private Long id;
    private String storeName;
    private String address;
    private String storePhone;
    private Double lnt;
    private Double lat;

    @CreatedDate
    private LocalDateTime regDate;

}
