package com.zerobase.springmission.store.repository;

import com.zerobase.springmission.store.domain.Store;
import com.zerobase.springmission.store.dto.StoreResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    boolean existsByStoreName(String storeName);

    boolean existsByAddress(String address);

    Page<Store> findAllByOrderByStoreNameAsc(Pageable pageable);

    Page<Store> findAllByOrderByRatingDesc(Pageable pageable);

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
}
