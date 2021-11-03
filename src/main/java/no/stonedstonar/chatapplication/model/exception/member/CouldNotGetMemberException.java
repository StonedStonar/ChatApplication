package no.stonedstonar.chatapplication.model.exception.member;

import java.io.Serializable;

/**
 * CouldNotGetMemberException represents an exception that gets thrown when a member could not be located.
 * @author Steinar Hjelle Midthus
 * @version 0.2
 */
public class CouldNotGetMemberException extends Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotGetMemberException class.
     * @param message the error message.
     */
    public CouldNotGetMemberException(String message) {
        super(message);

    }
}