package com.eunsun.storereservation.repository;

import com.eunsun.storereservation.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // 가입 이메일 중복 검사
    boolean existsByEmail(String email);


    // 이메일 정보 확인
    Optional<Customer> findByEmail(String email);
}


