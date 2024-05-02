package com.eunsun.storereservation.controller;

import com.eunsun.storereservation.dto.StoreDto;
import com.eunsun.storereservation.security.JwtTokenProvider;
import com.eunsun.storereservation.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/stores")
public class StoreController {

    private final StoreService storeService;
    private final JwtTokenProvider jwtTokenProvider;

    // 매장 정보 수정
    @PutMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> modifyStoreInfo(@RequestBody StoreDto storeDto, Authentication authentication) {

        return ResponseEntity.ok().body("수정 성공");
    }
    // 매장 정보 저장
    @PostMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> saveStoreInfo(@RequestBody StoreDto storeDto, Authentication authentication) {

        try {

            Long loginManagerId = jwtTokenProvider.extractManagerIdFromAuthentication(authentication);
            StoreDto savedStoreDto = storeService.createStore(storeDto, loginManagerId);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedStoreDto);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("매장 정보 저장 실패.");
        }
    }
}
