package com.floodhousing;

import com.floodhousing.dao.DatabaseConnection;
import com.floodhousing.dao.UserDao;
import com.floodhousing.ui.MainMenu;

import java.util.Scanner;

/** Application entry point. */
public class Main {

    public static void main(String[] args) {
        DatabaseConnection.initialize();

        UserDao userDao = new UserDao();
        userDao.seedDefaultAdmin();

        try (Scanner scanner = new Scanner(System.in)) {
            new MainMenu(userDao, scanner).show();
        }
    }
}
