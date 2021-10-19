package no.stonedstonar.chatapplication.model.networktransport.builder;

import no.stonedstonar.chatapplication.model.networktransport.ConversationRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a builder class for a conversation request.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class ConversationRequestBuilder {

    private boolean deleteConversation;

    private boolean removeMembers;

    private boolean addMembers;

    private boolean newConversation;

    private List<String> usernames;

    private long conversationNumber;

    private long lastMessage;

    private boolean checkForMessages;

    private String conversationName;

    private LocalDate dateMade;

    /**
     * Makes an instance of the MessageLogRequest class.
     */
    public ConversationRequestBuilder(){
        deleteConversation = false;
        removeMembers = false;
        addMembers = false;
        newConversation = false;
        checkForMessages = false;
        usernames = new ArrayList<>();
        conversationNumber = 0;
        lastMessage = 0;
        conversationName = "";
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
            deleteConversation = valid;
            removeMembers = false;
            addMembers = false;
            newConversation = false;
            checkForMessages = false;
        }else {
            deleteConversation = false;
        }
        return this;
    }

    /**
     * Says if the request wants to remove members in the list.
     * All the other booleans will be set to false if this is set to true.
     * @param valid <code>true</code> if all the members in the list should be removed.
     *              <code>false</code> if all the members in the list should not be removed.
     * @return this builder object.
     */
    public ConversationRequestBuilder setRemoveMembers(boolean valid){
        if (valid){
            deleteConversation = false;
            removeMembers = valid;
            addMembers = false;
            newConversation = false;
            checkForMessages = false;
        }else {
            removeMembers = false;
        }
        return this;
    }

    /**
     * Says if the request wants to add members that are in the list.
     * All the other booleans will be set to false if this is set to true.
     * @param valid <code>true</code> if the list with usernames are going to be added.
     *              <code>false</code> if the usernames should not be added.
     * @return this builder object.
     */
    public ConversationRequestBuilder setAddMembers(boolean valid){
        if (valid) {
            deleteConversation = false;
            removeMembers = false;
            addMembers = valid;
            newConversation = false;
            checkForMessages = false;
        }else{
            addMembers = false;
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
            removeMembers = false;
            addMembers = false;
            newConversation = valid;
            checkForMessages = false;
        }else {
            newConversation = false;
        }
        return this;
    }

    /**
     * Says if the request is about checking for new messages.
     * All the other booleans will be set to false if this is set to true.
     * @param valid <code>true</code> if the request wants to check for new memssages.
     *              <code>false</code> if the does not want to check for new messages.
     * @return this builder object.
     */
    public ConversationRequestBuilder setCheckForMessages(boolean valid){
        if (valid){
            deleteConversation = false;
            removeMembers = false;
            addMembers = false;
            newConversation = false;
            checkForMessages = valid;
        }else {
            checkForMessages = false;
        }
        return this;
    }

    /**
     * Adds a list with usernames to the request object.
     * @param usernames the list with usernames you want to add.
     * @return this builder object.
     */
    public ConversationRequestBuilder addUsernames(List<String> usernames){
        checkIfObjectIsNull(usernames, "usernames");
        this.usernames = usernames;
        return this;
    }

    /**
     * Adds a conversation number to the request.
     * @param conversationNumber the conversation number.
     * @return this builder object.
     */
    public ConversationRequestBuilder addConversationNumber(long conversationNumber){
        checkIfLongIsAboveZero(conversationNumber, "messagelog number", false);
        this.conversationNumber = conversationNumber;
        return this;
    }

    /**
     * Adds a list size to the request.
     * @param lastMessage the list size of the conversation.
     * @return this builder object.
     */
    public ConversationRequestBuilder addLastMessageNumber(long lastMessage){
        checkIfLongIsAboveZero(lastMessage, "list size", true);
        this.lastMessage = lastMessage;
        return this;
    }

    /**
     * Adds a conversation name to the request.
     * @param conversationName the conversation name.
     * @return this builder object.
     */
    public ConversationRequestBuilder addConversationName(String conversationName){
        checkString(conversationName, "conversation name");
        this.conversationName = conversationName;
        return this;
    }

    /**
     * Adds the date you want to check.
     * @param date the date that the request is about.
     * @return this builder object.
     */
    public ConversationRequestBuilder addDate(LocalDate date){
        checkIfObjectIsNull(date, "date");
        this.dateMade = date;
        return this;
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
        return conversationName;
    }

    /**
     * Gets the date of the conversation.
     * @return the date of the conversation.
     */
    public LocalDate getDate() {
        return dateMade;
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
    public void checkIfLongIsAboveZero(long logNumber, String prefix, boolean equalZero){
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
