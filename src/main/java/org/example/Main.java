package org.example;

import org.example.controller.UserAuthenticationController;
import org.example.domain.User;
import org.example.enums.UserRole;
import org.example.service.BookService;
import org.example.service.SearchAndSortService;
import org.example.service.TransactionService;
import org.example.service.UserService;

import java.util.Scanner;

public class Main {
    private final BookService bookService = new BookService();
    private final SearchAndSortService searchAndSortService =  new SearchAndSortService();
    private final TransactionService transactionService = new TransactionService();
    private final UserService userService = new UserService();

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        User user;
        do{
            user =authenticateUser(scan);
        }while (user == null);

        //Authentication completed!








    }

    private static User authenticateUser(Scanner scan) {
        User user = null;
        do {

            System.out.println("Do you have an account? (Y/N)");
            String answer = scan.nextLine().trim().toLowerCase();

            switch (answer) {
                case "y":
                    user = attemptSignIn(scan);
                    break;

                case "n":
                    user = attemptSignUp(scan);
                    break;

                default:
                    System.out.println("Invalid choice. Enter either 'Y' or 'N'");
                    break;
            }
        } while (user == null);

        return user;
    }

    private static User attemptSignIn(Scanner scan) {
        int signInAttempts = 0;
        User signInUser;

        do {
            signInUser = UserAuthenticationController.SignIn(scan);
            signInAttempts++;
            if (signInUser == null && signInAttempts >= 5) {
                System.out.println("Too many unsuccessful sign-in attempts. Returning to authentication page...");
                return null;
            }
        } while (signInUser == null);

        return signInUser;
    }

    private static User attemptSignUp(Scanner scan) {
        int signUpAttempts = 0;
        User signUpUser;

        do {
            signUpUser = UserAuthenticationController.SignUp(scan);
            signUpAttempts++;
            if (signUpUser == null && signUpAttempts >= 10) {
                System.out.println("Too many unsuccessful sign-up attempts. Returning to authentication page...");
                return null;
            }
        } while (signUpUser == null);

        return signUpUser;
    }

    private static void showMainMenu(User user) {

        UserRole role = user.getRole();

        switch (role){

            case UserRole.LIBRARIAN -> {

            }

            case UserRole.MEMBER -> {


            }

        }
    }

    private static void searchBooks() {
        // Implement book search functionality
    }

    private static void searchUsers() {
        // Implement user search functionality
    }

    private static void sortUsers() {
        // Implement user sorting functionality
    }

    private static void sortBooks() {
        // Implement book sorting functionality
    }
}
