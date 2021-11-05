package no.stonedstonar.chatapplication.model.message;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a message that is sent between two people.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public interface Message {

    /**
     * Sets the message's number.
     * @param messageNumber the message number this message is.
     */
    void setMessageNumber(long messageNumber);

    /**
     * Gets the message number this message is.
     * @return the message number.
     */
    long getMessageNumber();

    /**
     * Gets the user that this message is from.
     * @return the user that this message is from.
     */
    String getFromUsername();


    /**
     * Gets the time this message was sent.
     * When the message is received by the server the servers current time is set as time.
     * @return the local time object this message holds.
     */
    LocalTime getTime();

    /**
     * Gets the date this message was sent.
     * When the message is received by the server the servers current date is set as date.
     * @return the date this object was sent.
     */
    LocalDate getDate();

    /**
     * Gets the time the message was sent from the user.
     * @return the time the message was sent from the user.
     */
    LocalTime getSentFromUserTime();

    /**
     * Gets the date the message was sent form user.
     * @return the date this message was sent.
     */
    LocalDate getSentFromUserDate();
}
