package org.example.domain;

public class Hold implements Displayable {

    private Long transactionId;
    private Long userId;

    private double hold;

    public Hold(Long transactionId, Long userId, double hold) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.hold = hold;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public double getHold() {
        return hold;
    }

    public void setHold(double hold) {
        this.hold = hold;
    }

    @Override
    public String toString() {
        return transactionId + "," + userId + "," + hold;
    }
    @Override
    public String toFormattedString() {
        return String.format("Transaction ID: %-3s | UserID: %-25s | Hold: %-20s$",
                transactionId, userId, hold);
    }
}
