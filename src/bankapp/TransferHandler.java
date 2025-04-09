package bankapp;
import java.util.Scanner;

public class TransferHandler {

    /**
     * Transfers funds from one account to another.
     * 
     * @param accountManager The account manager that holds all user accounts.
     * @param fromAccount The account to transfer funds from.
     * @param scanner The scanner for user input.
     */
    public static void handleTransfer(AccountManager accountManager, BankAccount fromAccount, Scanner scanner) {
        if (accountManager.getCheckingAccounts().size()+accountManager.getSavingsAccounts().size() <= 1) {
            System.out.println("You need at least two accounts to transfer between.");
            return;
        }

        System.out.print("Enter the name of the account to transfer to: ");
        String toAccountName = scanner.nextLine().trim();

        BankAccount toAccount = accountManager.getAccountByName(toAccountName);

        if (toAccount == null) {
            System.out.println("Account not found.");
            return;
        }

        if (toAccount == fromAccount) {
            System.out.println("You cannot transfer money to the same account.");
            return;
        }

        System.out.print("Enter the amount to transfer: $");
        try {
            double amount = Double.parseDouble(scanner.nextLine());

            if (amount <= 0) {
                System.out.println("Transfer amount must be greater than zero.");
                return;
            }

            if (!fromAccount.withdraw(amount)) {
                System.out.println("Insufficient funds in the source account.");
                return;
            }

            toAccount.deposit(amount);
            System.out.printf("Successfully transferred $%.2f from '%s' to '%s'.\n",
                    amount, fromAccount.getAccountName(), toAccount.getAccountName());

            AccountStorage storage = new AccountStorage();
            try {
                storage.recordTransaction(accountManager.getUsername(), fromAccount.getAccountName(),
                        "Transfer Out: $" + amount + " to " + toAccount.getAccountName());
                storage.recordTransaction(accountManager.getUsername(), toAccount.getAccountName(),
                        "Transfer In: $" + amount + " from " + fromAccount.getAccountName());
            } catch (Exception e) {
                System.out.println("Error recording transaction: " + e.getMessage());
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a number.");
        }
    }
}