
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {

    private final String type;
    private final double amount;
    private final String description;
    private final LocalDateTime dateTime;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    // Constructor
    public Transaction(String type, double amount, String description) {

        this.type = type;
        this.amount = amount;
        this.description = description;
        this.dateTime = LocalDateTime.now();
    }

    // Getter methods
    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    // Display transaction details
    @Override
    public String toString() {

        return String.format(
                "%-12s Rs. %10.2f | %s | %s",
                type,
                amount,
                dateTime.format(FORMATTER),
                description
        );
    }
}
