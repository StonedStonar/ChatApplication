package no.stonedstonar.chatapplication.model.exception.messagelog;

import java.io.Serializable;

/**
 * CouldNotGetMessageLogException represents an exception that gets thrown when a message log could not be located.
 * @author Steinar Hjelle Midthus
 * @version 0.2
 */
public class CouldNotGetMessageLogException extends Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotGetMessageLogException class.
     * @param message the error message.
     */
    public CouldNotGetMessageLogException(String message) {
        super(message);

    }
}