package org.example.repisitory;

import org.example.domain.User;

import java.io.IOException;

public interface UserRepository {


     void removeUserById(Long id);
     boolean saveUser(User user);

     boolean loadAllUsers() throws IOException;

     User loadUserByUsername(String username) throws IOException;

     User loadUserByUserId(Long id);
}
