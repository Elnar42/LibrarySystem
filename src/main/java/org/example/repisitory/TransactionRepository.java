package org.example.repisitory;

import org.example.domain.Hold;
import org.example.domain.Transaction;
import org.example.enums.TransactionType;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository {
    void loadAllTransactions() throws IOException;

    void makeTransaction(Transaction transaction) throws IOException;

    void makeTransactions(Transaction... transactions) throws IOException;

    List<Transaction> loadTransactionsByUserId(Long userId);

    List<Transaction> loadTransactionsByType(TransactionType type);

    List<Transaction> loadTransactionsByTimeInterval(LocalDate startTime, LocalDate endTime);

    List<Transaction> loadTransactionsByTimeIntervalAndUserId(Long userId, LocalDate startTime, LocalDate endTime);

    void checkLateTransactions() throws IOException;

    List<Transaction> loadLateTransactions();

    void calculateAllHolds() throws IOException;

    List<Hold> loadTransactionHolds();

    Double loadHoldByUserId(Long userId);

    void payHold(Long userId);
}
