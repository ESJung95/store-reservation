package com.eunsun.storereservation.enums;

public enum ReservationStatus {
    PENDING,
    ACCEPTED,
    REJECTED,
    CANCELED
}

// PENDING : 예약이 요청되었지만 아직 매장에서 수락하지 않은 상태
// ACCEPTED : 매장에서 예약을 수락한 상태
// REJECTED : 매장에서 예약을 거절한 상태
// CANCELED : 고객이 예약을 취소한 상태