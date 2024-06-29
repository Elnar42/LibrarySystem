package org.example.service;

import org.example.domain.User;
import org.example.enums.UserRole;
import org.example.repisitory.UserRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
                User user = new User(Long.parseLong(userDetails[0]), userDetails[1], userDetails[2], userDetails[3], userDetails[4], userDetails[5], UserRole.valueOf(userDetails[6]));
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
                User user = new User(Long.parseLong(userDetails[0]), userDetails[1], userDetails[2], userDetails[3], userDetails[4], userDetails[5], UserRole.valueOf(userDetails[6]));
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
                            User user = new User(Long.parseLong(userDetails[0]), userDetails[1], userDetails[2], userDetails[3], userDetails[4], userDetails[5], UserRole.valueOf(userDetails[6]));
                            user.setHashedPassword(userDetails[2]);
                            userList.add(user);
                        }
                    }
                    case "id" -> {
                        Long id = Long.parseLong(userDetails[0]);
                        if (id.equals(loadBy)) {
                            User user = new User(Long.parseLong(userDetails[0]), userDetails[1], userDetails[2], userDetails[3], userDetails[4], userDetails[5], UserRole.valueOf(userDetails[6]));
                            user.setHashedPassword(userDetails[2]);
                            userList.add(user);
                        }
                    }
                    case "address" -> {
                        String address = userDetails[3];
                        if (address.contains(loadBy.toString())) {
                            User user = new User(Long.parseLong(userDetails[0]), userDetails[1], userDetails[2], userDetails[3], userDetails[4], userDetails[5], UserRole.valueOf(userDetails[6]));
                            user.setHashedPassword(userDetails[2]);
                            userList.add(user);
                        }
                    }

                    case "role" -> {
                        UserRole role = UserRole.valueOf(userDetails[6]);
                        if (role.equals(loadBy)) {
                            User user = new User(Long.parseLong(userDetails[0]), userDetails[1], userDetails[2], userDetails[3], userDetails[4], userDetails[5], UserRole.valueOf(userDetails[6]));
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


    public static HashMap<Long, User> getUsers() {
        return users;
    }

}
