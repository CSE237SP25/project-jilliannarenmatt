package bankapp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class AccountStorage {
    private final Path accountsRoot;
    private static final String HISTORY_FILE = "history.txt";
    private static final String BALANCE_FILE = "balance.txt";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

    public AccountStorage() {
        this(Paths.get("..", "data", "Accounts"));
    }

    public AccountStorage(Path accountsRoot) {
        this.accountsRoot = accountsRoot;
        try {
            Files.createDirectories(accountsRoot);
        } catch (IOException e) {
            System.err.println("Failed to create accounts directory: " + e.getMessage());
        }
    }

    private Path getUserPath(String username) throws IOException {
        Path userPath = accountsRoot.resolve(username);
        if (!Files.exists(userPath)) {
            Files.createDirectories(userPath);
        }
        return userPath;
    }

    public void recordTransaction(String username, String transaction) throws IOException {
        Path historyPath = getUserPath(username).resolve(HISTORY_FILE);
        String timestampedTransaction = transaction + ", " + DATE_FORMAT.format(new Date()) + System.lineSeparator();
        Files.write(historyPath, 
                   timestampedTransaction.getBytes(), 
                   StandardOpenOption.CREATE, 
                   StandardOpenOption.APPEND);
    }

    public void updateBalance(String username, double amount) throws IOException {
        Path balancePath = getUserPath(username).resolve(BALANCE_FILE);
        Files.write(balancePath,
                   String.valueOf(amount).getBytes(),
                   StandardOpenOption.CREATE,
                   StandardOpenOption.TRUNCATE_EXISTING);
    }

    public double getBalance(String username) throws IOException {
        Path balancePath = getUserPath(username).resolve(BALANCE_FILE);
        if (!Files.exists(balancePath)) {
            return 0.0;
        }
        return Double.parseDouble(Files.readString(balancePath).trim());
    }

    public List<String> getTransactionHistory(String username) throws IOException {
        Path historyPath = getUserPath(username).resolve(HISTORY_FILE);
        if (!Files.exists(historyPath)) {
            return Collections.emptyList();
        }
        return Files.readAllLines(historyPath);
    }

    public List<String> getLastFiveTransactions(String username) throws IOException {
        List<String> allTransactions = getTransactionHistory(username);
        int startIdx = Math.max(0, allTransactions.size() - 5);
        return allTransactions.subList(startIdx, allTransactions.size());
    }

}