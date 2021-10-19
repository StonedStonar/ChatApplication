package no.stonedstonar.chatapplication.model.conversationregister;

import no.stonedstonar.chatapplication.model.conversation.Conversation;
import no.stonedstonar.chatapplication.model.conversation.PersonalConversation;
import no.stonedstonar.chatapplication.model.exception.conversation.CouldNotGetConversationException;

import java.io.Serializable;

/**
 * Represents a basic template for what a personal conversation register should hold.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public interface PersonalConversationRegister extends ConversationRegister, ObservableConversationRegister, Serializable {

    @Override
    PersonalConversation getConversationByNumber(long messageLogNumber) throws CouldNotGetConversationException;
}
