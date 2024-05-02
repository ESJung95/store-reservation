package com.eunsun.storereservation.exception;

public class ManagerNotFoundException extends RuntimeException{
    public ManagerNotFoundException(String message) {
        super(message);
    }
}
