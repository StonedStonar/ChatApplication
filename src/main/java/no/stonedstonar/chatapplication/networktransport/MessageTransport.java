package no.stonedstonar.chatapplication.networktransport;

import no.stonedstonar.chatapplication.model.conversation.Conversation;
import no.stonedstonar.chatapplication.model.message.Message;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * Represents a class that transports the message to the right messagelog.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class MessageTransport implements Serializable {

    private List<Message> messageList;

    private long conversationNumber;

    private LocalDate dateForMessage;

    /**
      * Makes an instance of the MessageTransport class
      * @param messageList the message(s) you want to transport.
      * @param conversation the conversation that this message is a part of.
      * @param dateSent the date this message was sent.
      */
    public MessageTransport(List<Message> messageList, Conversation conversation, LocalDate dateSent){
        checkIfObjectIsNull(messageList, "message");
        checkIfObjectIsNull(dateSent, "date sent");
        checkIfLongIsAboveZero(conversation.getConversationNumber(), "message log number");
        this.conversationNumber = conversation.getConversationNumber();
        this.messageList = messageList;
        this.dateForMessage = dateSent;
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
        return conversationNumber;
    }

    /**
     * Gets the date for the message.
     * @return the date for the message.
     */
    public LocalDate getDateForMessage(){
        return dateForMessage;
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
