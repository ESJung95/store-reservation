package com.eunsun.storereservation.controller;

import com.eunsun.storereservation.dto.ReviewDto;
import com.eunsun.storereservation.dto.ReviewResponseDto;
import com.eunsun.storereservation.security.JwtTokenProvider;
import com.eunsun.storereservation.service.ReviewService;
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
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final JwtTokenProvider jwtTokenProvider;

    /*
    리뷰 삭제하기 - 매니저에 의한 삭제
    1. 매니저 권한이 있어야 삭제가 가능합니다.
    2. 리뷰 작성자가 아닌 매니저만 삭제할 수 있습니다.
    */

    @Operation(summary = "매니저에 의한 리뷰 삭제")
    @DeleteMapping("/{reviewId}/manager")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<String> deleteReviewByManager(@PathVariable("reviewId") Long reviewId,
                                                        Authentication authentication) {

        Long loginManagerId = jwtTokenProvider.managerIdFromAuthentication(authentication);
        reviewService.deleteReviewByManager(reviewId, loginManagerId);

        return ResponseEntity.ok("리뷰 삭제를 완료했습니다.");
    }

    /*
    리뷰 삭제하기 - 고객(작성자)에 의한 삭제
    1. 고객 권한이 있어야 삭제가 가능합니다.
    2. 리뷰 작성자 본인만 삭제할 수 있습니다.
    */
    @Operation(summary = "고객(작성자)에 의한 리뷰 삭제")
    @DeleteMapping("/{reviewId}/customer")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<String> deleteReviewByCustomer(@PathVariable("reviewId") Long reviewId,
                                                         Authentication authentication) {

        Long loginCustomerId = jwtTokenProvider.customerIdFromAuthentication(authentication);
        reviewService.deleteReviewByCustomer(reviewId, loginCustomerId);

        return ResponseEntity.ok("리뷰 삭제를 완료했습니다.");
    }

    /*
    리뷰 수정하기
    1. 고객 권한이 있어야 수정이 가능합니다.
    2. 리뷰 작성자 본인만 수정할 수 있습니다.
    */
    @Operation(summary = "리뷰 수정")
    @PutMapping("/{reviewId}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable("reviewId") Long reviewId,
                                                        @RequestBody ReviewDto reviewDto,
                                                        Authentication authentication) {

        Long loginCustomerId = jwtTokenProvider.customerIdFromAuthentication(authentication);
        ReviewDto updatedReview = reviewService.updateReview(reviewId, reviewDto, loginCustomerId);
        return ResponseEntity.ok(updatedReview);
    }



    /*
    리뷰 작성하기 - 매장 방문 후 리뷰 작성
    1. 고객 권한이 있어야 작성이 가능합니다.
    2. 매장을 방문한 경우에만 리뷰를 작성할 수 있습니다.
    */
    @Operation(summary = "매장 방문 후 리뷰 작성")
    @PostMapping("/{reservationId}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<ReviewResponseDto> createReview(@PathVariable("reservationId") Long reservationId,
                                                        @RequestBody ReviewDto reviewDto,
                                                        Authentication authentication) {

        Long loginCustomerId = jwtTokenProvider.customerIdFromAuthentication(authentication);
        ReviewResponseDto createdReview = reviewService.createReview(reservationId, reviewDto, loginCustomerId);
        return ResponseEntity.ok(createdReview);

    }

}
