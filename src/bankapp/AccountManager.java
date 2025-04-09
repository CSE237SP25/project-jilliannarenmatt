package bankapp;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages multiple bank accounts for a user, including both checking and savings accounts.
 * Handles account creation, retrieval, and persistence operations.
 */
public class AccountManager {
    private String username;
    private List<CheckingAccount> checkingAccounts;
    private List<SavingsAccount> savingsAccounts;
    
    // Constants
    private static final int MAX_CHECKING_ACCOUNTS = 2;
    private static final int MAX_SAVINGS_ACCOUNTS = 3;
    private static final String ACCOUNTS_DIRECTORY = "data/Accounts/";
    
    /**
     * Creates a new AccountManager for the specified user.
     * 
     * @param username The username this account manager belongs to
     */
    public AccountManager(String username) {
        this.username = username;
        this.checkingAccounts = new ArrayList<>();
        this.savingsAccounts = new ArrayList<>();
        createAccountsDirectory();
    }

    /**
     * Gets the username associated with this account manager.
     * 
     * @return The username
     */
    public String getUsername() {
        return this.username;
    }
    
    /**
     * Creates the directory structure for storing account data.
     */
    private void createAccountsDirectory() {
        try {
            Path userAccountsDir = Paths.get(ACCOUNTS_DIRECTORY + username);
            Files.createDirectories(userAccountsDir);
        } catch (IOException exception) {
            System.err.println("Failed to create accounts directory: " + exception.getMessage());
        }
    }
    
    /**
     * Adds a new checking account with the given name.
     * 
     * @param accountName The name for the new checking account
     * @return true if the account was successfully added, false otherwise
     */
    public boolean addCheckingAccount(String accountName) {
        if (checkingAccounts.size() >= MAX_CHECKING_ACCOUNTS) {
            System.out.println("Maximum number of checking accounts (" + MAX_CHECKING_ACCOUNTS + ") reached.");
            return false;
        }
        
        if (isAccountNameTaken(accountName)) {
            System.out.println("Account name '" + accountName + "' is already in use.");
            return false;
        }
        
        CheckingAccount newAccount = new CheckingAccount(accountName);
        checkingAccounts.add(newAccount);
        saveAccounts(); // Save after adding a new account
        return true;
    }
    
    /**
     * Adds a new savings account with the given name and interest rate.
     * 
     * @param accountName The name for the new savings account
     * @param interestRate The interest rate for the savings account
     * @return true if the account was successfully added, false otherwise
     */
    public boolean addSavingsAccount(String accountName, double interestRate) {
        if (savingsAccounts.size() >= MAX_SAVINGS_ACCOUNTS) {
            System.out.println("Maximum number of savings accounts (" + MAX_SAVINGS_ACCOUNTS + ") reached.");
            return false;
        }
        
        if (isAccountNameTaken(accountName)) {
            System.out.println("Account name '" + accountName + "' is already in use.");
            return false;
        }
        
        SavingsAccount newAccount = new SavingsAccount(accountName, interestRate);
        savingsAccounts.add(newAccount);
        saveAccounts(); // Save after adding a new account
        return true;
    }
    
    /**
     * Checks if an account name is already in use.
     * 
     * @param accountName The account name to check
     * @return true if the name is already taken, false otherwise
     */
    private boolean isAccountNameTaken(String accountName) {
        // Check if name exists in checking accounts
        for (CheckingAccount account : checkingAccounts) {
            if (account.getAccountName().equalsIgnoreCase(accountName)) {
                return true;
            }
        }
        
        // Check if name exists in savings accounts
        for (SavingsAccount account : savingsAccounts) {
            if (account.getAccountName().equalsIgnoreCase(accountName)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Gets all checking accounts for the user.
     * 
     * @return List of checking accounts
     */
    public List<CheckingAccount> getCheckingAccounts() {
        return checkingAccounts;
    }
    
    /**
     * Gets all savings accounts for the user.
     * 
     * @return List of savings accounts
     */
    public List<SavingsAccount> getSavingsAccounts() {
        return savingsAccounts;
    }
    
    /**
     * Gets a specific account by its name.
     * 
     * @param accountName The name of the account to retrieve
     * @return The bank account, or null if not found
     */
    public BankAccount getAccountByName(String accountName) {
        // Check checking accounts
        for (CheckingAccount account : checkingAccounts) {
            if (account.getAccountName().equalsIgnoreCase(accountName)) {
                return account;
            }
        }
        
        // Check savings accounts
        for (SavingsAccount account : savingsAccounts) {
            if (account.getAccountName().equalsIgnoreCase(accountName)) {
                return account;
            }
        }
        
        return null; // Account not found
    }
    
    /**
     * Displays a list of all accounts and their balances.
     */
    public void listAllAccounts() {
        System.out.println("\n===== YOUR ACCOUNTS =====");
        
        if (checkingAccounts.isEmpty() && savingsAccounts.isEmpty()) {
            System.out.println("You don't have any accounts yet.");
            return;
        }
        
        // List checking accounts
        if (!checkingAccounts.isEmpty()) {
            System.out.println("\nChecking Accounts:");
            for (CheckingAccount account : checkingAccounts) {
                System.out.printf("- %s: $%.2f\n", account.getAccountName(), account.getBalance());
            }
        }
        
        // List savings accounts
        if (!savingsAccounts.isEmpty()) {
            System.out.println("\nSavings Accounts:");
            for (SavingsAccount account : savingsAccounts) {
                System.out.printf("- %s: $%.2f (Interest Rate: %.2f%%)\n", 
                    account.getAccountName(), account.getBalance(), account.getInterestRate());
            }
        }
        
        System.out.println("==========================");
    }
    
    /**
     * Saves all accounts to the filesystem.
     * 
     * @return true if saving was successful, false otherwise
     */
    public boolean saveAccounts() {
        createAccountsDirectory();
        
        try {
            // Save checking accounts
            saveAccountList(checkingAccounts, "checking");
            
            // Save savings accounts
            saveAccountList(savingsAccounts, "savings");
            
            return true;
        } catch (IOException exception) {
            System.err.println("Error saving accounts for " + username + ": " + exception.getMessage());
            return false;
        }
    }
    
    /**
     * Saves a list of accounts to a file.
     * 
     * @param accounts The list of accounts to save
     * @param accountType The type of accounts ("checking" or "savings")
     * @throws IOException If there's an error writing to the file
     */
    private <T extends BankAccount> void saveAccountList(List<T> accounts, String accountType) throws IOException {
        Path filePath = Paths.get(ACCOUNTS_DIRECTORY + username + "/" + accountType + ".txt");
        
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (T account : accounts) {
                // For all account types, write name and balance
                writer.write(account.getAccountName() + "," + account.getBalance());
                
                // For savings accounts, also write interest rate
                if (account instanceof SavingsAccount) {
                    SavingsAccount savingsAccount = (SavingsAccount) account;
                    writer.write("," + savingsAccount.getInterestRate());
                }
                
                writer.newLine();
            }
        }
    }
    
    /**
     * Loads all accounts for the user from the filesystem.
     * 
     * @return true if loading was successful, false otherwise
     */
    public boolean loadAccounts() {
        // Clear existing accounts
        checkingAccounts.clear();
        savingsAccounts.clear();
        
        try {
            // Load checking accounts
            Path checkingPath = Paths.get(ACCOUNTS_DIRECTORY + username + "/checking.txt");
            if (Files.exists(checkingPath)) {
                loadCheckingAccounts(checkingPath);
            }
            
            // Load savings accounts
            Path savingsPath = Paths.get(ACCOUNTS_DIRECTORY + username + "/savings.txt");
            if (Files.exists(savingsPath)) {
                loadSavingsAccounts(savingsPath);
            }
            
            return true;
        } catch (IOException exception) {
            System.err.println("Error loading accounts for " + username + ": " + exception.getMessage());
            return false;
        }
    }
    
    /**
     * Loads checking accounts from a file.
     * 
     * @param filePath The path to the checking accounts file
     * @throws IOException If there's an error reading from the file
     */
    private void loadCheckingAccounts(Path filePath) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String accountName = parts[0];
                    double balance = Double.parseDouble(parts[1]);
                    
                    CheckingAccount account = new CheckingAccount(accountName);
                    account.deposit(balance); // Set the balance
                    checkingAccounts.add(account);
                }
            }
        }
    }
    
    /**
     * Loads savings accounts from a file.
     * 
     * @param filePath The path to the savings accounts file
     * @throws IOException If there's an error reading from the file
     */
    private void loadSavingsAccounts(Path filePath) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String accountName = parts[0];
                    double balance = Double.parseDouble(parts[1]);
                    double interestRate = Double.parseDouble(parts[2]);
                    
                    SavingsAccount account = new SavingsAccount(accountName, interestRate);
                    account.deposit(balance); // Set the balance
                    savingsAccounts.add(account);
                }
            }
        }
    }
    
    /**
     * Applies interest to all savings accounts.
     */
    public void applyInterestToAllSavingsAccounts() {
        for (SavingsAccount account : savingsAccounts) {
            account.applyInterest();
        }
        saveAccounts(); // Save changes after applying interest
    }
    
    /**
     * Migrates an existing single account to the multi-account system.
     * This is useful for backward compatibility.
     * 
     * @param existingAccount The existing single account to migrate
     * @return true if migration was successful, false otherwise
     */
    public boolean migrateExistingAccount(BankAccount existingAccount) {
        if (existingAccount == null) {
            return false;
        }
        
        // Default name for migrated accounts
        String defaultName = "Primary " + existingAccount.getAccountType().substring(0, 1).toUpperCase() + 
                             existingAccount.getAccountType().substring(1);
        
        // If name is taken, append a number
        String accountName = defaultName;
        int suffix = 1;
        while (isAccountNameTaken(accountName)) {
            accountName = defaultName + " " + suffix;
            suffix++;
        }
        
        if (existingAccount instanceof CheckingAccount) {
            if (checkingAccounts.size() >= MAX_CHECKING_ACCOUNTS) {
                System.out.println("Cannot migrate existing checking account: maximum number reached.");
                return false;
            }
            
            CheckingAccount newAccount = new CheckingAccount(accountName);
            newAccount.deposit(existingAccount.getBalance());
            checkingAccounts.add(newAccount);
            
        } else if (existingAccount instanceof SavingsAccount) {
            if (savingsAccounts.size() >= MAX_SAVINGS_ACCOUNTS) {
                System.out.println("Cannot migrate existing savings account: maximum number reached.");
                return false;
            }
            
            SavingsAccount oldSavings = (SavingsAccount) existingAccount;
            SavingsAccount newAccount = new SavingsAccount(accountName, oldSavings.getInterestRate());
            newAccount.deposit(oldSavings.getBalance());
            savingsAccounts.add(newAccount);
        }
        
        saveAccounts();
        return true;
    }
}