package com.eunsun.storereservation.service;

import com.eunsun.storereservation.domain.Manager;
import com.eunsun.storereservation.dto.AuthDto;
import com.eunsun.storereservation.exception.EmailDuplicateException;
import com.eunsun.storereservation.repository.ManagerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class ManagerService {

        private final ManagerRepository managerRepository;
        private final PasswordEncoder passwordEncoder;
    public String registerManager(AuthDto.signUp request) {

        // 가입 이메일 중복 검사
        if(managerRepository.existsByEmail(request.getEmail())) {
            throw new EmailDuplicateException();
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // Manager 객체 생성
        Manager manager = Manager.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .name(request.getName())
                .phone(request.getPhone())
                .build();

        Manager saveManager = managerRepository.save(manager);

        log.info("매니저의 회원가입 정보를 성공적으로 저장했습니다.");
        return saveManager.getEmail();

    }
}
