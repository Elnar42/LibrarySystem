package org.example.service;

import org.example.domain.User;
import org.example.enums.UserRole;
import org.example.repisitory.UserRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class UserService implements UserRepository {
    public static final HashMap<Long, User> users = new HashMap<>();

    //READY
    @Override
    public void removeUserById(Long id) {
        List<User> usersList = new ArrayList<>();
        File file = new File("user_database.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String details;
            while ((details = br.readLine()) != null) {
                String[] userDetails = details.split(",");
                if (userDetails[0].isEmpty()) break;
                User user = new User(Long.parseLong(userDetails[0]), userDetails[1], userDetails[2], userDetails[3], userDetails[4], userDetails[5], UserRole.valueOf(userDetails[6]), Double.parseDouble(userDetails[7]));
                if (user.getId().equals(id)) continue;
                usersList.add(user);
            }
            saveUsersWithoutAppend(usersList);
        } catch (IOException io) {
            System.out.println("Error in removing user!");
        }
    }

    //READY
    @Override
    public boolean saveUser(User user) {
        loadAllUsers();
        if (users.containsKey(user.getId()))
            throw new IllegalArgumentException("User with id (" + user.getId() + ") has already in library!");
        if (users.values().stream().anyMatch(x -> x.getUsername().equals(user.getUsername())))
            throw new IllegalArgumentException("Username (" + user.getUsername() + ") has been taken!");
        users.clear();
        String userTxt = user.toString();
        File file = new File("user_database.txt");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write(userTxt);
            bw.newLine();
            return true;
        } catch (IOException io) {
            return false;
        }
    }

    //READY
    @Override
    public boolean loadAllUsers() {
        File file = new File("user_database.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String details;
            while ((details = br.readLine()) != null) {
                String[] userDetails = details.split(",");
                if (userDetails[0].isEmpty()) break;
                User user = new User(Long.parseLong(userDetails[0]), userDetails[1], userDetails[2], userDetails[3], userDetails[4], userDetails[5], UserRole.valueOf(userDetails[6]), Double.parseDouble(userDetails[7]));
                user.setHashedPassword(userDetails[2]);
                users.put(Long.parseLong(userDetails[0]), user);
            }
            return true;
        } catch (IOException io) {
            return false;
        }
    }

    //READY
    @Override
    public List<User> loadUserByUsername(String username) {
        return LoadUser("username", username);
    }

    //READY
    @Override
    public List<User> loadUserByUserId(Long id) {
        return LoadUser("id", id);
    }


    @Override
    public List<User> loadUserByUserAddress(String address) {
        return LoadUser("address", address);
    }

    @Override
    public List<User> loadUserByUserRole(UserRole role) {
        return LoadUser("role", role);
    }

    @Override
    public void changeUsername(Scanner scan, Long id) {
        User user = loadUserByUserId(id).getFirst();
        System.out.println("Enter your password to continue the process: ");
        if (passwordAttemptChecker(scan, user)) {
            System.out.println("Unauthorized attempt to change username! Try later!");
        } else {
            System.out.println("Enter a new username: ");
            scan.nextLine();
            String newUsername = scan.nextLine();

            try {
                user.setUsername(newUsername);
                removeUserById(id);
                saveUser(user);
                System.out.println("Username successfully changed!");
            }catch (Exception exception){
                System.out.println("Error in updating email! Reason: " + exception.getCause().toString());
            }
        }
    }

    @Override
    public void resetPassword(Scanner scan, Long id) {
        User user = loadUserByUserId(id).getFirst();
        System.out.println("Enter an old password: ");
        if (passwordAttemptChecker(scan, user)) {
            System.out.println("Unauthorized attempt to change password! Try later!");
        } else {
            System.out.println("Enter a new password: ");
            scan.nextLine();
            String newPassword = scan.next();
            user.setHashedPassword(user.hashPassword(newPassword));
            removeUserById(id);
            saveUser(user);
            System.out.println("Password successfully set!");
        }
    }


    @Override
    public void changeEmail(Scanner scan, Long id) {
        User user = loadUserByUserId(id).getFirst();
        System.out.println("Enter your password to continue the process: ");
        if (passwordAttemptChecker(scan, user)) {
            System.out.println("Unauthorized attempt to change email! Try later!");
        } else {
            System.out.println("Enter an new email: ");
            scan.nextLine();
            String email = scan.nextLine();

            try {
                user.setEmail(email);
                removeUserById(id);
                saveUser(user);
                System.out.println("Email successfully changed!");
            }catch (Exception exception){
                System.out.println("Error in updating email! Reason: " + exception);
            }
        }
    }

    @Override
    public void changeAddress(Scanner scan, Long id) {
        User user = loadUserByUserId(id).getFirst();
        System.out.println("Enter your password to continue the process: ");
        if (passwordAttemptChecker(scan, user)) {
            System.out.println("Unauthorized attempt to change address! Try later!");
        } else {
            System.out.println("Enter an new address: ");
            scan.nextLine();
            String address = scan.nextLine();
            user.setAddress(address);
            removeUserById(id);
            saveUser(user);
            System.out.println("Address successfully changed!");
        }
    }

    @Override
    public void changePhoneNumber(Scanner scan, Long id) {
        User user = loadUserByUserId(id).getFirst();
        System.out.println("Enter your password to continue the process: ");
        if (passwordAttemptChecker(scan, user)) {
            System.out.println("Unauthorized attempt to change address! Try later!");
        } else {
            System.out.println("Enter an new phone number (+994 50/51/70/77/20 + 7 number, no space): ");
            scan.nextLine();
            String phone = scan.nextLine();
            try {
                user.setPhone(phone);
                removeUserById(id);
                saveUser(user);
                System.out.println("Phone number successfully set!");
            }catch (Exception exception){
                System.out.println("Error in updating email! Reason: " + exception);
            }
        }
    }

    @Override
    public void increaseAccountBalance(Scanner scan, Long id) {
        User user = loadUserByUserId(id).getFirst();
        System.out.println("Enter your password to continue the process: ");
        if (passwordAttemptChecker(scan, user)) {
            System.out.println("Unauthorized attempt to add deposit! Try later!");
        } else {
            System.out.println("Enter an amount you want to deposit to the account: ");
            Double deposit = scan.nextDouble();
            user.setBalance(user.getBalance() + deposit);
            removeUserById(id);
            saveUser(user);
            System.out.println("Successfully deposited money!");
        }
    }


    //READY
    private <T> List<User> LoadUser(String load, T loadBy) {
        File file = new File("user_database.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String details;
            List<User> userList = new ArrayList<>();
            while ((details = br.readLine()) != null) {
                String[] userDetails = details.split(",");
                switch (load) {
                    case "username" -> {
                        if (userDetails[1].equals(String.valueOf(loadBy))) {
                            User user = new User(Long.parseLong(userDetails[0]), userDetails[1], userDetails[2], userDetails[3], userDetails[4], userDetails[5], UserRole.valueOf(userDetails[6]), Double.parseDouble(userDetails[7]));
                            user.setHashedPassword(userDetails[2]);
                            userList.add(user);
                        }
                    }
                    case "id" -> {
                        Long id = Long.parseLong(userDetails[0]);
                        if (id.equals(loadBy)) {
                            User user = new User(Long.parseLong(userDetails[0]), userDetails[1], userDetails[2], userDetails[3], userDetails[4], userDetails[5], UserRole.valueOf(userDetails[6]), Double.parseDouble(userDetails[7]));
                            user.setHashedPassword(userDetails[2]);
                            userList.add(user);
                        }
                    }
                    case "address" -> {
                        String address = userDetails[3];
                        if (address.contains(loadBy.toString())) {
                            User user = new User(Long.parseLong(userDetails[0]), userDetails[1], userDetails[2], userDetails[3], userDetails[4], userDetails[5], UserRole.valueOf(userDetails[6]), Double.parseDouble(userDetails[7]));
                            user.setHashedPassword(userDetails[2]);
                            userList.add(user);
                        }
                    }

                    case "role" -> {
                        UserRole role = UserRole.valueOf(userDetails[6]);
                        if (role.equals(loadBy)) {
                            User user = new User(Long.parseLong(userDetails[0]), userDetails[1], userDetails[2], userDetails[3], userDetails[4], userDetails[5], UserRole.valueOf(userDetails[6]), Double.parseDouble(userDetails[7]));
                            user.setHashedPassword(userDetails[2]);
                            userList.add(user);
                        }
                    }
                }
            }
            return userList;
        } catch (IOException io) {
            return null;
        }
    }

    //READY
    private void saveUsersWithoutAppend(List<User> usersList) {
        File file = new File("user_database.txt");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
            for (User user : usersList) {
                bw.write(user.toString());
                bw.newLine();
            }
        } catch (IOException io) {
            System.out.println("Unable to save user!");
        }
    }

    public static boolean passwordAttemptChecker(Scanner scan, User user) {
        String password = scan.next().trim();
        int attemptCount = 3;
        boolean success = true;
        while (user.verifyPassword(password)) {
            attemptCount--;
            System.out.println("Wrong password!");
            if (attemptCount != 0) System.out.println("You have " + attemptCount + " chances left!");
            if (attemptCount == 0 && user.verifyPassword(password)) {
                success = false;
                break;
            }
            password = scan.next().trim();
        }
        return !success;
    }

    public static HashMap<Long, User> getUsers() {
        return users;
    }

}
