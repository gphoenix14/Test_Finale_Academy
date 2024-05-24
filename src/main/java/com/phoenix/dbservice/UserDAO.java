package com.phoenix.dbservice;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.phoenix.model.User;

public class UserDAO {

    public void createUser(User user) throws SQLException {
        String sql = "INSERT INTO User (username, password, email, balance) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.username);
            stmt.setString(2, user.password);
            stmt.setString(3, user.email);
            stmt.setDouble(4, user.balance);
            stmt.executeUpdate();
        }
    }

    public User getUserByUsernameAndPassword(String username, String password) throws SQLException {
        String sql = "SELECT * FROM User WHERE username = ? AND password = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.userId = rs.getInt("user_id");
                user.username = rs.getString("username");
                user.password = rs.getString("password");
                user.email = rs.getString("email");
                user.balance = rs.getDouble("balance");
                return user;
            }
        }
        return null;
    }

    public User getUser(int userId) throws SQLException {
        String sql = "SELECT * FROM User WHERE user_id = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.userId = rs.getInt("user_id");
                user.username = rs.getString("username");
                user.password = rs.getString("password");
                user.email = rs.getString("email");
                user.balance = rs.getDouble("balance");
                return user;
            }
        }
        return null;
    }

    public List<User> listUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM User";
        try (Connection conn = Database.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                User user = new User();
                user.userId = rs.getInt("user_id");
                user.username = rs.getString("username");
                user.password = rs.getString("password");
                user.email = rs.getString("email");
                user.balance = rs.getDouble("balance");
                users.add(user);
            }
        }
        return users;
    }

    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE User SET username = ?, password = ?, email = ?, balance = ? WHERE user_id = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.username);
            stmt.setString(2, user.password);
            stmt.setString(3, user.email);
            stmt.setDouble(4, user.balance);
            stmt.setInt(5, user.userId);
            stmt.executeUpdate();
        }
    }

    public void deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM User WHERE user_id = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

    public void deposit(int userId, double amount) throws SQLException {
        String sql = "UPDATE User SET balance = balance + ? WHERE user_id = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, amount);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }

    public boolean isUsernameUnique(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM User WHERE username = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        }
        return false;
    }
}
