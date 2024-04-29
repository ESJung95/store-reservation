package com.eunsun.storereservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RestController
public class DBConnectionController {
    @Autowired
    private DataSource dataSource;

    @GetMapping("/db-test")
    public String testDBConnection() {
        try (Connection connection = dataSource.getConnection()) {
            return "DB 연결 성공";
        } catch (SQLException e) {
            return "DB 연결 실패: " + e.getMessage();
        }
    }
}
