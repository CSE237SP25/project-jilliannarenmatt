package bankapp;
public abstract class BankAccount {
    protected double accountBalance;
    protected String accountName;
    
    //initialization constructor with account name
    public BankAccount(String accountName) {
        this.accountBalance = 0.0;
        this.accountName = accountName;
    }
    
    public String getAccountName() {
        return accountName;
    }
    
    public double getBalance() {
    	//simple getter function
        return accountBalance;
    }

    public void deposit(double amount) {
        accountBalance += amount;
    }

    public boolean withdraw(double amount) {
        if (accountBalance >= amount) {
            accountBalance -= amount;
            //successful withdrawal
            return true;
        }
        // user can't withdraw that amount.
        return false;
    }

    public abstract String getAccountType(); // implemented by subclasses

}


