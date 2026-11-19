
import java.util.HashMap;

public class Bank {

    private final HashMap<String, Account> accounts;

    // Constructor
    public Bank() {

        accounts = new HashMap<>();

        // Creating sample bank accounts
        accounts.put("1001",
                new Account("1001", "1234", 10000));

        accounts.put("1002",
                new Account("1002", "5678", 5000));
    }

    // Authenticate account using User ID and PIN
    public Account authenticate(String userId, String pin) {

        Account account = accounts.get(userId);

        if (account != null && account.authenticate(pin)) {
            return account;
        }

        return null;
    }

    // Find account using User ID
    public Account getAccount(String userId) {

        return accounts.get(userId);
    }

    // Transfer money between two accounts
    public boolean transfer(
            String senderId,
            String receiverId,
            double amount) {

        Account sender = accounts.get(senderId);
        Account receiver = accounts.get(receiverId);

        // Check whether both accounts exist
        if (sender == null || receiver == null) {
            return false;
        }

        // Prevent transfer to the same account
        if (senderId.equals(receiverId)) {
            return false;
        }

        // Deduct money from sender
        boolean success = sender.transferOut(amount, receiverId);

        if (!success) {
            return false;
        }

        // Add money to receiver
        receiver.transferIn(amount, senderId);

        return true;
    }

    // Display all available account IDs
    public void displayAccounts() {

        System.out.println("\nAvailable Bank Accounts:");

        for (String userId : accounts.keySet()) {
            System.out.println("Account ID: " + userId);
        }
    }
}
