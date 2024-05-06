package com.eunsun.storereservation.service;

import com.eunsun.storereservation.domain.Reservation;
import com.eunsun.storereservation.domain.Review;
import com.eunsun.storereservation.dto.ReviewCreateDto;
import com.eunsun.storereservation.dto.ReviewResponseDto;
import com.eunsun.storereservation.exception.ReservationNotFoundException;
import com.eunsun.storereservation.repository.ReservationRepository;
import com.eunsun.storereservation.repository.ReviewRepository;
import com.eunsun.storereservation.util.ReviewUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;

    // 리뷰 작성하기
    public ReviewResponseDto createReview(Long reservationId, ReviewCreateDto reviewCreateDto, Long loginCustomerId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("예약 정보를 찾을 수 없습니다. 예약 ID: " + reservationId));

        validateReviewCreation(reservation, loginCustomerId, reviewCreateDto);

        Review review = Review.builder()
                .rating(reviewCreateDto.getRating())
                .content(reviewCreateDto.getContent())
                .customer(reservation.getCustomer())
                .store(reservation.getStore())
                .build();

        Review savedReview = reviewRepository.save(review);

        return ReviewUtils.convertToReviewResponseDto(savedReview, reservation);

    }

    private void validateReviewCreation(Reservation reservation, Long loginCustomerId, ReviewCreateDto reviewCreateDto) {
        if (!reservation.getCustomer().getId().equals(loginCustomerId)) {
            throw new IllegalArgumentException("리뷰 작성 권한이 없습니다.");
        }

        // 방문 여부 확인
        if (!reservation.isVisited()) {
            throw new IllegalArgumentException("방문이 확인되지 않은 예약입니다.");
        }

        // 별점 조건 확인
        int rating = reviewCreateDto.getRating();
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("별점은 1점부터 5점까지만 가능합니다.");
        }

        // 리뷰 내용 비었는지 확인
        String content = reviewCreateDto.getContent();
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("리뷰 내용을 입력해주세요.");
        }

        // 이미 작성한 리뷰가 있는지 확인
        boolean hasExistingReview = reviewRepository.existsByCustomerIdAndStoreId(loginCustomerId, reservation.getStore().getId());
        if (hasExistingReview) {
            throw new IllegalArgumentException("이미 작성한 리뷰가 있습니다. 기존 리뷰를 수정해주세요.");
        }

    }
}