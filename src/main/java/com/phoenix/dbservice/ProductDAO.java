package com.phoenix.dbservice;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.phoenix.model.Product;

public class ProductDAO {

    public void createProduct(Product product) throws SQLException {
        String sql = "INSERT INTO Product (user_id, name, description, price, stock) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, product.userId);
            stmt.setString(2, product.name);
            stmt.setString(3, product.description);
            stmt.setDouble(4, product.price);
            stmt.setInt(5, product.stock);
            stmt.executeUpdate();
        }
    }

    public Product getProduct(int productId) throws SQLException {
        String sql = "SELECT * FROM Product WHERE product_id = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Product product = new Product();
                product.productId = rs.getInt("product_id");
                product.userId = rs.getInt("user_id");
                product.name = rs.getString("name");
                product.description = rs.getString("description");
                product.price = rs.getDouble("price");
                product.stock = rs.getInt("stock");
                return product;
            }
        }
        return null;
    }

    public List<Product> listProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM Product";
        try (Connection conn = Database.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Product product = new Product();
                product.productId = rs.getInt("product_id");
                product.userId = rs.getInt("user_id");
                product.name = rs.getString("name");
                product.description = rs.getString("description");
                product.price = rs.getDouble("price");
                product.stock = rs.getInt("stock");
                products.add(product);
            }
        }
        return products;
    }

    public void updateProduct(Product product) throws SQLException {
        String sql = "UPDATE Product SET user_id = ?, name = ?, description = ?, price = ?, stock = ? WHERE product_id = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, product.userId);
            stmt.setString(2, product.name);
            stmt.setString(3, product.description);
            stmt.setDouble(4, product.price);
            stmt.setInt(5, product.stock);
            stmt.setInt(6, product.productId);
            stmt.executeUpdate();
        }
    }

    public void deleteProduct(int productId) throws SQLException {
        String sql = "DELETE FROM Product WHERE product_id = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            stmt.executeUpdate();
        }
    }
}
