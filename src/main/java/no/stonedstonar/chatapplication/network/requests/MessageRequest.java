package no.stonedstonar.chatapplication.network.requests;

import no.stonedstonar.chatapplication.network.requests.builder.MessageRequestBuilder;
import no.stonedstonar.chatapplication.network.transport.MessageTransport;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * Represents a class that transports the message to the right messagelog.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class MessageRequest implements Serializable {

    private final List<MessageTransport> messageTransportList;

    private final long conversationNumber;

    private final long lastMessage;

    private final boolean isCheckForMessages;

    private final LocalDate checkMessageDate;

    private final String username;

    /**
      * Makes an instance of the MessageTransport class
      * @param messageRequestBuilder a message request builder that is used to make this object.
      */
    public MessageRequest(MessageRequestBuilder messageRequestBuilder){
        checkIfObjectIsNull(messageRequestBuilder, "message request builder");
        messageTransportList = messageRequestBuilder.getMessageTransportList();
        lastMessage = messageRequestBuilder.getLastMessage();
        conversationNumber = messageRequestBuilder.getConversationNumber();
        isCheckForMessages = messageRequestBuilder.isCheckForMessages();
        checkMessageDate = messageRequestBuilder.getCheckMessageDate();
        username = messageRequestBuilder.getUsername();
    }

    /**
     * Gets the username of the person in the request.
     * @return the username.
     */
    public String getUsername(){
        return username;
    }

    /**
     * Gets the date the object should check for new messages.
     * @return the date the messages should be checked for.
     */
    public LocalDate getCheckMessageDate(){
        return checkMessageDate;
    }

    /**
     * Gets the message transport list.
     * @return the list with all the message transports.
     */
    public List<MessageTransport> getMessageTransportList() {
        return messageTransportList;
    }

    /**
     * Gets the conversation number.
     * @return the conversation number.
     */
    public long getConversationNumber() {
        return conversationNumber;
    }

    /**
     * Gets the size of the conversation that you want to check.
     * @return the size of the conversation.
     */
    public long getLastMessage(){
        return lastMessage;
    }

    /**
     * Says true if the request is a check for new messages.
     * @return <code>true</code> if the request is about checking for new messages.
     *         <code>false</code> if the request is not about checking for new messages.
     */
    public boolean isCheckForMessages(){
        return isCheckForMessages;
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
