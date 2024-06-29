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

    public static User SignIn(Scanner scan) throws IOException {
        System.out.println("Enter Username: ");
        String username = scan.next().trim();
        User user = userService.loadUserByUsername(username).getFirst();
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

    public static User SignUp(Scanner scan) throws IOException {
        if (!userService.loadAllUsers()) {
            System.out.println("Something went wrong! Try later!");
            getUsers().clear();
            return null;
        }
        Long id = GeneralRepository.generateID();
        Set<Long> keySet = getUsers().keySet();
        while (keySet.contains(id)) {
            id = GeneralRepository.generateID();
        }
        System.out.println("Enter Username: ");
        String username = scan.next().trim();

        if (userService.loadUserByUsername(username) != null) {
            System.out.println("Username is taken!");
            getUsers().clear();
            return null;
        }
        System.out.println("Enter Password: ");
        String password = scan.next().trim();
        User user = new User(id, username, password, null, null, null, UserRole.MEMBER);
        if (!userService.saveUser(user)) {
            System.out.println("Something unexpected happened! User was not registered!");
            getUsers().clear();
            return null;
        }
        getUsers().clear();
        System.out.println("Signing up is successful! Complete your profile by adding your email, phone number and address!");
        return user;
    }


}

