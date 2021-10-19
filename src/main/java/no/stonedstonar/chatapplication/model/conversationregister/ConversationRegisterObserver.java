package no.stonedstonar.chatapplication.model.conversationregister;


import no.stonedstonar.chatapplication.model.conversation.PersonalConversation;

/**
 * An interface that represents an observer that waits for new messages to arrive.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public interface ConversationRegisterObserver {

    /**
     * Tells the observer that there is a new conversation.
     * @param personalConversation the new conversation.
     * @param removed <code>true</code> if the message was removed.
     *                <code>false</code> if the message was added.
     */
    void updateConversation(PersonalConversation personalConversation, boolean removed);
}
