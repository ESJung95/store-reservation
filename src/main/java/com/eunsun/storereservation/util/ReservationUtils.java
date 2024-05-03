package com.eunsun.storereservation.util;

import com.eunsun.storereservation.domain.Reservation;
import com.eunsun.storereservation.dto.ReservationCreateDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReservationUtils {

    // reservation Entity -> reservation Create Dto : 예약 정보 저장
    public static ReservationCreateDto convertToDto(Reservation reservation) {

        return ReservationCreateDto.builder()
                .storeId(reservation.getStore().getId())
                .reservationDate(reservation.getReservationDate())
                .reservationTime(reservation.getReservationTime())
                .numberOfPeople(reservation.getNumberOfPeople())
                .build();
    }
}
