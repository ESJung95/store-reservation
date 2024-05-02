package com.eunsun.storereservation.repository;

import com.eunsun.storereservation.domain.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {

    // 이메일로 id 조회
    @Query("SELECT m.id FROM Manager m WHERE m.email = :email")
    Long findManagerIdByEmail(@Param("email") String email);

    // 회원가입 이메일 중복 확인
    boolean existsByEmail(String email);

    // 이메일 정보 확인
    Optional<Manager> findByEmail(String email);
}
