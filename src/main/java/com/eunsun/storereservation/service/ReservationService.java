package com.eunsun.storereservation.service;

import com.eunsun.storereservation.domain.Customer;
import com.eunsun.storereservation.domain.Reservation;
import com.eunsun.storereservation.domain.Store;
import com.eunsun.storereservation.dto.ReservationCreateDto;
import com.eunsun.storereservation.dto.ReservationWithStoreDto;
import com.eunsun.storereservation.dto.VisitConfirmationDto;
import com.eunsun.storereservation.enums.ReservationStatus;
import com.eunsun.storereservation.exception.ReservationNotFoundException;
import com.eunsun.storereservation.exception.StoreNotFoundException;
import com.eunsun.storereservation.exception.UserNotFoundException;
import com.eunsun.storereservation.repository.CustomerRepository;
import com.eunsun.storereservation.repository.ReservationRepository;
import com.eunsun.storereservation.repository.StoreRepository;
import com.eunsun.storereservation.util.ReservationUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@AllArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;

    /*
    방문 확인 :  키오스크로 확인하는 것이므로 권한 설정을 따로 하지 않았습니다.
    1. 예약 ID와 고객 이름을 받아 예약 정보를 조회합니다.
    2. 예약자 이름과 입력된 고객 이름이 일치하는지 확인합니다.
    3. 예약 상태가 ACCEPTED인지 확인합니다.
    4. 현재 시간이 예약 시간 10분 전부터 20분 후 사이인지 확인합니다.
    5. 모든 조건을 만족하면 방문 확인 (visited)을 -> true 바꿔 준 후 반환합니다.
    */
    public VisitConfirmationDto confirmVisit(Long reservationId, String customerName) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("예약 정보를 찾을 수 없습니다. 예약 ID : " + reservationId));

        if (!reservation.getCustomer().getName().equals(customerName)) {
            throw new IllegalArgumentException("예약자 이름이 일치하지 않습니다. 확인 후 다시 시도해주세요.");
        }

        if (reservation.getStatus() != ReservationStatus.ACCEPTED) {
            throw new IllegalArgumentException("승인된 예약만 방문 확인이 가능합니다.");
        }

        LocalDateTime reservationDateTime = LocalDateTime.of(reservation.getReservationDate(), reservation.getReservationTime());
        LocalDateTime tenMinutesBeforeReservation = reservationDateTime.minusMinutes(10);
        LocalDateTime twentyMinutesAfterReservation = reservationDateTime.plusMinutes(20);
        LocalDateTime currentDateTime = LocalDateTime.now();

        if (currentDateTime.isBefore(tenMinutesBeforeReservation)) {
            throw new IllegalArgumentException("예약 시간 10분 전부터 방문 확인이 가능합니다.");
        }

        if (currentDateTime.isAfter(twentyMinutesAfterReservation)) {
            throw new IllegalArgumentException("예약 시간으로부터 20분이 지나 예약 확인이 불가능합니다.");
        }

        reservation.setVisited(true);
        reservationRepository.save(reservation);

        return VisitConfirmationDto.builder()
                .customerName(reservation.getCustomer().getName())
                .reservationDate(reservation.getReservationDate())
                .reservationTime(reservation.getReservationTime())
                .numberOfPeople(reservation.getNumberOfPeople())
                .build();
    }


    /*
    예약 상태 변경 : 매니저 권한을 가지고 본인 가게만 가능합니다.
    1. 예약 ID와 로그인한 매니저 ID, 변경할 예약 상태를 받습니다.
    2. 예약 정보를 조회하고, 해당 예약의 가게가 로그인한 매니저의 가게인지 확인합니다.
    3. 변경할 예약 상태가 ACCEPTED 또는 REJECTED 값만 작성 가능합니다.
    4. 변경된 예약 정보를 반환합니다.
    */
    public ReservationWithStoreDto updateReservationStatus(Long reservationId, Long loginManagerId, ReservationStatus status) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("예약 정보를 찾을 수 없습니다. 예약 ID : " + reservationId));

        Store reservationStore = reservation.getStore();
        if (!reservationStore.getManager().getId().equals(loginManagerId)) {
            throw new AccessDeniedException("다른 가게의 예약 상태를 변경할 수 없습니다.");
        }
        log.info("예약 상태 변경을 위해 값 체크");
        if (status == ReservationStatus.ACCEPTED || status == ReservationStatus.REJECTED) {
            reservation.setStatus(status);
            Reservation updatedReservation = reservationRepository.save(reservation);

            return ReservationUtils.convertToReservationWithStoreDto(updatedReservation);

        } else {
            throw new IllegalArgumentException("유효하지 않은 예약 상태입니다. 다시 확인해주세요.");
        }
    }

    /*
    매니저용 예약 조회
    1. 예약 ID와 로그인한 매니저 ID를 받습니다.
    2. 예약 정보를 조회하고, 해당 예약의 가게가 로그인한 매니저의 가게인지 확인합니다.
    3. 예약 정보를 반환합니다.
    */
    public ReservationWithStoreDto getReservationDetailForManager(Long reservationId, Long loginManagerId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("예약 정보를 찾을 수 없습니다. 예약 ID: " + reservationId));

        Store reservationStore = reservation.getStore();
        if (!reservationStore.getManager().getId().equals(loginManagerId)) {
            throw new AccessDeniedException("다른 가게의 예약 정보에 접근할 수 없습니다.");
        }

        return ReservationUtils.convertToReservationWithStoreDto(reservation);
    }

    /*
    고객용 예약 조회
    1. 예약 ID와 로그인한 고객 ID를 받습니다.
    2. 예약 정보를 조회하고, 해당 예약의 고객이 로그인한 고객인지 확인합니다.
    3. 예약 정보를 반환합니다.
    */
    public ReservationWithStoreDto getReservationDetail(Long reservationId, Long loginCustomerId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("예약 정보를 찾을 수 없습니다. 예약 ID : " + reservationId));

        Customer reservationCustomer = reservation.getCustomer();
        if (!reservationCustomer.getId().equals(loginCustomerId)) {
            throw new AccessDeniedException("다른 사용자의 예약정보입니다.");
        }

        return ReservationUtils.convertToReservationWithStoreDto(reservation);
    }

    /*
    예약 생성 : 반드시 로그인 된 고객 권한이 필요합니다.
    1. 권한을 체크하고 필요한 정보들을 가져옵니다.
    3. 예약 정보를 생성하고 저장합니다.
    4. 생성된 예약 정보를 반환합니다.
    */
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
                .status(ReservationStatus.PENDING)
                .build();

        reservationRepository.save(reservation);
        return ReservationUtils.convertToReservationDto(reservation);
    }

}
