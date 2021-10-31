package no.stonedstonar.chatapplication.network.transport;

import no.stonedstonar.chatapplication.model.message.Message;

import java.io.Serializable;

/**
 * Represents message when it's transported over the internet. Says if it's going to be added or deleted.
 * If addMessage is set to false then the message is "removed".
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class MessageTransport implements Serializable {

    private boolean addMessage;

    private Message message;

    /**
      * Makes an instance of the MessageTransport class.
      * @param message the message you want to transport.
      * @param isAddMessage <code>true</code> if the message is supposed to be added.
      *                     <code>false</code> if the message is supposed to be deleted.
      */
    public MessageTransport(Message message, boolean isAddMessage){
        checkIfObjectIsNull(message, "message");
        this.addMessage = isAddMessage;
        this.message = message;
    }

    /**
     * Says if the message is supposed to be added or removed.
     * @return <code>true</code> if the message is supposed to be added.
     *         <code>false</code> if the message is supposed to be deleted
     */
    public boolean isAddMessage() {
        return addMessage;
    }

    /**
     * Gets the message that this transport holds.
     * @return the message.
     */
    public Message getMessage(){
        return message;
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
