package com.eunsun.storereservation.controller;

import com.eunsun.storereservation.dto.AuthDto;
import com.eunsun.storereservation.security.JwtTokenProvider;
import com.eunsun.storereservation.service.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
public class ManagerController {

    private final ManagerService managerService;
    private final JwtTokenProvider jwtTokenProvider;

    // 로그인
    @PostMapping("/manager-login")
    public ResponseEntity<String> loginManager(@RequestBody AuthDto.login request) {
        log.info(request.getEmail() + " -> manager 로그인 요청");

        // 아이디와 패스워드 일치 확인 후 토큰 생성
        var manager = this.managerService.authenticateManager(request);
        var token = this.jwtTokenProvider.generateToken(manager.getEmail(), String.valueOf(manager.getRole()));

        return ResponseEntity.ok(token);

    }

    // 회원 가입
    @PostMapping("/manager-signup")
    public ResponseEntity<String> signupManager (@RequestBody AuthDto.signUp request) {
        log.info(request.getName() + "매니저님 회원가입 요청");
        String signupManagerId = this.managerService.registerManager(request);

        if(signupManagerId != null) {
            return ResponseEntity.ok(signupManagerId);
        } else {
            return ResponseEntity.badRequest().body("회원가입 실패");
        }

    }
}
