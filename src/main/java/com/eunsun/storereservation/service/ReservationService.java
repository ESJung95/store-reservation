package com.eunsun.storereservation.service;

import com.eunsun.storereservation.domain.Customer;
import com.eunsun.storereservation.domain.Reservation;
import com.eunsun.storereservation.domain.Store;
import com.eunsun.storereservation.dto.ReservationCreateDto;
import com.eunsun.storereservation.exception.StoreNotFoundException;
import com.eunsun.storereservation.exception.UserNotFoundException;
import com.eunsun.storereservation.repository.CustomerRepository;
import com.eunsun.storereservation.repository.ReservationRepository;
import com.eunsun.storereservation.repository.StoreRepository;
import com.eunsun.storereservation.util.ReservationUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;

    // 예약 정보 저장
    public ReservationCreateDto createReservation (Long loginCustomerId, ReservationCreateDto reservationCreateDto) {
        Customer customer = customerRepository.findById(loginCustomerId)
                .orElseThrow(() -> new UserNotFoundException(loginCustomerId + "에 해당하는 사용자를 찾을 수 없습니다."));

        Store store = storeRepository.findById(reservationCreateDto.getStoreId())
                .orElseThrow(() -> new StoreNotFoundException(reservationCreateDto.getStoreId() + "에 해당하는 가게를 찾을 수 없습니다."));

        Reservation reservation = Reservation.builder()
                .customer(customer)
                .store(store)
                .reservationDate(reservationCreateDto.getReservationDate())
                .reservationTime(reservationCreateDto.getReservationTime())
                .numberOfPeople(reservationCreateDto.getNumberOfPeople())
                .build();

        reservationRepository.save(reservation);
        return ReservationUtils.convertToDto(reservation);
    }
}
