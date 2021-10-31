package no.stonedstonar.chatapplication.model.userregister;

import no.stonedstonar.chatapplication.model.exception.user.CouldNotAddUserException;
import no.stonedstonar.chatapplication.model.exception.user.CouldNotLoginToUserException;
import no.stonedstonar.chatapplication.model.exception.user.CouldNotRemoveUserException;
import no.stonedstonar.chatapplication.model.user.User;
import java.io.Serializable;

/**
 * Represents a user register and its basic methods.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public interface UserRegister extends Serializable {

    /**
     * Logs the user in if the passwords matches.
     * @param username the username of the user.
     * @param password the password of the user.
     * @return the user that matches that username and password.
     * @throws CouldNotLoginToUserException gets thrown if the details of the user does not match.
     */
    User login(String username, String password) throws CouldNotLoginToUserException;

    /**
     * Checks if there is a user by this username in the register.
     * @param username the username of the person.
     * @return <code>true</code> if a user already has this username.
     *         <code>false</code> if this username does not match any user.
     */
    boolean checkIfUsernameIsTaken(String username);

    /**
     * Adds a user to the register.
     * @param user the user you want to add.
     * @throws CouldNotAddUserException gets thrown if the user could not be added to the register.
     */
    void addUser(User user) throws CouldNotAddUserException;

    /**
     * Removes a user from the register.
     * @param user the user you want to remove.
     * @throws CouldNotRemoveUserException gets thrown if the user could not be removed.
     */
    void removeUser(User user) throws CouldNotRemoveUserException;
}
