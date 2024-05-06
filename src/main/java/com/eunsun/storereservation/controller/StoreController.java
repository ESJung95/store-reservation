package com.eunsun.storereservation.controller;

import com.eunsun.storereservation.dto.StoreDetailDto;
import com.eunsun.storereservation.dto.StoreDto;
import com.eunsun.storereservation.dto.StoreRatingDto;
import com.eunsun.storereservation.security.JwtTokenProvider;
import com.eunsun.storereservation.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
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

    /*
    매장 상세 정보 조회
    1. 매장 ID를 받아 매장 상세 정보를 조회합니다.
    2. 조회한 매장 상세 정보를 반환합니다.
    */
    @Operation(summary = "매장 상세 정보 조회")
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreDetailDto> getStoreDetail(@PathVariable("storeId") Long storeId) {
        log.info(storeId + " : 매장 상세 정보 가져오기");
        StoreDetailDto storeDetailDto = storeService.getStoreDetail(storeId);

        return ResponseEntity.ok(storeDetailDto);
    }

    /*
    매장 이름으로 검색
    1. 검색어(keyword)를 받아 매장 이름으로 검색합니다.
    2. 검색 결과가 있으면 리스트로 반환하고, 없으면 "검색어를 입력하세요." 메시지를 반환합니다.
    */
    @Operation(summary = "매장 이름으로 검색")
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

    /*
    높은 리뷰 별점 순으로 매장 전체 정보 조회
    1. 매장 정보를 리뷰 별점 순(내림차순)으로 조회합니다.
    2. 조회한 매장 정보를 리스트로 반환합니다.
    */
    @Operation(summary = "높은 리뷰 별점 순으로 매장 전체 정보 조회")
    @GetMapping("/rating")
    public List<StoreRatingDto> getStoresOrderedByRating() {
        log.info("별점 순으로 매장 전체 정보 조회 성공");
        return storeService.getAllStoresOrderedByRating();
    }

    /*
    가나다순으로 매장 전체 정보 조회
    1. 매장 정보를 가나다순으로 조회합니다.
    2. 조회한 매장 정보를 리스트로 반환합니다.
    */
    @Operation(summary = "가나다순으로 매장 전체 정보 조회")
    @GetMapping("/basic")
    public List<StoreDto> getStores() {
        log.info("가다나순으로 매장 전체 정보 조회 성공");
        return storeService.getAllStores();
    }

    /*
    매장 정보 삭제 :  반드시 매니저 권한이 필요합니다.
    1. 매장 ID와 로그인한 매니저의 인증 정보를 받습니다.
    2. 로그인한 매니저의 ID를 가져옵니다.
    3. 매장 정보를 삭제합니다.
    4. 삭제에 성공하면 성공 응답을 반환하고, 권한이 없는 경우 예외 메시지를 반환합니다.
    */
    @Operation(summary = "매장 정보 삭제")
    @DeleteMapping("/{storeId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> deleteStore(@PathVariable("storeId") Long storeId, Authentication authentication) {
        try {
            Long loginManagerId = jwtTokenProvider.managerIdFromAuthentication(authentication);
            storeService.deleteStore(storeId, loginManagerId);
            log.info(storeId + "번 매장 정보 삭제 성공, 삭제 매니저 아이디 ->" + loginManagerId);

            return ResponseEntity.ok().build();

        } catch (IllegalArgumentException e) {
            log.error("매장 정보 삭제 실패 -> 권한 없음");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    /*
    매장 정보 수정 : 반드시 매니저 권한이 필요합니다.
    1. 매장 ID, 수정할 매장 정보(StoreDto), 로그인한 매니저의 인증 정보를 받습니다.
    2. 로그인한 매니저의 ID를 가져옵니다.
    3. 매장 정보를 수정합니다.
    4. 수정된 매장 정보를 반환하고, 권한이 없는 경우 예외 메시지를 반환합니다.
    */
    @Operation(summary = "매장 정보 수정")
    @PutMapping("/{storeId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> modifyStoreInfo(@PathVariable("storeId") Long storeId, @RequestBody StoreDto storeDto, Authentication authentication) {
        try {
            Long loginManagerId = jwtTokenProvider.managerIdFromAuthentication(authentication);
            StoreDto updateStoreDto = storeService.modifyStoreInfo(storeId, storeDto, loginManagerId);
            log.info("매장 정보 수정 성공 ->" + updateStoreDto);

            return ResponseEntity.ok(updateStoreDto);

        } catch (IllegalArgumentException e) {
            log.error("매장 정보 삭제 실패 -> 권한 없음");

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }


    /*
    매장 정보 저장 : 반드시 매니저 권한이 필요합니다.
    1. 저장할 매장 정보(StoreDto)와 로그인한 매니저의 인증 정보를 받습니다.
    2. 로그인한 매니저의 ID를 가져옵니다.
    3. 매장 정보를 저장합니다.
    4. 저장된 매장 정보를 반환하고, 예외 발생 시 실패 메시지를 반환합니다.
    */
    @Operation(summary = "매장 정보 저장")
    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> saveStoreInfo(@RequestBody StoreDto storeDto, Authentication authentication) {

        try {

            Long loginManagerId = jwtTokenProvider.managerIdFromAuthentication(authentication);
            StoreDto savedStoreDto = storeService.createStore(storeDto, loginManagerId);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedStoreDto);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("매장 정보 저장 실패.");
        }
    }
}
