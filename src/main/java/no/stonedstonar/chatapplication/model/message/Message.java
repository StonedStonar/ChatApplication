package no.stonedstonar.chatapplication.model.message;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a message that is sent between two people.
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public interface Message {

    /**
     * Gets the user that this message is from.
     * @return the user that this message is from.
     */
    String getFromUsername();


    /**
     * Gets the time this message was sent.
     * @return the local time object this message holds.
     */
    LocalTime getTime();

    /**
     * Gets the date this message was sent.
     * @return the date this object was sent.
     */
    LocalDate getDate();
}
