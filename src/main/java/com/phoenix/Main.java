package com.phoenix;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.phoenix.dbservice.OrderDAO;
import com.phoenix.dbservice.ProductDAO;
import com.phoenix.dbservice.UserDAO;
import com.phoenix.model.Product;
import com.phoenix.model.User;

public class Main {
    private static UserDAO userDAO = new UserDAO();
    private static ProductDAO productDAO = new ProductDAO();
    private static OrderDAO orderDAO = new OrderDAO();
    private static User loggedInUser = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (loggedInUser == null) {
                System.out.println("1) Login");
                System.out.println("2) Register");
                System.out.println("0) Exit");

                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                try {
                    if (choice == 1) {
                        login(scanner);
                    } else if (choice == 2) {
                        register(scanner);
                    } else if (choice == 0) {
                        break;
                    } else {
                        System.out.println("Invalid choice. Please try again.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                showMenu(scanner);
            }
        }
        scanner.close();
    }

    private static void login(Scanner scanner) throws SQLException {
        System.out.println("Enter username:");
        String username = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();

        loggedInUser = userDAO.getUserByUsernameAndPassword(username, password);
        if (loggedInUser != null) {
            System.out.println("Login successful.");
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }

    private static void register(Scanner scanner) throws SQLException {
        System.out.println("Enter username:");
        String username = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();
        System.out.println("Enter email:");
        String email = scanner.nextLine();
        System.out.println("Enter initial balance:");
        double balance = scanner.nextDouble();
        scanner.nextLine(); // consume newline

        if (!userDAO.isUsernameUnique(username)) {
            System.out.println("Username already exists. Please try a different username.");
            return;
        }

        User user = new User();
        user.username = username;
        user.password = password;
        user.email = email;
        user.balance = balance;

        userDAO.createUser(user);
        System.out.println("Registration successful. Please login to continue.");
    }

    private static void showMenu(Scanner scanner) {
        System.out.println("Choose an option:");
        System.out.println("1) Create User");
        System.out.println("2) Read User");
        System.out.println("3) Update User");
        System.out.println("4) Delete User");
        System.out.println("5) Create Product");
        System.out.println("6) Read Product");
        System.out.println("7) Update Product");
        System.out.println("8) Delete Product");
        System.out.println("9) Purchase Product");
        System.out.println("10) Deposit");
        System.out.println("11) List Users");
        System.out.println("12) List Products");
        System.out.println("0) Logout");

        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        try {
            switch (choice) {
                case 1:
                    createUser(scanner);
                    break;
                case 2:
                    readUser(scanner);
                    break;
                case 3:
                    updateUser(scanner);
                    break;
                case 4:
                    deleteUser(scanner);
                    break;
                case 5:
                    createProduct(scanner);
                    break;
                case 6:
                    readProduct(scanner);
                    break;
                case 7:
                    updateProduct(scanner);
                    break;
                case 8:
                    deleteProduct(scanner);
                    break;
                case 9:
                    purchaseProduct(scanner);
                    break;
                case 10:
                    deposit(scanner);
                    break;
                case 11:
                    listUsers();
                    break;
                case 12:
                    listProducts();
                    break;
                case 0:
                    loggedInUser = null;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createUser(Scanner scanner) throws SQLException {
        System.out.println("Enter username:");
        String username = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();
        System.out.println("Enter email:");
        String email = scanner.nextLine();
        System.out.println("Enter initial balance:");
        double balance = scanner.nextDouble();
        scanner.nextLine(); // consume newline

        if (!userDAO.isUsernameUnique(username)) {
            System.out.println("Username already exists. Please try a different username.");
            return;
        }

        User user = new User();
        user.username = username;
        user.password = password;
        user.email = email;
        user.balance = balance;

        userDAO.createUser(user);
        System.out.println("User created successfully.");
    }

    private static void readUser(Scanner scanner) throws SQLException {
        System.out.println("Enter user ID:");
        int userId = scanner.nextInt();
        scanner.nextLine(); // consume newline

        User user = userDAO.getUser(userId);
        if (user != null) {
            System.out.println("ID: " + user.userId);
            System.out.println("Username: " + user.username);
            System.out.println("Email: " + user.email);
            System.out.println("Balance: " + user.balance);
        } else {
            System.out.println("User not found.");
        }
    }

    private static void updateUser(Scanner scanner) throws SQLException {
        System.out.println("Enter user ID:");
        int userId = scanner.nextInt();
        scanner.nextLine(); // consume newline

        User user = userDAO.getUser(userId);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.println("Enter new username (current: " + user.username + "):");
        user.username = scanner.nextLine();
        System.out.println("Enter new password (current: " + user.password + "):");
        user.password = scanner.nextLine();
        System.out.println("Enter new email (current: " + user.email + "):");
        user.email = scanner.nextLine();
        System.out.println("Enter new balance (current: " + user.balance + "):");
        user.balance = scanner.nextDouble();
        scanner.nextLine(); // consume newline

        userDAO.updateUser(user);
        System.out.println("User updated successfully.");
    }

    private static void deleteUser(Scanner scanner) throws SQLException {
        System.out.println("Enter user ID:");
        int userId = scanner.nextInt();
        scanner.nextLine(); // consume newline

        userDAO.deleteUser(userId);
        System.out.println("User deleted successfully.");
    }

    private static void createProduct(Scanner scanner) throws SQLException {
        System.out.println("Enter product name:");
        String name = scanner.nextLine();
        System.out.println("Enter product description:");
        String description = scanner.nextLine();
        System.out.println("Enter product price:");
        double price = scanner.nextDouble();
        System.out.println("Enter product stock:");
        int stock = scanner.nextInt();
        scanner.nextLine(); // consume newline

        Product product = new Product();
        product.userId = loggedInUser.userId;
        product.name = name;
        product.description = description;
        product.price = price;
        product.stock = stock;

        productDAO.createProduct(product);
        System.out.println("Product created successfully.");
    }

    private static void readProduct(Scanner scanner) throws SQLException {
        System.out.println("Enter product ID:");
        int productId = scanner.nextInt();
        scanner.nextLine(); // consume newline

        Product product = productDAO.getProduct(productId);
        if (product != null) {
            System.out.println("ID: " + product.productId);
            System.out.println("Name: " + product.name);
            System.out.println("Description: " + product.description);
            System.out.println("Price: " + product.price);
            System.out.println("Stock: " + product.stock);
        } else {
            System.out.println("Product not found.");
        }
    }

    private static void updateProduct(Scanner scanner) throws SQLException {
        System.out.println("Enter product ID:");
        int productId = scanner.nextInt();
        scanner.nextLine(); // consume newline

        Product product = productDAO.getProduct(productId);
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        System.out.println("Enter new name (current: " + product.name + "):");
        product.name = scanner.nextLine();
        System.out.println("Enter new description (current: " + product.description + "):");
        product.description = scanner.nextLine();
        System.out.println("Enter new price (current: " + product.price + "):");
        product.price = scanner.nextDouble();
        System.out.println("Enter new stock (current: " + product.stock + "):");
        product.stock = scanner.nextInt();
        scanner.nextLine(); // consume newline

        productDAO.updateProduct(product);
        System.out.println("Product updated successfully.");
    }

    private static void deleteProduct(Scanner scanner) throws SQLException {
        System.out.println("Enter product ID:");
        int productId = scanner.nextInt();
        scanner.nextLine(); // consume newline

        productDAO.deleteProduct(productId);
        System.out.println("Product deleted successfully.");
    }

    private static void purchaseProduct(Scanner scanner) throws SQLException {
        System.out.println("Enter product IDs separated by commas:");
        String[] productIds = scanner.nextLine().split(",");

        List<Integer> productIdList = new ArrayList<>();
        for (String productId : productIds) {
            productIdList.add(Integer.parseInt(productId.trim()));
        }

        orderDAO.createOrder(loggedInUser.userId, productIdList);
        System.out.println("Order created successfully.");
    }

    private static void deposit(Scanner scanner) throws SQLException {
        System.out.println("Enter deposit amount:");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // consume newline

        userDAO.deposit(loggedInUser.userId, amount);
        System.out.println("Deposit successful.");
    }

    private static void listUsers() throws SQLException {
        List<User> users = userDAO.listUsers();
        for (User user : users) {
            System.out.println("ID: " + user.userId + ", Username: " + user.username + ", Email: " + user.email
                    + ", Balance: " + user.balance);
        }
    }

    private static void listProducts() throws SQLException {
        List<Product> products = productDAO.listProducts();
        for (Product product : products) {
            System.out.println("ID: " + product.productId + ", Name: " + product.name + ", Price: " + product.price
                    + ", Stock: " + product.stock);
        }
    }
}
