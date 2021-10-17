package no.stonedstonar.chatapplication.model.exception.message;

import java.io.Serializable;

/**
 * CouldNotGetTextMessageException represents an exception that gets thrown when a message could not be located.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class CouldNotGetMessageException extends Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotGetMessageException class.
     * @param message the error message.
     */
    public CouldNotGetMessageException(String message) {
        super(message);

    }
}