
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Account {

    private final String userId;
    private final String pin;
    private double balance;

    private final ArrayList<Transaction> transactions;

    // Constructor
    public Account(String userId, String pin, double initialBalance) {

        this.userId = userId;
        this.pin = pin;
        this.balance = initialBalance;

        transactions = new ArrayList<>();
    }

    // Authenticate user PIN
    public boolean authenticate(String enteredPin) {
        return pin.equals(enteredPin);
    }

    // Deposit money
    public boolean deposit(double amount) {

        if (!Double.isFinite(amount) || amount <= 0) {
            return false;
        }

        balance += amount;

        transactions.add(
                new Transaction(
                        "DEPOSIT",
                        amount,
                        "Cash Deposit"
                )
        );

        return true;
    }

    // Withdraw money
    public boolean withdraw(double amount) {

        if (!Double.isFinite(amount)
                || amount <= 0
                || amount > balance) {

            return false;
        }

        balance -= amount;

        transactions.add(
                new Transaction(
                        "WITHDRAWAL",
                        amount,
                        "Cash Withdrawal"
                )
        );

        return true;
    }

    // Transfer money from this account
    boolean transferOut(double amount, String recipientId) {

        if (!Double.isFinite(amount)
                || amount <= 0
                || amount > balance) {

            return false;
        }

        balance -= amount;

        transactions.add(
                new Transaction(
                        "TRANSFER SENT",
                        amount,
                        "To Account: " + recipientId
                )
        );

        return true;
    }

    // Receive money from another account
    void transferIn(double amount, String senderId) {

        balance += amount;

        transactions.add(
                new Transaction(
                        "TRANSFER IN",
                        amount,
                        "From Account: " + senderId
                )
        );
    }

    // Display transaction history in console
    public void displayTransactionHistory() {

        if (transactions.isEmpty()) {

            System.out.println("No transactions available.");
            return;
        }

        System.out.println(
                "\n========== TRANSACTION HISTORY =========="
        );

        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }

        System.out.println(
                "========================================="
        );
    }

    // Getter: User ID
    public String getUserId() {
        return userId;
    }

    // Getter: Account balance
    public double getBalance() {
        return balance;
    }

    // Getter: Transaction history for GUI
    public List<Transaction> getTransactions() {

        return Collections.unmodifiableList(
                new ArrayList<>(transactions)
        );
    }
}
