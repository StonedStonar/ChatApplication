package no.stonedstonar.chatapplication.model.networktransport;

import no.stonedstonar.chatapplication.model.networktransport.builder.ConversationRequestBuilder;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * Represents a conversation request that can do different operations based on the input values.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class ConversationRequest implements Serializable {

    private boolean deleteConversation;

    private boolean removeMembers;

    private boolean addMembers;

    private boolean newConversation;

    private boolean checkForMessages;

    private String nameOfConversation;

    private List<String> usernames;

    private long lastMessage;
    
    private LocalDate date;

    private long conversationNumber;

    /**
      * Makes an instance of the MessageLogRequest class.
      */
    public ConversationRequest(ConversationRequestBuilder conversationRequestBuilder){
        deleteConversation = conversationRequestBuilder.isDeleteConversation();
        removeMembers = conversationRequestBuilder.isRemoveMembers();
        addMembers = conversationRequestBuilder.isAddMembers();
        newConversation = conversationRequestBuilder.isNewConversation();
        usernames = conversationRequestBuilder.getUsernames();
        conversationNumber = conversationRequestBuilder.getConversationNumber();
        checkForMessages = conversationRequestBuilder.isCheckForMessages();
        lastMessage = conversationRequestBuilder.getLastMessage();
        nameOfConversation = conversationRequestBuilder.getNameOfConversation();
        date = conversationRequestBuilder.getDate();
    }

    /**
     * Says true if the request is a check for new messages.
     * @return <code>true</code> if the request is about checking for new messages.
     *         <code>false</code> if the request is not about checking for new messages.
     */
    public boolean isCheckForMessages(){
        return checkForMessages;
    }

    /**
     * Gets the size of the conversation that you want to check.
     * @return the size of the conversation.
     */
    public long getLastMessage(){
        return lastMessage;
    }

    /**
     * Says true if the request is a remove conversation request.
     * @return <code>true</code> if the conversation needs to be deleted.
     *         <code>false</code> if the conversation is going to be deleted.
     */
    public boolean isDeleteConversation() {
        return deleteConversation;
    }

    /**
     * Says true if the request is a removeMembers request.
     * @return <code>true</code> if the members are supposed to be removed.
     *         <code>false</code> if the members are not supposed to be removed.
     */
    public boolean isRemoveMembers() {
        return removeMembers;
    }

    /**
     * Says true if the request is an add members request.
     * @return <code>true</code> if the members are supposed to be added.
     *         <code>false</code> if the members are not supposed to be added.
     */
    public boolean isAddMembers() {
        return addMembers;
    }

    /**
     * Says true if this request is a new conversation.
     * @return <code>true</code> if this request is a new conversation.
     *         <code>false</code> if this request is not a new conversation.
     */
    public boolean isNewConversation() {
        return newConversation;
    }

    /***
     * Gets the list of all usernames.
     * @return a list with all the usernames.
     */
    public List<String> getUsernames() {
        return usernames;
    }

    /**
     * Gets the conversation number.
     * @return the conversation number.
     */
    public long getConversationNumber() {
        return conversationNumber;
    }

    /**
     * Gets the name of the conversation.
     * @return the name of the conversation.
     */
    public String getNameOfConversation() {
        return nameOfConversation;
    }

    /**
     * Gets the date of the conversation.
     * @return the date of the conversation.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Checks if a string is of a valid format or not.
     * @param stringToCheck the string you want to check.
     * @param errorPrefix the error the exception should have if the string is invalid.
     */
    public void checkString(String stringToCheck, String errorPrefix){
        checkIfObjectIsNull(stringToCheck, errorPrefix);
        if (stringToCheck.isEmpty()){
            throw new IllegalArgumentException("The " + errorPrefix + " cannot be empty.");
        }
    }
    
    /**
     * Checks if an object is null.
     * @param object the object you want to check.
     * @param error the error message the exception should have.
     */
    public void checkIfObjectIsNull(Object object, String error){
       if (object == null){
           throw new IllegalArgumentException("The " + error + " cannot be null.");
       }
    }
}
