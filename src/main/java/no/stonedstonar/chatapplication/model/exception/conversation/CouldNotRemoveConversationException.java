package no.stonedstonar.chatapplication.model.exception.conversation;

import java.io.Serializable;

/**
 * CouldNotRemoveConversationException represents an exception that gets thrown when a conversation could not be removed.
 * @author Steinar Hjelle Midthus
 * @version 0.2
 */
public class CouldNotRemoveConversationException extends Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotRemoveConversationException class.
     * @param message the error message.
     */
    public CouldNotRemoveConversationException(String message) {
        super(message);
    }
}