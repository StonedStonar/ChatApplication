package no.stonedstonar.chatapplication.network.requests;

import no.stonedstonar.chatapplication.network.requests.builder.UserRequestBuilder;

import java.io.Serializable;

/**
 * This is an object that contains a request the user wants to make to the server.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class UserRequest implements Serializable {

    private String username;

    private String password;

    private boolean login;

    private boolean newUser;

    private boolean checkUsername;

    /**
      * Makes an instance of the UserRequest class.
      */
    public UserRequest(UserRequestBuilder userRequestBuilder){
        username = userRequestBuilder.getUsername();
        password = userRequestBuilder.getPassword();
        login = userRequestBuilder.isLogin();
        newUser = userRequestBuilder.isNewUser();
        checkUsername = userRequestBuilder.isCheckUsername();

    }

    /**
     * Gets the username of the request.
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password of the request.
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets if the request is of a login kind.
     * @return <code>true</code> if the user wants to make a new user.
     *         <code>false</code> if the user does not want to make a new account.
     */
    public boolean isLogin() {
        return login;
    }

    /**
     * Gets if the request is about making a new user.
     * @return <code>true</code> if the user wants to make a new user.
     *         <code>false</code> if the user does not want to make a new account.
     */
    public boolean isNewUser() {
        return newUser;
    }

    /**
     * Gets if the request is about checking a username.
     * @return <code>true</code> if it should check the username.
     *         <code>false</code> if it should not check the username.
     */
    public boolean isCheckUsername() {
        return checkUsername;
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
    public void checkIfObjectIsNull(Object object, String error){
       if (object == null){
           throw new IllegalArgumentException("The " + error + " cannot be null.");
       }
    }

}
