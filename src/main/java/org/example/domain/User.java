package org.example.domain;

import org.example.enums.UserRole;
import org.example.exceptions.WrongEmailException;
import org.example.exceptions.WrongPasswordException;
import org.example.exceptions.WrongPhoneNumberException;
import org.example.exceptions.WrongUsernameException;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Objects;

public class User {
    private Long id;
    private String username;

    private String hashedPassword;

    private String address;

    private String phone;

    private String email;

    private UserRole role;


    private static final int MIN_PASSWORD_LENGTH = 7;
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";
    private static final String PHONE_REGEX = "^\\+994(50|51|55|70|77|10)(\\d{7})$";

    public User(Long id, String username, String password, String address, String phone, String email, UserRole role) {
        verifyUserDetails(id, username, password, address, phone, email, role);
        this.id = id;
        this.username = username;
        this.hashedPassword = hashPassword(password);
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.role = role;
    }


    private String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public boolean verifyPassword(String password) {
        return BCrypt.checkpw(password, this.hashedPassword);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }


    public String getPassword() {
        return hashedPassword;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (!phone.matches(PHONE_REGEX))
            throw new WrongPhoneNumberException("Phone number format is not correct! (it should be +994 (50/70/77/51/55/10) + 7 digit long)");
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email.matches(EMAIL_REGEX)) throw new WrongEmailException("Email format is not correct!");
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public String toString() {
        return id + "," + username + "," + hashedPassword + "," + address + "," + phone + "," + email + "," + role;
    }

    public static void verifyUserDetails(Long id, String username, String password, String address, String phone, String email, UserRole role) {
        if (username == null || username.trim().length() <= 1)
            throw new WrongUsernameException("Username must be provided and have at least 2 characters.");
        if (password == null || password.length() < MIN_PASSWORD_LENGTH)
            throw new WrongPasswordException("Password must be at least " + MIN_PASSWORD_LENGTH + " characters long.");
    }
}
