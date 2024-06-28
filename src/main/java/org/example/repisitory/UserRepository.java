package org.example.repisitory;

import org.example.domain.User;

import java.io.IOException;

public interface UserRepository {

     boolean takeUserRecord(User user);

     boolean loadAllUsers() throws IOException;

     User loadUserByUsername(String username) throws IOException;

     User loadUserByUserId(Long id);
}
