package no.stonedstonar.chatapplication.model.exception.messagelog;

import java.io.Serializable;

/**
 * CouldNotAddMessageLogException represents an exception that gets thrown when a message log could not be added.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class CouldNotAddMessageLogException extends Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotAddMessageLogException class.
     * @param message the error message.
     */
    public CouldNotAddMessageLogException(String message) {
        super(message);

    }
}