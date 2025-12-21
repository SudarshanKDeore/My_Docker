package com.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class BadCodeExample {

    // 🔐 SECURITY VULNERABILITY: Hardcoded secret
    private static final String DB_PASSWORD = "admin123";

    public static void main(String[] args) {

        // 🐞 BUG: NullPointerException (runtime bug)
        String data = null;
        System.out.println(data.length());

        // 🔐 SECURITY VULNERABILITY: SQL Injection
        insecureSql("admin' OR '1'='1");

        // 🧹 CODE SMELL: Useless assignment
        int unused = 100;
    }

    // 🔐 SECURITY VULNERABILITY: SQL Injection
    public static void insecureSql(String userInput) {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/test",
                    "root",
                    DB_PASSWORD
            );

            Statement stmt = conn.createStatement();
            stmt.execute(
                "SELECT * FROM users WHERE username = '" + userInput + "'"
            );

        } catch (Exception e) {
            // 🧹 CODE SMELL: printStackTrace
            e.printStackTrace();
        }
    }
}
