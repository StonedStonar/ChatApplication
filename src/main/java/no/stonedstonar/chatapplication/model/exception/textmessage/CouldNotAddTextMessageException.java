package no.stonedstonar.chatapplication.model.exception.textmessage;

import java.io.Serializable;

/**
 * CouldNotAddTextMessageException represents an exception that gets thrown when a text message could not be added.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class CouldNotAddTextMessageException extends Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotAddTextMessageException class.
     * @param message the error message.
     */
    public CouldNotAddTextMessageException(String message) {
        super(message);

    }
}