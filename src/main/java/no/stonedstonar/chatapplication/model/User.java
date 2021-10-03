package no.stonedstonar.chatapplication.model;

import java.io.Serializable;

/**
 *
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class User implements Serializable{

    private String username;

    private String password;

    /**
      * Makes an instance of the User class.
     * @param username the username the user should have.
     * @param password the password the user should have.
      */
    public User(String username, String password){
        checkString(username, "username");
        checkString(password, "password");
        this.password = password;
        this.username = username;
    }

    /**
     * Gets the username of this user.
     * @return the username of this user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username to a new value.
     * @param username the new useranme of the person.
     */
    public void setUsername(String username, String password) {
        checkString(username, "username");
        checkString(password, "password");
        if (checkPassword(password)){
            this.username = username;
        }else {
            throw new IllegalArgumentException("The passwords does not match.");
        }
    }

    /**
     * Sets the password to a new password if the old one matches the set password.
     * @param newPassword the new password of the user.
     * @param oldPassword the old password of the user.
     */
    public void setPassword(String newPassword, String oldPassword) {
        checkString(newPassword, "new password");
        if (checkPassword(oldPassword)){
            password = newPassword;
        }else {
            throw new IllegalArgumentException("The passwords does not match.");
        }
    }

    /**
     * Checks if the password is correct.
     * @param password the password you want to check.
     * @return <code>true</code> if the passwords match.
     *         <code>false</code> if the passwords mismatch.
     */
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
