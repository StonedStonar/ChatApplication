package no.stonedstonar.chatapplication.model.exception.user;

import java.io.Serializable;

/**
 * CouldNotAddUserException represents an exception that gets thrown when a user could not be added.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class CouldNotAddUserException extends Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotAddUserException class.
     *
     * @param message the error message.
     */
    public CouldNotAddUserException(String message) {
        super(message);

    }
}