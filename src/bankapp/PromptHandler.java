package bankapp;

import java.io.IOException;
import java.util.Scanner;

/**
 * Handles user prompts and interaction for bank account operations.
 */
public class PromptHandler {
    
    /**
     * Opens a new bank account based on user input.
     * 
     * @param scanner The scanner for user input
     * @return The newly created bank account
     */
    public static BankAccount openAccount(Scanner scanner) {
        System.out.println("What kind of account would you like to open?");
        System.out.println("1. Checking Account");
        System.out.println("2. Savings Account");
        
        String choice = scanner.nextLine();
        
        if (choice.equals("1")) {
            System.out.print("Enter a name for your checking account: ");
            String accountName = scanner.nextLine();
            return new CheckingAccount(accountName);
        } else if (choice.equals("2")) {
            System.out.print("Enter a name for your savings account: ");
            String accountName = scanner.nextLine();
            
            System.out.print("Enter interest rate (%): ");
            try {
                double interestRate = Double.parseDouble(scanner.nextLine());
                return new SavingsAccount(accountName, interestRate);
            } catch (NumberFormatException e) {
                System.out.println("Invalid interest rate. Using default rate of 1.5%");
                return new SavingsAccount(accountName, 1.5);
            }
        } else {
            System.out.println("Invalid choice. Creating a default checking account.");
            return new CheckingAccount("Default Checking");
        }
    }
    
    /**
     * Manages multiple bank accounts for a user.
     * 
     * @param accountManager The account manager for the user
     * @param scanner The scanner for user input
     */
    public static void manageAccounts(AccountManager accountManager, Scanner scanner) {
        boolean continueManaging = true;
        
        while (continueManaging) {
            System.out.println("\n===== ACCOUNT MANAGEMENT =====");
            System.out.println("1. List All Accounts");
            System.out.println("2. Create New Checking Account");
            System.out.println("3. Create New Savings Account");
            System.out.println("4. Select Account to Manage");
            System.out.println("0. Back to Main Menu");
            System.out.println("=============================");
            
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    accountManager.listAllAccounts();
                    break;
                case "2":
                    createCheckingAccount(accountManager, scanner);
                    break;
                case "3":
                    createSavingsAccount(accountManager, scanner);
                    break;
                case "4":
                    selectAccountToManage(accountManager, scanner);
                    break;
                case "0":
                    continueManaging = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    /**
     * Creates a new checking account.
     * 
     * @param accountManager The account manager for the user
     * @param scanner The scanner for user input
     */
    private static void createCheckingAccount(AccountManager accountManager, Scanner scanner) {
        System.out.println("\n----- Create Checking Account -----");
        System.out.print("Enter a name for your checking account: ");
        String accountName = scanner.nextLine();
        
        if (accountManager.addCheckingAccount(accountName)) {
            System.out.println("Checking account '" + accountName + "' created successfully!");
        }
    }
    
    /**
     * Creates a new savings account.
     * 
     * @param accountManager The account manager for the user
     * @param scanner The scanner for user input
     */
    private static void createSavingsAccount(AccountManager accountManager, Scanner scanner) {
        System.out.println("\n----- Create Savings Account -----");
        System.out.print("Enter a name for your savings account: ");
        String accountName = scanner.nextLine();
        
        double interestRate = 0;
        boolean validInput = false;
        
        while (!validInput) {
            System.out.print("Enter interest rate (%): ");
            try {
                interestRate = Double.parseDouble(scanner.nextLine());
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        
        if (accountManager.addSavingsAccount(accountName, interestRate)) {
            System.out.println("Savings account '" + accountName + "' created successfully!");
        }
    }
    
    /**
     * Selects an account to manage based on its name.
     * 
     * @param accountManager The account manager for the user
     * @param scanner The scanner for user input
     */
    private static void selectAccountToManage(AccountManager accountManager, Scanner scanner) {
        accountManager.listAllAccounts();
        
        if (accountManager.getCheckingAccounts().isEmpty() && accountManager.getSavingsAccounts().isEmpty()) {
            System.out.println("You don't have any accounts to manage. Please create an account first.");
            return;
        }
        
        System.out.print("\nEnter the name of the account you want to manage: ");
        String accountName = scanner.nextLine();
        
        BankAccount selectedAccount = accountManager.getAccountByName(accountName);
        
        if (selectedAccount == null) {
            System.out.println("Account not found. Please check the name and try again.");
            return;
        }
        
        System.out.println("Now managing account: " + selectedAccount.getAccountName());
        commandLoop(accountManager, selectedAccount, scanner);
    }
    
    /**
     * Displays the account's balance.
     * 
     * @param account The bank account
     */
    private static void displayBalance(BankAccount account) {
        System.out.printf("Current balance in %s: $%.2f\n", account.getAccountName(), account.getBalance());
    }
    
    /**
     * Handles the deposit process for an account.
     * 
     * @param account The bank account
     * @param scanner The scanner for user input
     */
    private static void handleDeposit(AccountManager accountManager, BankAccount account, Scanner scanner) {
        AccountStorage accountStorage = new AccountStorage();
        System.out.print("Enter amount to deposit: $");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            if (amount > 0) {
                // Fixed: directly call deposit method instead of DepositHandler
                account.deposit(amount);
                try {
                    accountStorage.recordTransaction(accountManager.getUsername(), account.getAccountName(), "Deposit: $" + amount);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.printf("Deposited $%.2f. New balance: $%.2f\n", amount, account.getBalance());
            } else {
                System.out.println("Deposit amount must be positive.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a valid number.");
        }
    }
    
    /**
     * Handles the withdrawal process for an account.
     * 
     * @param account The bank account
     * @param scanner The scanner for user input
     */
    private static void handleWithdrawal(AccountManager accountManager, BankAccount account, Scanner scanner) {
        AccountStorage accountStorage = new AccountStorage();
        System.out.printf("(Per-transaction limit: $%.2f)\n", account.getWithdrawalLimit());
        System.out.print("Enter amount to withdraw: $");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            if (amount > 0) {
                // Fixed: directly call withdraw method instead of WithdrawHandler
                boolean success = account.withdraw(amount);
                if (success) {
                    try {
                        accountStorage.recordTransaction(accountManager.getUsername(), account.getAccountName(), "Withdraw: $" + amount);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    System.out.printf("Withdrew $%.2f. New balance: $%.2f\n", amount, account.getBalance());
                } else {
                    System.out.println("Insufficient funds for withdrawal.");
                }
            } else {
                System.out.println("Withdrawal amount must be positive.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a valid number.");
        }
    }
    
    /**
     * Handles applying interest to a savings account.
     * 
     * @param account The bank account
     */
    private static void handleInterest(BankAccount account) {
        if (account instanceof SavingsAccount) {
            SavingsAccount savingsAccount = (SavingsAccount) account;
            savingsAccount.applyInterest();
            System.out.printf("Interest applied. New balance: $%.2f\n", account.getBalance());
        } else {
            System.out.println("This account does not earn interest.");
        }
    }
    
    /**
     * Handles ordering checks for a checking account.
     * 
     * @param account The bank account
     */
    private static void handleOrderChecks(BankAccount account) {
        if (account instanceof CheckingAccount) {
            CheckingAccount checkingAccount = (CheckingAccount) account;
            checkingAccount.orderChecks();
        } else {
            System.out.println("You can only order checks for a checking account.");
        }
    }
    
    /**
     * Main command loop for account operations.
     * 
     * @param account The bank account to operate on
     * @param scanner The scanner for user input
     */
    public static void commandLoop(AccountManager accountManager, BankAccount account, Scanner scanner) {
        boolean exitRequested = false;
        
        while (!exitRequested) {
            System.out.println("\n===== ACCOUNT OPERATIONS =====");
            System.out.printf("Account: %s (%s)\n", account.getAccountName(), account.getAccountType());
            System.out.println("1. Check Balance");
            System.out.println("2. Make Deposit");
            System.out.println("3. Make Withdrawal");
            
            // Display account-specific options
            if (account instanceof SavingsAccount) {
                System.out.println("4. Apply Interest");
            } else if (account instanceof CheckingAccount) {
                System.out.println("4. Order Checks");
            }
            
            System.out.println("5. View Transaction History");
            System.out.println("6. View Last 5 Transactions");
            System.out.println("7. Transfer to Another Account");
            System.out.println("0. Return to Account Selection");
            System.out.println("==============================");
            
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    displayBalance(account);
                    break;
                case "2":
                    handleDeposit(accountManager, account, scanner);
                    break;
                case "3":
                    handleWithdrawal(accountManager, account, scanner);
                    break;
                case "4":
                    if (account instanceof SavingsAccount) {
                        handleInterest(account);
                    } else if (account instanceof CheckingAccount) {
                        handleOrderChecks(account);
                    } else {
                        System.out.println("Invalid option for this account type.");
                    }
                    break;
                case "5":
                    AccountStorage accountStorage = new AccountStorage();
                    try {
                        System.out.println("Transaction History:");
                        for (String transaction : accountStorage.getAccountHistory(accountManager.getUsername(), account.getAccountName())) {
                            System.out.println(transaction);
                        }
                    } catch (IOException e) {
                        System.out.println("Error retrieving transaction history: " + e.getMessage());
                    }
                    break;
                case "6":
                    AccountStorage accountStorage2 = new AccountStorage();
                    try {
                        System.out.println("Last 5 Transactions:");
                        for (String transaction : accountStorage2.getLastFiveTransactions(accountManager.getUsername(), account.getAccountName())) {
                            System.out.println(transaction);
                        }
                    } catch (IOException e) {
                        System.out.println("Error retrieving last 5 transactions: " + e.getMessage());
                    }
                    break;
                case "7":
                    TransferHandler.handleTransfer(accountManager, account, scanner);
                    break;
                case "0":
                    exitRequested = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}