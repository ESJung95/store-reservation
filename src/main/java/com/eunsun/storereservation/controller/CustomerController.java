package com.eunsun.storereservation.controller;

import com.eunsun.storereservation.dto.AuthDto;
import com.eunsun.storereservation.security.JwtTokenProvider;
import com.eunsun.storereservation.service.CustomerService;
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
public class CustomerController {

    private final CustomerService customerService;
    private final JwtTokenProvider jwtTokenProvider;

    // 로그인
    @PostMapping("/customer-login")
    public ResponseEntity<String> loginCustomer(@RequestBody AuthDto.login request) {
        log.info(request.getEmail() + " -> customer 로그인 요청");

        // 아이디와 패스워드 일치 확인 후 토큰 생성
        var customer = this.customerService.authenticateCustomer(request);
        var token = this.jwtTokenProvider.generateToken(customer.getEmail(), String.valueOf(customer.getRole()));

        return ResponseEntity.ok(token);
    }

    // 회원 가입
    @PostMapping("/customer-signup")
    public ResponseEntity<String> signupCustomer (@RequestBody AuthDto.signUp request) {
        log.info(request.getName() + "고객님의 회원가입 요청");
        String signupCustomerId = this.customerService.registerCustomer(request);

        if (signupCustomerId != null) {
            return ResponseEntity.ok(signupCustomerId);
        } else {
            return ResponseEntity.badRequest().body("회원가입 실패");
        }
    }
}
