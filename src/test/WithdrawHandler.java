package test;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import AccountActions.WithdrawHandler;
import bankapp.CheckingAccount;
import AccountActions.BankAccount;
import static org.junit.jupiter.api.Assertions.*;

class WithdrawHandler {

    @Test
    void testSuccessfulWithdrawal() {
        // Simulate input: withdraw 50.0
        String input = "50.0\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        bankapp.BankAccount account = new CheckingAccount("Checking");
        account.deposit(100.0); // Preload funds

        bankapp.WithdrawHandler.handle(account, scanner);

        assertEquals(50.0, account.getBalance(), 0.001);
    }

    @Test
    void testFailedWithdrawalDueToInsufficientFunds() {
        // Simulate input: withdraw 200.0
        String input = "200.0\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        bankapp.BankAccount account = new bankapp.CheckingAccount("Checking");
        account.deposit(100.0); // Not enough funds
        bankapp.WithdrawHandler.handle(account, scanner);
        // Balance should remain unchanged
        assertEquals(100.0, account.getBalance(), 0.001);
    }
}
