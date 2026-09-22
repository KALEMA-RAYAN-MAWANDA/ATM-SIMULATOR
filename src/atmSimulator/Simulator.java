package atmSimulator;
import java.util.Scanner;
public class Simulator {
	static Scanner scanner = new Scanner(System.in);

    static final String CORRECT_PIN = "1234";
    static double balance = 50000; // starting balance (e.g. UGX/whatever currency)

    // Simple transaction log
    static String[] transactions = new String[50];
    static int transactionCount = 0;

    public static void main(String[] args) {

        // WHILE LOOP: keep asking for PIN until correct, or user runs out of tries
        int attempts = 0;
        boolean authenticated = false;

        while (attempts < 3 && !authenticated) {
            System.out.print("Enter your 4-digit PIN: ");
            String pin = scanner.nextLine().trim();

            if (pin.equals(CORRECT_PIN)) {
                authenticated = true;
            } else {
                attempts++;
                System.out.println("Incorrect PIN. Attempts left: " + (3 - attempts));
            }
        }

        if (!authenticated) {
            System.out.println("\nToo many failed attempts. Card retained. Goodbye.");
            scanner.close();
            return;
        }

        System.out.println("\nPIN accepted. Welcome!");
        logTransaction("Login successful");

        int choice;

        // DO-WHILE LOOP: main ATM menu, must show at least once, repeats
        // until the user chooses to exit.
        do {
            printMenu();
            choice = readMenuChoice();

            switch (choice) {
                case 1:
                    checkBalance();
                    break;
                case 2:
                    deposit();
                    break;
                case 3:
                    withdraw();
                    break;
                case 4:
                    printStatement();
                    break;
                case 5:
                    System.out.println("\nThank you for banking with us. Goodbye!");
                    break;
                default:
                    System.out.println("\nInvalid option, please choose 1-5.");
            }

        } while (choice != 5);

        scanner.close();
    }

    static void printMenu() {
        System.out.println("\n===== ATM MENU =====");
        System.out.println("1. Check balance");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Mini statement");
        System.out.println("5. Exit");
        System.out.print("Choose an option: ");
    }

    static int readMenuChoice() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1; // triggers "invalid option" in the switch
        }
    }

    static void checkBalance() {
        // TERNARY: account status based on balance
        String status = (balance < 10000) ? "LOW BALANCE" : "ACTIVE";

        System.out.println("\nCurrent balance: " + balance);
        System.out.println("Account status : " + status);
    }

    static void deposit() {
        System.out.print("\nEnter amount to deposit: ");
        double amount = readAmount();

        // TERNARY: only accept positive amounts
        if (amount <= 0) {
            System.out.println("Deposit amount must be greater than 0.");
            return;
        }

        balance += amount;
        logTransaction("Deposited " + amount);
        System.out.println("Deposit successful. New balance: " + balance);
    }

    static void withdraw() {
        System.out.print("\nEnter amount to withdraw: ");
        double amount = readAmount();

        // TERNARY: decide whether the withdrawal is allowed
        boolean sufficientFunds = (amount > 0 && amount <= balance);
        String result = sufficientFunds ? "APPROVED" : "DECLINED";

        System.out.println("Withdrawal " + result);

        if (sufficientFunds) {
            balance -= amount;
            logTransaction("Withdrew " + amount);
            System.out.println("New balance: " + balance);
        } else {
            System.out.println("Reason: insufficient funds or invalid amount.");
        }
    }

    static double readAmount() {
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1; // treated as invalid in the calling method
        }
    }

    static void logTransaction(String description) {
        if (transactionCount < transactions.length) {
            transactions[transactionCount] = description;
            transactionCount++;
        }
    }

    static void printStatement() {
        System.out.println("\n---- MINI STATEMENT ----");

        if (transactionCount == 0) {
            System.out.println("No transactions yet.");
            return;
        }

        // FOR LOOP: print every logged transaction in order
        for (int i = 0; i < transactionCount; i++) {
            System.out.println((i + 1) + ". " + transactions[i]);
        }
    }
}

