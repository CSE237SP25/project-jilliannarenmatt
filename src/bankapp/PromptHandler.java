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
            
            System.out.print("Would you like to enable overdraft protection? (yes/no): ");
            String overdraftChoice = scanner.nextLine().trim().toLowerCase();
            
            CheckingAccount account = new CheckingAccount(accountName);
            
            if (overdraftChoice.equals("yes")) {
                System.out.print("Enter overdraft limit ($): ");
                try {
                    double overdraftLimit = Double.parseDouble(scanner.nextLine());
                    if (overdraftLimit > 0) {
                        account.setOverdraftLimit(overdraftLimit);
                        
                        System.out.print("Enter overdraft interest rate (%): ");
                        double interestRate = Double.parseDouble(scanner.nextLine());
                        if (interestRate > 0) {
                            account.setOverdraftInterestRate(interestRate);
                        } else {
                            System.out.println("Invalid interest rate. Using default rate of 15%");
                        }
                    } else {
                        System.out.println("Invalid overdraft limit. Overdraft protection not enabled.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Overdraft protection not enabled.");
                }
            }
            
            return account;
            
        } else if (choice.equals("2")) {
            System.out.print("Enter a name for your savings account: ");
            String accountName = scanner.nextLine();
            
            double interestRate = 1.5; // Default rate
            try {
                System.out.print("Enter interest rate (%): ");
                interestRate = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid interest rate. Using default rate of 1.5%");
            }
            
            SavingsAccount account = new SavingsAccount(accountName, interestRate);
            
            System.out.print("Would you like to enable overdraft protection? (yes/no): ");
            String overdraftChoice = scanner.nextLine().trim().toLowerCase();
            
            if (overdraftChoice.equals("yes")) {
                System.out.print("Enter overdraft limit ($): ");
                try {
                    double overdraftLimit = Double.parseDouble(scanner.nextLine());
                    if (overdraftLimit > 0) {
                        account.setOverdraftLimit(overdraftLimit);
                        
                        System.out.print("Enter overdraft interest rate (%): ");
                        double overdraftRate = Double.parseDouble(scanner.nextLine());
                        if (overdraftRate > 0) {
                            account.setOverdraftInterestRate(overdraftRate);
                        } else {
                            System.out.println("Invalid interest rate. Using default rate of 15%");
                        }
                    } else {
                        System.out.println("Invalid overdraft limit. Overdraft protection not enabled.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Overdraft protection not enabled.");
                }
            }
            
            return account;
            
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
            System.out.println("5. Apply Overdraft Interest to All Accounts");
            System.out.println("6. Apply Savings Interest to All Accounts");
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
                case "5":
                    double interestCharged = accountManager.applyOverdraftInterestToAllAccounts();
                    if (interestCharged > 0) {
                        System.out.printf("Applied overdraft interest. Total interest charged: $%.2f\n", interestCharged);
                    } else {
                        System.out.println("No accounts are currently in overdraft.");
                    }
                    break;
                case "6":
                    accountManager.applyInterestToAllSavingsAccounts();
                    System.out.println("Applied interest to all savings accounts.");
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
        
        double overdraftLimit = 0.0; // Default: no overdraft
        
        System.out.print("Would you like to enable overdraft protection? (yes/no): ");
        String overdraftChoice = scanner.nextLine().trim().toLowerCase();
        
        if (overdraftChoice.equals("yes")) {
            boolean validInput = false;
            while (!validInput) {
                System.out.print("Enter overdraft limit ($): ");
                try {
                    overdraftLimit = Double.parseDouble(scanner.nextLine());
                    if (overdraftLimit <= 0) {
                        System.out.println("Overdraft limit must be greater than zero.");
                    } else {
                        validInput = true;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }
        }
        
        if (accountManager.addCheckingAccount(accountName, overdraftLimit)) {
            System.out.println("Checking account '" + accountName + "' created successfully!");
            
            if (overdraftLimit > 0) {
                System.out.print("Enter overdraft interest rate (%): ");
                try {
                    double interestRate = Double.parseDouble(scanner.nextLine());
                    if (interestRate > 0) {
                        accountManager.setOverdraftInterestRate(accountName, interestRate);
                        System.out.println("Overdraft interest rate set to " + interestRate + "%");
                    } else {
                        System.out.println("Interest rate must be positive. Using default rate of 15%");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid interest rate. Using default rate of 15%");
                }
            }
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
            
            System.out.print("Would you like to enable overdraft protection? (yes/no): ");
            String overdraftChoice = scanner.nextLine().trim().toLowerCase();
            
            if (overdraftChoice.equals("yes")) {
                boolean validOverdraft = false;
                double overdraftLimit = 0.0;
                
                while (!validOverdraft) {
                    System.out.print("Enter overdraft limit ($): ");
                    try {
                        overdraftLimit = Double.parseDouble(scanner.nextLine());
                        if (overdraftLimit <= 0) {
                            System.out.println("Overdraft limit must be greater than zero.");
                        } else {
                            validOverdraft = true;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                    }
                }
                
                if (accountManager.setOverdraftLimit(accountName, overdraftLimit)) {
                    System.out.print("Enter overdraft interest rate (%): ");
                    try {
                        double overdraftRate = Double.parseDouble(scanner.nextLine());
                        if (overdraftRate > 0) {
                            accountManager.setOverdraftInterestRate(accountName, overdraftRate);
                            System.out.println("Overdraft interest rate set to " + overdraftRate + "%");
                        } else {
                            System.out.println("Interest rate must be positive. Using default rate of 15%");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid interest rate. Using default rate of 15%");
                    }
                }
            }
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
        
        if (account.getBalance() < 0) {
            System.out.printf("Your account is in overdraft by $%.2f\n", Math.abs(account.getBalance()));
            System.out.printf("Overdraft interest rate: %.2f%%\n", account.getOverdraftInterestRate());
        }
        
        if (account.getOverdraftLimit() > 0) {
            System.out.printf("Overdraft limit: $%.2f\n", account.getOverdraftLimit());
            System.out.printf("Available funds (including overdraft): $%.2f\n", 
                             account.getBalance() >= 0 ? 
                             account.getBalance() + account.getOverdraftLimit() : 
                             account.getOverdraftLimit() - Math.abs(account.getBalance()));
        }
    }
    
    /**
     * Handles the deposit process for an account.
     * 
     * @param account The bank account
     * @param scanner The scanner for user input
     */
    private static void handleDeposit(AccountManager accountManager, BankAccount account, Scanner scanner) {
        if (account.isFrozen()) {
            System.out.println("This account is frozen. Unfreeze it first to make deposits.");
            return;
        }
        
        AccountStorage accountStorage = new AccountStorage();
        System.out.print("Enter amount to deposit: $");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            if (amount > 0) {
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
        if (account.isFrozen()) {
            System.out.println("This account is frozen. Unfreeze it first to make withdrawals.");
            return;
        }
        
        AccountStorage accountStorage = new AccountStorage();
        System.out.printf("(Per-transaction limit: $%.2f)\n", account.getWithdrawalLimit());
        
        if (account.getOverdraftLimit() > 0) {
            double availableFunds = account.getBalance() >= 0 ? 
                                   account.getBalance() + account.getOverdraftLimit() : 
                                   account.getOverdraftLimit() - Math.abs(account.getBalance());
            System.out.printf("Available funds (including overdraft): $%.2f\n", availableFunds);
        }
        
        System.out.print("Enter amount to withdraw: $");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            if (amount > 0) {
                boolean success = account.withdraw(amount);
                if (success) {
                    try {
                        accountStorage.recordTransaction(accountManager.getUsername(), account.getAccountName(), "Withdraw: $" + amount);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    
                    System.out.printf("Withdrew $%.2f. New balance: $%.2f\n", amount, account.getBalance());
                    
                    if (account.getBalance() < 0) {
                        System.out.printf("Your account is now in overdraft by $%.2f\n", Math.abs(account.getBalance()));
                        System.out.printf("You will be charged %.2f%% interest on this amount until it is repaid.\n", 
                                         account.getOverdraftInterestRate());
                    }
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
    public static void handleInterest(BankAccount account) {
        if (account.isFrozen()) {
            System.out.println("This account is frozen. Unfreeze it first to apply interest.");
            return;
        }
        
        if (account instanceof SavingsAccount) {
            SavingsAccount savingsAccount = (SavingsAccount) account;
            savingsAccount.applyInterest();
            System.out.printf("Interest applied. New balance: $%.2f\n", account.getBalance());
        } else {
            System.out.println("This account does not earn interest.");
        }
    }
    
    /**
     * Handles ordering checks and debit cards for a checking account.
     * 
     * @param account The bank account
     */
    public static void handleOrderChecksAndDebitCards(AccountManager accountManager, BankAccount account, Scanner scanner) {

        if (account.isFrozen()) {
            System.out.println("This account is frozen. Unfreeze it first to order checks.");
            return;
        }
        
        if (account instanceof CheckingAccount) {
            System.out.println("Would you like to order checks or a debit card?");
            System.out.println("1. Checks");
            System.out.println("2. Debit Card");
            System.out.println("=============================");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();
            CheckingAccount checkingAccount = (CheckingAccount) account;
            if (choice.equals("1")) {
                checkingAccount.orderChecks();
                AccountStorage accountStorage = new AccountStorage();
                try {
                    accountStorage.recordTransaction(accountManager.getUsername(), account.getAccountName(), "Ordered checks");
                } catch (IOException e) {
                    System.out.println("Error recording transaction: " + e.getMessage());
                }
            } else if (choice.equals("2")) {
                String lastFour = checkingAccount.orderDebitCard();
                AccountStorage accountStorage = new AccountStorage();
                try {
                    accountStorage.recordTransaction(accountManager.getUsername(), account.getAccountName(), "Ordered debit card ending in " + lastFour);
                } catch (IOException e) {
                    System.out.println("Error recording transaction: " + e.getMessage());
                }
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        } else {
            System.out.println("You can only order checks for a checking account.");
        }
    }
    
    /**
     * Handles modifying overdraft settings for an account.
     * 
     * @param accountManager The account manager for the user
     * @param account The bank account
     * @param scanner The scanner for user input
     */
    public static void handleOverdraftSettings(AccountManager accountManager, BankAccount account, Scanner scanner) {
        System.out.println("\n----- Overdraft Settings -----");
        System.out.printf("Current overdraft limit: $%.2f\n", account.getOverdraftLimit());
        System.out.printf("Current overdraft interest rate: %.2f%%\n", account.getOverdraftInterestRate());
        
        System.out.println("\n1. Update Overdraft Limit");
        System.out.println("2. Update Overdraft Interest Rate");
        System.out.println("0. Back");
        
        System.out.print("\nEnter your choice: ");
        String choice = scanner.nextLine();
        
        switch (choice) {
            case "1":
                System.out.print("Enter new overdraft limit ($): ");
                try {
                    double newLimit = Double.parseDouble(scanner.nextLine());
                    if (newLimit >= 0) {
                        accountManager.setOverdraftLimit(account.getAccountName(), newLimit);
                        System.out.printf("Overdraft limit updated to $%.2f\n", newLimit);
                    } else {
                        System.out.println("Overdraft limit cannot be negative.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
                break;
                
            case "2":
                System.out.print("Enter new overdraft interest rate (%): ");
                try {
                    double newRate = Double.parseDouble(scanner.nextLine());
                    if (newRate >= 0) {
                        accountManager.setOverdraftInterestRate(account.getAccountName(), newRate);
                        System.out.printf("Overdraft interest rate updated to %.2f%%\n", newRate);
                    } else {
                        System.out.println("Interest rate cannot be negative.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
                break;
                
            case "0":
                // Do nothing, just go back
                break;
                
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }
    
    /**
     * Handles freezing or unfreezing an account.
     * 
     * @param accountManager The account manager for the user
     * @param account The bank account
     * @param scanner The scanner for user input
     */
    public static void handleFreezeAccount(AccountManager accountManager, BankAccount account, Scanner scanner) {
        if (account.isFrozen()) {
            System.out.println("This account is currently frozen. Would you like to unfreeze it? (yes/no): ");
            String choice = scanner.nextLine().trim().toLowerCase();
            
            if (choice.equals("yes")) {
                accountManager.unfreezeAccount(account.getAccountName());
                System.out.println("Account unfrozen successfully.");
            }
        } else {
            System.out.println("Are you sure you want to freeze this account? This will prevent any transactions until unfrozen. (yes/no): ");
            String choice = scanner.nextLine().trim().toLowerCase();
            
            if (choice.equals("yes")) {
                accountManager.freezeAccount(account.getAccountName());
                System.out.println("Account frozen successfully.");
            }
        }
    }
    
    /**
     * Handles the process of closing a bank account.
     * 
     * The account can only be closed if its balance is zero or positive.
     * If the balance is negative, the user needs to repay the overdraft first.
     * The user must also confirm the closure with a "yes" input.
     * 
     * @param accountManager The manager containing all of the user's accounts
     * @param account The account to be closed
     * @param scanner The scanner used to read user input
     * @return true if the account was successfully closed, false otherwise
     */
    public static boolean handleCloseAccount(AccountManager accountManager, BankAccount account, Scanner scanner) {
        if (!account.canClose()) {
            System.out.printf("Cannot close account '%s'. Balance is $%.2f — please deposit funds to cover the overdraft first.\n",
                    account.getAccountName(), account.getBalance());
            return false;
        }
        
        System.out.print("Are you sure you want to close this account? This action cannot be undone. (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (!confirmation.equals("yes")) {
            System.out.println("Account closure canceled.");
            return false;
        }
        
        boolean removed = accountManager.closeAccount(account.getAccountName());
        if (removed) {
            System.out.println("Account closed successfully.");
            return true; 
        } else {
            System.out.println("Error closing account.");
            return false;
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
            System.out.printf("Account: %s (%s)%s\n", 
                            account.getAccountName(), 
                            account.getAccountType(),
                            account.isFrozen() ? " [FROZEN]" : "");
            System.out.println("1. Check Balance");
            System.out.println("2. Make Deposit");
            System.out.println("3. Make Withdrawal");
            
            // Display account-specific options
            if (account instanceof SavingsAccount) {
                System.out.println("4. Apply Interest");
            } else if (account instanceof CheckingAccount) {
                System.out.println("4. Order Checks or Debit Card");
            }
            
            System.out.println("5. View Transaction History");
            System.out.println("6. View Last 5 Transactions");
            System.out.println("7. Transfer to Another Account");
            System.out.println("8. Overdraft Settings");
            System.out.println("9. " + (account.isFrozen() ? "Unfreeze" : "Freeze") + " Account");
            System.out.println("10. Close This Account");
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
                        handleOrderChecksAndDebitCards(accountManager, account, scanner);
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
                    if (account.isFrozen()) {
                        System.out.println("This account is frozen. Unfreeze it first to make transfers.");
                    } else {
                        TransferHandler.handleTransfer(accountManager, account, scanner);
                    }
                    break;
                case "8":
                    handleOverdraftSettings(accountManager, account, scanner);
                    break;
                case "9":
                    handleFreezeAccount(accountManager, account, scanner);
                    break;
                case "10":
                    if (handleCloseAccount(accountManager, account, scanner)) {
                        exitRequested = true; // Leave loop if account was closed
                    }
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