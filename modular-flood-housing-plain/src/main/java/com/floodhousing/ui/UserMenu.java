package com.floodhousing.ui;

import com.floodhousing.model.Role;
import com.floodhousing.model.User;

import java.util.Scanner;

/** Screen shown after a successful login. Lists role-based access from the proposal and handles logout. */
public class UserMenu {

    private final User user;
    private final Scanner scanner;

    public UserMenu(User user, Scanner scanner) {
        this.user = user;
        this.scanner = scanner;
    }

    public void show() {
        System.out.println("\nLogin successful! Welcome, " + user.username()
                + " (" + user.role().label() + ")");

        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n--- " + user.role().label().toUpperCase() + " MENU ---");
            System.out.println("Your access (coming in the next phase):");
            for (String feature : featuresFor(user.role())) {
                System.out.println("  - " + feature);
            }
            System.out.println("[0] Logout");
            System.out.print("Choose: ");

            if (scanner.nextLine().trim().equals("0")) {
                System.out.println("Logged out. Returning to main menu...");
                loggedIn = false;
            } else {
                System.out.println("That feature is not available yet.");
            }
        }
    }

    private String[] featuresFor(Role role) {
        return switch (role) {
            case ADMIN -> new String[]{"Add house records", "View all house records", "Search house records",
                    "Update house status", "Update livelihood information", "Delete house records"};
            case STAFF -> new String[]{"Add house records", "View all house records", "Search house records",
                    "Update house status", "Update livelihood information"};
            case RESIDENT -> new String[]{"View all house records", "Search house records",
                    "Update livelihood information"};
        };
    }
}
