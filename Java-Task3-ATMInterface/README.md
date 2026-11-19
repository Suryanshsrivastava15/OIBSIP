
# ATM Interface Using Java

## Project Overview
ATM Interface is a console-based banking simulation developed using Java and Object-Oriented Programming (OOP).

It allows users to authenticate using their User ID and PIN and perform basic banking operations such as deposits, withdrawals, money transfers and transaction history viewing.

## Technologies Used
- Java
- Object-Oriented Programming (OOP)
- ArrayList
- HashMap
- Scanner
- LocalDateTime

## Features
- User authentication with User ID and PIN.
- Maximum three incorrect login attempts.
- Deposit money.
- Withdraw money.
- Transfer money between accounts.
- View transaction history.
- Insufficient balance validation.
- Invalid input handling.
- Transaction date and time tracking.

## Demo Login Credentials

| User ID | PIN | Initial Balance |
|---|---|---|
| 1001 | 1234 | ₹10,000 |
| 1002 | 5678 | ₹5,000 |

These are predefined demo accounts for testing purposes.

## Project Structure

```text
Java-Task3-ATMInterface/
├── src/
│   ├── Main.java
│   ├── ATM.java
│   ├── Bank.java
│   ├── Account.java
│   └── Transaction.java
├── out/
└── README.md
```

## How to Run

### Step 1: Open the project directory

```bash
cd Java-Task3-ATMInterface
```

### Step 2: Compile Java files

```bash
javac -d out src/*.java
```

### Step 3: Run the application

```bash
java -cp out Main
```

## OOP Concepts Implemented
- Classes and Objects
- Encapsulation
- Constructors
- Abstraction
- Composition
- ArrayList
- HashMap

## Testing
- Successful login.
- Three incorrect PIN attempts.
- Deposit and withdrawal operations.
- Money transfer between accounts.
- Transaction history.
- Insufficient balance handling.
- Invalid amount validation.

## Important Note
This is an educational ATM simulation. Account details and transactions are stored in memory and reset when the application restarts. It is not connected to a real banking system.

## Internship
Oasis Infobyte – Java Development Internship

## Developed By
Suryansh Srivastava
