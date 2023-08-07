package com.zerobase.springmission.store.repository;

import com.zerobase.springmission.store.domain.Store;
import com.zerobase.springmission.store.dto.StoreResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    boolean existsByStoreName(String storeName);

    boolean existsByAddress(String address);

    /**
     * 이름순 정렬
     */
    Page<Store> findAllByOrderByStoreNameAsc(Pageable pageable);

    /**
     * 별점순 정렬
     */
    Page<Store> findAllByOrderByRatingDesc(Pageable pageable);

    /**
     * 거리순 정렬
     */
    @Query(value = "select NEW com.zerobase.springmission" +
            ".store.dto.StoreResult(s, " +
            "ROUND((6371*acos(cos(radians(:lat))*cos(radians(s.lat))" +
            "*cos(radians(s.lnt) -radians(:lnt))+sin(radians(:lat))" +
            "*sin(radians(s.lat)))), 3)) FROM Store s order by " +
            "ROUND((6371*acos(cos(radians(:lat))*cos(radians(s.lat))" +
            "*cos(radians(s.lnt) -radians(:lnt))+sin(radians(:lat))" +
            "*sin(radians(s.lat)))), 3) asc")
    Page<StoreResult> findAllByOrderByDistanceAsc(Pageable pageable,
                                                  @Param("lat") Double lat,
                                                  @Param("lnt") Double lnt);

    Store findByStoreName(String storeName);

    Store findByStoreId(Long storeId);

    /**
     * 리뷰 작성에 따른 별점 평균 계산
     */
    @Modifying
    @Query("UPDATE Store s SET s.rating = " +
            "(SELECT AVG(r.rating) FROM Review r WHERE r.store = s) " +
            "WHERE s.storeId = :storeId")
    void updateStoreRatingFromReviewRatings(@Param("storeId") Long storeId);
}
