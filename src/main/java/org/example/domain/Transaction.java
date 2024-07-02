package org.example.domain;

import org.example.enums.TransactionType;
import org.example.repisitory.GeneralRepository;
import org.example.service.BookService;
import org.example.service.TransactionService;
import org.example.service.UserService;

import java.io.IOException;
import java.time.LocalDate;

public class Transaction implements Displayable {

    private final UserService userService = new UserService();
    private final BookService bookService = new BookService();
    private final TransactionService transactionService = new TransactionService();

    private Long id;
    private Long userId;
    private Long bookId;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private TransactionType transactionType;


    //BORROWING, Borrow date is current date (Reservation will be in the later stage)
    public Transaction(Long userId, Long bookId, LocalDate returnDate) {
        LocalDate borrowDate = LocalDate.now();
        verifyCredentials(userId, bookId, borrowDate, returnDate, TransactionType.BORROW);
        Long idtoCheck = GeneralRepository.generateID();
        while (transactionService.transactions.containsKey(idtoCheck)) idtoCheck = GeneralRepository.generateID();
        this.id = idtoCheck;
        this.userId = userId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.transactionType = TransactionType.BORROW;
    }

    //RETURNING
    public Transaction(Long userId, Long bookId) {
        LocalDate returnDate = LocalDate.now();
        verifyCredentials(userId, bookId, borrowDate, returnDate, TransactionType.RETURN);
        Long idtoCheck = GeneralRepository.generateID();
        while (transactionService.transactions.containsKey(idtoCheck)) idtoCheck = GeneralRepository.generateID();
        this.id = idtoCheck;
        this.userId = userId;
        this.bookId = bookId;
        this.returnDate = returnDate;
        this.transactionType = TransactionType.RETURN;
    }

    public Transaction(Long userId, Long bookId, LocalDate borrowDate, LocalDate returnDate, TransactionType transactionType) throws IOException {
        this.id = GeneralRepository.generateID();
        this.userId = userId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.transactionType = transactionType;
    }

    private void verifyCredentials(Long userId, Long bookId, LocalDate borrowDate, LocalDate returnDate, TransactionType transactionType) {

        if (userService.loadUserByUserId(userId).isEmpty())
            throw new IllegalArgumentException("User with id (" + userId + ") does not exist!");

        if (bookService.loadById(bookId).isEmpty())
            throw new IllegalArgumentException("Book with id (" + bookId + ") does not exist!");

        switch (transactionType) {

            case BORROW -> {

                if (returnDate.isBefore(borrowDate)) {
                    System.out.println("Return date is invalid!");
                    return;
                }
                if (borrowDate.plusDays(7).isBefore(returnDate)) {
                    System.out.println("Maximum borrow period exceeded (7 days)");
                }

            }

            case RETURN -> {

            }

            default -> System.out.println("No such transaction type!");
        }


    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public String toString() {
        return id + "," + userId + "," + bookId + "," + borrowDate.toString() + "," + returnDate.toString() + "," + transactionType;
    }

    @Override
    public String toFormattedString() {
        if (transactionType == TransactionType.RETURN) {
            return String.format("ID: %-5s | User Id: %-5s | Book Id: %s | Borrow Date: %-5s | Return Date: %-20s | Transaction Type: %s",
                    id, userId, bookId, borrowDate, returnDate, transactionType);
        } else {
            return String.format("ID: %-5s | User Id: %-5s | Book Id: %s | Borrow Date: %-5s | Expected Return Date: %-11s | Transaction Type: %s",
                    id, userId, bookId, borrowDate, returnDate, transactionType);
        }
    }

}
