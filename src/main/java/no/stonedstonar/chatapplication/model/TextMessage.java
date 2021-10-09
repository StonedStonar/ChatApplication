package no.stonedstonar.chatapplication.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class TextMessage implements Serializable {

    private String message;

    private String fromUsername;

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
}
