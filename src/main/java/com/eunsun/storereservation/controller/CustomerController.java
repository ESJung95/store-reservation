package com.eunsun.storereservation.controller;

import com.eunsun.storereservation.dto.AuthDto;
import com.eunsun.storereservation.security.JwtTokenProvider;
import com.eunsun.storereservation.service.CustomerService;
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
public class CustomerController {

    private final CustomerService customerService;
    private final JwtTokenProvider jwtTokenProvider;

    /*
    Customer 로그인 요청을 처리합니다.
    1. 클라이언트로부터 전송된 로그인 요청 데이터(이메일, 패스워드)를 받아옵니다.
    2. 이메일과 패스워드가 일치하면 해당 고객의 정보로 JWT 토큰을 생성합니다.
    3. 생성된 JWT 토큰을 반환합니다.
    */
    @Operation(summary = "고객 로그인")
    @PostMapping("/customer-login")
    public ResponseEntity<String> loginCustomer(@RequestBody AuthDto.login request) {
        log.info(request.getEmail() + " -> customer 로그인 요청");

        // 아이디와 패스워드 일치 확인 후 토큰 생성
        var customer = this.customerService.authenticateCustomer(request);
        var token = this.jwtTokenProvider.generateToken(customer.getEmail(), String.valueOf(customer.getRole()));

        return ResponseEntity.ok(token);
    }

    /*
    Customer 회원 가입 요청을 처리합니다.
    1. 클라이언트로부터 전송된 회원 가입 요청 데이터(이름, 이메일, 패스워드)를 받아옵니다.
    2. 회원 가입을 처리합니다.
    3. 회원 가입이 성공하면 생성된 고객 ID를 반환하고, 실패하면 실패 메시지를 반환합니다.
    */
    @Operation(summary = "고객 회원 가입")
    @PostMapping("/customer-signup")
    public ResponseEntity<String> signupCustomer (@RequestBody AuthDto.signUp request) {
        log.info("[ "+ request.getName() + " ] 고객님의 회원가입 요청");

        String signupCustomerId = this.customerService.registerCustomer(request);

        if (signupCustomerId != null) {
            return ResponseEntity.ok(signupCustomerId);
        } else {
            return ResponseEntity.badRequest().body("회원가입 실패");
        }
    }
}
