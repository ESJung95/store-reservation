package com.eunsun.storereservation.repository;

import com.eunsun.storereservation.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // 가입 이메일 중복 검사
    boolean existsByEmail(String email);

}


