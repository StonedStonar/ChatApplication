package no.stonedstonar.chatapplication.model.message;

import javafx.scene.text.Text;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class TextMessage implements Serializable {

    private final String message;

    private final String fromUsername;

    private long messageNumber;

    private LocalDate date;

    private LocalTime time;


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
        date = LocalDate.now();
        time = LocalTime.now();
        messageNumber = -1;
    }

    /**
     * Sets the message number and puts a date on the message.
     * @param messageNumber the number this message is in the log.
     */
    public void setMessageNumber(long messageNumber){
        if (this.messageNumber == -1){
            checkIfLongIsAboveZero(messageNumber, "message number");
            this.messageNumber = messageNumber;
            date = LocalDate.now();
            time = LocalTime.now();
        }else {
            throw new IllegalArgumentException("Cannot set the message number since its already set.");
        }
    }

    /**
     * Gets the message's long number.
     * @return number this message is.
     */
    public long getMessageNumber(){
        return messageNumber;
    }

    /**
     * Gets the time that this message was sent.
     * @return the time object that holds the time.
     */
    public LocalTime getTime(){
        return time;
    }

    /**
     * Gets the date this message was sent.
     * @return the local date object that holds the date.
     */
    public LocalDate getDate(){
        return date;
    }

    /**
     * Gets the message.
     * @return the message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the username that this message is from.
     * @return the username of the person who sent the message.
     */
    public String getFromUsername() {
        return fromUsername;
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
     * Checks if the long is above zero.
     * @param number the number you want to check.
     * @param prefix the error message the exception should have.
     */
    private void checkIfLongIsAboveZero(long number, String prefix){
        if (number <= 0){
            throw new IllegalArgumentException("The " + prefix + " must be larger than zero.");
        }
    }
}
