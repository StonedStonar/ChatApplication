package no.stonedstonar.chatapplication.model.conversationregister.server;

import no.stonedstonar.chatapplication.model.conversation.ServerConversation;
import no.stonedstonar.chatapplication.model.conversationregister.ConversationRegister;
import no.stonedstonar.chatapplication.model.conversationregister.personal.NormalPersonalConversationRegister;
import no.stonedstonar.chatapplication.model.exception.conversation.CouldNotAddConversationException;
import no.stonedstonar.chatapplication.model.exception.conversation.CouldNotGetConversationException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotAddMemberException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotAddMessageLogException;
import no.stonedstonar.chatapplication.model.member.Member;

import java.util.List;

/**
 * Represents what a server conversation register should have of methods.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public interface ServerConversationRegister extends ConversationRegister {

    @Override
    ServerConversation getConversationByNumber(long messageLogNumber) throws CouldNotGetConversationException;

    /**
     * Gets all the conversations of a user and makes it into a personal register.
     * @return the personal register with all the message logs.
     */
    NormalPersonalConversationRegister getAllConversationsUserHasAndMakePersonalRegister(String username);

    /**
     * Gets the conversations the username is a part of.
     * @param username the username that wants its conversations.
     * @return the conversations that belongs to this username in a list.
     */
    List<ServerConversation> getAllConversationsOfUsername(String username);

    /**
     * Adds a new conversation based on a list of names that are in it.
     * @param members list with all the members of this conversation.
     * @param nameOfConversation the name the conversation should have.
     * @return the conversation that was just added to the register.
     * @throws CouldNotAddMessageLogException gets thrown if the conversation is already in the register.
     * @throws CouldNotAddMemberException gets thrown if a member could not be added.
     */
    ServerConversation addNewConversationWithUsernames(List<Member> members, String nameOfConversation) throws CouldNotAddMemberException, CouldNotAddConversationException;

}
