package no.stonedstonar.chatapplication.model.messagelog;

import no.stonedstonar.chatapplication.model.exception.message.CouldNotAddMessageException;
import no.stonedstonar.chatapplication.model.exception.message.CouldNotRemoveMessageException;
import no.stonedstonar.chatapplication.model.message.Message;
import no.stonedstonar.chatapplication.model.message.TextMessage;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 *
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public interface MessageLog {

    /**
     * Gets the date the message log represents.
     * @return the local date object this message log holds.
     */
    LocalDate getDateMade();

    /**
     * Gets the messages this message log holds.
     * @return the map that holds all the messages.
     */
    Map<Long, Message> getMessages();

    /**
     * Gets the number of the last message this message log holds.
     * @return the number of the last message this message log holds.
     */
    long getLastMessageNumber();

    /**
     * Adds a new message to the message log.
     * @param message the message to add.
     * @throws CouldNotAddMessageException gets thrown if the message could not be added.
     */
    void addMessage(Message message) throws CouldNotAddMessageException;

    /**
     * Removes a message form the message log.
     * @param message the message to be removed.
     * @throws CouldNotRemoveMessageException gets thrown if the message is not in the message log.
     */
    void removeMessage(Message message) throws CouldNotRemoveMessageException;

    /**
     * Checks if all the messages in the list is new messages to this message log.
     * @param messageList the list of text messages you want to check.
     * @return <code>true</code> if one of the input messages matches one in the list.
     *         <code>false</code> if none of the input messages are in the message log.
     */
    boolean checkIfAllMessagesAreNewMessages(List<Message> messageList);
}
