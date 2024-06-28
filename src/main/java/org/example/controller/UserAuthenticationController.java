package org.example.controller;

import org.example.domain.User;
import org.example.enums.UserRole;
import org.example.service.UserService;

import java.io.*;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class UserAuthenticationController {
    private static final HashMap<Long, User> users = new HashMap<>();
    private static final UserService userService = new UserService();

    public static User SignIn(Scanner scan) throws IOException {

        System.out.println("Enter Username: ");
        String username = scan.next().trim();

        User user = userService.loadUserByUsername(username);
        if (user == null) {
            System.out.println("User does not exist");
            return null;
        }
        System.out.println("Enter password: ");
        String password = scan.next().trim();

        int attemptCount = 3;
        boolean success = true;
        while (!user.verifyPassword(password)) {
            attemptCount--;
            System.out.println("Wrong password!");
            if (attemptCount != 0) System.out.println("You have " + attemptCount + " chances left!");
            if (attemptCount == 0 && !user.verifyPassword(password)) {
                success = false;
                break;
            }
            password = scan.next().trim();
        }

        if (!success) {
            System.out.println("Signing in is unsuccessful!");
            return null;
        } else {
            System.out.println("Welcome back, " + username + "!");

            return user;
        }

    }

    public static boolean SignUp(Scanner scan) throws IOException {
        if (!userService.loadUsers()) {
            System.out.println("Something went wrong! Try later!");
            users.clear();
            return false;
        }
        Long id = generateID();
        Set<Long> keySet = users.keySet();
        while (keySet.contains(id)) {
            id = generateID();
        }
        System.out.println("Enter Username: ");
        String username = scan.next().trim();

        if (userService.loadUserByUsername(username) != null) {
            System.out.println("Username is taken!");
            users.clear();
            return false;
        }
        System.out.println("Enter Password: ");
        String password = scan.next().trim();
        User user = new User(id, username, password, null, null, null, UserRole.MEMBER);
        if (!userService.takeUserRecord(user)) {
            System.out.println("Something unexpected happened! User was not registered!");
            users.clear();
            return false;
        }
         users.clear();
        System.out.println("Signing up is successful! Keep in mind to add your email, phone number and address in later stage!");
        return true;
    }


    public static Long generateID() {
        return new Random().nextLong(9000) + 1000;
    }

    public static HashMap<Long, User> getUsers() throws IOException {
        return users;
    }

}

