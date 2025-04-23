package test;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import bankapp.AccountManager;
import bankapp.BankAccount;
import bankapp.CheckingAccount;
import bankapp.PromptHandler;

import static org.junit.jupiter.api.Assertions.*;

class PromptHandlerTest {

    @Test
    void testOpenCheckingAccount() {
        String input = String.join("\n",
                "1",          
                "MyChecking"  
            ) + "\n";

        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        BankAccount account = PromptHandler.openAccount(scanner);
        assertNotNull(account);
        assertEquals("checking", account.getAccountType());
        assertEquals(0.0, account.getBalance(), 0.001);
    }

    @Test
    void testOpenAccountWithInvalidInputCreatesDefault() {
        String input = "no\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        BankAccount account = PromptHandler.openAccount(scanner);
        assertNotNull(account);
        assertTrue(account instanceof CheckingAccount);
        assertEquals("Default Checking", account.getAccountName());
    }
    @Test
    void testCommandLoopDepositWithdrawDone() {
        // Simulates: deposit 100 → withdraw 30 → done
        String input = String.join("\n",
            "2", "100",
            "3", "30",
            "0"
        ) + "\n";

        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        bankapp.BankAccount account = new CheckingAccount("Checking");
        bankapp.AccountManager manager = new AccountManager("mike");

        PromptHandler.commandLoop(manager,account, scanner);

        assertEquals(70.0, account.getBalance(), 0.001);
    }
}
