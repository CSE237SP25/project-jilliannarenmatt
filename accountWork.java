import java.util.Scanner;
public class BankAccount {

    private String username;
    private String password;
    private double accountBalance;

    public BankAccount(String username, String password) {
        this.username = username;
        this.password = password;
        this.accountBalance = 0.0; 
    }
    
    public BankAccount openAccount(String username, String password) {
    	BankAccount newAccount = new BankAccount(username,password);
    	return newAccount;
    }
    public void showAccountInfo(BankAccount account) {
    	System.out.println("Welcome " + account.username + "! Here is your account information");
    	System.out.println("Account Balance: " + account.accountBalance);
    }
    
    public void withdrawBalance(BankAccount account, double amount) {
		System.out.println("Attempting to withdraw " + amount + " from your account ...");
    	if(account.accountBalance>=amount){
    		account.accountBalance -= amount;
    		System.out.println("Withdrawal successful. Your new account balance is " + account.accountBalance);
    	}
    	else{
    		System.out.println("Withdrawal failed. You have insufficient funds");
    	}
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose and enter a username: ");
        String enteredUsername = scanner.nextLine();
        Boolean differentPasswords = true;
        String enteredPassword = "";
        while (differentPasswords) {
        	System.out.println("Choose and enter a password: ");
        	enteredPassword = scanner.nextLine();
        	System.out.println("Enter same password again to confirm: ");
        	String confirmedPassword = scanner.nextLine();
        	if (enteredPassword.equals(confirmedPassword)) {
        		differentPasswords=false;
        	}
        	else {
        		System.out.println("Passwords do not match. Try again");
        	}
        }
        BankAccount newAccount = new BankAccount(enteredUsername, enteredPassword);
        
        System.out.println("Account opened successfully.");
        
        // here we can prompt the user for actions - withdrawing, displaying, depositing, etc with one big while loop. 
        // Examples: help, withdraw, deposit, display, done (end while loop).
       
        
        scanner.close();
        
    }
}
	
}
