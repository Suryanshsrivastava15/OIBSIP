
# Number Guessing Game

## Project Overview
Number Guessing Game is a console-based Java application in which the computer generates a random number and the player tries to guess it within a limited number of attempts.

The game provides hints such as "Too High" and "Too Low" to help the player find the correct number.

## Technologies Used
- Java
- Object-Oriented Programming (OOP)
- Java Random Class
- ArrayList
- Scanner Class

## Features
- Three difficulty levels: Easy, Medium and Hard.
- Random number generation.
- Higher and lower hints.
- Limited attempts based on difficulty.
- Input validation.
- Play Again functionality.
- Round-wise game history.
- Final score summary.

## Difficulty Levels

| Level | Number Range | Maximum Attempts |
|---|---|---|
| Easy | 1–50 | 10 |
| Medium | 1–100 | 7 |
| Hard | 1–200 | 5 |

## Project Structure

```text
Java-Task2-NumberGuessingGame/
│
├── src/
│   ├── Main.java
│   ├── Difficulty.java
│   ├── GameRound.java
│   └── NumberGuessingGame.java
│
├── out/
└── README.md
```

## How to Run

### Step 1: Compile the Java files

```bash
javac -d out src/*.java
```

### Step 2: Run the application

```bash
java -cp out Main
```

## Concepts Implemented
- Classes and Objects
- Constructors
- Encapsulation
- Enum
- Conditional Statements
- Loops
- ArrayList
- Input Validation
- Random Number Generation

## Internship
Oasis Infobyte – Java Development Internship

## Developed By
Suryansh Srivastava
