package com.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class FailGate {

    private static final String SECRET = "password123";

    public static void main(String[] args) {
        String s = null;
        System.out.println(s.length());
    }
}

public class BadCodeExample {

    // 🔐 Security Vulnerability (Hardcoded credentials)
    private static final String DB_PASSWORD = "admin123";

    public static void main(String[] args) {

        BadCodeExample obj = new BadCodeExample();
        obj.calculate(10, 0);  // 🐞 Bug: division by zero

        obj.sqlInjection("admin' OR '1'='1"); // 🔐 Vulnerability
        obj.unusedMethod(); // 🧹 Code smell
    }

    // 🐞 Bug: No zero check
    public int calculate(int a, int b) {
        return a / b;
    }

    // 🔐 Security Vulnerability: SQL Injection
    public void sqlInjection(String userInput) {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/test",
                    "root",
                    DB_PASSWORD
            );

            Statement stmt = conn.createStatement();
            stmt.execute("SELECT * FROM users WHERE name = '" + userInput + "'");
        } catch (Exception e) {
            e.printStackTrace(); // 🧹 Code smell
        }
    }

    // 🧹 Code smell: unused method
    private void unusedMethod() {
        int x = 10;
    }
}
