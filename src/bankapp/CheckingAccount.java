package bankapp;

public class CheckingAccount extends BankAccount {

    public CheckingAccount(String accountName) {
        super(accountName);
    }

    @Override
    public String getAccountType() {
        return "checking";
    }

    public void orderChecks() {
        System.out.println("Checks ordered for your checking account.");
    }
}
