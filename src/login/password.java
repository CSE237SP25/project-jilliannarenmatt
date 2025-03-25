package login;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for handling password validation and security operations.
 * Includes methods to check if passwords meet security requirements.
 */
public class password {
    
    // Password validation constants
    private static final int MIN_LENGTH = 8;
    private static final String SPECIAL_CHARS = "!@#$%^&*()_+-=[]{}|;':,./<>?";
    
    /**
     * Validates if a password meets all security requirements:
     * - Minimum length (8 characters)
     * - Contains at least one uppercase letter
     * - Contains at least one lowercase letter
     * - Contains at least one digit
     * - Contains at least one special character
     * 
     * @param password The password to validate
     * @return true if password meets all requirements, false otherwise
     */
    public static boolean isValid(String password) {
        return hasMinimumLength(password) &&
               hasUpperCase(password) &&
               hasLowerCase(password) &&
               hasDigit(password) &&
               hasSpecialChar(password);
    }
    
    /**
     * Checks if password meets minimum length requirement.
     * 
     * @param password The password to check
     * @return true if password is long enough, false otherwise
     */
    private static boolean hasMinimumLength(String password) {
        return password != null && password.length() >= MIN_LENGTH;
    }
    
    /**
     * Checks if password contains at least one uppercase letter.
     * 
     * @param password The password to check
     * @return true if password contains uppercase, false otherwise
     */
    private static boolean hasUpperCase(String password) {
        if (password == null) return false;
        
        Pattern pattern = Pattern.compile("[A-Z]");
        Matcher matcher = pattern.matcher(password);
        
        return matcher.find();
    }
    
    /**
     * Checks if password contains at least one lowercase letter.
     * 
     * @param password The password to check
     * @return true if password contains lowercase, false otherwise
     */
    private static boolean hasLowerCase(String password) {
        if (password == null) return false;
        
        Pattern pattern = Pattern.compile("[a-z]");
        Matcher matcher = pattern.matcher(password);
        
        return matcher.find();
    }
    
    /**
     * Checks if password contains at least one digit.
     * 
     * @param password The password to check
     * @return true if password contains digit, false otherwise
     */
    private static boolean hasDigit(String password) {
        if (password == null) return false;
        
        Pattern pattern = Pattern.compile("[0-9]");
        Matcher matcher = pattern.matcher(password);
        
        return matcher.find();
    }
    
    /**
     * Checks if password contains at least one special character.
     * 
     * @param password The password to check
     * @return true if password contains special character, false otherwise
     */
    private static boolean hasSpecialChar(String password) {
        if (password == null) return false;
        
        Pattern pattern = Pattern.compile("[" + Pattern.quote(SPECIAL_CHARS) + "]");
        Matcher matcher = pattern.matcher(password);
        
        return matcher.find();
    }
    
    /**
     * Returns a string describing password requirements.
     * 
     * @return Password requirement description
     */
    public static String getRequirements() {
        return "Password must contain at least " + MIN_LENGTH + " characters, including:\n" +
               "- At least one uppercase letter (A-Z)\n" +
               "- At least one lowercase letter (a-z)\n" +
               "- At least one digit (0-9)\n" +
               "- At least one special character (" + SPECIAL_CHARS + ")";
    }
    
    /**
     * Gets detailed validation errors for a password.
     * 
     * @param password The password to check
     * @return String containing all validation errors, or empty string if valid
     */
    public static String getValidationErrors(String password) {
        StringBuilder errors = new StringBuilder();
        
        if (!hasMinimumLength(password)) {
            errors.append("- Password must be at least ").append(MIN_LENGTH).append(" characters long\n");
        }
        
        if (!hasUpperCase(password)) {
            errors.append("- Password must contain at least one uppercase letter\n");
        }
        
        if (!hasLowerCase(password)) {
            errors.append("- Password must contain at least one lowercase letter\n");
        }
        
        if (!hasDigit(password)) {
            errors.append("- Password must contain at least one digit\n");
        }
        
        if (!hasSpecialChar(password)) {
            errors.append("- Password must contain at least one special character\n");
        }
        
        return errors.toString();
    }
}