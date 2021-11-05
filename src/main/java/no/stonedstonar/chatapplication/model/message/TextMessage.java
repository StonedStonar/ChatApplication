package no.stonedstonar.chatapplication.model.message;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a message that holds text.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class TextMessage implements Serializable, Message{

    private final String message;

    private final String fromUsername;

    private final LocalDate sentDate;

    private final LocalTime sentTime;

    private LocalTime receivedByServerTime;

    private LocalDate receivedByServerDate;

    private long messageNumber;


    /**
      * Makes an instance of the Message class.
     * @param message the message this object should contain.
     * @param fromUsername the username this message was sent form.
      */
    public TextMessage(String message, String fromUsername){
        checkString(message, "Message");
        checkString(fromUsername, "From username");
        this.message = message;
        this.fromUsername = fromUsername;
        sentDate = LocalDate.now();
        sentTime = LocalTime.now();
        receivedByServerDate = null;
        receivedByServerTime = null;
    }

    /**
     * Makes an instance of the Message class.
     * @param message the message this object should contain.
     * @param fromUsername the username this message was sent form.
     * @param localDate the date this message was made.
     */
    public TextMessage(String message, String fromUsername, LocalDate localDate){
        checkString(message, "Message");
        checkString(fromUsername, "From username");
        checkIfObjectIsNull(localDate, "local date");
        this.message = message;
        this.fromUsername = fromUsername;
        sentDate = localDate;
        sentTime = LocalTime.now();
        messageNumber = -1;
    }

    @Override
    public LocalTime getTime(){
        LocalTime localTime = receivedByServerTime;
        if (receivedByServerTime == null){
            localTime = sentTime;
        }
        return localTime;
    }

    @Override
    public LocalDate getDate() {
        LocalDate localDate = receivedByServerDate;
        if (receivedByServerDate == null) {
            localDate = sentDate;
        }
        return localDate;
    }

    @Override
    public LocalTime getSentFromUserTime() {
        return sentTime;
    }

    @Override
    public LocalDate getSentFromUserDate() {
        return sentDate;
    }

    @Override
    public void setMessageNumber(long messageNumber) {
        checkIfLongIsNegative(messageNumber, "message number");
        if (this.messageNumber >= -1){
            this.messageNumber = messageNumber;
            receivedByServerDate = LocalDate.now();
            receivedByServerTime = LocalTime.now();
        }else {
            throw new IllegalArgumentException("The message number cannot be altered after its set.");
        }
    }

    @Override
    public long getMessageNumber() {
        return messageNumber;
    }

    @Override
    public String getFromUsername() {
        return fromUsername;
    }

    /**
     * Gets the message.
     * @return the message.
     */
    public String getMessage() {
        return message;
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
     * Checks if the date, time and from username.
     * @param messageToCheck the message you want to compare against.
     * @return <code>true</code> if the message's time, date, contents and from user matches this messages fields.
     *         <code>false</code> if the message's time, date, contents and form user does not match this messages fields.
     */
    public boolean checkIfMessageContentsAreEqual(Message messageToCheck){
        boolean valid = false;
        if (messageToCheck instanceof TextMessage textMessage){
            valid = (textMessage.getSentFromUserDate().isEqual(sentDate)) && (textMessage.getSentFromUserTime().equals(sentTime)) && (textMessage.getFromUsername().equals(fromUsername)) && textMessage.getMessage().equals(this.message);
        }
        return valid;
    }
}
