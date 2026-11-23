
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class ATMGUI extends JFrame {

    // ===== DARK THEME COLORS =====
    private static final Color BG = new Color(11, 18, 32);
    private static final Color CARD = new Color(17, 28, 46);
    private static final Color PANEL = new Color(25, 39, 59);
    private static final Color GREEN = new Color(16, 185, 129);
    private static final Color GREEN_DARK = new Color(6, 95, 70);
    private static final Color TEXT = new Color(241, 245, 249);
    private static final Color MUTED = new Color(148, 163, 184);
    private static final Color BORDER = new Color(51, 65, 85);
    private static final Color RED = new Color(248, 113, 113);
    private static final Color YELLOW = new Color(251, 191, 36);

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    // ===== BANK OBJECTS =====
    private final Bank bank;
    private Account currentAccount;

    // ===== LOGIN VARIABLES =====
    private int loginAttempts = 0;
    private final int MAX_LOGIN_ATTEMPTS = 3;

    private JTextField userIdField;
    private JPasswordField pinField;
    private JButton loginButton;
    private JLabel loginFeedback;

    // ===== DASHBOARD VARIABLES =====
    private CardLayout rootCardLayout;
    private JPanel rootPanel;

    private CardLayout pageLayout;
    private JPanel dashboardPages;

    private JLabel accountHeaderLabel;
    private JLabel balanceLabel;
    private JLabel accountIdLabel;
    private JLabel transactionCountLabel;

    // ===== TRANSACTION FORM VARIABLES =====
    private JTextField depositAmountField;
    private JTextField withdrawAmountField;
    private JTextField receiverIdField;
    private JTextField transferAmountField;

    private JLabel depositFeedback;
    private JLabel withdrawFeedback;
    private JLabel transferFeedback;

    // ===== TRANSACTION TABLE =====
    private DefaultTableModel tableModel;
    private JTable transactionTable;

    // ===== CONSTRUCTOR =====
    public ATMGUI() {

        bank = new Bank();

        setTitle("OASIS BANK - ATM MANAGEMENT SYSTEM");

        setSize(1100, 720);
        setMinimumSize(new Dimension(850, 600));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        rootCardLayout = new CardLayout();
        rootPanel = new JPanel(rootCardLayout);

        rootPanel.add(buildLoginPanel(), "LOGIN");
        rootPanel.add(buildDashboard(), "DASHBOARD");

        setContentPane(rootPanel);
    }

    // ===== LOGIN SCREEN =====
    private JPanel buildLoginPanel() {

        JPanel background = new JPanel(new GridBagLayout());
        background.setBackground(BG);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD);

        card.setBorder(
                new CompoundBorder(
                        new LineBorder(BORDER, 1, true),
                        new EmptyBorder(35, 40, 35, 40)
                )
        );

        card.setPreferredSize(new Dimension(430, 490));

        // Brand title
        JLabel logo = new JLabel("OASIS BANK");

        logo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        logo.setForeground(GREEN);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("SECURE ATM BANKING");

        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(logo);
        card.add(Box.createVerticalStrut(6));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(35));

        JLabel loginTitle = new JLabel("Welcome Back");

        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        loginTitle.setForeground(TEXT);
        loginTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel loginSubtitle = new JLabel(
                "Login to access your bank account"
        );

        loginSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        loginSubtitle.setForeground(MUTED);
        loginSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(loginTitle);
        card.add(Box.createVerticalStrut(5));
        card.add(loginSubtitle);
        card.add(Box.createVerticalStrut(25));

        // User ID
        userIdField = createTextField();

        addInputField(card, "USER ID", userIdField);

        card.add(Box.createVerticalStrut(15));

        // PIN
        pinField = new JPasswordField();
        styleTextField(pinField);

        pinField.setEchoChar('*');

        addInputField(card, "ATM PIN", pinField);

        card.add(Box.createVerticalStrut(22));

        // Login button
        loginButton = createPrimaryButton("LOGIN TO ACCOUNT");

        loginButton.addActionListener(e -> handleLogin());

        card.add(loginButton);

        card.add(Box.createVerticalStrut(12));

        // Feedback
        loginFeedback = createFeedbackLabel(
                "Enter your credentials to continue."
        );

        card.add(loginFeedback);

        card.add(Box.createVerticalStrut(18));

        JLabel demoInfo = new JLabel(
                "<html><center>"
                        + "Demo Account: 1001 | PIN: 1234"
                        + "<br>Second Account: 1002 | PIN: 5678"
                        + "</center></html>"
        );

        demoInfo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        demoInfo.setForeground(MUTED);
        demoInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(demoInfo);

        background.add(card);

        getRootPane().setDefaultButton(loginButton);

        return background;
    }

    // ===== HANDLE LOGIN =====
    private void handleLogin() {

        if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
            return;
        }

        String userId = userIdField.getText().trim();

        String pin = new String(pinField.getPassword()).trim();

        if (userId.isEmpty() || pin.isEmpty()) {

            showFeedback(
                    loginFeedback,
                    "Please enter User ID and PIN.",
                    RED
            );

            return;
        }

        Account account = bank.authenticate(userId, pin);

        if (account != null) {

            currentAccount = account;

            loginAttempts = 0;

            pinField.setText("");

            accountHeaderLabel.setText(
                    "ACCOUNT: " + currentAccount.getUserId()
            );

            updateDashboard();

            rootCardLayout.show(rootPanel, "DASHBOARD");

        } else {

            loginAttempts++;

            int remaining = MAX_LOGIN_ATTEMPTS - loginAttempts;

            pinField.setText("");

            if (remaining > 0) {

                showFeedback(
                        loginFeedback,
                        "Invalid credentials! "
                                + remaining + " attempts remaining.",
                        RED
                );

            } else {

                showFeedback(
                        loginFeedback,
                        "Access blocked! Restart application to try again.",
                        RED
                );

                loginButton.setEnabled(false);
                userIdField.setEnabled(false);
                pinField.setEnabled(false);
            }
        }
    }

    // ===== DASHBOARD =====
    private JPanel buildDashboard() {

        JPanel dashboard = new JPanel(new BorderLayout());
        dashboard.setBackground(BG);

        // Header
        dashboard.add(buildHeader(), BorderLayout.NORTH);

        // Sidebar
        dashboard.add(buildSidebar(), BorderLayout.WEST);

        // Main pages
        pageLayout = new CardLayout();

        dashboardPages = new JPanel(pageLayout);
        dashboardPages.setBackground(BG);

        dashboardPages.add(buildOverviewPage(), "OVERVIEW");
        dashboardPages.add(buildDepositPage(), "DEPOSIT");
        dashboardPages.add(buildWithdrawPage(), "WITHDRAW");
        dashboardPages.add(buildTransferPage(), "TRANSFER");
        dashboardPages.add(buildHistoryPage(), "HISTORY");

        dashboard.add(dashboardPages, BorderLayout.CENTER);

        return dashboard;
    }

    // ===== HEADER =====
    private JPanel buildHeader() {

        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(CARD);

        header.setBorder(
                new CompoundBorder(
                        new MatteBorder(0, 0, 1, 0, BORDER),
                        new EmptyBorder(15, 25, 15, 25)
                )
        );

        JLabel brand = new JLabel("OASIS BANK");

        brand.setFont(new Font("Segoe UI", Font.BOLD, 22));
        brand.setForeground(GREEN);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBackground(CARD);

        accountHeaderLabel = new JLabel("ACCOUNT");

        accountHeaderLabel.setForeground(TEXT);
        accountHeaderLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );

        JButton logoutButton = createSecondaryButton("LOGOUT");

        logoutButton.addActionListener(e -> logout());

        rightPanel.add(accountHeaderLabel);
        rightPanel.add(logoutButton);

        header.add(brand, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);

        return header;
    }

    // ===== SIDEBAR =====
    private JPanel buildSidebar() {

        JPanel sidebar = new JPanel();

        sidebar.setLayout(
                new BoxLayout(sidebar, BoxLayout.Y_AXIS)
        );

        sidebar.setBackground(CARD);

        sidebar.setBorder(
                new CompoundBorder(
                        new MatteBorder(0, 0, 0, 1, BORDER),
                        new EmptyBorder(25, 12, 20, 12)
                )
        );

        sidebar.setPreferredSize(new Dimension(210, 0));

        JLabel menuTitle = new JLabel("MAIN MENU");

        menuTitle.setForeground(MUTED);
        menuTitle.setFont(
                new Font("Segoe UI", Font.BOLD, 11)
        );

        menuTitle.setBorder(
                new EmptyBorder(0, 12, 15, 0)
        );

        sidebar.add(menuTitle);

        sidebar.add(createNavigationButton(
                "Dashboard", "OVERVIEW"
        ));

        sidebar.add(Box.createVerticalStrut(8));

        sidebar.add(createNavigationButton(
                "Deposit Money", "DEPOSIT"
        ));

        sidebar.add(Box.createVerticalStrut(8));

        sidebar.add(createNavigationButton(
                "Withdraw Money", "WITHDRAW"
        ));

        sidebar.add(Box.createVerticalStrut(8));

        sidebar.add(createNavigationButton(
                "Transfer Money", "TRANSFER"
        ));

        sidebar.add(Box.createVerticalStrut(8));

        sidebar.add(createNavigationButton(
                "Transaction History", "HISTORY"
        ));

        sidebar.add(Box.createVerticalGlue());

        JLabel footer = new JLabel(
                "<html><center>OASIS BANK<br>JAVA ATM SYSTEM</center></html>"
        );

        footer.setForeground(MUTED);
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(footer);

        return sidebar;
    }

    // ===== NAVIGATION BUTTON =====
    private JButton createNavigationButton(
            String text,
            String page) {

        JButton button = new JButton(text);

        button.setForeground(TEXT);
        button.setBackground(PANEL);

        button.setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );

        button.setFocusPainted(false);
        button.setOpaque(true);

        button.setBorder(
                new EmptyBorder(13, 12, 13, 12)
        );

        button.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        button.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 45)
        );

        button.setAlignmentX(Component.LEFT_ALIGNMENT);

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        button.addActionListener(e -> showPage(page));

        return button;
    }

    // ===== SHOW DASHBOARD PAGE =====
    private void showPage(String page) {

        if (page.equals("HISTORY")) {
            refreshTransactionHistory();
        }

        if (page.equals("OVERVIEW")) {
            updateOverview();
        }

        pageLayout.show(dashboardPages, page);
    }

    // ===== OVERVIEW PAGE =====
    private JPanel buildOverviewPage() {

        JPanel page = new JPanel();

        page.setLayout(
                new BoxLayout(page, BoxLayout.Y_AXIS)
        );

        page.setBackground(BG);

        page.setBorder(
                new EmptyBorder(30, 35, 30, 35)
        );

        JLabel heading = new JLabel("Dashboard Overview");

        heading.setFont(
                new Font("Segoe UI", Font.BOLD, 26)
        );

        heading.setForeground(TEXT);

        JLabel subtitle = new JLabel(
                "Manage your account and banking activities."
        );

        subtitle.setForeground(MUTED);

        subtitle.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );

        page.add(heading);
        page.add(Box.createVerticalStrut(5));
        page.add(subtitle);
        page.add(Box.createVerticalStrut(25));

        // Balance card
        JPanel balanceCard = new JPanel();

        balanceCard.setLayout(
                new BoxLayout(balanceCard, BoxLayout.Y_AXIS)
        );

        balanceCard.setBackground(GREEN_DARK);

        balanceCard.setBorder(
                new EmptyBorder(23, 25, 23, 25)
        );

        balanceCard.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 150)
        );

        JLabel balanceTitle = new JLabel("AVAILABLE BALANCE");

        balanceTitle.setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );

        balanceTitle.setForeground(new Color(209, 250, 229));

        balanceLabel = new JLabel("Rs. 0.00");

        balanceLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 33)
        );

        balanceLabel.setForeground(Color.WHITE);

        balanceCard.add(balanceTitle);
        balanceCard.add(Box.createVerticalStrut(10));
        balanceCard.add(balanceLabel);

        page.add(balanceCard);
        page.add(Box.createVerticalStrut(22));

        // Account information cards
        JPanel informationPanel = new JPanel(
                new GridLayout(1, 2, 15, 0)
        );

        informationPanel.setBackground(BG);

        accountIdLabel = new JLabel("1001");

        transactionCountLabel = new JLabel("0");

        informationPanel.add(
                createMetricCard(
                        "ACCOUNT ID",
                        accountIdLabel
                )
        );

        informationPanel.add(
                createMetricCard(
                        "TOTAL TRANSACTIONS",
                        transactionCountLabel
                )
        );

        informationPanel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 110)
        );

        page.add(informationPanel);
        page.add(Box.createVerticalStrut(25));

        JLabel quickTitle = new JLabel("QUICK ACTIONS");

        quickTitle.setFont(
                new Font("Segoe UI", Font.BOLD, 15)
        );

        quickTitle.setForeground(TEXT);

        page.add(quickTitle);
        page.add(Box.createVerticalStrut(15));

        JPanel quickActions = new JPanel(
                new GridLayout(1, 3, 12, 0)
        );

        quickActions.setBackground(BG);

        JButton deposit = createPrimaryButton("DEPOSIT");

        deposit.addActionListener(
                e -> showPage("DEPOSIT")
        );

        JButton withdraw = createPrimaryButton("WITHDRAW");

        withdraw.addActionListener(
                e -> showPage("WITHDRAW")
        );

        JButton transfer = createPrimaryButton("TRANSFER");

        transfer.addActionListener(
                e -> showPage("TRANSFER")
        );

        quickActions.add(deposit);
        quickActions.add(withdraw);
        quickActions.add(transfer);

        page.add(quickActions);

        return page;
    }

    // ===== CREATE METRIC CARD =====
    private JPanel createMetricCard(
            String title,
            JLabel valueLabel) {

        JPanel card = new JPanel();

        card.setLayout(
                new BoxLayout(card, BoxLayout.Y_AXIS)
        );

        card.setBackground(PANEL);

        card.setBorder(
                new EmptyBorder(18, 20, 18, 20)
        );

        JLabel heading = new JLabel(title);

        heading.setFont(
                new Font("Segoe UI", Font.BOLD, 11)
        );

        heading.setForeground(MUTED);

        valueLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 22)
        );

        valueLabel.setForeground(TEXT);

        card.add(heading);
        card.add(Box.createVerticalStrut(8));
        card.add(valueLabel);

        return card;
    }

    // ===== UPDATE OVERVIEW =====
    private void updateOverview() {

        if (currentAccount == null) {
            return;
        }

        balanceLabel.setText(
                String.format(
                        "Rs. %,.2f",
                        currentAccount.getBalance()
                )
        );

        accountIdLabel.setText(
                currentAccount.getUserId()
        );

        transactionCountLabel.setText(
                String.valueOf(
                        currentAccount.getTransactions().size()
                )
        );
    }

    // ===== DEPOSIT PAGE =====
    private JPanel buildDepositPage() {

        depositAmountField = createTextField();

        depositFeedback = createFeedbackLabel(
                "Enter the amount you want to deposit."
        );

        return buildFormPage(
                "Deposit Money",
                "Add money to your bank account.",
                "DEPOSIT AMOUNT",
                depositAmountField,
                null,
                null,
                "CONFIRM DEPOSIT",
                depositFeedback,
                this::performDeposit
        );
    }

    // ===== WITHDRAW PAGE =====
    private JPanel buildWithdrawPage() {

        withdrawAmountField = createTextField();

        withdrawFeedback = createFeedbackLabel(
                "Enter the amount you want to withdraw."
        );

        return buildFormPage(
                "Withdraw Money",
                "Withdraw money from your account.",
                "WITHDRAWAL AMOUNT",
                withdrawAmountField,
                null,
                null,
                "CONFIRM WITHDRAWAL",
                withdrawFeedback,
                this::performWithdrawal
        );
    }

    // ===== TRANSFER PAGE =====
    private JPanel buildTransferPage() {

        receiverIdField = createTextField();

        transferAmountField = createTextField();

        transferFeedback = createFeedbackLabel(
                "Enter recipient account and transfer amount."
        );

        return buildFormPage(
                "Transfer Money",
                "Transfer funds to another bank account.",
                "RECIPIENT ACCOUNT ID",
                receiverIdField,
                "TRANSFER AMOUNT",
                transferAmountField,
                "CONFIRM TRANSFER",
                transferFeedback,
                this::performTransfer
        );
    }

    // ===== COMMON FORM PAGE =====
    private JPanel buildFormPage(
            String title,
            String description,
            String firstLabel,
            JTextField firstField,
            String secondLabel,
            JTextField secondField,
            String buttonText,
            JLabel feedback,
            Runnable operation) {

        JPanel background = new JPanel(new GridBagLayout());

        background.setBackground(BG);

        JPanel card = new JPanel();

        card.setLayout(
                new BoxLayout(card, BoxLayout.Y_AXIS)
        );

        card.setBackground(CARD);

        card.setBorder(
                new CompoundBorder(
                        new LineBorder(BORDER, 1, true),
                        new EmptyBorder(28, 32, 28, 32)
                )
        );

        card.setPreferredSize(
                new Dimension(550, secondField == null ? 390 : 450)
        );

        JLabel heading = new JLabel(title);

        heading.setFont(
                new Font("Segoe UI", Font.BOLD, 25)
        );

        heading.setForeground(TEXT);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel(description);

        subtitle.setFont(
                new Font("Segoe UI", Font.PLAIN, 12)
        );

        subtitle.setForeground(MUTED);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(heading);
        card.add(Box.createVerticalStrut(6));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(30));

        addInputField(card, firstLabel, firstField);

        if (secondField != null) {

            card.add(Box.createVerticalStrut(18));

            addInputField(card, secondLabel, secondField);
        }

        card.add(Box.createVerticalStrut(25));

        JButton actionButton = createPrimaryButton(buttonText);

        actionButton.addActionListener(e -> operation.run());

        card.add(actionButton);

        card.add(Box.createVerticalStrut(17));

        card.add(feedback);

        background.add(card);

        return background;
    }

    // ===== PERFORM DEPOSIT =====
    private void performDeposit() {

        double amount = parseAmount(
                depositAmountField,
                depositFeedback
        );

        if (amount <= 0) {
            return;
        }

        if (currentAccount.deposit(amount)) {

            showFeedback(
                    depositFeedback,
                    String.format(
                            "Deposit successful! New balance: Rs. %,.2f",
                            currentAccount.getBalance()
                    ),
                    GREEN
            );

            depositAmountField.setText("");

            updateDashboard();
        }
    }

    // ===== PERFORM WITHDRAWAL =====
    private void performWithdrawal() {

        double amount = parseAmount(
                withdrawAmountField,
                withdrawFeedback
        );

        if (amount <= 0) {
            return;
        }

        if (amount > currentAccount.getBalance()) {

            showFeedback(
                    withdrawFeedback,
                    "Insufficient funds in your account!",
                    RED
            );

            return;
        }

        if (currentAccount.withdraw(amount)) {

            showFeedback(
                    withdrawFeedback,
                    String.format(
                            "Withdrawal successful! Remaining: Rs. %,.2f",
                            currentAccount.getBalance()
                    ),
                    GREEN
            );

            withdrawAmountField.setText("");

            updateDashboard();
        }
    }

    // ===== PERFORM TRANSFER =====
    private void performTransfer() {

        String receiverId = receiverIdField.getText().trim();

        if (receiverId.isEmpty()) {

            showFeedback(
                    transferFeedback,
                    "Please enter recipient account ID.",
                    RED
            );

            return;
        }

        Account receiver = bank.getAccount(receiverId);

        if (receiver == null) {

            showFeedback(
                    transferFeedback,
                    "Recipient account not found!",
                    RED
            );

            return;
        }

        if (receiverId.equals(currentAccount.getUserId())) {

            showFeedback(
                    transferFeedback,
                    "Cannot transfer to your own account!",
                    RED
            );

            return;
        }

        double amount = parseAmount(
                transferAmountField,
                transferFeedback
        );

        if (amount <= 0) {
            return;
        }

        if (amount > currentAccount.getBalance()) {

            showFeedback(
                    transferFeedback,
                    "Insufficient funds!",
                    RED
            );

            return;
        }

        boolean success = bank.transfer(
                currentAccount.getUserId(),
                receiverId,
                amount
        );

        if (success) {

            showFeedback(
                    transferFeedback,
                    "Transfer successful! Remaining balance: Rs. "
                            + String.format(
                                    "%,.2f",
                                    currentAccount.getBalance()
                            ),
                    GREEN
            );

            receiverIdField.setText("");
            transferAmountField.setText("");

            updateDashboard();
        } else {

            showFeedback(
                    transferFeedback,
                    "Transfer failed. Please try again.",
                    RED
            );
        }
    }

    // ===== VALIDATE AMOUNT =====
    private double parseAmount(
            JTextField field,
            JLabel feedback) {

        String input = field.getText().trim();

        if (input.isEmpty()) {

            showFeedback(
                    feedback,
                    "Please enter an amount.",
                    RED
            );

            return -1;
        }

        try {

            double amount = Double.parseDouble(input);

            if (!Double.isFinite(amount) || amount <= 0) {

                showFeedback(
                        feedback,
                        "Enter a valid positive amount.",
                        RED
                );

                return -1;
            }

            return amount;

        } catch (NumberFormatException e) {

            showFeedback(
                    feedback,
                    "Invalid amount! Enter a numeric value.",
                    RED
            );

            return -1;
        }
    }

    // ===== TRANSACTION HISTORY PAGE =====
    private JPanel buildHistoryPage() {

        JPanel page = new JPanel(
                new BorderLayout(0, 18)
        );

        page.setBackground(BG);

        page.setBorder(
                new EmptyBorder(28, 30, 28, 30)
        );

        JPanel headingPanel = new JPanel(new BorderLayout());

        headingPanel.setBackground(BG);

        JPanel titles = new JPanel();

        titles.setLayout(
                new BoxLayout(titles, BoxLayout.Y_AXIS)
        );

        titles.setBackground(BG);

        JLabel title = new JLabel("Transaction History");

        title.setFont(
                new Font("Segoe UI", Font.BOLD, 25)
        );

        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
                "View all transactions made from your account."
        );

        subtitle.setFont(
                new Font("Segoe UI", Font.PLAIN, 12)
        );

        subtitle.setForeground(MUTED);

        titles.add(title);
        titles.add(Box.createVerticalStrut(5));
        titles.add(subtitle);

        JButton refreshButton = createSecondaryButton("REFRESH");

        refreshButton.addActionListener(
                e -> refreshTransactionHistory()
        );

        headingPanel.add(titles, BorderLayout.WEST);
        headingPanel.add(refreshButton, BorderLayout.EAST);

        // TABLE
        String[] columns = {
                "DATE & TIME",
                "TYPE",
                "AMOUNT",
                "DESCRIPTION"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        transactionTable = new JTable(tableModel);

        transactionTable.setRowHeight(36);

        transactionTable.setBackground(PANEL);
        transactionTable.setForeground(TEXT);

        transactionTable.setGridColor(BORDER);

        transactionTable.setSelectionBackground(GREEN_DARK);
        transactionTable.setSelectionForeground(Color.WHITE);

        transactionTable.setFont(
                new Font("Segoe UI", Font.PLAIN, 12)
        );

        transactionTable.setFillsViewportHeight(true);

        transactionTable.setAutoResizeMode(
                JTable.AUTO_RESIZE_ALL_COLUMNS
        );

        transactionTable.getTableHeader().setBackground(CARD);
        transactionTable.getTableHeader().setForeground(GREEN);

        transactionTable.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 11)
        );

        JScrollPane scrollPane = new JScrollPane(transactionTable);

        scrollPane.setBorder(
                new LineBorder(BORDER, 1, true)
        );

        scrollPane.getViewport().setBackground(PANEL);

        page.add(headingPanel, BorderLayout.NORTH);
        page.add(scrollPane, BorderLayout.CENTER);

        return page;
    }

    // ===== REFRESH TRANSACTION HISTORY =====
    private void refreshTransactionHistory() {

        if (tableModel == null || currentAccount == null) {
            return;
        }

        tableModel.setRowCount(0);

        for (Transaction transaction :
                currentAccount.getTransactions()) {

            tableModel.addRow(new Object[]{

                    transaction.getDateTime().format(DATE_FORMAT),

                    transaction.getType(),

                    String.format(
                            "Rs. %,.2f",
                            transaction.getAmount()
                    ),

                    transaction.getDescription()
            });
        }
    }

    // ===== UPDATE ALL DASHBOARD DATA =====
    private void updateDashboard() {

        if (currentAccount == null) {
            return;
        }

        updateOverview();
        refreshTransactionHistory();
    }

    // ===== LOGOUT =====
    private void logout() {

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {

            currentAccount = null;

            loginAttempts = 0;

            userIdField.setText("");
            pinField.setText("");

            loginButton.setEnabled(true);
            userIdField.setEnabled(true);
            pinField.setEnabled(true);

            showFeedback(
                    loginFeedback,
                    "Please login to continue.",
                    MUTED
            );

            rootCardLayout.show(rootPanel, "LOGIN");
        }
    }

    // ===== CREATE TEXT FIELD =====
    private JTextField createTextField() {

        JTextField field = new JTextField();

        styleTextField(field);

        return field;
    }

    // ===== STYLE TEXT FIELD =====
    private void styleTextField(JTextField field) {

        field.setFont(
                new Font("Segoe UI", Font.PLAIN, 15)
        );

        field.setForeground(TEXT);
        field.setBackground(PANEL);
        field.setCaretColor(GREEN);

        field.setBorder(
                new CompoundBorder(
                        new LineBorder(BORDER, 1, true),
                        new EmptyBorder(11, 12, 11, 12)
                )
        );

        field.setPreferredSize(
                new Dimension(350, 43)
        );

        field.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 43)
        );
    }

    // ===== ADD INPUT FIELD TO FORM =====
    private void addInputField(
            JPanel parent,
            String labelText,
            JTextField field) {

        JPanel fieldPanel = new JPanel();

        fieldPanel.setLayout(
                new BoxLayout(fieldPanel, BoxLayout.Y_AXIS)
        );

        fieldPanel.setBackground(CARD);

        fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(labelText);

        label.setFont(
                new Font("Segoe UI", Font.BOLD, 11)
        );

        label.setForeground(MUTED);

        label.setBorder(
                new EmptyBorder(0, 0, 8, 0)
        );

        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        fieldPanel.add(label);
        fieldPanel.add(field);

        fieldPanel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 75)
        );

        parent.add(fieldPanel);
    }

    // ===== CREATE FEEDBACK LABEL =====
    private JLabel createFeedbackLabel(String message) {

        JLabel label = new JLabel(message);

        label.setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );

        label.setForeground(MUTED);

        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        return label;
    }

    // ===== SHOW FEEDBACK =====
    private void showFeedback(
            JLabel label,
            String message,
            Color color) {

        label.setText(message);
        label.setForeground(color);
    }

    // ===== PRIMARY BUTTON =====
    private JButton createPrimaryButton(String text) {

        JButton button = new JButton(text);

        button.setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );

        button.setForeground(Color.WHITE);
        button.setBackground(GREEN_DARK);

        button.setOpaque(true);
        button.setFocusPainted(false);

        button.setBorder(
                new LineBorder(GREEN, 1, true)
        );

        button.setPreferredSize(
                new Dimension(250, 45)
        );

        button.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 45)
        );

        button.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        return button;
    }

    // ===== SECONDARY BUTTON =====
    private JButton createSecondaryButton(String text) {

        JButton button = new JButton(text);

        button.setFont(
                new Font("Segoe UI", Font.BOLD, 11)
        );

        button.setForeground(GREEN);
        button.setBackground(PANEL);

        button.setOpaque(true);
        button.setFocusPainted(false);

        button.setBorder(
                new LineBorder(BORDER, 1, true)
        );

        button.setPreferredSize(
                new Dimension(110, 36)
        );

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        return button;
    }

    // ===== MAIN METHOD =====
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            ATMGUI atm = new ATMGUI();

            atm.setVisible(true);
        });
    }
}
