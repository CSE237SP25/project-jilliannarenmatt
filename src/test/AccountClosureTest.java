package test;

import bankapp.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;
import bankapp.PromptHandler;

import static org.junit.jupiter.api.Assertions.*;

public class AccountClosureTest {

    @Test
    void testCloseAccountSuccessWithConfirmation() {
        AccountManager manager = new AccountManager("testuser");
        BankAccount account = new CheckingAccount("TestAccount");
        manager.getCheckingAccounts().add((CheckingAccount) account);

        String input = "yes\n"; // simulate user confirming
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        boolean result = PromptHandler.handleCloseAccount(manager, account, scanner);

        assertTrue(result, "Account should be closed successfully");
        assertNull(manager.getAccountByName("TestAccount"), "Account should no longer exist");
    }

    @Test
    void testCloseAccountFailsDueToNonZeroBalance() {
        AccountManager manager = new AccountManager("testuser");
        BankAccount account = new CheckingAccount("TestAccount");
        account.deposit(100.0);
        manager.getCheckingAccounts().add((CheckingAccount) account);

        String input = "yes\n"; // still confirms, but balance > 0
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        boolean result = PromptHandler.handleCloseAccount(manager, account, scanner);

        assertFalse(result, "Account should not be closed due to non-zero balance");
        assertNotNull(manager.getAccountByName("TestAccount"), "Account should still exist");
    }

    @Test
    void testCloseAccountCancelledByUser() {
        AccountManager manager = new AccountManager("testuser");
        BankAccount account = new CheckingAccount("TestAccount");
        manager.getCheckingAccounts().add((CheckingAccount) account);

        String input = "no\n"; // simulate user saying "no"
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        boolean result = PromptHandler.handleCloseAccount(manager, account, scanner);

        assertFalse(result, "Account closure should be cancelled by user");
        assertNotNull(manager.getAccountByName("TestAccount"), "Account should still exist");
    }
}
