package org.example.service;

import org.example.controller.UserAuthenticationController;
import org.example.domain.User;
import org.example.enums.UserRole;
import org.example.repisitory.UserRepository;

import java.io.*;

public class UserService implements UserRepository {

    //READY
    @Override
    public boolean takeUserRecord(User user){
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
    public boolean loadAllUsers() throws IOException {
        File file = new File("user_database.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String details;
            while ((details = br.readLine()) != null) {
                String[] userDetails = details.split(",");
                if (userDetails[0].isEmpty()) break;
                User user = new User(Long.parseLong(userDetails[0]), userDetails[1], userDetails[2], userDetails[3], userDetails[4], userDetails[5], UserRole.valueOf(userDetails[6]));
                user.setHashedPassword(userDetails[2]);
                UserAuthenticationController.getUsers().put(Long.parseLong(userDetails[0]), user);
            }
            return true;
        } catch (IOException io) {
            return false;
        }
    }

    //READY
    @Override
    public  User loadUserByUsername(String username) throws IOException {
       return LoadUser("username", username);
    }
    @Override
    public User loadUserByUserId(Long id) {
       return LoadUser("id", id);
    }

    private <T> User LoadUser(String load, T loadBy) {
        File file = new File("user_database.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String details;
            while ((details = br.readLine()) != null) {
                String[] userDetails = details.split(",");
                if (userDetails[0].isEmpty()) break;
                switch (load) {
                    case "username" -> {
                        if (userDetails[1].equals(String.valueOf(loadBy))) {
                            User user = new User(Long.parseLong(userDetails[0]), userDetails[1], userDetails[2], userDetails[3], userDetails[4], userDetails[5], UserRole.valueOf(userDetails[6]));
                            user.setHashedPassword(userDetails[2]);
                            return user;
                        }
                    }
                    case "id" -> {
                        if (userDetails[0].equals(loadBy)) {
                            User user = new User(Long.parseLong(userDetails[0]), userDetails[1], userDetails[2], userDetails[3], userDetails[4], userDetails[5], UserRole.valueOf(userDetails[6]));
                            user.setHashedPassword(userDetails[2]);
                            return user;
                        }
                    }
                }
            }
        }catch (IOException io){
            return null;
        }
        return null;
    }

}
