package no.stonedstonar.chatapplication.model.conversation.register;


import no.stonedstonar.chatapplication.model.conversation.Conversation;

/**
 * An interface that represents an observer that waits for new messages to arrive.
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public interface ConversationRegisterObserver {

    /**
     * Tells the observer that there is a new message.
     * @param conversation the new message.
     * @param removed <code>true</code> if the message was removed.
     *                <code>false</code> if the message was added.
     */
    void updateConversation(Conversation conversation, boolean removed);
}
