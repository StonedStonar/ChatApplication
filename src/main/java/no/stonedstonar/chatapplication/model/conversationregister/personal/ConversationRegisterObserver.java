package no.stonedstonar.chatapplication.model.conversationregister.personal;


import no.stonedstonar.chatapplication.model.conversation.ObservableConversation;

/**
 * An interface that represents an observer that waits for new messages to arrive.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public interface ConversationRegisterObserver {

    /**
     * Tells the observer that there is a new conversation.
     * @param observableConversation the new conversation.
     * @param removed <code>true</code> if the message was removed.
     *                <code>false</code> if the message was added.
     */
    void updateConversation(ObservableConversation observableConversation, boolean removed);
}
