package com.eunsun.storereservation.controller;

import com.eunsun.storereservation.dto.ReservationCreateDto;
import com.eunsun.storereservation.security.JwtTokenProvider;
import com.eunsun.storereservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final JwtTokenProvider jwtTokenProvider;

    // 예약 생성
    @PostMapping
    public ResponseEntity<ReservationCreateDto> createReservation(@RequestBody ReservationCreateDto reservationCreateDto, Authentication authentication) {
        Long loginCustomerId = jwtTokenProvider.customerIdFromAuthentication(authentication);
        ReservationCreateDto createReservation = reservationService.createReservation(loginCustomerId, reservationCreateDto);
        log.info("예약 생성 성공");
        return ResponseEntity.ok(createReservation);
    }

}
