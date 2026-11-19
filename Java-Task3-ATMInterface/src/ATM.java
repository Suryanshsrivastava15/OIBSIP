
import java.util.Scanner;

public class ATM {

    private final Bank bank;
    private final Scanner scanner;
    private Account currentAccount;

    // Constructor
    public ATM() {
        bank = new Bank();
        scanner = new Scanner(System.in);
    }

    // Start ATM application
    public void start() {

        System.out.println("==================================");
        System.out.println("        WELCOME TO ATM");
        System.out.println("==================================");

        if (!login()) {
            System.out.println("Access Denied. Exiting ATM...");
            return;
        }

        showMenu();
    }

    // User authentication
    private boolean login() {

        int attempts = 0;
        int maxAttempts = 3;

        while (attempts < maxAttempts) {

            System.out.print("\nEnter User ID: ");
            String userId = scanner.nextLine().trim();

            System.out.print("Enter PIN: ");
            String pin = scanner.nextLine().trim();

            currentAccount = bank.authenticate(userId, pin);

            if (currentAccount != null) {

                System.out.println("\nLogin Successful!");
                System.out.println("Welcome, Account "
                        + currentAccount.getUserId());

                return true;
            }

            attempts++;

            System.out.println("\nInvalid User ID or PIN!");

            if (attempts < maxAttempts) {
                System.out.println("Attempts remaining: "
                        + (maxAttempts - attempts));
            }
        }

        System.out.println("\nAccount access blocked after 3 failed attempts.");

        return false;
    }

    // Display ATM menu
    private void showMenu() {

        while (true) {

            System.out.println("\n========== ATM MENU ==========");
            System.out.println("1. Transaction History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Quit");
            System.out.println("==============================");

            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {

                case "1":
                    currentAccount.displayTransactionHistory();
                    break;

                case "2":
                    withdrawMoney();
                    break;

                case "3":
                    depositMoney();
                    break;

                case "4":
                    transferMoney();
                    break;

                case "5":
                    System.out.println("\nThank you for using our ATM!");
                    System.out.println("Please visit again.");
                    return;

                default:
                    System.out.println("Invalid choice! Select 1 to 5.");
            }
        }
    }

    // Withdraw money
    private void withdrawMoney() {

        System.out.print("\nEnter withdrawal amount: Rs. ");

        double amount = readAmount();

        if (amount <= 0) {
            return;
        }

        if (currentAccount.withdraw(amount)) {

            System.out.println("\nWithdrawal Successful!");
            System.out.printf("Withdrawn Amount: Rs. %.2f%n", amount);
            System.out.printf("Remaining Balance: Rs. %.2f%n",
                    currentAccount.getBalance());

        } else {

            System.out.println("Insufficient Funds!");
        }
    }

    // Deposit money
    private void depositMoney() {

        System.out.print("\nEnter deposit amount: Rs. ");

        double amount = readAmount();

        if (amount <= 0) {
            return;
        }

        if (currentAccount.deposit(amount)) {

            System.out.println("\nDeposit Successful!");
            System.out.printf("Deposited Amount: Rs. %.2f%n", amount);
            System.out.printf("Updated Balance: Rs. %.2f%n",
                    currentAccount.getBalance());

        } else {

            System.out.println("Invalid deposit amount!");
        }
    }

    // Transfer money
    private void transferMoney() {

        System.out.print("\nEnter recipient Account ID: ");

        String receiverId = scanner.nextLine().trim();

        Account receiver = bank.getAccount(receiverId);

        if (receiver == null) {
            System.out.println("Recipient account not found!");
            return;
        }

        if (receiverId.equals(currentAccount.getUserId())) {
            System.out.println("Cannot transfer to your own account!");
            return;
        }

        System.out.print("Enter transfer amount: Rs. ");

        double amount = readAmount();

        if (amount <= 0) {
            return;
        }

        if (amount > currentAccount.getBalance()) {
            System.out.println("Insufficient Funds!");
            return;
        }

        boolean success = bank.transfer(
                currentAccount.getUserId(),
                receiverId,
                amount
        );

        if (success) {

            System.out.println("\nTransfer Successful!");
            System.out.printf("Transferred Amount: Rs. %.2f%n", amount);
            System.out.printf("Remaining Balance: Rs. %.2f%n",
                    currentAccount.getBalance());

        } else {

            System.out.println("Transfer Failed!");
        }
    }

    // Validate amount input
    private double readAmount() {

        String input = scanner.nextLine().trim();

        try {

            double amount = Double.parseDouble(input);

            if (!Double.isFinite(amount) || amount <= 0) {
                System.out.println("Enter a valid positive amount!");
                return -1;
            }

            return amount;

        } catch (NumberFormatException e) {

            System.out.println("Invalid amount! Enter numeric value.");
            return -1;
        }
    }
}
