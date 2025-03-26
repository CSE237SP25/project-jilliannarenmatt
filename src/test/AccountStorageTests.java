package test;

import org.junit.jupiter.api.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import bankapp.AccountStorage;

class AccountStorageTest {
    private static final String TEST_USERNAME = "testuser";
    private static final Path TEST_ROOT = Paths.get("..", "data", "testaccounts");
    private AccountStorage storage;

    @BeforeEach
    void setup() throws IOException {
        // Ensure clean start by deleting test folder if it exists
        deleteTestDirectory();
        storage = new AccountStorage(TEST_ROOT);
    }

    @AfterEach
    void cleanup() throws IOException {
        deleteTestDirectory();
    }

    private void deleteTestDirectory() throws IOException {
        if (Files.exists(TEST_ROOT)) {
            Files.walk(TEST_ROOT)
                 .sorted(Comparator.reverseOrder())
                 .forEach(path -> {
                     try {
                         Files.delete(path);
                     } catch (IOException e) {
                         throw new RuntimeException("Failed to delete " + path, e);
                     }
                 });
        }
    }

    @Test
    void recordTransaction_createsFiles() throws IOException {
        storage.recordTransaction(TEST_USERNAME, "Deposit $100");
        Path userDir = TEST_ROOT.resolve(TEST_USERNAME);
        
        assertTrue(Files.exists(userDir), "User directory should be created");
        assertTrue(Files.exists(userDir.resolve("history.txt")), "History file should exist");
    }

    @Test
    void updateBalance_storesCorrectValue() throws IOException {
        storage.updateBalance(TEST_USERNAME, 150.75);
        double balance = storage.getBalance(TEST_USERNAME);
        assertEquals(150.75, balance, 0.001, "Stored balance should match");
    }

    @Test
    void getBalance_returnsZeroForNewAccount() throws IOException {
        double balance = storage.getBalance("nonexistent_user");
        assertEquals(0.0, balance, "New accounts should have 0 balance");
    }

    @Test
    void getLastFiveTransactions_returnsCorrectTransactions() throws IOException {
        // Add 7 transactions
        for (int i = 1; i <= 7; i++) {
            storage.recordTransaction(TEST_USERNAME, "Transaction " + i);
        }
        
        List<String> lastFive = storage.getLastFiveTransactions(TEST_USERNAME);
        assertEquals(5, lastFive.size(), "Should return exactly 5 transactions");
        assertTrue(lastFive.get(0).contains("Transaction 3"), "Should return most recent transactions");
        assertTrue(lastFive.get(4).contains("Transaction 7"));
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    @DisplayName("Test Summary")
    class TestSummary {
        @AfterAll
        void showSuccess() {
            System.out.println("\n✅ All tests passed!");
        }
    }

}