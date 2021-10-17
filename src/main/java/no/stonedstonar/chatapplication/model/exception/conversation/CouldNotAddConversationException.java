package no.stonedstonar.chatapplication.model.exception.conversation;

import java.io.Serializable;

/**
 * CouldNotAddConversationException represents an exception that gets thrown when a conversation could not be added.
 * @author Steinar Hjelle Midthus
 * @version 0.2
 */
public class CouldNotAddConversationException extends Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotAddConversationException class.
     * @param message the error message.
     */
    public CouldNotAddConversationException(String message) {
        super(message);

    }
}