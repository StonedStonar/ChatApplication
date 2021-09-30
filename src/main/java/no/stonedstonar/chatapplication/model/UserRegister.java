package no.stonedstonar.chatapplication.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class UserRegister {

    private List<User> userList;

    /**
      * Makes an instance of the UserRegister class.
      */
    public UserRegister(){
        userList = new ArrayList<>();
    }

    /**
     * Adds a user to the register.
     * @param user the user you want to add.
     */
    public void addUser(User user){
        if (!checkIfUsernameIsTaken(user.getUsername())){
            userList.add(user);
        }else {
            throw new IllegalArgumentException("The user by the username " + user.getUsername() + " is already in the system.");
        }
    }

    /**
     * Removes a user from the register.
     * @param user the user you want to remove.
     */
    public void removeUser(User user){
        if (checkIfUsernameIsTaken(user.getUsername())){
            userList.remove(user);
        }else {
            throw new IllegalArgumentException("The user by the username " + user.getUsername() + " is already in the system.");
        }
    }

    //Todo: Kanskje denne skal fjernes og heller arves av en annen klasse så denne også kan holde på vanlige brukere.

    /**
     * Logs the user in if the passwords matches.
     * @param username the username of the user.
     * @param password the password of the user.
     * @return the user that matches that username and password.
     */
    public User login(String username, String password){
        checkString(password, "password");
        User user = getUserByUsername(username);
        if (user.checkPassword(password)){
            return user;
        }else {
            throw new IllegalArgumentException("The passwords does not match.");
        }
    }

    /**
     * Checks if there is a user by this username in the register.
     * @param username the username of the person.
     * @return <code>true</code> if a user already has this username.
     *         <code>false</code> if this username does not match any user.
     */
    public boolean checkIfUsernameIsTaken(String username){
        checkString(username, "username");
        return userList.stream().anyMatch(name -> name.equals(username));
    }

    /**
     * Gets the user that matches this username.
     * @param username the username you want.
     * @return the user that matches this username.
     */
    private User getUserByUsername(String username) {
        Optional<User> opUser = userList.stream().filter(name -> name.getUsername().equals(username)).findFirst();
        if (opUser.isPresent()) {
            return opUser.get();
        } else{
            throw new IllegalArgumentException("The user by the username " + username + " is not a part of this register.");
        }
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
}
