package login;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for handling username validation.
 */
public class username {
    
    // Username validation constants
    private static final int MIN_LENGTH = 4;
    private static final int MAX_LENGTH = 20;
    
    /**
     * Validates if a username meets all requirements:
     * - Must be between 4-20 characters
     * - Can only contain letters, numbers, and underscores
     * - Must start with a letter
     * 
     * @param username The username to validate
     * @return true if username meets all requirements, false otherwise
     */
    public static boolean isValid(String username) {
        if (username == null) {
            return false;
        }
        
        // Check length
        if (username.length() < MIN_LENGTH || username.length() > MAX_LENGTH) {
            return false;
        }
        
        // Check if starts with a letter
        if (!Character.isLetter(username.charAt(0))) {
            return false;
        }
        
        // Check allowed characters (letters, numbers, underscores)
        Pattern pattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]*$");
        Matcher matcher = pattern.matcher(username);
        
        return matcher.matches();
    }
    
    /**
     * Returns a string describing username requirements.
     * 
     * @return Username requirement description
     */
    public static String getRequirements() {
        return "Username must:\n" +
               "- Be between " + MIN_LENGTH + " and " + MAX_LENGTH + " characters long\n" +
               "- Start with a letter\n" +
               "- Contain only letters, numbers, and underscores";
    }
    
    /**
     * Gets validation errors for a username.
     * 
     * @param username The username to check
     * @return String containing validation errors, or empty string if valid
     */
    public static String getValidationErrors(String username) {
        StringBuilder errors = new StringBuilder();
        
        if (username == null || username.isEmpty()) {
            return "Username cannot be empty";
        }
        
        if (username.length() < MIN_LENGTH || username.length() > MAX_LENGTH) {
            errors.append("- Username must be between ").append(MIN_LENGTH)
                  .append(" and ").append(MAX_LENGTH).append(" characters long\n");
        }
        
        if (!Character.isLetter(username.charAt(0))) {
            errors.append("- Username must start with a letter\n");
        }
        
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_]*$");
        Matcher matcher = pattern.matcher(username);
        if (!matcher.matches()) {
            errors.append("- Username can only contain letters, numbers, and underscores\n");
        }
        
        return errors.toString();
    }
}