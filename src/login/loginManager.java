package login;

import java.util.Scanner;

/**
 * Manages user login and registration functionality.
 * This class provides an interface for users to login or create a new account.
 */
public class loginManager {
    private Scanner scanner;
    private user currentUser;
    
    /**
     * Creates a new LoginManager.
     */
    public loginManager() {
        scanner = new Scanner(System.in);
        currentUser = null;
    }
    
    /**
     * Starts the login/registration process.
     * 
     * @return The authenticated user if login/registration is successful, null otherwise
     */
    public user start() {
        boolean running = true;
        
        while (running) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    currentUser = loginUser();
                    if (currentUser != null) {
                        return currentUser;
                    }
                    break;
                case 2:
                    currentUser = registerUser();
                    if (currentUser != null) {
                        return currentUser;
                    }
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        
        return null;
    }
    
    /**
     * Displays the login/registration menu.
     */
    private void displayMenu() {
        System.out.println("\n===== BANKING SYSTEM =====");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("0. Exit");
        System.out.println("=========================");
    }
    
    /**
     * Handles the user login process.
     * 
     * @return The authenticated user if login is successful, null otherwise
     */
    private user loginUser() {
        System.out.println("\n----- LOGIN -----");
        String usernameText = getStringInput("Enter your username: ");
        String passwordText = getStringInput("Enter your password: ");
        
        user loggedInUser = user.login(usernameText, passwordText);
        if (loggedInUser != null) {
            System.out.println("Login successful!");
            return loggedInUser;
        } else {
            System.out.println("Invalid username or password.");
            return null;
        }
    }
    
    /**
     * Handles the user registration process.
     * 
     * @return The newly created user if registration is successful, null otherwise
     */
    private user registerUser() {
        System.out.println("\n----- REGISTER -----");
        System.out.println(username.getRequirements());
        
        String usernameText = "";
        boolean validUsername = false;
        
        while (!validUsername) {
            usernameText = getStringInput("Choose a username: ");
            if (username.isValid(usernameText)) {
                validUsername = true;
            } else {
                if (user.usernameExists(usernameText)) {
                    System.out.println("Username already exists. Please choose another one.");
                } else {
                    System.out.println("Invalid username. " + username.getRequirements());
                }
            }
        }
        
        System.out.println(password.getRequirements());
        
        String passwordText = "";
        boolean validPassword = false;
        
        while (!validPassword) {
            passwordText = getStringInput("Choose a password: ");
            if (password.isValid(passwordText)) {
                validPassword = true;
            } else {
                System.out.println("Invalid password.");
                System.out.println(password.getValidationErrors(passwordText));
            }
        }
        
        String confirmPassword = getStringInput("Confirm your password: ");
        if (!passwordText.equals(confirmPassword)) {
            System.out.println("Passwords do not match. Registration failed.");
            return null;
        }
        
        user newUser = user.createAccount(usernameText, passwordText);
        if (newUser != null) {
            System.out.println("Registration successful!");
            return newUser;
        } else {
            System.out.println("Error registering user. Please try again.");
            return null;
        }
    }
    
    /**
     * Gets string input from the user.
     * 
     * @param prompt The prompt to display
     * @return The user's input
     */
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
    
    /**
     * Gets integer input from the user.
     * 
     * @param prompt The prompt to display
     * @return The user's input as an integer
     */
    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    /**
     * Closes the scanner.
     */
    public void close() {
        scanner.close();
    }
}