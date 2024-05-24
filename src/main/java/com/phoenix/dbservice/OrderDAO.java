package com.phoenix.dbservice;

import java.sql.*;
import java.util.List;

public class OrderDAO {

    public void createOrder(int userId, List<Integer> productIds) throws SQLException {
        String orderSql = "INSERT INTO `Order` (user_id, total_amount, status) VALUES (?, ?, 'In elaborazione')";
        String productSql = "SELECT price, user_id FROM Product WHERE product_id = ?";
        String orderProductSql = "INSERT INTO OrderProduct (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        String updateUserBalanceSql = "UPDATE User SET balance = balance - ? WHERE user_id = ?";
        String updateSellerBalanceSql = "UPDATE User SET balance = balance + ? WHERE user_id = ?";

        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement orderStmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);
                    PreparedStatement productStmt = conn.prepareStatement(productSql);
                    PreparedStatement orderProductStmt = conn.prepareStatement(orderProductSql);
                    PreparedStatement updateUserBalanceStmt = conn.prepareStatement(updateUserBalanceSql);
                    PreparedStatement updateSellerBalanceStmt = conn.prepareStatement(updateSellerBalanceSql)) {

                // Create order
                double totalAmount = 0;
                for (int productId : productIds) {
                    productStmt.setInt(1, productId);
                    try (ResultSet rs = productStmt.executeQuery()) {
                        if (rs.next()) {
                            totalAmount += rs.getDouble("price");
                        }
                    }
                }

                orderStmt.setInt(1, userId);
                orderStmt.setDouble(2, totalAmount);
                orderStmt.executeUpdate();

                // Get generated order ID
                int orderId;
                try (ResultSet rs = orderStmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        orderId = rs.getInt(1);
                    } else {
                        conn.rollback();
                        throw new SQLException("Failed to create order, no ID obtained.");
                    }
                }

                // Insert products into OrderProduct
                for (int productId : productIds) {
                    productStmt.setInt(1, productId);
                    try (ResultSet rs = productStmt.executeQuery()) {
                        if (rs.next()) {
                            double price = rs.getDouble("price");
                            int sellerId = rs.getInt("user_id");
                            orderProductStmt.setInt(1, orderId);
                            orderProductStmt.setInt(2, productId);
                            orderProductStmt.setInt(3, 1); // quantity is always 1 in this example
                            orderProductStmt.setDouble(4, price);
                            orderProductStmt.executeUpdate();

                            // Update seller's balance
                            updateSellerBalanceStmt.setDouble(1, price * 0.80);
                            updateSellerBalanceStmt.setInt(2, sellerId);
                            updateSellerBalanceStmt.executeUpdate();
                        }
                    }
                }

                // Update user's balance
                updateUserBalanceStmt.setDouble(1, totalAmount);
                updateUserBalanceStmt.setInt(2, userId);
                updateUserBalanceStmt.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}
