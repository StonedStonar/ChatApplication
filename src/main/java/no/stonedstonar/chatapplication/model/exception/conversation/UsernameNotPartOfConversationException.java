package no.stonedstonar.chatapplication.model.exception.conversation;

import java.io.Serializable;

/**
 * UsernameNotPartOfConversationException represents an exception that gets thrown when gets thrown if the user is not a part of the conversation.
 * @author Steinar Hjelle Midthus
 * @version 0.2
 */
public class UsernameNotPartOfConversationException extends Exception implements Serializable {

    /**
     * Makes an instance of the UsernameNotPartOfConversationException class.
     * @param message the error message.
     */
    public UsernameNotPartOfConversationException(String message) {
        super(message);
    }
}