package com.eunsun.storereservation.exception;

public class ReservationNotFoundException extends RuntimeException{
    public ReservationNotFoundException(String message) {
        super(message);
    }
}
