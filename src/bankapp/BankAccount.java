package bankapp;
public abstract class BankAccount {
    protected double accountBalance;
    protected String accountName;
    protected double withdrawalLimit;

    //initialization constructor with account name
    public BankAccount(String accountName) {
        this.accountBalance = 0.0;
        this.accountName = accountName;
        this.withdrawalLimit = 10000.0; 

        
    }

    public void setWithdrawalLimit(double limit) {
        this.withdrawalLimit = limit;
    }

    public double getWithdrawalLimit() {
        return withdrawalLimit;
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
        if (amount > withdrawalLimit) {
            System.out.printf("Withdrawal exceeds the limit of $%.2f per transaction.\n", withdrawalLimit);
            return false;
        }

        if (accountBalance >= amount) {
            accountBalance -= amount;
            return true;
        }

        return false;
    }

    public abstract String getAccountType(); // implemented by subclasses

}


