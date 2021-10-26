package no.stonedstonar.chatapplication.model.userregister;


import no.stonedstonar.chatapplication.model.exception.user.CouldNotAddUserException;
import no.stonedstonar.chatapplication.model.exception.user.CouldNotLoginToUserException;
import no.stonedstonar.chatapplication.model.exception.user.CouldNotRemoveUserException;
import no.stonedstonar.chatapplication.model.user.EndUser;

/**
 * Represents what an end user register should have of basic methods.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public interface EndUserRegister extends UserRegister {

    /**
     * Adds a user to the register.
     * @param endUser the user you want to add.
     * @throws CouldNotAddUserException gets thrown if the user could not be added to the register.
     */
    void addUser(EndUser endUser) throws CouldNotAddUserException;

    /**
     * Removes a user from the register.
     * @param endUser the user you want to remove.
     * @throws CouldNotRemoveUserException gets thrown if the user could not be removed.
     */
    void removeUser(EndUser endUser) throws CouldNotRemoveUserException;

    @Override
    EndUser login(String username, String password) throws CouldNotLoginToUserException;
}
