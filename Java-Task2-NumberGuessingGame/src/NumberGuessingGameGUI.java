
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.Random;

public class NumberGuessingGameGUI extends JFrame {

    // ===== DARK THEME COLORS =====
    private static final Color BG = new Color(11, 18, 32);
    private static final Color CARD = new Color(17, 28, 46);
    private static final Color PANEL = new Color(25, 39, 59);
    private static final Color GREEN = new Color(16, 185, 129);
    private static final Color GREEN_DARK = new Color(6, 95, 70);
    private static final Color TEXT = new Color(241, 245, 249);
    private static final Color MUTED = new Color(148, 163, 184);
    private static final Color BORDER = new Color(51, 65, 85);
    private static final Color YELLOW = new Color(251, 191, 36);
    private static final Color RED = new Color(248, 113, 113);

    // ===== GAME VARIABLES =====
    private Difficulty selectedDifficulty = Difficulty.MEDIUM;

    private final Random random = new Random();

    private int secretNumber;
    private int attemptsUsed;
    private int roundNumber = 1;

    private boolean gameOver = false;

    // ===== GUI COMPONENTS =====
    private JLabel roundLabel;
    private JLabel rangeLabel;
    private JLabel attemptsLabel;
    private JLabel feedbackLabel;

    private JTextField guessField;

    private JButton easyButton;
    private JButton mediumButton;
    private JButton hardButton;
    private JButton guessButton;
    private JButton restartButton;

    private JProgressBar attemptsBar;

    // ===== CONSTRUCTOR =====
    public NumberGuessingGameGUI() {

        setTitle("GUESS IT - Number Guessing Game");

        setSize(780, 700);
        setMinimumSize(new Dimension(650, 620));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        buildUI();
        updateDifficultyButtons();
        startNewGame();
    }

    // ===== BUILD USER INTERFACE =====
    private void buildUI() {

        JPanel root = new JPanel(new BorderLayout(0, 10));

        root.setBackground(BG);
        root.setBorder(new EmptyBorder(15, 22, 15, 22));

        // HEADER
        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(BG);

        JPanel heading = new JPanel();

        heading.setLayout(
                new BoxLayout(heading, BoxLayout.Y_AXIS)
        );

        heading.setBackground(BG);

        JLabel title = new JLabel("GUESS IT.");

        title.setFont(
                new Font("Segoe UI", Font.BOLD, 30)
        );

        title.setForeground(GREEN);

        JLabel subtitle = new JLabel(
                "The Number Challenge"
        );

        subtitle.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );

        subtitle.setForeground(MUTED);

        heading.add(title);
        heading.add(Box.createVerticalStrut(2));
        heading.add(subtitle);

        JLabel badge = new JLabel(" JAVA GAME ");

        badge.setForeground(GREEN);
        badge.setBackground(GREEN_DARK);
        badge.setOpaque(true);

        badge.setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );

        badge.setBorder(
                new EmptyBorder(9, 12, 9, 12)
        );

        header.add(heading, BorderLayout.WEST);
        header.add(badge, BorderLayout.EAST);

        // MAIN CARD
        JPanel card = new JPanel();

        card.setLayout(
                new BoxLayout(card, BoxLayout.Y_AXIS)
        );

        card.setBackground(CARD);

        card.setBorder(
                new CompoundBorder(
                        new LineBorder(BORDER, 1, true),
                        new EmptyBorder(12, 18, 12, 18)
                )
        );

        // DIFFICULTY HEADING
        JLabel difficultyHeading = new JLabel(
                "Choose Your Difficulty"
        );

        styleLabel(difficultyHeading, 18, TEXT, true);

        difficultyHeading.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        card.add(difficultyHeading);
        card.add(Box.createVerticalStrut(8));

        // DIFFICULTY BUTTONS
        easyButton = createDifficultyButton(
                "<html><center><b>EASY</b><br>1 - 50<br>10 Attempts</center></html>"
        );

        mediumButton = createDifficultyButton(
                "<html><center><b>MEDIUM</b><br>1 - 100<br>7 Attempts</center></html>"
        );

        hardButton = createDifficultyButton(
                "<html><center><b>HARD</b><br>1 - 200<br>5 Attempts</center></html>"
        );

        easyButton.addActionListener(
                e -> changeDifficulty(Difficulty.EASY)
        );

        mediumButton.addActionListener(
                e -> changeDifficulty(Difficulty.MEDIUM)
        );

        hardButton.addActionListener(
                e -> changeDifficulty(Difficulty.HARD)
        );

        JPanel difficultyPanel = new JPanel(
                new GridLayout(1, 3, 10, 0)
        );

        difficultyPanel.setBackground(CARD);

        difficultyPanel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 64)
        );

        difficultyPanel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        difficultyPanel.add(easyButton);
        difficultyPanel.add(mediumButton);
        difficultyPanel.add(hardButton);

        card.add(difficultyPanel);
        card.add(Box.createVerticalStrut(10));

        // INFORMATION CARDS
        JPanel infoPanel = new JPanel(
                new GridLayout(1, 3, 10, 0)
        );

        infoPanel.setBackground(CARD);

        infoPanel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 54)
        );

        infoPanel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        roundLabel = createInfoCard("ROUND", "1");

        rangeLabel = createInfoCard(
                "NUMBER RANGE", "1 - 100"
        );

        attemptsLabel = createInfoCard(
                "ATTEMPTS LEFT", "7"
        );

        infoPanel.add(roundLabel);
        infoPanel.add(rangeLabel);
        infoPanel.add(attemptsLabel);

        card.add(infoPanel);
        card.add(Box.createVerticalStrut(12));

        // GUESS HEADING
        JLabel guessHeading = new JLabel(
                "Enter Your Guess"
        );

        styleLabel(guessHeading, 18, TEXT, true);

        guessHeading.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        card.add(guessHeading);
        card.add(Box.createVerticalStrut(7));

        // GUESS INPUT
        guessField = new JTextField();

        guessField.setFont(
                new Font("Segoe UI", Font.BOLD, 23)
        );

        guessField.setForeground(TEXT);
        guessField.setBackground(PANEL);
        guessField.setCaretColor(GREEN);

        guessField.setHorizontalAlignment(
                JTextField.CENTER
        );

        guessField.setBorder(
                new CompoundBorder(
                        new LineBorder(BORDER, 1, true),
                        new EmptyBorder(6, 12, 6, 12)
                )
        );

        guessField.setPreferredSize(
                new Dimension(400, 40)
        );

        guessField.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 40)
        );

        guessField.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        guessField.addActionListener(
                e -> checkGuess()
        );

        card.add(guessField);
        card.add(Box.createVerticalStrut(8));

        // GUESS BUTTON
        guessButton = createActionButton("GUESS NOW");

        guessButton.addActionListener(
                e -> checkGuess()
        );

        card.add(guessButton);
        card.add(Box.createVerticalStrut(9));

        // FEEDBACK LABEL
        feedbackLabel = new JLabel(
                "Make your first guess!"
        );

        styleLabel(feedbackLabel, 14, GREEN, true);

        feedbackLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        card.add(feedbackLabel);
        card.add(Box.createVerticalStrut(9));

        // ATTEMPTS PROGRESS BAR
        attemptsBar = new JProgressBar();

        attemptsBar.setStringPainted(true);
        attemptsBar.setForeground(GREEN);
        attemptsBar.setBackground(PANEL);

        attemptsBar.setBorderPainted(false);

        attemptsBar.setFont(
                new Font("Segoe UI", Font.BOLD, 11)
        );

        attemptsBar.setPreferredSize(
                new Dimension(400, 20)
        );

        attemptsBar.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 20)
        );

        card.add(attemptsBar);
        card.add(Box.createVerticalStrut(9));

        // RESTART BUTTON
        restartButton = createActionButton("PLAY AGAIN");

        restartButton.setEnabled(false);

        restartButton.addActionListener(e -> {

            roundNumber++;
            startNewGame();

        });

        card.add(restartButton);

        // SCROLLABLE CENTER
        JScrollPane scrollPane = new JScrollPane(card);

        scrollPane.setBorder(null);

        scrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );

        scrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        scrollPane.getViewport().setBackground(BG);

        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // FOOTER
        JLabel footer = new JLabel(
                "JAVA SWING  |  OBJECT ORIENTED PROGRAMMING"
        );

        footer.setForeground(MUTED);

        footer.setFont(
                new Font("Segoe UI", Font.PLAIN, 11)
        );

        footer.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        root.add(header, BorderLayout.NORTH);
        root.add(scrollPane, BorderLayout.CENTER);
        root.add(footer, BorderLayout.SOUTH);

        setContentPane(root);

        getRootPane().setDefaultButton(guessButton);
    }

    // ===== CREATE DIFFICULTY BUTTON =====
    private JButton createDifficultyButton(String text) {

        JButton button = new JButton(text);

        button.setFont(
                new Font("Segoe UI", Font.PLAIN, 12)
        );

        button.setForeground(TEXT);
        button.setBackground(PANEL);

        button.setOpaque(true);
        button.setFocusPainted(false);

        button.setBorder(
                new LineBorder(BORDER, 1, true)
        );

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        return button;
    }

    // ===== CREATE ACTION BUTTON =====
    private JButton createActionButton(String text) {

        JButton button = new JButton(text);

        button.setFont(
                new Font("Segoe UI", Font.BOLD, 13)
        );

        button.setForeground(Color.WHITE);
        button.setBackground(GREEN_DARK);

        button.setOpaque(true);
        button.setFocusPainted(false);

        button.setBorder(
                new LineBorder(GREEN, 1, true)
        );

        button.setPreferredSize(
                new Dimension(400, 36)
        );

        button.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 36)
        );

        button.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        return button;
    }

    // ===== CREATE INFORMATION CARD =====
    private JLabel createInfoCard(
            String heading,
            String value) {

        JLabel label = new JLabel(
                createInfoHTML(heading, value),
                SwingConstants.CENTER
        );

        label.setOpaque(true);
        label.setBackground(PANEL);

        label.setBorder(
                new EmptyBorder(8, 5, 8, 5)
        );

        return label;
    }

    // ===== GENERATE INFORMATION CARD HTML =====
    private String createInfoHTML(
            String heading,
            String value) {

        return "<html><center>"
                + "<font color='#94A3B8'>"
                + heading
                + "</font><br>"
                + "<font color='#F1F5F9' size='5'>"
                + value
                + "</font></center></html>";
    }

    // ===== STYLE LABEL =====
    private void styleLabel(
            JLabel label,
            int size,
            Color color,
            boolean bold) {

        label.setFont(
                new Font(
                        "Segoe UI",
                        bold ? Font.BOLD : Font.PLAIN,
                        size
                )
        );

        label.setForeground(color);
    }

    // ===== CHANGE DIFFICULTY =====
    private void changeDifficulty(Difficulty difficulty) {

        if (selectedDifficulty == difficulty) {
            return;
        }

        selectedDifficulty = difficulty;

        roundNumber = 1;

        updateDifficultyButtons();
        startNewGame();
    }

    // ===== UPDATE DIFFICULTY BUTTON COLORS =====
    private void updateDifficultyButtons() {

        resetDifficultyButton(easyButton);
        resetDifficultyButton(mediumButton);
        resetDifficultyButton(hardButton);

        if (selectedDifficulty == Difficulty.EASY) {

            selectDifficultyButton(easyButton);

        } else if (selectedDifficulty == Difficulty.MEDIUM) {

            selectDifficultyButton(mediumButton);

        } else {

            selectDifficultyButton(hardButton);
        }
    }

    private void resetDifficultyButton(JButton button) {

        button.setBackground(PANEL);

        button.setBorder(
                new LineBorder(BORDER, 1, true)
        );
    }

    private void selectDifficultyButton(JButton button) {

        button.setBackground(GREEN_DARK);

        button.setBorder(
                new LineBorder(GREEN, 2, true)
        );
    }

    // ===== START NEW GAME =====
    private void startNewGame() {

        int maxNumber = selectedDifficulty.getMaxNumber();

        secretNumber = random.nextInt(maxNumber) + 1;

        attemptsUsed = 0;
        gameOver = false;

        guessField.setText("");
        guessField.setEnabled(true);

        guessButton.setEnabled(true);
        restartButton.setEnabled(false);

        roundLabel.setText(
                createInfoHTML(
                        "ROUND",
                        String.valueOf(roundNumber)
                )
        );

        rangeLabel.setText(
                createInfoHTML(
                        "NUMBER RANGE",
                        "1 - " + maxNumber
                )
        );

        updateAttempts();

        showFeedback(
                "Make your first guess!",
                GREEN
        );

        guessField.requestFocusInWindow();
    }

    // ===== CHECK USER GUESS =====
    private void checkGuess() {

        if (gameOver) {
            return;
        }

        String input = guessField.getText().trim();

        if (input.isEmpty()) {

            showFeedback(
                    "Please enter a number!",
                    RED
            );

            return;
        }

        int guess;

        try {

            guess = Integer.parseInt(input);

        } catch (NumberFormatException e) {

            showFeedback(
                    "Enter a valid whole number!",
                    RED
            );

            guessField.setText("");
            return;
        }

        int maxNumber = selectedDifficulty.getMaxNumber();

        if (guess < 1 || guess > maxNumber) {

            showFeedback(
                    "Enter a number between 1 and " + maxNumber,
                    RED
            );

            guessField.setText("");
            return;
        }

        // Only valid guesses consume attempts
        attemptsUsed++;

        guessField.setText("");

        updateAttempts();

        if (guess == secretNumber) {

            gameOver = true;

            showFeedback(
                    "Congratulations! You guessed it!",
                    GREEN
            );

            finishGame();

        } else if (guess < secretNumber) {

            showFeedback(
                    "Too Low! Try a higher number.",
                    YELLOW
            );

            checkRemainingAttempts();

        } else {

            showFeedback(
                    "Too High! Try a lower number.",
                    YELLOW
            );

            checkRemainingAttempts();
        }

        guessField.requestFocusInWindow();
    }

    // ===== UPDATE ATTEMPTS =====
    private void updateAttempts() {

        int maxAttempts = selectedDifficulty.getMaxAttempts();

        int remaining = maxAttempts - attemptsUsed;

        attemptsLabel.setText(
                createInfoHTML(
                        "ATTEMPTS LEFT",
                        String.valueOf(remaining)
                )
        );

        attemptsBar.setMaximum(maxAttempts);
        attemptsBar.setValue(attemptsUsed);

        attemptsBar.setString(
                attemptsUsed + " / " + maxAttempts + " attempts used"
        );
    }

    // ===== CHECK REMAINING ATTEMPTS =====
    private void checkRemainingAttempts() {

        int remaining =
                selectedDifficulty.getMaxAttempts() - attemptsUsed;

        if (remaining == 0) {

            gameOver = true;

            showFeedback(
                    "You Lost! Correct number: " + secretNumber,
                    RED
            );

            finishGame();
        }
    }

    // ===== FINISH GAME =====
    private void finishGame() {

        guessField.setEnabled(false);
        guessButton.setEnabled(false);

        restartButton.setEnabled(true);

        restartButton.requestFocusInWindow();
    }

    // ===== DISPLAY FEEDBACK =====
    private void showFeedback(
            String message,
            Color color) {

        feedbackLabel.setText(message);
        feedbackLabel.setForeground(color);
    }

    // ===== MAIN METHOD =====
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            NumberGuessingGameGUI game =
                    new NumberGuessingGameGUI();

            game.setVisible(true);
        });
    }
}
