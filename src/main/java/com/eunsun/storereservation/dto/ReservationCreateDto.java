package com.eunsun.storereservation.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationCreateDto {

    private Long storeId;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private int numberOfPeople;
}
