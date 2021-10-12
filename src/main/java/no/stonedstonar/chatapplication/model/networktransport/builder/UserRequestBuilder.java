package no.stonedstonar.chatapplication.model.networktransport.builder;

import no.stonedstonar.chatapplication.model.networktransport.UserRequest;

/**
 * A builder that makes an user request. This can be used to make a new user or check a username.
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class UserRequestBuilder {

    private String username;

    private String password;

    private boolean login;

    private boolean newUser;

    private boolean checkUsername;



    /**
     * Makes an instance of the UserRequestBuilder class.
     * Sets all the boolean values to false as a standard.
     */
    public UserRequestBuilder(){
        username = "";
        password = "";
        login = false;
        newUser = false;
        checkUsername = false;
    }

    /**
     * Sets the username of the user.
     * @param username the username of the user.
     * @return this builder object
     */
    public UserRequestBuilder setUsername(String username) {
        checkString(username, "username");
        this.username = username;
        return this;
    }

    /**
     * Sets the username of the user.
     * @param password the password the user wants.
     * @return this builder object
     */
    public UserRequestBuilder setPassword(String password){
        checkString(password, "password");
        this.password = password;
        return this;
    }

    /**
     * Says if this user request wants to log in or not.
     * All the other booleans will be set to false if this is set to true.
     * @param valid <code>true</code> if the user wants to log in.
     *              <code>false</code> if the user doesn't want to log in.
     * @return this builder object
     */
    public UserRequestBuilder setLogin(boolean valid){
        login = valid;
        if (valid){
            checkUsername = false;
            newUser = false;
        }
        return this;
    }

    /**
     * Says if this user request wants to make a new user.
     * All the other booleans will be set to false if this is set to true.
     * @param valid <code>true</code> if the user wants to make a new user.
     *              <code>false</code> if the user does not want to make a new account.
     * @return this builder object
     */
    public UserRequestBuilder setNewUser(boolean valid){
        newUser = valid;
        if (valid){
            login = false;
            checkUsername = false;
        }
        return this;
    }

    /**
     * Says if the request is for checking a username.
     * All the other booleans will be set to false if this is set to true.
     * @param valid <code>true</code> if it should check the username.
     *              <code>false</code> if it should not check the username.
     * @return this builder object
     */
    public UserRequestBuilder setCheckUsername(boolean valid){
        checkUsername = valid;
        if (valid){
            login = false;
            newUser = false;
        }
        return this;
    }

    /**
     * Builds the object and returns it.
     */
    public UserRequest build(){
        UserRequest userRequest = new UserRequest(this);
        return userRequest;
    }

    /**
     * Checks if a string is of a valid format or not.
     * @param stringToCheck the string you want to check.
     * @param errorPrefix the error the exception should have if the string is invalid.
     */
    public void checkString(String stringToCheck, String errorPrefix){
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

    /**
     * Gets the username of the builder.
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password of the builder.
     * @return gets the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the boolean if its a login from the builder.
     * @return <code>true</code> if the person wants to login.
     *         <code>false</code> if the person does not want to login.
     */
    public boolean isLogin() {
        return login;
    }

    /**
     * Gets if the boolean if the builder is a new user.
     * @return <code>true</code> if the person wants to make a new user.
     *         <code>false</code> if the person does not want to make a new user.
     */
    public boolean isNewUser() {
        return newUser;
    }

    /**
     * Gets the boolean if the builder is for checking a username.
     * @return <code>true</code> if the person wants to check for useranme.
     *         <code>false</code> if the person does not want to check for username.
     */
    public boolean isCheckUsername() {
        return checkUsername;
    }
}
