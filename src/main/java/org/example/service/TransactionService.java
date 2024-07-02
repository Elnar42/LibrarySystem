package org.example.service;

import org.example.domain.Book;
import org.example.domain.Hold;
import org.example.domain.Transaction;
import org.example.domain.User;
import org.example.enums.TransactionType;
import org.example.repisitory.TransactionRepository;
import org.example.repisitory.UserRepository;

import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class TransactionService implements TransactionRepository {
    public final HashMap<Long, Transaction> transactions = new HashMap<>();
    private final BookService bookService = new BookService();
    private final UserRepository userRepository = new UserService();

    @Override
    public void loadAllTransactions() {
        File file = new File("transaction_database.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String details;
            while ((details = br.readLine()) != null) {
                String[] detail = details.split(",");
                if (detail[0].isEmpty()) break;
                Transaction transaction = new Transaction(Long.parseLong(detail[1]), Long.parseLong(detail[2]), LocalDate.parse(detail[3]), LocalDate.parse(detail[4]), TransactionType.valueOf(detail[5]));
                transactions.put(Long.parseLong(detail[0]), transaction);
            }
        } catch (IOException io) {
            System.out.println("Unable to load transactions!");
        }
    }


    @Override
    public void makeTransaction(Transaction transaction) throws IOException {
        validateTransaction(transaction);
        File file = new File("transaction_database.txt");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write(transaction.toString());
            bw.newLine();

        } catch (IOException io) {
            System.out.println("Unable to make transaction!");
        }

        checkLateTransactions();
        calculateAllHolds();
    }

    @Override
    public void makeTransactions(Transaction... transactions) throws IOException {
        for (Transaction transaction : transactions) {
            makeTransaction(transaction);
        }
    }

    public List<Transaction> loadTransactionsByUserId(Long userId) {
        if (userRepository.loadUserByUserId(userId).isEmpty())
            throw new IllegalArgumentException("No user found with provided id!");
        List<Transaction> filteredTransactions = new ArrayList<>();
        List<Transaction> allTransactions = loadTransactionsFromFile();
        for (Transaction transaction : allTransactions) {
            if (transaction.getUserId().equals(userId)) {
                filteredTransactions.add(transaction);
            }
        }
        return filteredTransactions;
    }

    @Override
    public List<Transaction> loadTransactionsByType(TransactionType type) {
        List<Transaction> filteredTransactions = new ArrayList<>();
        List<Transaction> allTransactions = loadTransactionsFromFile();
        for (Transaction transaction : allTransactions) {
            if (transaction.getTransactionType().equals(type)) {
                filteredTransactions.add(transaction);
            }
        }
        return filteredTransactions;
    }

    @Override
    public List<Transaction> loadTransactionsByTimeInterval(LocalDate startTime, LocalDate endTime) {
        List<Transaction> filteredTransactions = new ArrayList<>();
        List<Transaction> allTransactions = loadTransactionsFromFile();
        for (Transaction transaction : allTransactions) {
            LocalDate borrowDate = transaction.getBorrowDate();
            LocalDate returnDate = transaction.getReturnDate();
            if ((borrowDate.isEqual(startTime) || borrowDate.isAfter(startTime)) &&
                    (returnDate.isEqual(endTime) || returnDate.isBefore(endTime))) {
                filteredTransactions.add(transaction);
            }
        }
        return filteredTransactions;
    }

    @Override
    public List<Transaction> loadTransactionsByTimeIntervalAndUserId(Long userId, LocalDate startTime, LocalDate endTime) {
        if (userRepository.loadUserByUserId(userId).isEmpty())
            throw new IllegalArgumentException("No user found by given id");
        List<Transaction> transactions1 = loadTransactionsByTimeInterval(startTime, endTime);
        return transactions1.stream().filter(t -> t.getUserId().equals(userId)).toList();

    }

    @Override
    public void checkLateTransactions() {
        List<Transaction> returnTransactions = loadTransactionsByType(TransactionType.RETURN);
        List<Transaction> lateTransactions = new ArrayList<>();
        returnTransactions.forEach(t -> {
            long daysBetween = ChronoUnit.DAYS.between(t.getBorrowDate(), t.getReturnDate());
            if (daysBetween > 7) lateTransactions.add(t);
        });
        writeLateTransactionsToFile(lateTransactions);
    }

    @Override
    public List<Transaction> loadLateTransactions() {
        List<Transaction> transactions1 = new ArrayList<>();
        File file = new File("late_transactions_database.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String details;
            while ((details = br.readLine()) != null) {
                String[] detail = details.split(",");
                if (detail[0].isEmpty()) break;
                Transaction transaction = new Transaction(Long.parseLong(detail[1]), Long.parseLong(detail[2]));
                transaction.setId(Long.parseLong(detail[0]));
                transaction.setBorrowDate(LocalDate.parse(detail[3]));
                transaction.setReturnDate(LocalDate.parse(detail[4]));
                transactions1.add(transaction);
            }
        } catch (IOException e) {
            System.err.println("Error loading transactions: " + e.getMessage());
        }
        return transactions1;

    }

    @Override
    public List<Hold> loadTransactionHolds() {
        List<Hold> holds = new ArrayList<>();
        File file = new File("transaction_holds_database.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String details;
            while ((details = br.readLine()) != null) {
                String[] detail = details.split(",");
                if (detail[0].isEmpty()) break;
                Hold hold = new Hold(Long.parseLong(detail[0]), Long.parseLong(detail[1]), Double.parseDouble(detail[2]));
                holds.add(hold);
            }
        } catch (IOException e) {
            System.err.println("Error loading transactions: " + e.getMessage());
        }
        return holds;
    }

    @Override
    public Double loadHoldByUserId(Long userId) {
        if (userRepository.loadUserByUserId(userId).isEmpty())
            throw new IllegalArgumentException("No such user with id(" + userId + ")");
        List<Hold> holds = loadTransactionHolds();
        double totalHold = 0.0;
        for (Hold hold : holds) {
            if (hold.getUserId().equals(userId)) {
                totalHold += hold.getHold();
            }
        }
        return totalHold;
    }

    @Override
    public void payHold(Long userId) {
        if (userRepository.loadUserByUserId(userId).isEmpty())
            throw new IllegalArgumentException("User with given id not found!");
        User user = userRepository.loadUserByUserId(userId).getFirst();

        Double userHold = loadHoldByUserId(userId);
        Double userBalance = user.getBalance();
        if (userHold > userBalance) {
            throw new IllegalArgumentException("Balance is insufficient! (" + userBalance + "$)");
        }

        user.setBalance(userBalance - userHold);
        System.out.println("Hold is withdrawn from the account!");
        List<Transaction> lateTransactions = loadLateTransactions();
        lateTransactions.removeIf(t -> t.getUserId().equals(userId));
        writeLateTransactionsToFile(lateTransactions);
        List<Transaction> allTransactions = loadTransactionsFromFile();
        List<Transaction> removingTransactions = allTransactions.stream().filter(t -> t.getTransactionType() == TransactionType.RETURN && t.getUserId().equals(userId)).toList();

        allTransactions.removeIf(t -> t.getTransactionType() == TransactionType.RETURN && t.getUserId().equals(userId));
        File transactionFile = new File("transaction_database.txt");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(transactionFile))) {
            for (Transaction transaction : allTransactions) {
                bw.write(transaction.toString());
                bw.newLine();
            }
        } catch (IOException io) {
            System.out.println("Unable to update transaction database!");
        }

        File removingTransactionsFile = new File("paid_transaction_database.txt");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(removingTransactionsFile, true))) {
            for (Transaction transaction : removingTransactions) {
                bw.write(transaction.toString());
                bw.newLine();
            }
        } catch (IOException io) {
            System.out.println("Unable to update paid transaction database!");
        }

        calculateAllHolds();
        userRepository.removeUserById(userId);
        userRepository.saveUser(user);
    }


    private void writeFile(List<Transaction> lateTransactions, File file) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Transaction transaction : lateTransactions) {
                String message = transaction.toString() + " -> Maximum expected return date was 7, returned " + (ChronoUnit.DAYS.between(transaction.getBorrowDate(), transaction.getReturnDate()) - 7) + " day(s) late (at " + ChronoUnit.DAYS.between(transaction.getBorrowDate(), transaction.getReturnDate()) + "th day)!";
                bw.write(message);
                bw.newLine();
            }
        } catch (IOException io) {
            System.out.println("Unable to check late transactions!");
        }
    }

    private void writeLateTransactionsToFile(List<Transaction> lateTransactions) {
        File file = new File("late_transactions_database.txt");
        writeFile(lateTransactions, file);
    }

    @Override
    public void calculateAllHolds() {
        List<Transaction> lateTransactions = loadLateTransactions();
        File file = new File("transaction_holds_database.txt");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Transaction transaction : lateTransactions) {
                int returnDays = (int) ChronoUnit.DAYS.between(transaction.getBorrowDate(), transaction.getReturnDate()) - 7;
                String message = transactionFeeCalculation(transaction, returnDays);
                bw.write(message);
                bw.newLine();
            }
        } catch (IOException io) {
            System.out.println("Unable to calculate late transaction holds!");
        }
    }

    private String transactionFeeCalculation(Transaction transaction, int returnDays) {
        double hold;
        if (returnDays < 10) {
            hold = 5 + returnDays; // 5 base hold + 1 per day late
        } else if (returnDays < 20) {
            hold = 10 + returnDays - 5; // 10 base hold for 10-19 days late + additional for each day
        } else {
            hold = 15 + returnDays - 10; // 15 base hold for 20+ days late + additional for each day
        }
        return new Hold(transaction.getId(), transaction.getUserId(), hold).toString();
    }

    private List<Transaction> loadTransactionsFromFile() {
        List<Transaction> transactions = new ArrayList<>();
        File file = new File("transaction_database.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String details;
            while ((details = br.readLine()) != null) {
                String[] detail = details.split(",");
                if (!(detail.length > 1)) break;
                Transaction transaction = new Transaction(Long.parseLong(detail[1]), Long.parseLong(detail[2]), LocalDate.parse(detail[3]), LocalDate.parse(detail[4]), TransactionType.valueOf(detail[5]));
                transaction.setId(Long.parseLong(detail[0]));
                transactions.add(transaction);
            }
        } catch (IOException e) {
            System.err.println("Error loading transactions: " + e.getMessage());
        }
        return transactions;
    }

    private void validateTransaction(Transaction transaction) throws IOException {
        loadAllTransactions();
        if (transaction.getTransactionType().equals(TransactionType.BORROW)) {
            bookAndUserChecker(transaction);

            if (transactions.containsKey(transaction.getId())) {
                throw new IllegalArgumentException("Transaction with id (" + transaction.getId() + ") has already been accepted!");
            }
            if (loadHoldByUserId(transaction.getUserId()) > 10)
                throw new IllegalArgumentException("User can not take a new book since there is a hold more than 10$ (" + loadHoldByUserId(transaction.getUserId()) + "$). Pay hold first!");
            if (!bookService.loadById(transaction.getBookId()).getFirst().isAvailable()) {
                throw new IllegalArgumentException("Book with id (" + transaction.getBookId() + ") has been taken");

            }
            bookService.setBookAvailability(transaction.getBookId(), false);
        } else if (transaction.getTransactionType().equals(TransactionType.RETURN)) {

            bookAndUserChecker(transaction);
            List<Book> book = bookService.loadById(transaction.getBookId());
            if (book.getFirst().isAvailable())
                throw new IllegalArgumentException("User with given id (" + transaction.getUserId() + ") has not taken book with id (" + transaction.getBookId() + ")");
            if (transactions.entrySet().stream().noneMatch(t -> t.getValue().getUserId().equals(transaction.getUserId()) && t.getValue().getBookId().equals(transaction.getBookId())))
                throw new IllegalArgumentException("User with given id (" + transaction.getUserId() + ") has not taken book with id (" + transaction.getBookId() + ")");
            Stream<Map.Entry<Long, Transaction>> entryStream = transactions.entrySet().stream().filter(t -> t.getValue().getUserId().equals(transaction.getUserId()) && t.getValue().getBookId().equals(transaction.getBookId()));
            transaction.setBorrowDate(entryStream.findFirst().get().getValue().getBorrowDate());
            bookService.setBookAvailability(transaction.getBookId(), true);
        }
        transactions.clear();
    }

    private void bookAndUserChecker(Transaction transaction) {
        List<User> user = userRepository.loadUserByUserId(transaction.getUserId());
        List<Book> book = bookService.loadById(transaction.getBookId());
        if (user.isEmpty())
            throw new IllegalArgumentException("No user found with id (" + transaction.getUserId() + ")");
        if (book.isEmpty())
            throw new IllegalArgumentException("No book found with id (" + transaction.getBookId() + ")");
    }

}
