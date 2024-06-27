package org.example.domain;

import org.example.enums.UserRole;
import org.example.exceptions.WrongPasswordException;
import org.example.exceptions.WrongPhoneNumberException;
import org.example.exceptions.WrongUsernameException;

import java.util.Objects;

public class User {

    private final Long id;
    private String username;

    private String password;

    private String address;

    private String phone;

    private String email;

    private UserRole role;

    private static final int MIN_PASSWORD_LENGTH = 10;
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";
    private static final String PHONE_REGEX = "^\\+994(50|51|55|70|77|10)(\\d{7})$";

    public User(Long id, String username, String password, String address, String phone, String email, UserRole role) {
        if (username == null || username.trim().length() <= 1)  throw new WrongUsernameException("Username must be provided and have at least 2 characters.");
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) throw new WrongPasswordException("Password must be at least " + MIN_PASSWORD_LENGTH + " characters long.");
        if (email == null || !email.matches(EMAIL_REGEX)) throw new IllegalArgumentException("Invalid email address format.");
        if (phone == null || !phone.matches(PHONE_REGEX)) throw new WrongPhoneNumberException("Invalid phone number format.");
        if (address == null || address.trim().isEmpty()) throw new IllegalArgumentException("Address must be provided.");
        if (role == null) throw new IllegalArgumentException("User role must be provided.");
        if (id == null || id <= 0) throw new IllegalArgumentException("Invalid user ID.");

        this.id = id;
        this.username = username;
        this.password = password;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.role = role;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
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
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(address, user.address) && Objects.equals(phone, user.phone) && Objects.equals(email, user.email) && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, address, phone, email, role);
    }


}
