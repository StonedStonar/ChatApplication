package no.stonedstonar.chatapplication.model.conversation;

import no.stonedstonar.chatapplication.model.membersregister.ConversationMembers;
import no.stonedstonar.chatapplication.model.exception.conversation.UsernameNotPartOfConversationException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotAddMemberException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotRemoveMemberException;
import no.stonedstonar.chatapplication.model.exception.message.CouldNotAddMessageException;
import no.stonedstonar.chatapplication.model.exception.message.CouldNotRemoveMessageException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotGetMessageLogException;
import no.stonedstonar.chatapplication.model.membersregister.Members;
import no.stonedstonar.chatapplication.model.message.Message;
import no.stonedstonar.chatapplication.model.messagelog.MessageLog;

import java.time.LocalDate;
import java.util.List;

/**
 * A conversation is a dialog between one or more people over a period.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public interface Conversation {

    /**
     * Gets the name of the conversation.
     * @return the name of the conversation.
     */
    String getConversationName();

    /**
     * Gets the date the conversation was made.
     * @return the date the conversation was made.
     */
    LocalDate getDateMade();

    /**
     * Gets the designated conversation number this conversation holds.
     * Used to identify conversation in the system.
     * @return the conversation number.
     */
    long getConversationNumber();

    /**
     * Gets the message log that matches the date as long as the date is not before current date.
     * If no message log exists for that date it is made.
     * @param localDate the date to ask for messages.
     * @param username the username of the user that wants that log.
     * @return the message log that has that date.
     * @throws CouldNotGetMessageLogException gets thrown if the message log could not be found.
     * @throws UsernameNotPartOfConversationException if the user is not a part of this conversation.
     */
    MessageLog getMessageLogForDate(LocalDate localDate, String username) throws CouldNotGetMessageLogException, UsernameNotPartOfConversationException;

    /**
     * Sets the conversation name to a new value.
     * @param conversationName the new conversation name.
     */
    void setConversationName(String conversationName);

    /**
     * Checks if the input date matches a message log's date.
     * @param localDate the local date you want to check.
     * @return <code>true</code> if a message log has that date.
     *         <code>false</code> if the there is no message log with that date.
     */
    boolean checkForMessageLogByDate(LocalDate localDate);

    /**
     * Adds a new message to the current date.
     * Adds the message log too if it can't be found.
     * @param message the new message to add.
     * @throws CouldNotAddMessageException gets thrown if the message could not be added.
     * @throws CouldNotGetMessageLogException gets thrown if the message log with the same date as the message could not be found.
     * @throws UsernameNotPartOfConversationException if the user is not a part of this conversation.
     */
    void addNewMessage(Message message) throws CouldNotGetMessageLogException, CouldNotAddMessageException, UsernameNotPartOfConversationException;

    /**
     * Removes a message that is in the conversation.
     * @param message the message to remove.
     * @throws CouldNotRemoveMessageException gets thrown if the message is not in the message log.
     * @throws CouldNotGetMessageLogException gets thrown if the message log could not be found.
     * @throws UsernameNotPartOfConversationException if the user is not a part of this conversation.
     */
    void removeMessage(Message message) throws CouldNotRemoveMessageException, CouldNotGetMessageLogException, UsernameNotPartOfConversationException;

    /**
     * Adds all the messages in the list to the conversation.
     * @param newMessageList the list that contains all the new messages.
     * @throws CouldNotAddMessageException gets thrown if one or more messages are already in the conversation.
     * @throws CouldNotGetMessageLogException gets thrown if the message log with the same date as the message could not be found.
     * @throws UsernameNotPartOfConversationException if the user is not a part of this conversation.
     */
    void addAllMessagesWithSameDate(List<Message> newMessageList) throws CouldNotAddMessageException, CouldNotGetMessageLogException, UsernameNotPartOfConversationException;

    /**
     * Removes all the messages in the list from the conversation.
     * @param messagesToRemove the list with all the messages to remove.
     * @throws CouldNotRemoveMessageException gets thrown if one or more messages are not in this conversation.
     * @throws CouldNotGetMessageLogException gets thrown if the message log for that date cannot be found.
     * @throws UsernameNotPartOfConversationException gets thrown if one or more of the messages' comes from a user that are not in the conversation
     */
    void removeAllMessagesWithSameDate(List<Message> messagesToRemove) throws CouldNotRemoveMessageException, CouldNotGetMessageLogException, UsernameNotPartOfConversationException;

    /**
     * Gets the conversations members.
     * @return the conversations members.
     */
    Members getMembers();
}
