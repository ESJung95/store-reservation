package com.eunsun.storereservation.controller;

import com.eunsun.storereservation.dto.ReservationCreateDto;
import com.eunsun.storereservation.dto.ReservationWithStoreDto;
import com.eunsun.storereservation.dto.VisitConfirmationDto;
import com.eunsun.storereservation.enums.ReservationStatus;
import com.eunsun.storereservation.security.JwtTokenProvider;
import com.eunsun.storereservation.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
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

    /*
    매장 방문 확인 요청 처리
    1. 예약 번호와 고객 이름을 키오스크에서 받아옵니다.
    2. 예약 정보에 예약 상태(status)를 확인과 10분전 부터 방문 확인을 처리합니다.
        예약 시간 20분 후 부터는 방문 확인 요청이 불가능 합니다.
    */
    @Operation(summary = "매장 방문 확인")
    @PutMapping("/{reservationId}/visit")
    public ResponseEntity<?> confirmVisit(@PathVariable("reservationId") Long reservationId,
                                          @RequestParam("customerName") String customerName) {

        log.info(reservationId + "매장 방문 확인 요청");
        VisitConfirmationDto visitConfirmationDto = reservationService.confirmVisit(reservationId, customerName);

        return ResponseEntity.ok(visitConfirmationDto);
    }


    /*
    예약 상태 변경
    1. 예약 번호와 변경할 상태(status)를 받아옵니다.
    2. 매니저 권한을 가진 사용자만 수정 가능합니다.
    3. 예약 상태를 변경합니다.
    */
    @Operation(summary = "예약 상태 변경")
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


    /*
    매니저용 예약 상세 정보 조회
    1. 예약 번호를 받아옵니다.
    2. 매니저 권한을 가진 사용자 +  자신의 store 예약만 조회 가능합니다.
    3. 예약 상태 변경을 위해 사용합니다.
    */
    @Operation(summary = "매니저용 예약 상세 정보 조회")
    @GetMapping("/manager/{reservationId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<ReservationWithStoreDto> getReservationDetailForManager(@PathVariable("reservationId") Long reservationId, Authentication authentication) {
        log.info(reservationId + "-> 매니저용 예약 상세 정보 가져오기");
        Long loginManagerId = jwtTokenProvider.managerIdFromAuthentication(authentication);
        ReservationWithStoreDto reservationWithStoreDto = reservationService.getReservationDetailForManager(reservationId, loginManagerId);

        return ResponseEntity.ok(reservationWithStoreDto);

    }


    /*
    고객용 예약 상세 정보 조회
    1. 예약 번호를 받아옵니다.
    2. 고객 권한을 가진 사용자 + 자신의 예약만 조회 가능합니다.
    */
    @Operation(summary = "고객용 예약 상세 정보 조회")
    @GetMapping("/customer/{reservationId}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<ReservationWithStoreDto> getReservationDetail(@PathVariable("reservationId") Long reservationId, Authentication authentication) {
        log.info(reservationId + "-> 예약 상세 정보 가져오기");
        Long loginCustomerId = jwtTokenProvider.customerIdFromAuthentication(authentication);
        ReservationWithStoreDto reservationWithStoreDto = reservationService.getReservationDetail(reservationId, loginCustomerId);
        return ResponseEntity.ok(reservationWithStoreDto);
    }


    /*
    예약 생성
    1. 예약 생성에 필요한 정보를 받아옵니다.
    2. 고객 권한을 가진 사용자만 예약을 생성할 수 있습니다.
    */
    @Operation(summary = "예약 생성")
    @PostMapping
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<ReservationCreateDto> createReservation(@RequestBody ReservationCreateDto reservationCreateDto, Authentication authentication) {
        Long loginCustomerId = jwtTokenProvider.customerIdFromAuthentication(authentication);
        ReservationCreateDto createReservation = reservationService.createReservation(loginCustomerId, reservationCreateDto);
        log.info("예약 생성 성공");
        return ResponseEntity.ok(createReservation);
    }

}
