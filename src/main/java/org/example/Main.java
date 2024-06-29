package org.example;


import org.example.controller.UserAuthenticationController;
import org.example.domain.Book;
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


        UserService userService = new UserService();

//        userService.saveUser(new User(7248L, "Librarian", "admin123", "Baku Azerbaijan", "+994503421212", "elibrarian@gmail.com", UserRole.LIBRARIAN));
//        userService.saveUser(new User(7249L, "elsad_12", "admin123", "Baku Azerbaijan", "+994104531212", "elsad12@gmail.com", UserRole.MEMBER));
//        userService.saveUser(new User(7250L, "asim_42", "main_ch_12", "Baku Azerbaijan", "+994778906756", "asim42@gmail.com", UserRole.MEMBER));
//        userService.saveUser(new User(7251L, "eldar_123", "alfa_betta_56", "Baku Azerbaijan", "+994551231213", "eldar1234@gmail.com", UserRole.MEMBER));
//        userService.saveUser(new User(7252L, "anar_032", "user12313", "Baku Azerbaijan", "+994773211213", "anarqocayev12@gmail.com", UserRole.MEMBER));
//        userService.saveUser(new User(7253L, "aqsin_12345", "myNameIsAqsin", "Baku Azerbaijan", "+994999991990", "aqsin12332@gmail.com", UserRole.MEMBER));
//        userService.saveUser(new User(7254L, "elchin_143", "BakuAze1234", "Baku Azerbaijan", "+994503786412", "eclhin1432@gmail.com", UserRole.MEMBER));
//        userService.saveUser(new User(7255L, "ramil_342", "1234567", "Baku Azerbaijan", "+994513454515", "ramilaliyev1232@gmail.com", UserRole.MEMBER));
//        userService.saveUser(new User(7256L, "alim_1232", "IdLibrary12", "Baku Azerbaijan", "+994553121212", "alimqasimov1432@gmail.com", UserRole.MEMBER))
//       userService.saveUser(new User(725997L, "akif_90452", "MyLibrary1234", "Baku Azerbaijan", "+994771655423", "akifAliyev90452@gmail.com", UserRole.MEMBER));

        // System.out.println(userService.loadUserByUsername("aqsin_12345"));



        BookService bookService = new BookService();



        System.out.println(bookService.getBookAvailability(11L));



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







