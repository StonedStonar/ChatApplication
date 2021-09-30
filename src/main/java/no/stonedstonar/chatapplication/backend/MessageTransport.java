package no.stonedstonar.chatapplication.backend;

import no.stonedstonar.chatapplication.model.Message;
import no.stonedstonar.chatapplication.model.MessageLog;

import java.io.Serializable;

/**
 * Represents a class that transports the message to the right messagelog.
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class MessageTransport implements Serializable {

    private Message message;

    private long messageLogNumber;

    /**
      * Makes an instance of the MessageTransport class
      * @param message the message you want to transport.
      * @param messageLog the message log that this message is a part of.
      */
    public MessageTransport(Message message, MessageLog messageLog){
        checkIfObjectIsNull(message, "message");
        this.messageLogNumber = messageLog.getMessageLogNumber();
        this.message = message;
    }

    /**
     * Gets the message.
     * @return the message the user wanted to send.
     */
    public Message getMessage(){
        return message;
    }

    /**
     * Gets the message log number this message is a part of.
     * @return the message log number.
     */
    public long getMessageLogNumber(){
        return messageLogNumber;
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
