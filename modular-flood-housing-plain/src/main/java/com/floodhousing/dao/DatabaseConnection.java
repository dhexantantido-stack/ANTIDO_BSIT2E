package com.floodhousing.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/** Handles the SQLite connection and creates the database schema. */
public final class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:flood_housing.db";

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    /** Creates the users table if it does not exist yet. */
    public static void initialize() {
        String sql = """
                CREATE TABLE IF NOT EXISTS users (
                    id       INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL,
                    roles    TEXT NOT NULL CHECK (roles IN ('ADMIN', 'STAFF', 'RESIDENT')),
                    status   TEXT NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'ACTIVE'))
                )
                """;
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new IllegalStateException("Could not initialize database: " + e.getMessage(), e);
        }
    }
}
