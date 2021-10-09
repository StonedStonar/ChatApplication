package no.stonedstonar.chatapplication.model.exception.user;

import java.io.Serializable;

/**
 * CouldNotLoginToUserException represents an exception that gets thrown when the password or username does not match
 * a user or the values of the user.
 *
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class CouldNotLoginToUserException extends Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotLoginToUserException class.
     * @param message the error message.
     */
    public CouldNotLoginToUserException(String message) {
        super(message);
    }
}