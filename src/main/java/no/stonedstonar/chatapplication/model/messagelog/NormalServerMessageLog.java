package no.stonedstonar.chatapplication.model.messagelog;

import no.stonedstonar.chatapplication.model.exception.message.CouldNotAddMessageException;
import no.stonedstonar.chatapplication.model.exception.message.CouldNotRemoveMessageException;
import no.stonedstonar.chatapplication.model.message.Message;
import no.stonedstonar.chatapplication.model.message.TextMessage;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

/**
 * Represents a class that holds messages for each day of the month.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class NormalServerMessageLog implements Serializable, ServerMessageLog {

    private final ArrayList<Message> messageList;

    private final LocalDate dateMade;

    private long lastMessageNumber;

    /**
      * Makes an instance of the MessageLog class.
     * @param dateMade the date this object was made.
      */
    public NormalServerMessageLog(LocalDate dateMade){
        checkIfObjectIsNull(dateMade, "date made");
        messageList = new ArrayList<>();
        lastMessageNumber = 0;
        this.dateMade = dateMade;
    }

    @Override
    public void addMessage(Message message) throws CouldNotAddMessageException {
        checkIfObjectIsNull(message, "message");
        if (!checkIfMessageIsInMessageLog(message)){
            lastMessageNumber += 1;
            message.setMessageNumber(lastMessageNumber);
            messageList.add(message);
        }else {
            throw new CouldNotAddMessageException("The message " + message + " is already in the system.");
        }
    }

    /**
     * Checks if the message is in the message log already.
     * @param message the message you want to check.
     * @return <code>true</code> if the message matches a message on time, date, contents and from username.
     *         <code>false</code> if the message does not match any messages in the log.
     */
    private boolean checkIfMessageIsInMessageLog(Message message){
        return messageList.stream().anyMatch(mess -> {
            boolean valid = false;
            if (mess instanceof TextMessage textMessage){
                return textMessage.checkIfMessageContentsAreEqual(message);
            }
            return valid;
        });
    }

    @Override
    public List<Message> checkForNewMessages(long lastMessageNumber){
        checkIfLongIsNegative(lastMessageNumber, "last message number");
        List<Message> newMessageList = new ArrayList<>();
        if (this.lastMessageNumber > lastMessageNumber){
            newMessageList = getMessagesOverMessageNumber(lastMessageNumber);
        }
        return newMessageList;
    }

    /**
     * Finds all the messages that are over a certain message number.
     * @param numberOfMessage the message number.
     * @return a list with all the messages that are over the input message number.
     */
    private List<Message> getMessagesOverMessageNumber(long numberOfMessage){
        return messageList.stream().filter(mess -> mess.getMessageNumber() > numberOfMessage).toList();
    }
    
    /**
     * Checks if an object is null.
     * @param object the object you want to check.
     * @param error the error message the exception should have.
     */
    private void checkIfObjectIsNull(Object object, String error){
       if (object == null){
           throw new IllegalArgumentException("The " + error + " cannot be null.");
       }
    }

    /**
     * Checks if a long is negative or equal to zero.
     * @param number the number to check.
     * @param prefix the prefix the error should have.
     */
    private void checkIfLongIsNegative(long number, String prefix){
        if (number < 0){
            throw new IllegalArgumentException("Expected the " + prefix + " to be larger than zero.");
        }

    }

    @Override
    public LocalDate getDateMade() {
        return dateMade;
    }

    @Override
    public List<Message> getMessages() {
        return messageList;
    }

    @Override
    public long getLastMessageNumber() {
        return lastMessageNumber;
    }

    @Override
    public void removeMessage(Message message) throws CouldNotRemoveMessageException {
        checkIfObjectIsNull(message, "message");
        if (messageList.contains(message)){
            messageList.remove(message);
        }else {
            throw new CouldNotRemoveMessageException("Could not remove the message since its not in the register.");
        }
    }

    @Override
    public boolean checkIfAllMessagesAreNewMessages(List<Message> messageList){
        return messageList.stream().noneMatch(this::checkIfMessageIsInMessageLog);
    }

    @Override
    public boolean checkIfAllMessagesAreInMessageLog(List<Message> messageList) {
        return messageList.stream().allMatch(this::checkIfMessageIsInMessageLog);
    }
}
