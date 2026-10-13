package com.floodhousing.dao;

import com.floodhousing.model.Role;
import com.floodhousing.model.Status;
import com.floodhousing.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/** All SQL queries for the users table. */
public class UserDao {

    /** Saves a new account with status PENDING. Returns false if the username is taken. */
    public boolean register(String username, String rawPassword, Role role) {
        if (usernameExists(username)) {
            return false;
        }
        String sql = "INSERT INTO users (username, password, roles, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, hash(rawPassword));
            ps.setString(3, role.name());
            ps.setString(4, Status.PENDING.name());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new IllegalStateException("Registration failed: " + e.getMessage(), e);
        }
    }

    /** Verifies username and password. Returns the user if the credentials match. */
    public Optional<User> login(String username, String rawPassword) {
        String sql = "SELECT id, username, password, roles, status FROM users "
                   + "WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, hash(rawPassword));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            Role.valueOf(rs.getString("roles")),
                            Status.valueOf(rs.getString("status"))));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Login failed: " + e.getMessage(), e);
        }
    }

    public boolean usernameExists(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Query failed: " + e.getMessage(), e);
        }
    }

    /** Creates a default ACTIVE admin (admin / admin123) so the system can be tested right away. */
    public void seedDefaultAdmin() {
        if (usernameExists("admin")) {
            return;
        }
        String sql = "INSERT INTO users (username, password, roles, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "admin");
            ps.setString(2, hash("admin123"));
            ps.setString(3, Role.ADMIN.name());
            ps.setString(4, Status.ACTIVE.name());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Could not seed admin: " + e.getMessage(), e);
        }
    }

    /** Passwords are stored as plain text so they are visible in the database. */
    private static String hash(String raw) {
        return raw;
    }
}
