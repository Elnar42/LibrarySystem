package org.example.repisitory;

import org.example.domain.User;

import java.io.IOException;

public interface UserRepository {

     boolean takeUserRecord(User user);

     boolean loadUsers() throws IOException;

     User loadUserByUsername(String username) throws IOException;
}
