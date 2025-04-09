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
    void testOpenAccountYes() {
        String input = "yes\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        BankAccount account = PromptHandler.openAccount(scanner);
        assertNotNull(account);
        assertEquals("Checking", account.getAccountType());
        assertEquals(0.0, account.getBalance(), 0.001);
    }

    @Test
    void testOpenAccountNo() {
        String input = "no\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        BankAccount account = PromptHandler.openAccount(scanner);
        assertNull(account);
    }

    @Test
    void testCommandLoopDepositWithdrawDone() {
        // Simulates: deposit 100 → withdraw 30 → done
        String input = String.join("\n",
            "deposit", "100",
            "withdraw", "30",
            "done"
        ) + "\n";

        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        bankapp.BankAccount account = new CheckingAccount("Checking");
        bankapp.AccountManager manager = new AccountManager("mike");

        PromptHandler.commandLoop(manager,account, scanner);

        assertEquals(70.0, account.getBalance(), 0.001);
    }
}
