package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import bankapp.BankAccount;
import bankapp.CheckingAccount;
import bankapp.SavingsAccount;

class BankAccountTest {

    private static final String TEST_ACCOUNT_NAME = "Test Account";
    
    @Test
    void testCheckingAccountCreation() {
        BankAccount account = new CheckingAccount(TEST_ACCOUNT_NAME);
        assertEquals(0.0, account.getBalance());
        assertEquals(TEST_ACCOUNT_NAME, account.getAccountName());
        assertEquals("checking", account.getAccountType());
    }
    
    @Test
    void testSavingsAccountCreation() {
        double interestRate = 2.5;
        BankAccount account = new SavingsAccount(TEST_ACCOUNT_NAME, interestRate);
        assertEquals(0.0, account.getBalance());
        assertEquals(TEST_ACCOUNT_NAME, account.getAccountName());
        assertEquals("savings", account.getAccountType());
    }
    
    @Test
    void testDeposit() {
        BankAccount account = new CheckingAccount(TEST_ACCOUNT_NAME);
        account.deposit(100.0);
        assertEquals(100.0, account.getBalance());
        
        account.deposit(50.0);
        assertEquals(150.0, account.getBalance());
    }
    
    @Test
    void testWithdrawalSufficientFunds() {
        BankAccount account = new CheckingAccount(TEST_ACCOUNT_NAME);
        account.deposit(100.0);
        
        boolean result = account.withdraw(50.0);
        
        assertTrue(result);
        assertEquals(50.0, account.getBalance());
    }
    
    @Test
    void testWithdrawalInsufficientFunds() {
        BankAccount account = new CheckingAccount(TEST_ACCOUNT_NAME);
        account.deposit(100.0);
        
        boolean result = account.withdraw(150.0);
        
        assertFalse(result);
        assertEquals(100.0, account.getBalance()); // Balance should remain unchanged
    }
    
    @Test
    void testWithdrawalExceedsLimit() {
        BankAccount account = new CheckingAccount("Test");
        account.deposit(1000.0);
        account.setWithdrawalLimit(200.0); // limit is $200 per withdrawal

        boolean result = account.withdraw(300.0); // exceeds the limit

        assertFalse(result, "Withdrawal should fail because it exceeds the limit");
        assertEquals(1000.0, account.getBalance(), 0.001, "Balance should remain unchanged");
    }

    @Test
    void testWithdrawalWithinLimitSucceeds() {
        BankAccount account = new CheckingAccount("Test");
        account.deposit(500.0);
        account.setWithdrawalLimit(300.0);

        boolean result = account.withdraw(200.0); // within the limit

        assertTrue(result, "Withdrawal should succeed within the limit");
        assertEquals(300.0, account.getBalance(), 0.001, "Balance should be reduced correctly");
    }
    
    @Test
    void testGetAccountName() {
        BankAccount checkingAccount = new CheckingAccount("Personal Checking");
        BankAccount savingsAccount = new SavingsAccount("Vacation Fund", 1.5);
        
        assertEquals("Personal Checking", checkingAccount.getAccountName());
        assertEquals("Vacation Fund", savingsAccount.getAccountName());
    }
    
    @Test
    void testMultipleOperations() {
        BankAccount account = new CheckingAccount(TEST_ACCOUNT_NAME);
        
        account.deposit(100.0);
        assertEquals(100.0, account.getBalance());
        
        account.deposit(50.0);
        assertEquals(150.0, account.getBalance());
        
        boolean result1 = account.withdraw(30.0);
        assertTrue(result1);
        assertEquals(120.0, account.getBalance());
        
        boolean result2 = account.withdraw(200.0);
        assertFalse(result2);
        assertEquals(120.0, account.getBalance());
    }
}