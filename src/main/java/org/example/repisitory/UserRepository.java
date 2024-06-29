package org.example.repisitory;

import org.example.domain.User;
import org.example.enums.UserRole;

import java.io.IOException;
import java.util.List;

public interface UserRepository {


     void removeUserById(Long id);
     boolean saveUser(User user);

     boolean loadAllUsers() throws IOException;

     List<User> loadUserByUsername(String username) throws IOException;

     List<User> loadUserByUserId(Long id);
     List<User> loadUserByUserAddress(String address);
     List<User> loadUserByUserRole(UserRole role);
}
