package no.stonedstonar.chatapplication.model.exception.messagelog;

import java.io.Serializable;

/**
 * CouldNotRemoveMessageLogException represents an exception that gets thrown when a message log could not be removed.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class CouldNotRemoveConversationException extends Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotRemoveMessageLogException class.
     * @param message the error message.
     */
    public CouldNotRemoveConversationException(String message) {
        super(message);

    }
}