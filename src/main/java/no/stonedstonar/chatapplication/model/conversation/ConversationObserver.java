package no.stonedstonar.chatapplication.model.conversation;

import no.stonedstonar.chatapplication.model.message.Message;

/**
 * Represents an object that wants a conversations updates.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public interface ConversationObserver {

    /**
     * Tells the observer that there is a new message.
     * @param message the new message.
     * @param removed <code>true</code> if the message log was removed.
     *                <code>false</code> if the message log was added.
     */
    void updateConversationMessage(Message message, boolean removed);
}
