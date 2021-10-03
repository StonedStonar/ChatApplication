package no.stonedstonar.chatapplication.model.networktransport;

import no.stonedstonar.chatapplication.model.TextMessage;
import no.stonedstonar.chatapplication.model.MessageLog;

import java.io.Serializable;

/**
 * Represents a class that transports the message to the right messagelog.
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class MessageTransport implements Serializable {

    private TextMessage textMessage;

    private long messageLogNumber;

    /**
      * Makes an instance of the MessageTransport class
      * @param textMessage the message you want to transport.
      * @param messageLog the message log that this message is a part of.
      */
    public MessageTransport(TextMessage textMessage, MessageLog messageLog){
        checkIfObjectIsNull(textMessage, "message");
        this.messageLogNumber = messageLog.getMessageLogNumber();
        this.textMessage = textMessage;
    }

    /**
     * Gets the message.
     * @return the message the user wanted to send.
     */
    public TextMessage getMessage(){
        return textMessage;
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
