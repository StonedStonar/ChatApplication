package no.stonedstonar.chatapplication.model.exception.member;

import java.io.Serializable;

/**
 * CouldNotRemoveMemberException represents an exception that gets thrown when a member could not be removed.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class CouldNotRemoveMemberException extends Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotRemoveMemberException class.
     *
     * @param message the error message.
     */
    public CouldNotRemoveMemberException(String message) {
        super(message);
    }
}