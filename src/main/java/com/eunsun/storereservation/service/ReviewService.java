package com.eunsun.storereservation.service;

import com.eunsun.storereservation.domain.Reservation;
import com.eunsun.storereservation.domain.Review;
import com.eunsun.storereservation.domain.Store;
import com.eunsun.storereservation.dto.ReviewDto;
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

    // 리뷰 삭제하기 - store manager
    public void deleteReviewByManager(Long reviewId, Long managerId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다."));

        Store store = review.getStore();
        log.info("store.getManager().getId(): {}", store.getManager().getId());
        log.info("managerId: {}", managerId);

        if (!store.getManager().getId().equals(managerId)) {
            throw new IllegalArgumentException("리뷰 삭제 권한이 없습니다. 해당 리뷰는 다른 매니저의 가게에 속합니다.");
        }

        reviewRepository.delete(review);
    }

    // 리뷰 삭제하기 - 작성자
    public void deleteReviewByCustomer(Long reviewId, Long customerId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다."));

        if (!review.getCustomer().getId().equals(customerId)) {
            throw new IllegalArgumentException("리뷰 삭제 권한이 없습니다.");
        }

        reviewRepository.delete(review);
    }

    // 리뷰 수정하기
    public ReviewDto updateReview(Long reviewId, ReviewDto reviewDto, Long loginCustomerId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다."));

        validateReviewUpdate(review, loginCustomerId);
        validateReviewData(reviewDto);

        review.setRating(reviewDto.getRating());
        review.setContent(reviewDto.getContent());

        Review updatedReview = reviewRepository.save(review);

        return ReviewDto.builder()
                .rating(updatedReview.getRating())
                .content(updatedReview.getContent())
                .build();
    }


    // 리뷰 작성하기
    public ReviewResponseDto createReview(Long reservationId, ReviewDto reviewDto, Long loginCustomerId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("예약 정보를 찾을 수 없습니다. 예약 ID: " + reservationId));

        validateReviewCreation(reservation, loginCustomerId);
        validateReviewData(reviewDto);

        Review review = Review.builder()
                .rating(reviewDto.getRating())
                .content(reviewDto.getContent())
                .customer(reservation.getCustomer())
                .store(reservation.getStore())
                .build();

        Review savedReview = reviewRepository.save(review);

        return ReviewUtils.convertToReviewResponseDto(savedReview, reservation);

    }

    // 리뷰 수정 권한 검사
    private void validateReviewUpdate(Review review, Long loginCustomerId) {
        if (!review.getCustomer().getId().equals(loginCustomerId)) {
            throw new IllegalArgumentException("리뷰 수정 권한이 없습니다.");
        }
    }

    // 리뷰 작성 권한 검사
    private void validateReviewCreation(Reservation reservation, Long loginCustomerId) {
        if (!reservation.getCustomer().getId().equals(loginCustomerId)) {
            throw new IllegalArgumentException("리뷰 작성 권한이 없습니다.");
        }

        // 방문 여부 확인
        if (!reservation.isVisited()) {
            throw new IllegalArgumentException("방문이 확인되지 않은 예약입니다.");
        }


        // 이미 작성한 리뷰가 있는지 확인
        boolean hasExistingReview = reviewRepository.existsByCustomerIdAndStoreId(loginCustomerId, reservation.getStore().getId());
        if (hasExistingReview) {
            throw new IllegalArgumentException("이미 작성한 리뷰가 있습니다. 기존 리뷰를 수정해주세요.");
        }

    }

    // 리뷰 작성/수정 내용, 별점 조건 확인
    private void validateReviewData(ReviewDto reviewDto) {

        // 별점 조건 확인
        int rating = reviewDto.getRating();
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("별점은 1점부터 5점까지만 가능합니다.");
        }

        // 리뷰 내용 비었는지 확인
        String content = reviewDto.getContent();
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("리뷰 내용을 입력해주세요.");
        }
    }


}