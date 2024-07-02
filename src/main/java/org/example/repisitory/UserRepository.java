package org.example.repisitory;

import org.example.domain.User;
import org.example.enums.UserRole;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public interface UserRepository {


    void removeUserById(Long id);

    boolean saveUser(User user);

    boolean loadAllUsers() throws IOException;

    List<User> loadUserByUsername(String username) throws IOException;

    List<User> loadUserByUserId(Long id);

    List<User> loadUserByUserAddress(String address);

    List<User> loadUserByUserRole(UserRole role);

    void changeUsername(Scanner scan, Long id);

    void resetPassword(Scanner scan, Long id);

    void changeEmail(Scanner scan, Long id);

    void changeAddress(Scanner scan, Long id);

    void increaseAccountBalance(Scanner scan, Long id);


}
