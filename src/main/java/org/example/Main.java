package org.example;

import org.example.controller.UserAuthenticationController;
import org.example.domain.Book;
import org.example.domain.User;
import org.example.enums.BookGenre;
import org.example.service.BookService;
import org.example.service.UserService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;


/*
 * Librarian Account Details:
 * Username -> Librarian
 * Password -> admin123
 * */

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);



        BookService bookService = new BookService();

        bookService.loadByTitle("").forEach(System.out::println);






//        do {
//            System.out.println("Welcome to our Library!");
//            System.out.println("1. Sign In");
//            System.out.println("2. Sign Up");
//            System.out.println("3. Exit");
//            System.out.print("Enter your choice: ");
//            choice = scan.nextInt();
//
//            switch (choice) {
//                case 1:
//                    UserAuthenticationController.SignIn(scan);
//
//                    break;
//                case 2:
//                    UserAuthenticationController.SignUp(scan);
//
//
//                    break;
//                case 3:
//                    System.out.println("Exiting...");
//                    break;
//                default:
//                    System.out.println("Invalid choice. Please try again.");
//                    break;
//            }
//        } while (choice != 3);


    }

}







