
import java.util.Random;
import java.util.Scanner;

public class GameRound {

    private final int secretNumber;
    private final int maxNumber;
    private final int maxAttempts;

    private int attemptsUsed;
    private boolean won;

    // Constructor
    public GameRound(Difficulty difficulty) {

        this.maxNumber = difficulty.getMaxNumber();
        this.maxAttempts = difficulty.getMaxAttempts();

        Random random = new Random();

        this.secretNumber = random.nextInt(maxNumber) + 1;

        this.attemptsUsed = 0;
        this.won = false;
    }

    // Start one round
    public void play(Scanner scanner) {

        System.out.println("\nGuess a number between 1 and " + maxNumber);

        while (attemptsUsed < maxAttempts && !won) {

            System.out.print("Attempt " + (attemptsUsed + 1)
                    + "/" + maxAttempts + " - Enter your guess: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Please enter a valid whole number.");
                scanner.next();
                continue;
            }

            int guess = scanner.nextInt();

            if (guess < 1 || guess > maxNumber) {
                System.out.println("Enter a number within the given range.");
                continue;
            }

            attemptsUsed++;

            if (guess == secretNumber) {

                won = true;

                System.out.println("Congratulations! Correct Guess!");

            } else if (guess < secretNumber) {

                System.out.println("Too Low! Try a higher number.");

            } else {

                System.out.println("Too High! Try a lower number.");
            }

            if (!won && attemptsUsed < maxAttempts) {
                System.out.println("Attempts remaining: "
                        + (maxAttempts - attemptsUsed));
            }
        }

        if (!won) {
            System.out.println("\nYou Lost!");
            System.out.println("The correct number was: " + secretNumber);
        }
    }

    // Getter methods

    public int getAttemptsUsed() {
        return attemptsUsed;
    }

    public boolean isWon() {
        return won;
    }
}
