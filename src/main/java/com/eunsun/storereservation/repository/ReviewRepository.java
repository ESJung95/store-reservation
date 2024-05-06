package com.eunsun.storereservation.repository;

import com.eunsun.storereservation.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByStoreId(Long storeId);

    boolean existsByCustomerIdAndStoreId(Long customerId, Long storeId);
}
