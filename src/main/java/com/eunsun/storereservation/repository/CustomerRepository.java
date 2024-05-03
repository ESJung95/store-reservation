package com.eunsun.storereservation.repository;

import com.eunsun.storereservation.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // 이메일로 id 조회
    @Query("SELECT c.id FROM Customer c WHERE c.email = :email")
    Optional<Long> findCustomerIdByEmail(@Param("email") String email);

    // 가입 이메일 중복 검사
    boolean existsByEmail(String email);


    // 이메일 정보 확인
    Optional<Customer> findByEmail(String email);
}


