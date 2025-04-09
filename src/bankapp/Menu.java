package bankapp;

import java.util.Scanner;

public class Menu {
	public static void main(String[] args) {
		System.out.println("Welcome to the Banking System");
		System.out.println("Please login or create an account");

		// Create a login manager to handle user authentication
		LoginManager manager = new LoginManager();

		// Start the login/registration process
		User authenticatedUser = manager.start();

		// Check if login/registration was successful
		if (authenticatedUser != null) {
			// Check if login/registration was successful
			if (authenticatedUser != null) {
			    System.out.println("\nWelcome, " + authenticatedUser.getUsername() + "!");
			    System.out.println("You are now logged in to your account.");
			    
			    // Get the user's profile
			    UserProfile userProfile = authenticatedUser.getProfile();
			    
			    // open up scanner
			    Scanner scanner = new Scanner(System.in);
			    
			    // Present options for account management or profile management
			    boolean continueSession = true;
			    while (continueSession) {
			        System.out.println("\n===== ACCOUNT OPTIONS =====");
			        System.out.println("1. Manage Bank Account");
			        System.out.println("2. Manage Personal Profile");
			        System.out.println("0. Logout");
			        System.out.println("==========================");
			        
			        System.out.print("Enter your choice: ");
			        String choice = scanner.nextLine();
			        
			        switch (choice) {
			            case "1":
			                // open new account
			                BankAccount account = PromptHandler.openAccount(scanner);
			                // prompt user for actions
			                PromptHandler.commandLoop(account, scanner);
			                break;
			            case "2":
			                // Manage profile information
			                ProfileHandler.manageProfile(userProfile, scanner);
			                break;
			            case "0":
			                continueSession = false;
			                break;
			            default:
			                System.out.println("Invalid option. Please try again.");
			        }
			    }
			    
			    // finish up
			    scanner.close();
			} 
		} else {
			System.out.println("Login/Registration canceled or failed.");
		}
	   	// open up scanner
        Scanner scanner = new Scanner(System.in);
        
        // open new account
        BankAccount account = PromptHandler.openAccount(scanner);
        //prompt user for actions
        PromptHandler.commandLoop(account, scanner);
        //finish up.
        scanner.close();
		System.out.println("\nThank you for using our banking system!");
		manager.close();
	}
}
