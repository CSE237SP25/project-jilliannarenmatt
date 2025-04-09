package bankapp;

import java.util.Scanner;

/**
 * Class for prompting user for inputs
 * Includes a method to get the user to open an account,
 * and another method to show user possible actions and execute them.
 */


public class PromptHandler {

	   public static BankAccount openAccount(Scanner scanner) {
	        String response = "";

	        // Force user to answer yes or no
	        while (true) {
	            System.out.print("Would you like to open a bank account? (yes/no): ");
	            response = scanner.nextLine().trim().toLowerCase();

	            if (response.equals("yes")) {
	                break;
	            } else if (response.equals("no")) {
	                System.out.println("No account will be opened at this time.");
	                scanner.close();
	                return null;
	            } else {
	                System.out.println("Please enter 'yes' or 'no'.");
	            }
	        }

	        // Force user to choose checkings or savings
	        while (true) {
	            System.out.print("What type of account would you like to open? (checkings/savings): ");
	            String accountType = scanner.nextLine().trim().toLowerCase();

	            if (accountType.equals("checkings")) {
	                System.out.println("You have chosen to open a checkings account.");
	                return new CheckingAccount();
	            } else if (accountType.equals("savings")) {
	                System.out.println("You have chosen to open a savings account.");
	                return new SavingsAccount(0.1); 
	            } else {
	                System.out.println("Invalid input. Please enter 'checkings' or 'savings'.");
	            }
	        }
	    }

    public static void commandLoop(BankAccount account, Scanner scanner) {
        if (account == null) return;

        String command;

        System.out.println("\nAvailable commands:");
        System.out.println("- deposit");
        System.out.println("- withdraw");

        if (account instanceof CheckingAccount) {
            System.out.println("- order checks");
        } else if (account instanceof SavingsAccount) {
            System.out.println("- view interest");
        }

        System.out.println("- done");

        do {
            System.out.print("\nEnter a command: ");
            command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "deposit":
                    DepositHandler.handle(account, scanner);
                    break;
                case "withdraw":
                    WithdrawHandler.handle(account, scanner);
                    break;
                case "order checks":
                    if (account instanceof CheckingAccount) {
                        System.out.println("Ordering checks for your checking account...");
                        
                    } else {
                        System.out.println("This command is only available for checking accounts.");
                    }
                    break;
                case "view interest":
                    if (account instanceof SavingsAccount) {
                        SavingsAccount savings = (SavingsAccount) account;
                        System.out.printf("Current interest rate: %.2f%%\n", savings.getInterestRate());
                    } else {
                        System.out.println("This command is only available for savings accounts.");
                    }
                    break;
                case "done":
                    System.out.println("Thank you for banking with us, have a great day!");
                    break;
                default:
                    System.out.println("Command not recognized. Please try again.");
            	}

        if (!command.equals("done") && account instanceof SavingsAccount) {
            ((SavingsAccount) account).applyInterest();
        }
        } while (!command.equals("done")); // <-- missing closing bracket was here

    }
    
}
