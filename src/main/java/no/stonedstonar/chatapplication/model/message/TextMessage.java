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

    private final LocalDate date;

    private final LocalTime time;


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
    }

    @Override
    public LocalTime getTime(){
        return time;
    }

    @Override
    public LocalDate getDate(){
        return date;
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
