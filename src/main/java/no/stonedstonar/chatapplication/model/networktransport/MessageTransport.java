package no.stonedstonar.chatapplication.model.networktransport;

import javafx.scene.text.Text;
import no.stonedstonar.chatapplication.model.TextMessage;
import no.stonedstonar.chatapplication.model.MessageLog;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a class that transports the message to the right messagelog.
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class MessageTransport implements Serializable {

    private List<TextMessage> textMessageList;

    private long messageLogNumber;

    /**
      * Makes an instance of the MessageTransport class
      * @param textMessageList the message you want to transport.
      * @param messageLog the message log that this message is a part of.
      */
    public MessageTransport(List<TextMessage> textMessageList, MessageLog messageLog){
        checkIfObjectIsNull(textMessageList, "message");
        checkIfLongIsAboveZero(messageLog.getMessageLogNumber(), "message log number");
        this.messageLogNumber = messageLog.getMessageLogNumber();
        this.textMessageList = textMessageList;
    }

    /**
     * Gets the message.
     * @return the message the user wanted to send.
     */
    public List<TextMessage> getMessages(){
        return textMessageList;
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

    /**
     * Checks if a long is above 0.
     * @param number the number you want to check.
     * @param prefix the prefix the error should have.
     */
    private void checkIfLongIsAboveZero(long number, String prefix){
        if (number <= 0){
            throw new IllegalArgumentException("The " + prefix + " must be above 0.");
        }
    }
}
