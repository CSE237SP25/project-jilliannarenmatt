package bankapp;

public class SavingsAccount extends BankAccount {
    private double interestRate;

    public SavingsAccount(double interestRate) {
        super();
        this.interestRate = interestRate;
    }

    @Override
    public String getAccountType() {
        return "savings";
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void applyInterest() {
        double interest = accountBalance * (interestRate / 100);
        accountBalance += interest;
        System.out.printf("Interest of %.2f applied. New balance: %.2f\n", interest, accountBalance);
    }
}
