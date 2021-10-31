package no.stonedstonar.chatapplication.model.userregister;

import no.stonedstonar.chatapplication.model.exception.user.CouldNotAddUserException;
import no.stonedstonar.chatapplication.model.exception.user.CouldNotGetUserException;
import no.stonedstonar.chatapplication.model.exception.user.CouldNotLoginToUserException;
import no.stonedstonar.chatapplication.model.exception.user.CouldNotRemoveUserException;
import no.stonedstonar.chatapplication.model.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a register that can hold users.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class NormalUserRegister implements UserRegister {

    private final List<User> basicEndUserList;

    /**
      * Makes an instance of the UserRegister class.
      */
    public NormalUserRegister(){
        basicEndUserList = new ArrayList<>();
    }

    @Override
    public void addUser(User userToAdd) throws CouldNotAddUserException {
        checkIfObjectIsNull(userToAdd, "end user");
        if (!checkIfUsernameIsTaken(userToAdd.getUsername())){
            basicEndUserList.add(userToAdd);
        }else {
            throw new CouldNotAddUserException("The user by the username " + userToAdd.getUsername() + " is already in the system.");
        }
    }

    @Override
    public void removeUser(User userToRemove) throws CouldNotRemoveUserException {
        checkIfObjectIsNull(userToRemove, "end user");
        if (checkIfUsernameIsTaken(userToRemove.getUsername())){
            basicEndUserList.remove(userToRemove);
        }else {
            throw new CouldNotRemoveUserException("The user by the username " + userToRemove.getUsername() + " is already in the system.");
        }
    }

    //Todo: Kanskje denne skal fjernes og heller arves av en annen klasse så denne også kan holde på vanlige brukere.

    @Override
    public User login(String username, String password) throws CouldNotLoginToUserException {
        checkString(password, "password");
        checkString(username, "username");
        try {
            User basicEndUser = getUserByUsername(username);
            if (basicEndUser.checkPassword(password)){
                return basicEndUser;
            }else {
                throw new CouldNotLoginToUserException("The passwords does not match.");
            }
        }catch (CouldNotGetUserException exception){
            throw new CouldNotLoginToUserException("The username is not in this register.");
        }
    }

    @Override
    public boolean checkIfUsernameIsTaken(String username){
        checkString(username, "username");
        return basicEndUserList.stream().anyMatch(name -> name.getUsername().equals(username));
    }

    /**
     * Gets the user that matches this username.
     * @param username the username you want.
     * @return the user that matches this username.
     * @throws CouldNotGetUserException gets thrown if the user could not be found.
     */
    private User getUserByUsername(String username) throws CouldNotGetUserException {
        Optional<User> opUser = basicEndUserList.stream().filter(name -> name.getUsername().equals(username)).findFirst();
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
