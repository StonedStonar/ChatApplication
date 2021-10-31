package no.stonedstonar.chatapplication.network.requests.builder;

import no.stonedstonar.chatapplication.network.requests.ConversationRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a builder class for a conversation request.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class ConversationRequestBuilder {

    private boolean deleteConversation;

    private boolean newConversation;

    private String nameOfConversation;

    private List<Long> conversationNumberList;

    private boolean checkForNewConversation;

    /**
     * Makes an instance of the MessageLogRequest class.
     */
    public ConversationRequestBuilder(){
        deleteConversation = false;
        newConversation = false;
        checkForNewConversation = false;
        nameOfConversation = "";
        conversationNumberList = new ArrayList<>();
    }

    /**
     * Says if the request wants to delete a conversation.
     * All the other booleans will be set to false if this is set to true.
     * @param valid <code>true</code> if the user wants to delete a conversation.
     *              <code>false</code> if the user don't want to delete a conversation.
     * @return this builder object.
     */
    public ConversationRequestBuilder setDeleteConversation(boolean valid){
        if (valid){
            deleteConversation = true;
            newConversation = false;
            checkForNewConversation = false;
        }else {
            deleteConversation = false;
        }
        return this;
    }

    /**
     * Says if the request is about making a new conversation.
     * All the other booleans will be set to false if this is set to true.
     * @param valid <code>true</code> if the request is about making a new conversation.
     *              <code>false</code> if the request is not about making a new conversation.
     * @return this builder object.
     */
    public ConversationRequestBuilder setNewConversation(boolean valid){
        if (valid){
            deleteConversation = false;
            newConversation = true;
            checkForNewConversation = false;
        }else {
            newConversation = false;
        }
        return this;
    }

    /**
     * Says if the request is about checking for new messages.
     * @param valid <code>true</code> if the request wants to check for new conversations.
     *              <code>false</code> if the does not want to check for new conversations.
     * @return this builder object.
     */
    public ConversationRequestBuilder setCheckForNewConversations(boolean valid){
        if (valid){
            deleteConversation = false;
            newConversation = false;
            checkForNewConversation = true;
        }else {
            checkForNewConversation = false;
        }
        return this;
    }

    /**
     * Adds a conversation name to the request.
     * @param conversationName the conversation name.
     * @return this builder object.
     */
    public ConversationRequestBuilder addConversationName(String conversationName){
        checkString(conversationName, "conversation name");
        this.nameOfConversation = conversationName;
        return this;
    }

    /**
     * Adds a list with all the conversation numbers.
     * @param conversationNumberList the list with all conversation numbers.
     * @return a list with all the conversation numbers.
     */
    public ConversationRequestBuilder addConversationNumberList(List<Long> conversationNumberList){
        checkIfObjectIsNull(conversationNumberList, "conversation number list");
        this.conversationNumberList = conversationNumberList;
        return this;
    }

    /**
     * Gets the list with all the conversation numbers.
     * @return the list with all the conversation numbers.
     */
    public List<Long> getConversationNumberList(){
        return conversationNumberList;
    }

    /**
     * Says true if the request is a check for new conversations.
     * @return <code>true</code> if the request wants to check for new conversations.
     *         <code>false</code> if the does not want to check for new conversations.
     */
    public boolean isCheckForNewConversation(){
        return checkForNewConversation;
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
     * Says true if this request is a new conversation.
     * @return <code>true</code> if this request is a new conversation.
     *         <code>false</code> if this request is not a new conversation.
     */
    public boolean isNewConversation() {
        return newConversation;
    }

    /**
     * Gets the name of the conversation.
     * @return the name of the conversation.
     */
    public String getNameOfConversation() {
        return nameOfConversation;
    }

    /**
     * Makes the conversation request.
     * @return a request that matches the set values in this builder.
     */
    public ConversationRequest build(){
        ConversationRequest conversationRequest = new ConversationRequest(this);
        return conversationRequest;
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

    /**
     * Checks if the long is above zero.
     * @param logNumber the log number you want to check.
     * @param prefix the error the exception should mention.
     */
    private void checkIfLongIsAboveZero(long logNumber, String prefix, boolean equalZero){
        if (equalZero){
            if (logNumber < 0){
                throw new IllegalArgumentException("The " + prefix + " cannot be negative or null.");
            }
        }else {
            if(logNumber <= 0){
                throw new IllegalArgumentException("The " + prefix + " cannot be negative or null.");
            }
        }
    }
}
