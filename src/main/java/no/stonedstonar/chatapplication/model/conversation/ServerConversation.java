package no.stonedstonar.chatapplication.model.conversation;

import no.stonedstonar.chatapplication.model.User;
import no.stonedstonar.chatapplication.model.exception.conversation.UsernameNotPartOfConversationException;
import no.stonedstonar.chatapplication.model.exception.message.CouldNotAddMessageException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotGetMessageLogException;
import no.stonedstonar.chatapplication.model.message.Message;
import no.stonedstonar.chatapplication.model.messagelog.ServerMessageLog;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a conversation that is stored on the server side.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public interface ServerConversation extends Conversation {


    /**
     * Gets all the message logs of a conversation if the user is a part of it.
     * @param username the username of a member.
     * @return a list with all the message logs.
     */
    List<ServerMessageLog> getMessageLogs(String username);

    @Override
    ServerMessageLog getMessageLogForDate(LocalDate localDate, String username) throws CouldNotGetMessageLogException, UsernameNotPartOfConversationException;

    /**
     * Checks for new messages on a specified date.
     * If there is no message log for that date (the date must be current day) it is made, but then last message must be zero.
     * @param localDate the date you want to check for new messages for.
     * @param lastMessage the number of the last message in the other register.
     * @param username the username of a member of the conversation.
     * @throws CouldNotGetMessageLogException If the last message is larger than zero or the date is not today an exception is thrown.
     *                                        If the date is before today a new message log is not made.
     * @throws UsernameNotPartOfConversationException gets thrown if the username is not a part of this conversation.
     */
    List<Message> checkForNewMessagesOnDate(LocalDate localDate, long lastMessage, String username) throws CouldNotGetMessageLogException, UsernameNotPartOfConversationException;
}
