package com.eunsun.storereservation.controller;

import com.eunsun.storereservation.dto.ReservationCreateDto;
import com.eunsun.storereservation.dto.ReservationWithStoreDto;
import com.eunsun.storereservation.dto.VisitConfirmationDto;
import com.eunsun.storereservation.enums.ReservationStatus;
import com.eunsun.storereservation.security.JwtTokenProvider;
import com.eunsun.storereservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final JwtTokenProvider jwtTokenProvider;

    // 방문 확인 - 예약 시간 10분 전 + 예약 번호 입력으로 확인
    @PutMapping("/{reservationId}/visit")
    public ResponseEntity<?> confirmVisit(@PathVariable("reservationId") Long reservationId,
                                          @RequestParam("customerName") String customerName) {

        log.info(reservationId + "매장 방문 확인 요청");
        VisitConfirmationDto visitConfirmationDto = reservationService.confirmVisit(reservationId, customerName);

        return ResponseEntity.ok(visitConfirmationDto);
    }


    // 예약 상태 변경 - 승인, 거절
    @PutMapping("/{reservationId}/status")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> updateReservationStatus(@PathVariable("reservationId") Long reservationId,
                                                          @RequestParam("status")ReservationStatus status,
                                                          Authentication authentication) {

        log.info(reservationId + " : 요청 예약 상태 업데이트");
        Long loginManagerId = jwtTokenProvider.managerIdFromAuthentication(authentication);
        ReservationWithStoreDto updateReservation = reservationService.updateReservationStatus(reservationId, loginManagerId, status);

        log.info("예약 상태 변경 성공");
        return ResponseEntity.ok(updateReservation);
    }


    // 예약 조회 기능 - manager + 자기 가게만 조회 가능
    @GetMapping("/manager/{reservationId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<ReservationWithStoreDto> getReservationDetailForManager(@PathVariable("reservationId") Long reservationId, Authentication authentication) {
        log.info(reservationId + "-> 매니저용 예약 상세 정보 가져오기");
        Long loginManagerId = jwtTokenProvider.managerIdFromAuthentication(authentication);
        ReservationWithStoreDto reservationWithStoreDto = reservationService.getReservationDetailForManager(reservationId, loginManagerId);

        return ResponseEntity.ok(reservationWithStoreDto);

    }


    // 예약 조회 기능 - customer + 본인 예약만
    @GetMapping("/customer/{reservationId}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<ReservationWithStoreDto> getReservationDetail(@PathVariable("reservationId") Long reservationId, Authentication authentication) {
        log.info(reservationId + "-> 예약 상세 정보 가져오기");
        Long loginCustomerId = jwtTokenProvider.customerIdFromAuthentication(authentication);
        ReservationWithStoreDto reservationWithStoreDto = reservationService.getReservationDetail(reservationId, loginCustomerId);
        return ResponseEntity.ok(reservationWithStoreDto);
    }


    // 예약 생성
    @PostMapping
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<ReservationCreateDto> createReservation(@RequestBody ReservationCreateDto reservationCreateDto, Authentication authentication) {
        Long loginCustomerId = jwtTokenProvider.customerIdFromAuthentication(authentication);
        ReservationCreateDto createReservation = reservationService.createReservation(loginCustomerId, reservationCreateDto);
        log.info("예약 생성 성공");
        return ResponseEntity.ok(createReservation);
    }

}
