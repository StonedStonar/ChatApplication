package no.stonedstonar.chatapplication.model.conversationregister;

import no.stonedstonar.chatapplication.model.conversation.Conversation;
import no.stonedstonar.chatapplication.model.exception.conversation.CouldNotAddConversationException;
import no.stonedstonar.chatapplication.model.exception.conversation.CouldNotGetConversationException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotAddMemberException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotAddMessageLogException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotGetMessageLogException;

import java.util.List;

/**
 * Represents the basic methods a ConversationRegister should hold.
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public interface ConversationRegister {

    /**
     * Gets the conversation that matches the conversation number.
     * @param messageLogNumber the number that the conversation has.
     * @return the conversation that matches this number.
     * @throws CouldNotGetMessageLogException gets thrown if the conversation could not be found.
     */
    Conversation getConversationByNumber(long messageLogNumber) throws CouldNotGetConversationException;
}
