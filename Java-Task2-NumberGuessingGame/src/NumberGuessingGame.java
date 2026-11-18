
import java.util.ArrayList;
import java.util.Scanner;

public class NumberGuessingGame {

    private final Scanner scanner;
    private final ArrayList<String> roundHistory;
    private int roundNumber;

    // Constructor
    public NumberGuessingGame() {
        scanner = new Scanner(System.in);
        roundHistory = new ArrayList<>();
        roundNumber = 0;
    }

    // Start the game
    public void start() {

        System.out.println("==================================");
        System.out.println("   WELCOME TO NUMBER GUESSING GAME");
        System.out.println("==================================");

        boolean playAgain;

        do {

            Difficulty difficulty = selectDifficulty();

            roundNumber++;

            GameRound currentRound = new GameRound(difficulty);

            currentRound.play(scanner);

            // Clear the remaining newline
            scanner.nextLine();

            // Store round result
            String result;

            if (currentRound.isWon()) {

                result = "Round " + roundNumber
                        + " - guessed in "
                        + currentRound.getAttemptsUsed()
                        + " attempts";

            } else {

                result = "Round " + roundNumber
                        + " - failed to guess in "
                        + currentRound.getAttemptsUsed()
                        + " attempts";
            }

            roundHistory.add(result);

            System.out.println("\n" + result);

            playAgain = askPlayAgain();

        } while (playAgain);

        displayFinalSummary();

        System.out.println("\nThank you for playing!");

    }

    // Select difficulty
    private Difficulty selectDifficulty() {

        while (true) {

            System.out.println("\nSelect Difficulty:");
            System.out.println("1. Easy (1-50, 10 Attempts)");
            System.out.println("2. Medium (1-100, 7 Attempts)");
            System.out.println("3. Hard (1-200, 5 Attempts)");

            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {

                case "1":
                    return Difficulty.EASY;

                case "2":
                    return Difficulty.MEDIUM;

                case "3":
                    return Difficulty.HARD;

                default:
                    System.out.println("Invalid choice! Select 1, 2 or 3.");
            }
        }
    }

    // Ask user to play again
    private boolean askPlayAgain() {

        while (true) {

            System.out.print("\nDo you want to play again? (Y/N): ");

            String choice = scanner.nextLine().trim();

            if (choice.equalsIgnoreCase("Y")) {
                return true;
            }

            if (choice.equalsIgnoreCase("N")) {
                return false;
            }

            System.out.println("Invalid input! Please enter Y or N.");
        }
    }

    // Display round history
    private void displayFinalSummary() {

        System.out.println("\n========== GAME SUMMARY ==========");

        for (String result : roundHistory) {
            System.out.println(result);
        }

        System.out.println("Total rounds played: " + roundNumber);

        System.out.println("==================================");
    }
}
