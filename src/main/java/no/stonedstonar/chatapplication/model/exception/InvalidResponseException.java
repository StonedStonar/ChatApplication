package no.stonedstonar.chatapplication.model.exception;

import java.io.Serializable;

/**
 * InvalidResponseException represents an exception that gets thrown when an invalid response is recived from the server.
 * @author Steinar Hjelle Midthus
 * @version 0.2
 */
public class InvalidResponseException extends Exception implements Serializable {

    /**
     * Makes an instance of the InvalidResponseException class.
     *
     * @param message the error message.
     */
    public InvalidResponseException(String message) {
        super(message);

    }
}