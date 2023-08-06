package com.zerobase.springmission.store.service;

import com.zerobase.springmission.global.exception.MemberException;
import com.zerobase.springmission.global.exception.StoreException;
import com.zerobase.springmission.member.domain.Member;
import com.zerobase.springmission.member.repository.MemberRepository;
import com.zerobase.springmission.member.type.MemberType;
import com.zerobase.springmission.store.domain.Store;
import com.zerobase.springmission.store.dto.Coordinate;
import com.zerobase.springmission.store.dto.RegisterStore;
import com.zerobase.springmission.store.dto.StoreResponse;
import com.zerobase.springmission.store.repository.StoreRepository;
import com.zerobase.springmission.store.type.SortingType;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;

import static com.zerobase.springmission.global.exception.ErrorCode.*;
import static com.zerobase.springmission.review.type.RatingType.ZERO_STAR;

@Service
@AllArgsConstructor
public class StoreService {

    private final GeocodingService geocodingService;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;

    /**
     * 매장등록기능, 파트너회원이라면 진행
     */
    @Transactional
    public RegisterStore.Response registerStore(RegisterStore.Request registerStoreRequest) throws IOException {
        Member member = getPartnerAccount(registerStoreRequest.getMemberId());
        checkDuplicateStoreName(registerStoreRequest);

        Coordinate coordinate = geocodingService
                .getCoordinate(registerStoreRequest.getAddress());

        return RegisterStore.Response.fromEntity(storeRepository.save(Store.builder()
                .member(member)
                .storeName(registerStoreRequest.getStoreName())
                .address(registerStoreRequest.getAddress())
                .storePhone(registerStoreRequest.getStorePhone())
                .lnt(coordinate.getLnt())
                .lat(coordinate.getLat())
                .rating(ZERO_STAR.getRating())
                .openTime(registerStoreRequest.getOpenTime())
                .closeTime(registerStoreRequest.getCloseTime())
                .regDate(LocalDateTime.now())
                .build()));
    }

    /**
     * 파트너회원을 검증
     */
    private Member getPartnerAccount(String memberId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberException(USER_NOT_FOUND));

        if (member.getMemberType() != MemberType.PARTNER) {
            throw new MemberException(NOT_A_PARTNER);
        }

        return member;
    }

    /**
     * 매장 등록 시 중복된 매장이름을 검증
     */
    private void checkDuplicateStoreName(RegisterStore.Request registerStoreRequest) {
        if (storeRepository.existsByStoreName(registerStoreRequest.getStoreName())
                || storeRepository.existsByAddress(registerStoreRequest.getAddress())) {
            throw new StoreException(STORE_ALREADY_EXIST);
        }
    }

    /**
     * 매장정렬기준에 따라 정렬된 매장 page를 return
     */
    public Page<StoreResponse> getStores(SortingType sortingType, Pageable pageable, Double lat, Double lnt) {
        if (storeRepository.count() == 0) {
            throw new StoreException(STORE_EMPTY);
        }

        switch (sortingType) {
            case NAME:
                return getStoresByName(pageable);
            case RATING:
                return getStoresByRating(pageable);
            case DISTANCE:
                return getStoresByDistance(pageable, lat, lnt);
            default:
                throw new StoreException(WRONG_SORTINGTYPE);
        }
    }

    /**
     *  이름순으로 정렬된 매장 return
     */
    private Page<StoreResponse> getStoresByName(Pageable pageable) {
        return StoreResponse.fromEntities(storeRepository
                .findAllByOrderByStoreNameAsc(pageable));
    }

    /**
     *  별점순으로 정렬된 매장 return
     */
    private Page<StoreResponse> getStoresByRating(Pageable pageable) {
        return StoreResponse.fromEntities(storeRepository
                .findAllByOrderByRatingDesc(pageable));
    }

    /**
     *  거리순으로 정렬된 매장 return
     */
    private Page<StoreResponse> getStoresByDistance(Pageable pageable,
                                                    Double lat, Double lnt) {
        return StoreResponse.fromResults(storeRepository
                .findAllByOrderByDistanceAsc(pageable, lat, lnt));
    }

    /**
     * 매장 상세정보 return
     */
    public StoreResponse getStore(String storeName) {
        boolean exist = storeRepository.existsByStoreName(storeName);

        if (!exist) {
            throw new StoreException(STORE_NOT_FOUND);
        }

        return StoreResponse.fromEntity(storeRepository
                .findByStoreName(storeName));
    }
}
