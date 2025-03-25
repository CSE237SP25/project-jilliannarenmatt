package login;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Class that manages user account creation and authentication.
 */
public class user {
    private String username;
    private String passwordHash;
    private static final String USER_FILE = "users.txt";
    private boolean isExistingUser; // Added to differentiate between constructors
    
    /**
     * Creates a new user with the specified username and password.
     * The password is stored as a hash, not in plain text.
     * 
     * @param usernameText The user's username
     * @param passwordText The user's password
     */
    public user(String usernameText, String passwordText) {
        this.username = usernameText;
        this.passwordHash = hashPassword(passwordText);
        this.isExistingUser = false;
    }
    
    /**
     * Constructor for loading an existing user from stored data.
     * Uses a flag parameter to distinguish from the other constructor.
     * 
     * @param usernameText The user's username
     * @param passwordHashText The user's already hashed password
     * @param isExisting Flag to indicate this is an existing user
     */
    private user(String usernameText, String passwordHashText, boolean isExisting) {
        this.username = usernameText;
        this.passwordHash = passwordHashText;
        this.isExistingUser = isExisting;
    }
    
    /**
     * Gets the username.
     * 
     * @return The username
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Creates a hash of the provided password.
     * 
     * @param passwordText The password to hash
     * @return The hashed password
     */
    private String hashPassword(String passwordText) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(
                    passwordText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            // Fallback to simple hashing if SHA-256 isn't available
            return String.valueOf(passwordText.hashCode());
        }
    }
    
    /**
     * Validates if the provided password matches the stored password hash.
     * 
     * @param passwordText The password to validate
     * @return true if password matches, false otherwise
     */
    public boolean validatePassword(String passwordText) {
        String hashedInput = hashPassword(passwordText);
        return passwordHash.equals(hashedInput);
    }
    
    /**
     * Checks if a username already exists in the system.
     * 
     * @param usernameText The username to check
     * @return true if username exists, false otherwise
     */
    public static boolean usernameExists(String usernameText) {
        List<user> users = loadAllUsers();
        for (user user : users) {
            if (user.getUsername().equalsIgnoreCase(usernameText)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Saves the user to the data file.
     * 
     * @return true if save was successful, false otherwise
     */
    public boolean saveUser() {
        try {
            File file = new File(USER_FILE);
            
            // Create the file if it doesn't exist
            if (!file.exists()) {
                file.createNewFile();
            }
            
            // Check if user already exists
            List<user> existingUsers = loadAllUsers();
            for (user existingUser : existingUsers) {
                if (existingUser.getUsername().equalsIgnoreCase(this.username)) {
                    // User already exists - handle updating if needed
                    return false;
                }
            }
            
            // Add new user
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write(username + "," + passwordHash);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error saving user: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Loads all users from the data file.
     * 
     * @return List of all users
     */
    public static List<user> loadAllUsers() {
        List<user> users = new ArrayList<>();
        File file = new File(USER_FILE);
        
        if (!file.exists()) {
            return users;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String usernameText = parts[0];
                    String passwordHashText = parts[1];
                    users.add(new user(usernameText, passwordHashText, true));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
        
        return users;
    }
    
    /**
     * Authenticates a user login attempt.
     * 
     * @param usernameText The username to authenticate
     * @param passwordText The password to authenticate
     * @return The authenticated User object if successful, null otherwise
     */
    public static user login(String usernameText, String passwordText) {
        List<user> users = loadAllUsers();
        for (user user : users) {
            if (user.getUsername().equalsIgnoreCase(usernameText) && user.validatePassword(passwordText)) {
                return user;
            }
        }
        return null;
    }
    
    /**
     * Creates a new user account if it passes all validation.
     * 
     * @param usernameText The username for the new account
     * @param passwordText The password for the new account
     * @return The new User object if creation was successful, null otherwise
     */
    public static user createAccount(String usernameText, String passwordText) {
        // Validate username and password
        if (!login.username.isValid(usernameText)) {
            return null;
        }
        
        if (!login.password.isValid(passwordText)) {
            return null;
        }
        
        // Check if username already exists
        if (usernameExists(usernameText)) {
            return null;
        }
        
        // Create and save new user
        user newUser = new user(usernameText, passwordText);
        if (newUser.saveUser()) {
            return newUser;
        }
        
        return null;
    }
}