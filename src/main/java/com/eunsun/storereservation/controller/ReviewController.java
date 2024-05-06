package com.eunsun.storereservation.controller;

import com.eunsun.storereservation.dto.ReviewCreateDto;
import com.eunsun.storereservation.dto.ReviewResponseDto;
import com.eunsun.storereservation.security.JwtTokenProvider;
import com.eunsun.storereservation.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final JwtTokenProvider jwtTokenProvider;

    // 리뷰 작성하기 - 방문한 고객만
    @PostMapping("/{reservationId}")
    public ResponseEntity<ReviewResponseDto> createReview(@PathVariable("reservationId") Long reservationId,
                                                        @RequestBody ReviewCreateDto reviewCreateDto,
                                                        Authentication authentication) {

        Long loginCustomerId = jwtTokenProvider.customerIdFromAuthentication(authentication);
        ReviewResponseDto createdReview = reviewService.createReview(reservationId, reviewCreateDto, loginCustomerId);
        return ResponseEntity.ok(createdReview);

    }

}
