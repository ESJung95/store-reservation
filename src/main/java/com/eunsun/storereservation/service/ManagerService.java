package com.eunsun.storereservation.service;

import com.eunsun.storereservation.domain.Manager;
import com.eunsun.storereservation.dto.AuthDto;
import com.eunsun.storereservation.enums.Authority;
import com.eunsun.storereservation.exception.EmailDuplicateException;
import com.eunsun.storereservation.exception.UserEmailNotFoundException;
import com.eunsun.storereservation.repository.ManagerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final PasswordEncoder passwordEncoder;

    // 로그인 정보 일치 확인
    public Manager authenticateManager(AuthDto.login request) {
        Manager manager = managerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserEmailNotFoundException("매니저 아이디를 찾을 수 없습니다."));

        if(!passwordEncoder.matches(request.getPassword(), manager.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        return manager;
    }

    // 토큰 생성 시 필요한 정보
    public UserDetails loadUserByEmail(String email) {
        Manager manager = managerRepository.findByEmail(email)
                .orElseThrow(() -> new UserEmailNotFoundException("해당 이메일을 가진 사용자를 찾을 수 없습니다. -> " + email));
        // 사용자의 권한 정보를 설정
        Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(Authority.ROLE_MANAGER.name()));

        return User.builder()
                .username(manager.getEmail())
                .password(manager.getPassword())
                .authorities(authorities)
                .build();
    }

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
