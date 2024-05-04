package com.eunsun.storereservation.util;

import com.eunsun.storereservation.domain.Reservation;
import com.eunsun.storereservation.dto.ReservationCreateDto;
import com.eunsun.storereservation.dto.ReservationWithStoreDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReservationUtils {

    // reservation Entity -> reservation Create Dto : 예약 정보 저장
    public static ReservationWithStoreDto convertToReservationWithStoreDto(Reservation reservation) {

        return ReservationWithStoreDto.builder()
                .reservationId(reservation.getId())
                .storeName(reservation.getStore().getStoreName())
                .storeAddress(reservation.getStore().getLocation())
                .storePhoneNumber(reservation.getStore().getStoreCallNumber())
                .reservationDate(reservation.getReservationDate())
                .reservationTime(reservation.getReservationTime())
                .numberOfPeople(reservation.getNumberOfPeople())
                .build();
    }

    // reservation Entity -> reservation Create Dto : 예약 정보 저장
    public static ReservationCreateDto convertToReservationDto(Reservation reservation) {

        return ReservationCreateDto.builder()
                .storeId(reservation.getStore().getId())
                .reservationDate(reservation.getReservationDate())
                .reservationTime(reservation.getReservationTime())
                .numberOfPeople(reservation.getNumberOfPeople())
                .build();
    }
}
