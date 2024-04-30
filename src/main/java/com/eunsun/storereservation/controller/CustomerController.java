package com.eunsun.storereservation.controller;

import com.eunsun.storereservation.dto.AuthDto;
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

    @PostMapping("/customer-signup")
    public ResponseEntity<String> signup(@RequestBody AuthDto.signUp request) {
        log.info(request.getName() + "고객님의 회원가입 요청");
        String signupCustomerId = this.customerService.registerCustomer(request);

        if (signupCustomerId != null) {
            return ResponseEntity.ok(signupCustomerId);
        } else {
            return ResponseEntity.badRequest().body("회원가입 실패");
        }
    }
}
