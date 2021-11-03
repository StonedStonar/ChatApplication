package no.stonedstonar.chatapplication.model.exception.conversation;

import java.io.Serializable;

/**
 * CouldNotGetConversationException represents an exception that gets thrown when
 * @author Steinar Hjelle Midthus
 * @version 0.2
 */
public class CouldNotGetConversationException extends Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotGetConversationException class.
     * @param message the error message.
     */
    public CouldNotGetConversationException(String message) {
        super(message);

    }
}