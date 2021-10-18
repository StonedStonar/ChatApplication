package no.stonedstonar.chatapplication.model.conversation.register;

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
     * Gets the conversations the username is a part of.
     * @param username the username that wants its conversations.
     * @return the conversations that belongs to this username in a list.
     */
    List<Conversation> getAllConversationsOfUsername(String username);

    /**
     * Adds a new conversation based on a list of names that are in it.
     * @param usernames list with all the usernames that wants to be in the conversation.
     * @param nameOfConversation the name the conversation should have.
     * @return the conversation that was just added to the register.
     * @throws CouldNotAddMessageLogException gets thrown if the conversation is already in the register.
     * @throws CouldNotAddMemberException gets thrown if a member could not be added.
     */
    Conversation addNewConversationWithUsernames(List<String> usernames, String nameOfConversation) throws CouldNotAddMemberException, CouldNotAddConversationException;

    /**
     * Gets the conversation that matches the conversation number.
     * @param messageLogNumber the number that the conversation has.
     * @return the conversation that matches this number.
     * @throws CouldNotGetMessageLogException gets thrown if the conversation could not be found.
     */
    Conversation getConversationByNumber(long messageLogNumber) throws CouldNotGetConversationException;
}
