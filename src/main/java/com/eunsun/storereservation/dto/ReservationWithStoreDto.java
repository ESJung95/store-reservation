package com.eunsun.storereservation.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationWithStoreDto {

    private Long reservationId;

    private String storeName;
    private String storeAddress;
    private String storePhoneNumber;

    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private int numberOfPeople;
}
