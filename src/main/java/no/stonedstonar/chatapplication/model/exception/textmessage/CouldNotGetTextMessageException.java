package no.stonedstonar.chatapplication.model.exception.textmessage;

import java.io.Serializable;

/**
 * CouldNotGetTextMessageException represents an exception that gets thrown when a text message could not be located.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class CouldNotGetTextMessageException extends Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotGetTextMessageException class.
     * @param message the error message.
     */
    public CouldNotGetTextMessageException(String message) {
        super(message);

    }
}