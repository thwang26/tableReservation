package com.zerobase.springmission.review.repository;

import com.zerobase.springmission.reservation.domain.Reservation;
import com.zerobase.springmission.review.domain.Review;
import com.zerobase.springmission.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByReservation(Reservation reservation);

    Optional<Review> findByReservation(Reservation reservation);

    Optional<List<Review>> findAllByStore(Store store);
}
