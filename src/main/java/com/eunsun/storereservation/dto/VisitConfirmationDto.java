package com.eunsun.storereservation.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitConfirmationDto {

    private String customerName;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private int numberOfPeople;
    private String message;
}