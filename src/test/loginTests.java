package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import login.password;
import login.user;
import login.username;

/**
 * JUnit 5 test class for the login system.
 */
public class loginTests {
    
    private static final String TEST_USERNAME = "TestUser123";
    private static final String TEST_PASSWORD = "Password123!";
    private static final String USERS_FILE = "users.txt";
    
    @BeforeEach
    public void setUp() {
        // Ensure test user doesn't exist at start of each test
        cleanupTestUser();
    }
    
    @AfterEach
    public void tearDown() {
        // Clean up after each test
        cleanupTestUser();
    }
    
    @Test
    public void testValidPassword() {
        // Call method being tested
        boolean isValid = password.isValid(TEST_PASSWORD);
        
        // Assert the result
        assertTrue(isValid, "Valid password should pass validation");
    }
    
    @Test
    public void testShortPassword() {
        // Call method being tested
        boolean isValid = password.isValid("Pass1!");
        
        // Assert the result
        assertFalse(isValid, "Password that is too short should fail validation");
    }
    
    @Test
    public void testPasswordWithoutUppercase() {
        // Call method being tested
        boolean isValid = password.isValid("password123!");
        
        // Assert the result
        assertFalse(isValid, "Password without uppercase should fail validation");
    }
    
    @Test
    public void testPasswordWithoutLowercase() {
        // Call method being tested
        boolean isValid = password.isValid("PASSWORD123!");
        
        // Assert the result
        assertFalse(isValid, "Password without lowercase should fail validation");
    }
    
    @Test
    public void testPasswordWithoutDigit() {
        // Call method being tested
        boolean isValid = password.isValid("Password!");
        
        // Assert the result
        assertFalse(isValid, "Password without digit should fail validation");
    }
    
    @Test
    public void testPasswordWithoutSpecialChar() {
        // Call method being tested
        boolean isValid = password.isValid("Password123");
        
        // Assert the result
        assertFalse(isValid, "Password without special character should fail validation");
    }
    
    @Test
    public void testNullPassword() {
        // Call method being tested
        boolean isValid = password.isValid(null);
        
        // Assert the result
        assertFalse(isValid, "Null password should fail validation");
    }
    
    @Test
    public void testValidUsername() {
        // Call method being tested
        boolean isValid = username.isValid(TEST_USERNAME);
        
        // Assert the result
        assertTrue(isValid, "Valid username should pass validation");
    }
    
    @Test
    public void testShortUsername() {
        // Call method being tested
        boolean isValid = username.isValid("usr");
        
        // Assert the result
        assertFalse(isValid, "Username that is too short should fail validation");
    }
    
    @Test
    public void testUsernameStartingWithNumber() {
        // Call method being tested
        boolean isValid = username.isValid("1user");
        
        // Assert the result
        assertFalse(isValid, "Username starting with number should fail validation");
    }
    
    @Test
    public void testUsernameWithInvalidChars() {
        // Call method being tested
        boolean isValid = username.isValid("user@name");
        
        // Assert the result
        assertFalse(isValid, "Username with invalid characters should fail validation");
    }
    
    @Test
    public void testNullUsername() {
        // Call method being tested
        boolean isValid = username.isValid(null);
        
        // Assert the result
        assertFalse(isValid, "Null username should fail validation");
    }
    
    @Test
    public void testCreateValidUser() {
        // Call method being tested
        user newUser = user.createAccount(TEST_USERNAME, TEST_PASSWORD);
        
        // Assert the result
        assertNotNull(newUser, "Creating account with valid credentials should succeed");
        assertEquals(TEST_USERNAME, newUser.getUsername(), "Username should match after account creation");
    }
    
    @Test
    public void testCreateUserWithInvalidUsername() {
        // Call method being tested
        user newUser = user.createAccount("123User", TEST_PASSWORD);
        
        // Assert the result
        assertNull(newUser, "Creating account with invalid username should fail");
    }
    
    @Test
    public void testCreateUserWithInvalidPassword() {
        // Call method being tested
        user newUser = user.createAccount(TEST_USERNAME, "password");
        
        // Assert the result
        assertNull(newUser, "Creating account with invalid password should fail");
    }
    
    @Test
    public void testCreateDuplicateUser() {
        // Setup - create initial user
        user firstUser = user.createAccount(TEST_USERNAME, TEST_PASSWORD);
        assertNotNull(firstUser, "Setup: User creation should succeed");
        
        // Call method being tested
        user duplicateUser = user.createAccount(TEST_USERNAME, "DifferentPass123!");
        
        // Assert the result
        assertNull(duplicateUser, "Creating duplicate user account should fail");
    }
    
    @Test
    public void testLoginWithValidCredentials() {
        // Setup - create test user
        user createdUser = user.createAccount(TEST_USERNAME, TEST_PASSWORD);
        assertNotNull(createdUser, "Setup: User creation should succeed");
        
        // Call method being tested
        user loggedInUser = user.login(TEST_USERNAME, TEST_PASSWORD);
        
        // Assert the result
        assertNotNull(loggedInUser, "Login with valid credentials should succeed");
        assertEquals(TEST_USERNAME, loggedInUser.getUsername(), "Username should match after login");
    }
    
    @Test
    public void testLoginWithInvalidUsername() {
        // Setup - create test user
        user createdUser = user.createAccount(TEST_USERNAME, TEST_PASSWORD);
        assertNotNull(createdUser, "Setup: User creation should succeed");
        
        // Call method being tested
        user loggedInUser = user.login("NonExistentUser", TEST_PASSWORD);
        
        // Assert the result
        assertNull(loggedInUser, "Login with invalid username should fail");
    }
    
    @Test
    public void testLoginWithInvalidPassword() {
        // Setup - create test user
        user createdUser = user.createAccount(TEST_USERNAME, TEST_PASSWORD);
        assertNotNull(createdUser, "Setup: User creation should succeed");
        
        // Call method being tested
        user loggedInUser = user.login(TEST_USERNAME, "WrongPassword123!");
        
        // Assert the result
        assertNull(loggedInUser, "Login with invalid password should fail");
    }
    
    @Test
    public void testUserDataPersistence() {
        // Setup - create test user
        user createdUser = user.createAccount(TEST_USERNAME, TEST_PASSWORD);
        assertNotNull(createdUser, "Setup: User creation should succeed");
        
        // Call method being tested
        List<user> users = user.loadAllUsers();
        
        // Find our test user in the loaded list
        boolean userFound = false;
        for (user u : users) {
            if (u.getUsername().equals(TEST_USERNAME)) {
                userFound = true;
                break;
            }
        }
        
        // Assert the result
        assertTrue(userFound, "User data should be persisted");
    }
    
    @Test
    public void testUserStorageFileCreation() {
        // Setup - create test user
        user createdUser = user.createAccount(TEST_USERNAME, TEST_PASSWORD);
        assertNotNull(createdUser, "Setup: User creation should succeed");
        
        // Call method being tested - verify file exists
        File usersFile = new File(USERS_FILE);
        
        // Assert the result
        assertTrue(usersFile.exists(), "Users file should be created");
    }
    
    /**
     * Utility method to clean up test user from users.txt file.
     */
    private void cleanupTestUser() {
        try {
            File usersFile = new File(USERS_FILE);
            if (usersFile.exists()) {
                List<String> lines = Files.readAllLines(Paths.get(USERS_FILE));
                List<String> filteredLines = lines.stream()
                    .filter(line -> !line.startsWith(TEST_USERNAME + ","))
                    .toList();
                
                Files.write(Paths.get(USERS_FILE), filteredLines);
            }
        } catch (Exception e) {
            System.err.println("Error cleaning up test user: " + e.getMessage());
        }
    }
}