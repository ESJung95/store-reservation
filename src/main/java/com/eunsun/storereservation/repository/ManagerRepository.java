package com.eunsun.storereservation.repository;

import com.eunsun.storereservation.domain.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {

    boolean existsByEmail(String email);

    // 이메일 정보 확인
    Optional<Manager> findByEmail(String email);
}
