package no.stonedstonar.chatapplication.network.requests.builder;

import no.stonedstonar.chatapplication.network.requests.MessageRequest;
import no.stonedstonar.chatapplication.network.transport.MessageTransport;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a builder for the message request class.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class MessageRequestBuilder {

    private List<MessageTransport> messageTransportList;

    private long conversationNumber;

    private long lastMessage;

    private boolean isCheckForMessages;

    private LocalDate checkMessageDate;

    private String username;

    /**
      * Makes an instance of the MessageRequestBuilder class.
      */
    public MessageRequestBuilder(){
        isCheckForMessages = false;
        username = "";
        messageTransportList = new ArrayList<>();
    }

    /**
     * Sets the username of the person that wants to check for new messages.
     * @param username the username of the user.
     * @return this builder object.
     */
    public MessageRequestBuilder setUsername(String username){
        checkString(username, "username");
        this.username = username;
        return this;
    }

    /**
     * Says if the request is about checking for new messages.
     * All the other booleans will be set to false if this is set to true.
     * @param valid <code>true</code> if the request wants to check for new memssages.
     *              <code>false</code> if the does not want to check for new messages.
     * @return this builder object.
     */
    public MessageRequestBuilder setCheckForMessages(boolean valid){
        if (valid){
            isCheckForMessages = true;
        }else {
            isCheckForMessages = false;
        }
        return this;
    }

    /**
     * Adds the last message long.
     * @param lastMessage the last message's long number.
     * @return this builder object.
     */
    public MessageRequestBuilder addLastMessage(long lastMessage){
        checkIfLongIsAboveZero(lastMessage, "last message");
        this.lastMessage = lastMessage;
        return this;
    }

    /**
     * Adds the date you want to check.
     * @param date the date that the request is about.
     * @return this builder object.
     */
    public MessageRequestBuilder addDate(LocalDate date){
        checkIfObjectIsNull(date, "date");
        this.checkMessageDate = date;
        return this;
    }

    /**
     * Sets the message transport list.
     * @param messageTransportList the message transport list.
     * @return this builder object.
     */
    public MessageRequestBuilder addMessageTransportList(List<MessageTransport> messageTransportList){
        checkIfObjectIsNull(messageTransportList, "message transport list");
        this.messageTransportList = messageTransportList;
        return this;
    }

    /**
     * Adds the conversation number
     * @param conversationNumber the conversation number you need.
     * @return this builder object.
     */
    public MessageRequestBuilder addConversationNumber(long conversationNumber){
        checkIfLongIsAboveZero(conversationNumber, "conversation number");
        this.conversationNumber = conversationNumber;
        return this;
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
     * Makes a new message request based on the input.
     * @return a message request that is based on the input.
     */
    public MessageRequest build(){
        return new MessageRequest(this);
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
     * Checks if a string is of a valid format or not.
     * @param stringToCheck the string you want to check.
     * @param errorPrefix the error the exception should have if the string is invalid.
     */
    private synchronized void checkString(String stringToCheck, String errorPrefix){
        checkIfObjectIsNull(stringToCheck, errorPrefix);
        if (stringToCheck.isEmpty()){
            throw new IllegalArgumentException("The " + errorPrefix + " cannot be empty.");
        }
    }

    /**
     * Checks if a long is above 0.
     * @param number the number you want to check.
     * @param prefix the prefix the error should have.
     */
    private void checkIfLongIsAboveZero(long number, String prefix){
        if (number > 0){
            throw new IllegalArgumentException("The " + prefix + " must be above 0.");
        }
    }
}
