package no.stonedstonar.chatapplication.model.conversation;

import no.stonedstonar.chatapplication.model.member.Member;
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
     * @param conversationNumber the conversation number.
     */
    void updateConversationMessage(Message message, boolean removed, long conversationNumber);

    /**
     * Tells if there is a new member to update.
     * @param conversationNumber the conversation number of this conversation.
     */
    void updateMemberInConversation(long conversationNumber);
}
