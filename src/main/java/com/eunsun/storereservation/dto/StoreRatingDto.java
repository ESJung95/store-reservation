package com.eunsun.storereservation.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreRatingDto {

    private StoreDto store;
    private double averageRating;
}
