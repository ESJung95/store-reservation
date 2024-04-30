package com.eunsun.storereservation.exception;

public class EmailDuplicateException extends RuntimeException{

    private static final String MESSAGE = "이미 존재하는 이메일입니다.";

    public EmailDuplicateException() {
        super(MESSAGE);
    }
}
