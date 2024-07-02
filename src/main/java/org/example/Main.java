package org.example;

import org.example.controller.UserAuthenticationController;
import org.example.domain.Book;
import org.example.domain.Transaction;
import org.example.domain.User;
import org.example.enums.BookGenre;
import org.example.enums.TransactionType;
import org.example.enums.UserRole;
import org.example.service.BookService;
import org.example.service.SearchAndSortService;
import org.example.service.TransactionService;
import org.example.service.UserService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;

/*
 * ALL THE PASSWORD ARE HASHED, HERE ARE TWO ACCOUNTS YOU CAN REFER TO
 * Username: Librarian password: admin123 -> LIBRARIAN PRIVILEGE (HAVE ALL ACCESS RIGHTS)
 * Username: Ilkin123 password: user123 -> MEMBER PRIVILEGE (LIMITED ACCESS RIGHTS)
 */
public class Main {
    private final static BookService bookService = new BookService();
    private final static SearchAndSortService searchAndSortService = new SearchAndSortService();
    private final static TransactionService transactionService = new TransactionService();
    private final static UserService userService = new UserService();

    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);

        System.out.println("---------------------------WELCOME TO AKHUNDOV LIBRARY---------------------------");
        User principal = authenticateUser(scan);

        if (principal != null) {
            showMainMenu(principal, scan);
        }

        System.out.println("Logged out successfully!");
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
        User signInUser = null;

        do {

            try {
                signInUser = UserAuthenticationController.SignIn(scan);
                signInAttempts++;
                if (signInUser == null && signInAttempts >= 5) {
                    System.out.println("Too many unsuccessful sign-in attempts. Returning to authentication page...");
                    return null;
                }
            } catch (Exception exception) {
                System.out.println("Something unexpected happened (" + exception.toString() + ")");
            }


        } while (signInUser == null);

        return signInUser;
    }

    private static User attemptSignUp(Scanner scan) {
        int signUpAttempts = 0;
        User signUpUser = null;

        do {
            try {
                signUpUser = UserAuthenticationController.SignUp(scan);
                signUpAttempts++;
                if (signUpUser == null && signUpAttempts >= 10) {
                    System.out.println("Too many unsuccessful sign-up attempts. Returning to authentication page...");
                    return null;
                }
            } catch (Exception exception) {
                System.out.println("Something unexpected happened (" + exception.toString() + ")");
            }
        } while (signUpUser == null);

        return signUpUser;
    }

    private static void showMainMenu(User user, Scanner scan) {
        UserRole role = user.getRole();

        switch (role) {
            case LIBRARIAN:
                showLibrarianMenu(scan, user);
                break;

            case MEMBER:
                showMemberMenu(scan, user);
                break;
        }
    }

    private static void showLibrarianMenu(Scanner scan, User user) {
        boolean logout = false;

        while (!logout) {
            String menuOptionsLibrarian = """
                    === Librarian Menu ===
                    1. Load Transactions by User ID
                    2. Load Transactions by Type
                    3. Load Transactions by Time Interval
                    4. Load Transactions by Time Interval and User ID
                    5. Check Late Transactions
                    6. Calculate All Holds
                    7. Load Late Transactions
                    8. Load Transaction Holds
                    9. Load Hold by User ID
                    10. Save Book to Library
                    11. Remove Book by ID
                    12. Search Users
                    13. Sort Users
                    14. Search Books
                    15. Sort Books
                    16. Logout
                    """;
            System.out.println(menuOptionsLibrarian);

            System.out.println("Select an option: ");
            int option = scan.nextInt();
            scan.nextLine();

            switch (option) {
                case 1:
                    loadTransactionsByUserId(scan);
                    break;
                case 2:
                    loadTransactionsByType(scan);
                    break;
                case 3:
                    loadTransactionsByTimeInterval(scan);
                    break;
                case 4:
                    loadTransactionsByTimeIntervalAndUserId(scan);
                    break;
                case 5:
                    transactionService.checkLateTransactions();
                    System.out.println("Successfully checked late transactions! Refer to 'late_transactions_database.txt'");
                    break;
                case 6:
                    transactionService.calculateAllHolds();
                    System.out.println("Successfully calculated the holds! Refer to 'transaction_holds_database.txt'");
                    break;
                case 7:
                    transactionService.loadLateTransactions().forEach(System.out::println);
                    break;
                case 8:
                    transactionService.loadTransactionHolds().forEach(System.out::println);
                    break;
                case 9:
                    loadHoldByUserId(scan);
                    break;
                case 10:
                    saveBook(scan);
                    break;
                case 11:
                    removeBookById(scan);
                    break;
                case 12:
                    searchAndSortService.searchUsers(scan);
                    break;
                case 13:
                    searchAndSortService.sortUsers(scan);
                    break;
                case 14:
                    searchAndSortService.searchBooks(scan);
                    break;
                case 15:
                    searchAndSortService.sortBooks(scan);
                    break;
                case 16:
                    logout = true;
                    break;
                default:
                    System.out.println("Invalid option. Please choose a valid option.");
                    break;
            }
        }
    }

    private static void loadTransactionsByUserId(Scanner scan) {
        System.out.println("Enter user ID: ");
        Long id = scan.nextLong();
        User userBySearch = userService.loadUserByUserId(id).getFirst();
        System.out.println("Here is the user: ");
        System.out.println(userBySearch.toFormattedString());
    }

    private static void loadTransactionsByType(Scanner scan) {
        System.out.println("Enter a transaction type: " + Arrays.toString(TransactionType.values()));
        String type = scan.next().toUpperCase();
        switch (TransactionType.valueOf(type)) {
            case BORROW:
                transactionService.loadTransactionsByType(TransactionType.BORROW).forEach(System.out::println);
                break;
            case RETURN:
                transactionService.loadTransactionsByType(TransactionType.RETURN).forEach(System.out::println);
                break;
            default:
                System.out.println("Invalid transaction type.");
                break;
        }
    }

    private static void loadTransactionsByTimeInterval(Scanner scan) {
        System.out.println("Enter start time (YYYY-MM-DD): ");
        String startTime = scan.next().trim();
        System.out.println("Enter end time (YYYY-MM-DD): ");
        String endTime = scan.next().trim();
        LocalDate start = LocalDate.parse(startTime);
        LocalDate end = LocalDate.parse(endTime);
        if (transactionService.loadTransactionsByTimeInterval(start, end).isEmpty())
            System.out.println("You have no transaction between given interval!");
        else transactionService.loadTransactionsByTimeInterval(start, end).forEach(System.out::println);
    }

    private static void loadTransactionsByTimeIntervalAndUserId(Scanner scan) {
        System.out.println("Enter start time (YYYY-MM-DD): ");
        String startTime = scan.next().trim();
        System.out.println("Enter end time (YYYY-MM-DD): ");
        String endTime = scan.next().trim();
        System.out.println("Enter user ID: ");
        Long id = scan.nextLong();
        LocalDate start = LocalDate.parse(startTime);
        LocalDate end = LocalDate.parse(endTime);
        transactionService.loadTransactionsByTimeIntervalAndUserId(id, start, end).forEach(System.out::println);
    }

    private static void loadHoldByUserId(Scanner scan) {
        transactionService.checkLateTransactions();
        transactionService.calculateAllHolds();
        System.out.println("Enter a user id to see his/her hold: ");
        Long id = scan.nextLong();
        System.out.println("User with id (" + id + ") has $" + transactionService.loadHoldByUserId(id) + " hold");
    }

    private static void saveBook(Scanner scan) {
        System.out.println("Enter book id (make sure it does not overlap with already defined books): ");
        Long id = scan.nextLong();
        System.out.println("Enter title: ");
        String title = scan.next();
        System.out.println("Enter author's full name: ");
        scan.nextLine();
        String author = scan.nextLine();
        System.out.println("Enter a book genre " + Arrays.toString(BookGenre.values()) + ":");
        String genre = scan.next().toUpperCase();
        System.out.println("Enter a publication date (YYYY-MM-DD): ");
        String publication = scan.next();
        Book book = new Book(id, title, author, BookGenre.valueOf(genre), LocalDate.parse(publication), true);
        bookService.saveBook(book);
        System.out.println("Book saved successfully. Refer to 'book_database.txt' file!");
    }

    private static void removeBookById(Scanner scan) {
        System.out.println("Enter a book id for removal: ");
        Long id = scan.nextLong();
        if (bookService.removeBookById(id)) {
            System.out.println("Book removed successfully");
        } else {
            System.out.println("Book not found!");
        }
    }

    private static void showMemberMenu(Scanner scan, User user) {
        boolean logout = false;

        while (!logout) {
            String menuOptionsMembers = """
                    === Member Menu ===
                    1. Make Transaction (load your transactions first)
                    2. Load My Transactions
                    3. Load Transactions by Time Interval
                    4. Load My Holds
                    5. Update Profile (Username, Password, Email, or Balance)
                    6. Pay Hold
                    7. Search Books
                    8. Sort Books
                    9. Logout
                    """;
            System.out.println(menuOptionsMembers);

            System.out.println("Select an option: ");
            int option = scan.nextInt();
            scan.nextLine();

            switch (option) {
                case 1:
                    makeTransaction(scan, user);
                    break;
                case 2:
                    loadMyTransactions(scan, user);
                    break;
                case 3:
                    loadTransactionsByTimeInterval(scan);
                    break;
                case 4:
                    loadMyHolds(user);
                    break;
                case 5:
                    updateProfile(scan, user);
                    break;
                case 6:
                    payHold(user);
                    break;
                case 7:
                    searchBooks(scan);
                    break;
                case 8:
                    sortBooks(scan);
                    break;
                case 9:
                    logout = true;
                    break;
                default:
                    System.out.println("Invalid option. Please choose a valid option.");
                    break;
            }
        }
    }

    private static void makeTransaction(Scanner scan, User user) {
        System.out.println("""
                Select a transaction type:
                1. BORROW
                2. RETURN
                """);
        int transactionType = scan.nextInt();
        switch (transactionType) {
            case 1 -> {
                System.out.println("Enter a book id (highly recommended to search books first): ");
                Long id = scan.nextLong();
                System.out.println("Enter a return time [YYYY-MM-DD] (maximum time limit is 7 inclusively): ");
                String returnTime = scan.next();
                transactionService.makeTransaction(new Transaction(user.getId(), id, LocalDate.parse(returnTime)));
                System.out.println("Successfully added. Make sure to return the book on specified time to avoid the fines!");
            }
            case 2 -> {
                System.out.println("Enter a book's id you want to return: ");
                Long id = scan.nextLong();
                transactionService.makeTransaction(new Transaction(user.getId(), id));

            }
            default -> System.out.println("Invalid option. Please choose a valid option.");
        }

    }

    private static void loadMyTransactions(Scanner scan, User user) {
        if (transactionService.loadTransactionsByUserId(user.getId()).isEmpty())
            System.out.println("You have no transaction yet!");
        else transactionService.loadTransactionsByUserId(user.getId()).forEach(System.out::println);
    }

    private static void loadMyHolds(User user) {
        transactionService.checkLateTransactions();
        transactionService.calculateAllHolds();

        Double v = transactionService.loadHoldByUserId(user.getId());
        System.out.println("You have " + v + " hold!");
    }

    private static void updateProfile(Scanner scan, User user) {
        System.out.println("""
                Select an option to update:
                1. Username
                2. Password
                3. Email
                4. Phone Number
                5. Deposit Money
                """);

        int option = scan.nextInt();
        switch (option) {
            case 1:
                userService.changeUsername(scan, user.getId());
                break;
            case 2:
                userService.resetPassword(scan, user.getId());
                break;
            case 3:
                userService.changeEmail(scan, user.getId());
                break;
            case 4:
                userService.changePhoneNumber(scan, user.getId());
                break;
            case 5:
                userService.increaseAccountBalance(scan, user.getId());
                break;
            default:
                System.out.println("Wrong operation!");
        }


    }

    private static void payHold(User user) {
        transactionService.payHold(user.getId());

    }

    private static void searchBooks(Scanner scan) {
        searchAndSortService.searchBooks(scan);
    }

    private static void sortBooks(Scanner scan) {
        searchAndSortService.sortBooks(scan);
    }
}
