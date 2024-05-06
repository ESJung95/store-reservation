package com.eunsun.storereservation.util;

import com.eunsun.storereservation.domain.Reservation;
import com.eunsun.storereservation.domain.Review;
import com.eunsun.storereservation.dto.ReviewResponseDto;

public class ReviewUtils {

    // Review Entity -> ReviewDto
    public static ReviewResponseDto convertToReviewResponseDto(Review review, Reservation reservation) {
        return ReviewResponseDto.builder()
                .id(review.getId())
                .rating(review.getRating())
                .content(review.getContent())
                .storeName(review.getStore().getStoreName())
                .customerName(review.getCustomer().getName())
                .reservationDate(reservation.getReservationDate())
                .build();
    }
}