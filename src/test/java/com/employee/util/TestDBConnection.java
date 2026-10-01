package com.employee.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/employee_test_db";

    private static final String USER = "root";

    private static final String PASSWORD = "2305";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}