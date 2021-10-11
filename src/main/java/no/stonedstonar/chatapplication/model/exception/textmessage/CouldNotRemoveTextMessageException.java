package no.stonedstonar.chatapplication.model.exception.textmessage;

import java.io.Serializable;

/**
 * CouldNotRemoveTextMessageException represents an exception that gets thrown when a text message could not be removed.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class CouldNotRemoveTextMessageException extends Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotRemoveTextMessageException class.
     * @param message the error message.
     */
    public CouldNotRemoveTextMessageException(String message) {
        super(message);
    }
}