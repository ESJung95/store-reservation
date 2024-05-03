package com.eunsun.storereservation.controller;

import com.eunsun.storereservation.dto.StoreDetailDto;
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

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/stores")
public class StoreController {

    private final StoreService storeService;
    private final JwtTokenProvider jwtTokenProvider;

    // 매장 상세 정보 확인
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreDetailDto> getStoreDetail(@PathVariable("storeId") Long storeId) {
        log.info(storeId + " : 매장 상세 정보 가져오기");
        StoreDetailDto storeDetailDto = storeService.getStoreDetail(storeId);

        return ResponseEntity.ok(storeDetailDto);
    }

    // 매장 목록 조회 - 매장 이름으로 조회
    @GetMapping("/search/{keyword}")
    public ResponseEntity<?> searchStores(@PathVariable("keyword") String keyword) {
        log.info("매장 이름으로 검색 ->" + keyword);
        List<StoreDto> storeDtos = storeService.searchStoresByName(keyword);

        if (storeDtos.isEmpty()) {
            log.info("매장명이 존재하지 않는다 : " + keyword);
            return ResponseEntity.ok("검색어를 입력하세요.");
        }

        return ResponseEntity.ok(storeDtos);
    }
    // 매장 목록 조회 - 전체 조회
    @GetMapping
    public List<StoreDto> getStores() {
        log.info("매장 전체 정보 조회 성공");
        return storeService.getAllStores();
    }

    // 매장 정보 삭제
    @DeleteMapping("/{storeId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> deleteStore(@PathVariable("storeId") Long storeId, Authentication authentication) {
        try {
            Long loginManagerId = jwtTokenProvider.extractManagerIdFromAuthentication(authentication);
            storeService.deleteStore(storeId, loginManagerId);
            log.info(storeId + "번 매장 정보 삭제 성공, 삭제 매니저 아이디 ->" + loginManagerId);

            return ResponseEntity.ok().build();

        } catch (IllegalArgumentException e) {
            log.error("매장 정보 삭제 실패 -> 권한 없음");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
    // 매장 정보 수정
    @PutMapping("/{storeId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> modifyStoreInfo(@PathVariable("storeId") Long storeId, @RequestBody StoreDto storeDto, Authentication authentication) {
        try {
            Long loginManagerId = jwtTokenProvider.extractManagerIdFromAuthentication(authentication);
            StoreDto updateStoreDto = storeService.modifyStoreInfo(storeId, storeDto, loginManagerId);
            log.info("매장 정보 수정 성공 ->" + updateStoreDto);

            return ResponseEntity.ok(updateStoreDto);

        } catch (IllegalArgumentException e) {
            log.error("매장 정보 삭제 실패 -> 권한 없음");

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }


    // 매장 정보 저장
    @PostMapping("/create")
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
