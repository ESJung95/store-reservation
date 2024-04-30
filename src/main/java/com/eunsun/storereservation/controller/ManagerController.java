package com.eunsun.storereservation.controller;

import com.eunsun.storereservation.dto.AuthDto;
import com.eunsun.storereservation.service.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.RequestEntity;
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

    @PostMapping("/manager-signup")
    public ResponseEntity<String> signup(@RequestBody AuthDto.signUp request) {
        log.info(request.getName() + "매니저님 회원가입 요청");
        String signupManagerId = this.managerService.registerManager(request);

        if(signupManagerId != null) {
            return ResponseEntity.ok(signupManagerId);
        } else {
            return ResponseEntity.badRequest().body("회원가입 실패");
        }

    }
}
