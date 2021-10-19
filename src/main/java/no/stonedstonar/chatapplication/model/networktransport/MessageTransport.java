package no.stonedstonar.chatapplication.model.networktransport;

import no.stonedstonar.chatapplication.model.conversation.Conversation;
import no.stonedstonar.chatapplication.model.message.Message;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a class that transports the message to the right messagelog.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class MessageTransport implements Serializable {

    private List<Message> messageList;

    private long messageLogNumber;

    /**
      * Makes an instance of the MessageTransport class
      * @param messageList the message(s) you want to transport.
      * @param conversation the conversation that this message is a part of.
      */
    public MessageTransport(List<Message> messageList, Conversation conversation){
        checkIfObjectIsNull(messageList, "message");
        checkIfLongIsAboveZero(conversation.getConversationNumber(), "message log number");
        this.messageLogNumber = conversation.getConversationNumber();
        this.messageList = messageList;
    }

    /**
     * Gets the messages.
     * @return the message the user wanted to send.
     */
    public List<Message> getMessages(){
        return messageList;
    }

    /**
     * Gets the message log number this message is a part of.
     * @return the message log number.
     */
    public long getConversationNumber(){
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
