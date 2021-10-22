package no.stonedstonar.chatapplication.model.conversationregister.personal;

import no.stonedstonar.chatapplication.model.conversation.PersonalConversation;
import no.stonedstonar.chatapplication.model.conversationregister.ConversationRegister;
import no.stonedstonar.chatapplication.model.conversationregister.personal.ObservableConversationRegister;
import no.stonedstonar.chatapplication.model.exception.conversation.CouldNotAddConversationException;
import no.stonedstonar.chatapplication.model.exception.conversation.CouldNotGetConversationException;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a basic template for what a personal conversation register should hold.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public interface PersonalConversationRegister extends ConversationRegister, ObservableConversationRegister, Serializable {

    @Override
    PersonalConversation getConversationByNumber(long messageLogNumber) throws CouldNotGetConversationException;

    /**
     * Adds a conversation to the personal conversation.
     * @param personalConversation the new conversation.
     * @throws CouldNotAddConversationException gets thrown if a conversation is already in the register.
     */
    void addConversation(PersonalConversation personalConversation) throws CouldNotAddConversationException;

    /**
     * Gets all the conversation numbers of the personal register.
     * @return a list with all the conversation numbers.
     */
    List<Long> getAllConversationNumbers();
}
