package org.example.controller;

import org.example.domain.User;
import org.example.enums.UserRole;
import org.example.repisitory.GeneralRepository;
import org.example.service.UserService;

import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

import static org.example.service.UserService.getUsers;

public class UserAuthenticationController {
    private static final UserService userService = new UserService();

    public static User SignIn(Scanner scan) {
        System.out.println("Enter Username: ");
        String username = scan.next().trim();
        User user = null;

        if (!userService.loadUserByUsername(username).isEmpty()) {
            user = userService.loadUserByUsername(username).getFirst();
        }

        if (user == null) {
            System.out.println("User does not exist");
            return null;
        }

        System.out.println("Enter password: ");

        if (UserService.passwordAttemptChecker(scan, user)) {
            System.out.println("Incorrect password. Signing in failed.");
            return null;
        } else {
            System.out.println("Welcome back, " + username + "!");
            System.out.println("You are now signed in.");

            System.out.println("-------------- HERE'S WHAT YOU CAN DO:-------------- ");
            return user;
        }
    }

    public static User SignUp(Scanner scan) {
        if (!userService.loadAllUsers()) {
            System.out.println("Something went wrong! Please try again later.");
            getUsers().clear();
            return null;
        }

        Long id = GeneralRepository.generateID();
        Set<Long> keySet = getUsers().keySet();
        while (keySet.contains(id)) {
            id = GeneralRepository.generateID();
        }

        System.out.println("-------------- REGISTER NOW TO BECOME OUR MEMBER --------------");
        System.out.println("Welcome to our library community!");
        System.out.println("Enter a username: ");
        String username = scan.next().trim();

        if (!userService.loadUserByUsername(username).isEmpty()) {
            System.out.println("Sorry, that username is already taken. Please choose another one.");
            getUsers().clear();
            return null;
        }

        System.out.println("Enter a password: ");
        String password = scan.next().trim();
        User user = new User(id, username, password, null, null, null, UserRole.MEMBER, 100.0);

        if (!userService.saveUser(user)) {
            System.out.println("Oops! Something unexpected happened, and we couldn't register you. Please try again.");
            getUsers().clear();
            return null;
        }

        getUsers().clear();
        System.out.println("Congratulations, " + user.getUsername() + "! You have successfully signed up!");
        System.out.println("You are now part of our library community!");

        System.out.println("-------------- HERE'S WHAT YOU CAN DO:-------------- ");


        return user;
    }


}

