package com.eunsun.storereservation.dto;

import lombok.Data;

public class AuthDto {

    @Data
    public static class login {
        private String email;
        private String password;
    }

    @Data
    public static class signUp {

        private String email;
        private String password;
        private String name;
        private String phone;

    }
}
