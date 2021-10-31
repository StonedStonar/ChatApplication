package no.stonedstonar.chatapplication.model.user;

/**
 * Represents a user of the program.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class EndUser implements User {

    private String username;

    private String password;

    /**
      * Makes an instance of the User class.
     * @param username the username the user should have.
     * @param password the password the user should have.
      */
    public EndUser(String username, String password){
        checkString(username, "username");
        checkString(password, "password");
        this.password = password;
        this.username = username;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username, String password) {
        checkString(username, "username");
        checkString(password, "password");
        if (checkPassword(password)){
            this.username = username;
        }else {
            throw new IllegalArgumentException("The passwords does not match.");
        }
    }

    @Override
    public void setPassword(String newPassword, String oldPassword) {
        checkString(newPassword, "new password");
        if (checkPassword(oldPassword)){
            password = newPassword;
        }else {
            throw new IllegalArgumentException("The passwords does not match.");
        }
    }

    @Override
    public boolean checkPassword(String password){
        checkString(password, "input password");
        boolean valid = false;
        if (password.equals(this.password)){
            valid = true;
        }
        return valid;
    }

    /**
     * Checks if a string is of a valid format or not.
     * @param stringToCheck the string you want to check.
     * @param errorPrefix the error the exception should have if the string is invalid.
     */
    private void checkString(String stringToCheck, String errorPrefix){
        checkIfObjectIsNull(stringToCheck, errorPrefix);
        if (stringToCheck.isEmpty()){
            throw new IllegalArgumentException("The " + errorPrefix + " cannot be empty.");
        }
    }

    /**
     * Checks if an object is null.
     * @param object the object you want to check.
     * @param error the error message the exception should have.
     */
    private void checkIfObjectIsNull(Object object, String error){
        if (object == null){
            throw new IllegalArgumentException("The " + error + " cannot be null.");
        }
    }
}
