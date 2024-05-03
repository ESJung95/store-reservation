package com.eunsun.storereservation.repository;

import com.eunsun.storereservation.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findByStoreNameLikeIgnoreCase(String searchKeyword);

    Optional<Store> findByIdAndManagerId(Long storeId, Long managerId);
}