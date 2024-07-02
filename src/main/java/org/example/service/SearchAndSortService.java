package org.example.service;

import org.example.domain.Book;
import org.example.domain.Displayable;
import org.example.domain.User;
import org.example.enums.BookGenre;
import org.example.enums.UserRole;
import org.example.repisitory.SearchAndSortRepository;

import java.util.*;

import static org.example.service.BookService.books;
import static org.example.service.UserService.users;

public class SearchAndSortService implements SearchAndSortRepository {
    private final BookService bookService = new BookService();
    private final UserService userService = new UserService();

    //READY
    @Override
    public void searchBooks(Scanner scan) {
        System.out.println("Which criteria do you want to search books by?");
        System.out.print("""
                Possible options:
                1. ID
                2. Genre
                3. Author
                4. Title
                5. Availability
                """);
        System.out.println("\nYour choice: ");
        int answer = scan.nextInt();
        scan.nextLine();

        switch (answer) {
            case 1 -> searchById(scan);
            case 2 -> searchByGenre(scan);
            case 3 -> searchByAuthor(scan);
            case 4 -> searchByTitle(scan);
            case 5 -> searchByAvailability(scan);
            default -> throw new IllegalArgumentException("Unknown criteria!");
        }
    }

    //READY
    @Override
    public void sortBooks(Scanner scan) {
        System.out.println("Which criteria do you want to sort books by?");
        System.out.print("""
                Possible options:
                1. Author
                2. Title
                3. Publication Date
                4. ID
                """);
        System.out.println("Your choice: ");
        int choice = scan.nextInt();
        scan.nextLine();

        Comparator<Book> comparator;
        String sortCriteria;

        switch (choice) {
            case 1 -> {
                comparator = Comparator.comparing(Book::getAuthor);
                sortCriteria = "Authors";
            }
            case 2 -> {
                comparator = Comparator.comparing(Book::getTitle);
                sortCriteria = "Title";
            }
            case 3 -> {
                comparator = Comparator.comparing(Book::getPublicationDate);
                sortCriteria = "Publication Year";
            }
            case 4 -> {
                comparator = Comparator.comparing(Book::getId);
                sortCriteria = "ID";
            }
            default -> throw new IllegalArgumentException("No valid option!");
        }

        if (comparator != null) {
            sortByCriteria(comparator, sortCriteria);
        }
    }

    //READY
    @Override
    public void searchUsers(Scanner scan) {
        System.out.println("Which criteria do you want to search users by?");
        System.out.print("""
                Possible options:
                1. ID
                2. Username
                3. User Role
                4. Address
                """);
        System.out.println("\nYour choice: ");
        int answer = scan.nextInt();
        scan.nextLine();

        switch (answer) {
            case 1 -> searchByUserId(scan);
            case 2 -> searchByUsername(scan);
            case 3 -> searchByUserRole(scan);
            case 4 -> searchByAddress(scan);
            default -> throw new IllegalArgumentException("Unknown criteria!");
        }


    }

    //READY
    @Override
    public void sortUsers(Scanner scan) {
        System.out.println("Which order of ID do you want to sort for users?");
        System.out.println("""
                1. Ascending
                2. Descending
                """);
        System.out.println("Your choice: ");
        int order = scan.nextInt();
        scan.nextLine();

        userService.loadAllUsers();
        List<User> userList1 = new ArrayList<>(users.values());
        users.clear();

        Comparator<User> comparator = Comparator.comparing(User::getId);

        switch (order) {
            case 1 -> userList1.sort(comparator);
            case 2 -> userList1.sort(comparator.reversed());
            default -> throw new IllegalArgumentException("Unknown Ordering!");
        }
        display(userList1, "user");
    }

    //READY
    private void searchByUserRole(Scanner scan) {
        System.out.println("Enter user's role: " + Arrays.toString(UserRole.values()));
        String userRole = scan.nextLine();
        List<User> users = userService.loadUserByUserRole(UserRole.valueOf(userRole.toUpperCase()));
        display(users, "user");
    }

    //READY
    private void searchByAddress(Scanner scan) {
        System.out.println("Enter an address to search user by (You might write whatever your remember): ");
        String address = scan.nextLine();
        List<User> users = userService.loadUserByUserAddress(address);
        display(users, "user");
    }

    //READY
    private void searchByUsername(Scanner scan) {
        System.out.println("Enter username: ");
        String username = scan.nextLine();
        List<User> users = userService.loadUserByUsername(username);
        display(users, "user");
    }

    //READY
    private void searchByUserId(Scanner scan) {
        System.out.println("Enter user id: ");
        Long id = scan.nextLong();
        List<User> users = userService.loadUserByUserId(id);
        display(users, "user");
    }


    //READY
    private void sortByCriteria(Comparator<Book> comparator, String sortCriteria) {
        bookService.loadAllBooks();
        List<Book> books1 = new ArrayList<>(books.values());
        books.clear();

        System.out.printf("""
                Which order of %s do you want to search?
                1. Ascending
                2. Descending
                %n""", sortCriteria);
        System.out.println("Your choice: ");
        Scanner scan = new Scanner(System.in);
        int order = scan.nextInt();
        scan.nextLine();

        switch (order) {
            case 1 -> books1.sort(comparator);
            case 2 -> books1.sort(comparator.reversed());
            default -> throw new IllegalArgumentException("Unknown Ordering!");
        }
        display(books1, "book");
    }

    //READY
    private void searchByAvailability(Scanner scan) {
        System.out.println("Select one: ");
        System.out.println("""
                1. Available
                2. Unavailable
                """);
        int answer = scan.nextInt();
        List<Book> books = switch (answer) {
            case 1 -> bookService.loadByAvailability(true);
            case 2 -> bookService.loadByAvailability(false);
            default -> throw new IllegalArgumentException("Invalid choice!");
        };
        display(books, "book");
    }

    //READY
    private void searchById(Scanner scan) {
        System.out.println("Enter book id: ");
        long id = scan.nextLong();
        scan.nextLine();
        List<Book> books = bookService.loadById(id);
        display(books, "book");
    }

    //READY
    private void searchByGenre(Scanner scan) {
        System.out.println("Enter book genre: " + Arrays.toString(BookGenre.values()));
        String genre = scan.nextLine().toUpperCase();
        List<Book> books = bookService.loadByGenre(BookGenre.valueOf(genre));
        display(books, "book");
    }

    //READY
    private void searchByAuthor(Scanner scan) {
        System.out.println("Enter author's full name: ");
        String author = scan.nextLine();
        List<Book> books = bookService.loadByAuthor(author);
        display(books, "book");
    }

    //READY
    private void searchByTitle(Scanner scan) {
        System.out.println("Enter book's title (You might write whatever you remember if you do not exactly know the title): ");
        String title = scan.nextLine();
        List<Book> books = bookService.loadByTitle(title);
        display(books, "book");
    }

    //READY
    private <T extends Displayable> void display(List<T> thingToDisplay, String message) {
        System.out.println("Here is (are) " + message + "(s): ");
        if (!thingToDisplay.isEmpty()) {
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            thingToDisplay.forEach(t -> System.out.println(t.toFormattedString()));
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        } else {
            System.out.println("--------------------------------");
            System.out.println("No " + message + " found!");
            System.out.println("--------------------------------");
        }
    }


}
