package com.floodhousing.ui;

import com.floodhousing.dao.UserDao;
import com.floodhousing.model.Role;
import com.floodhousing.model.Status;
import com.floodhousing.model.User;

import java.util.Optional;
import java.util.Scanner;

/** Entry menu: Register, Login, Exit. */
public class MainMenu {

    private final UserDao userDao;
    private final Scanner scanner;

    public MainMenu(UserDao userDao, Scanner scanner) {
        this.userDao = userDao;
        this.scanner = scanner;
    }

    public void show() {
        boolean running = true;
        while (running) {
            System.out.println("\n=====================================");
            System.out.println("   MODULAR FLOOD HOUSING SYSTEM");
            System.out.println("=====================================");
            System.out.println("[1] Register Account");
            System.out.println("[2] Login");
            System.out.println("[3] Exit");
            System.out.print("Choose: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> register();
                case "2" -> login();
                case "3" -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }

    private void register() {
        System.out.println("\n--- REGISTER ACCOUNT ---");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Username and password are required.");
            return;
        }
        if (userDao.usernameExists(username)) {
            System.out.println("Username already taken.");
            return;
        }

        System.out.println("Select role:");
        System.out.println("[1] Barangay Staff");
        System.out.println("[2] Resident");
        System.out.print("Choose: ");
        Role role;
        switch (scanner.nextLine().trim()) {
            case "1" -> role = Role.STAFF;
            case "2" -> role = Role.RESIDENT;
            default -> {
                System.out.println("Invalid role. Registration cancelled.");
                return;
            }
        }

        if (userDao.register(username, password, role)) {
            System.out.println("Account created as " + role.label() + " with status "
                    + Status.PENDING + ". Please wait for approval.");
        } else {
            System.out.println("Registration failed.");
        }
    }

    private void login() {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Optional<User> result = userDao.login(username, password);
        if (result.isEmpty()) {
            System.out.println("Invalid username or password.");
            return;
        }

        User user = result.get();
        if (user.status() == Status.PENDING) {
            System.out.println("Your account is still PENDING approval. Please try again later.");
            return;
        }

        new UserMenu(user, scanner).show();
    }
}
