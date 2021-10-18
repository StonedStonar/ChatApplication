package no.stonedstonar.chatapplication.model.messagelog;

import no.stonedstonar.chatapplication.model.exception.message.CouldNotAddMessageException;
import no.stonedstonar.chatapplication.model.exception.message.CouldNotRemoveMessageException;
import no.stonedstonar.chatapplication.model.message.Message;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

/**
 * Represents a class that holds messages for each day of the month.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class NormalMessageLog implements Serializable, MessageLog {

    private Map<Long, Message> messageMap;

    private LocalDate dateMade;

    private long lastMessageNumber;

    /**
      * Makes an instance of the MessageLog class.
     * @param dateMade the date this object was made.
      */
    public NormalMessageLog(LocalDate dateMade){
        checkIfObjectIsNull(dateMade, "date made");
        messageMap = new HashMap<>();
        lastMessageNumber = 0;
        this.dateMade = dateMade;
    }

    @Override
    public void addMessage(Message message) throws CouldNotAddMessageException {
        checkIfObjectIsNull(message, "message");
        if (!checkIfMessageIsInMessageLog(message)){
            lastMessageNumber += 1;
            messageMap.put(lastMessageNumber, message);
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
        return messageMap.values().stream().anyMatch(mess -> mess.equals(message));
    }

    @Override
    public Map<Long, Message> checkForNewMessages(long lastMessageNumber){
        checkIfLongIsNegative(lastMessageNumber, "last message number");
        Map<Long, Message> newMessageMap = new HashMap<>();
        if (this.lastMessageNumber > lastMessageNumber){
            long start = lastMessageNumber + 1;
            long stop = this.lastMessageNumber;
            do {
                Message message = messageMap.get(start);
                newMessageMap.put(start, message);
                start += 1;
            }while (start < stop);
        }
        return newMessageMap;
    }

    //Todo: Vurder om denne metoden trengs.
    ///**
     //* Gets the message that matches the message contents.
     //* @param fromUsername the username this message is from.
     //* @param localDate the local date object this message has.
     //* @param localTime the local time object this message has.
     //* @return the message that matches the input message.
     //* @throws CouldNotGetMessageException gets thrown when a message could not be found.
     //*/
//    public Message getMessage(String fromUsername, LocalTime localTime, LocalDate localDate) throws CouldNotGetMessageException {
//        checkString(fromUsername, "from username");
//        checkIfObjectIsNull(localDate, "local date");
//        checkIfObjectIsNull(localTime, "local time");
//        Optional<Message> optionalMessage = messageMap.values().stream().filter(mess -> mess.getTime().equals(localTime)).filter(message -> message.getDate().equals(localDate)).filter(message -> message.getFromUsername().equals(fromUsername)).findFirst();
//        if (optionalMessage.isPresent()){
//            return optionalMessage.get();
//        }else {
//            throw new CouldNotGetMessageException("The message that has the date and time \"" + localDate + " " + localTime+ "\" is not a part of this messages.");
//        }
//    }
    
    /**
     * Checks if a string is of a valid format or not.
     * @param stringToCheck the string you want to check.
     * @param errorPrefix the error the exception should have if the string is invalid.
     */
    private void checkString(String stringToCheck, String errorPrefix){
        checkIfObjectIsNull(stringToCheck, errorPrefix);
        if (stringToCheck.isEmpty()){
            throw new IllegalArgumentException("The " + errorPrefix + " cannot be empty.");
        }
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
    public Map<Long, Message> getMessages() {
        return messageMap;
    }

    @Override
    public long getLastMessageNumber() {
        return lastMessageNumber;
    }

    @Override
    public void removeMessage(Message message) throws CouldNotRemoveMessageException {
        checkIfObjectIsNull(message, "message");
        if (checkIfMessageIsInMessageLog(message)){
            messageMap.values().remove(message);
        }else {
            throw new CouldNotRemoveMessageException("Could not remove the message since its not in the register.");
        }
    }

    @Override
    public boolean checkIfAllMessagesAreNewMessages(List<Message> messageList){
        return messageList.stream().anyMatch(this::checkIfMessageIsInMessageLog);
    }
}
