package com.eunsun.storereservation.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponseDto {

    private Long id;
    private int rating;
    private String content;

    private String storeName;

    private String customerName;
    private LocalDate reservationDate;

}
