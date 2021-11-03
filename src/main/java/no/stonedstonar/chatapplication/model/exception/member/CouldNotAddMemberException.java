package no.stonedstonar.chatapplication.model.exception.member;

import java.io.Serializable;

/**
 * CouldNotAddMemberException represents an exception that gets thrown when a member could not be added.
 * @author Steinar Hjelle Midthus
 * @version 0.2
 */
public class CouldNotAddMemberException extends Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotAddMemberException class.
     * @param message the error message.
     */
    public CouldNotAddMemberException(String message) {
        super(message);

    }
}