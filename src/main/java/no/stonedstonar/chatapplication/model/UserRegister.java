package no.stonedstonar.chatapplication.model;

import no.stonedstonar.chatapplication.model.exception.user.CouldNotAddUserException;
import no.stonedstonar.chatapplication.model.exception.user.CouldNotGetUserException;
import no.stonedstonar.chatapplication.model.exception.user.CouldNotLoginToUserException;
import no.stonedstonar.chatapplication.model.exception.user.CouldNotRemoveUserException;
import no.stonedstonar.chatapplication.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a register that can hold users.
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
     * @throws CouldNotAddUserException gets thrown if the user could not be added to the register.
     */
    public void addUser(User user) throws CouldNotAddUserException {
        checkIfObjectIsNull(user, "user");
        if (!checkIfUsernameIsTaken(user.getUsername())){
            userList.add(user);
        }else {
            throw new CouldNotAddUserException("The user by the username " + user.getUsername() + " is already in the system.");
        }
    }

    /**
     * Removes a user from the register.
     * @param user the user you want to remove.
     * @throws CouldNotRemoveUserException gets thrown if the user could not be removed.
     */
    public void removeUser(User user) throws CouldNotRemoveUserException {
        checkIfObjectIsNull(user, "user");
        if (checkIfUsernameIsTaken(user.getUsername())){
            userList.remove(user);
        }else {
            throw new CouldNotRemoveUserException("The user by the username " + user.getUsername() + " is already in the system.");
        }
    }

    //Todo: Kanskje denne skal fjernes og heller arves av en annen klasse så denne også kan holde på vanlige brukere.

    /**
     * Logs the user in if the passwords matches.
     * @param username the username of the user.
     * @param password the password of the user.
     * @return the user that matches that username and password.
     * @throws CouldNotLoginToUserException gets thrown if the details of the user does not match.
     */
    public User login(String username, String password) throws CouldNotLoginToUserException {
        checkString(password, "password");
        checkString(username, "username");
        try {
            User user = getUserByUsername(username);
            if (user.checkPassword(password)){
                return user;
            }else {
                throw new CouldNotLoginToUserException("The passwords does not match.");
            }
        }catch (CouldNotGetUserException exception){
            throw new CouldNotLoginToUserException("The username is not in this register.");
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
        return userList.stream().anyMatch(name -> name.getUsername().equals(username));
    }

    /**
     * Gets the user that matches this username.
     * @param username the username you want.
     * @return the user that matches this username.
     * @throws CouldNotGetUserException gets thrown if the user could not be found.
     */
    private User getUserByUsername(String username) throws CouldNotGetUserException {
        Optional<User> opUser = userList.stream().filter(name -> name.getUsername().equals(username)).findFirst();
        if (opUser.isPresent()) {
            return opUser.get();
        } else{
            throw new CouldNotGetUserException("The user by the username " + username + " is not a part of this register.");
        }
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
