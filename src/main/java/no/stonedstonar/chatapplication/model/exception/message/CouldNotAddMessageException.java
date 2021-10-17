package no.stonedstonar.chatapplication.model.exception.message;

import java.io.Serializable;

/**
 * CouldNotAddTextMessageException represents an exception that gets thrown when a message could not be added.
 * @author Steinar Hjelle Midthus
 * @version 0.2
 */
public class CouldNotAddMessageException extends Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotAddMessageException class.
     * @param message the error message.
     */
    public CouldNotAddMessageException(String message) {
        super(message);

    }
}