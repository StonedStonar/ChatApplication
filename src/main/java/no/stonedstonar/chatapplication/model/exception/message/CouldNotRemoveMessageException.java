package no.stonedstonar.chatapplication.model.exception.message;

import java.io.Serializable;

/**
 * CouldNotRemoveTextMessageException represents an exception that gets thrown when a text message could not be removed.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class CouldNotRemoveMessageException extends Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotRemoveMessageException class.
     * @param message the error message.
     */
    public CouldNotRemoveMessageException(String message) {
        super(message);
    }
}