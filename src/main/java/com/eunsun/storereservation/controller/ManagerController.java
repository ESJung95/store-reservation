package com.eunsun.storereservation.controller;

import com.eunsun.storereservation.dto.AuthDto;
import com.eunsun.storereservation.security.JwtTokenProvider;
import com.eunsun.storereservation.service.ManagerService;
import io.swagger.v3.oas.annotations.Operation;
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

    /*
    Manager 로그인 요청을 처리합니다.
    1. 클라이언트로부터 전송된 로그인 요청 데이터(이메일, 패스워드)를 받아옵니다.
    2. 이메일과 패스워드가 일치하면 해당 매니저의 정보로 JWT 토큰을 생성합니다.
    3. 생성된 JWT 토큰을 반환합니다.
    */
    @Operation(summary = "매니저 로그인")
    @PostMapping("/manager-login")
    public ResponseEntity<String> loginManager(@RequestBody AuthDto.login request) {
        log.info(request.getEmail() + " manager -> 로그인 요청");

        // 아이디와 패스워드 일치 확인 후 토큰 생성
        var manager = this.managerService.authenticateManager(request);
        var token = this.jwtTokenProvider.generateToken(manager.getEmail(), String.valueOf(manager.getRole()));

        log.info(request.getEmail() + " manager -> 로그인 성공");
        return ResponseEntity.ok(token);

    }

    /*
    Manager 회원 가입 요청을 처리합니다.
    1. 클라이언트로부터 전송된 회원 가입 요청 데이터(이름, 이메일, 패스워드)를 받아옵니다.
    2. 회원 가입을 처리합니다.
    3. 회원 가입이 성공하면 생성된 매니저 ID를 반환하고, 실패하면 실패 메시지를 반환합니다.
    */
    @Operation(summary = "매니저 회원 가입")
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
