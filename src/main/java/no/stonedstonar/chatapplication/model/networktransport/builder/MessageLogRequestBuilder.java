package no.stonedstonar.chatapplication.model.networktransport.builder;

import no.stonedstonar.chatapplication.model.networktransport.MessageLogRequest;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class MessageLogRequestBuilder {

    private boolean deleteMessageLog;

    private boolean removeMembers;

    private boolean addMembers;

    private boolean newMessageLog;

    private List<String> usernames;

    private long messageLogNumber;

    /**
     * Makes an instance of the MessageLogRequest class.
     */
    public MessageLogRequestBuilder(){
        deleteMessageLog = false;
        removeMembers = false;
        addMembers = false;
        newMessageLog = false;
        usernames = new ArrayList<>();
        messageLogNumber = 0;
    }

    /**
     * Says if the request wants to delete a message log.
     * All the other booleans will be set to false if this is set to true.
     * @param valid <code>true</code> if the user wants to delete a message log.
     *              <code>false</code> if the user don't want to delete a messagelog.
     * @return this builder object.
     */
    public MessageLogRequestBuilder setDeleteMessageLog(boolean valid){
        if (valid){
            deleteMessageLog = valid;
            removeMembers = false;
            addMembers = false;
            newMessageLog = false;
        }else {
            deleteMessageLog = false;
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
    public MessageLogRequestBuilder setRemoveMembers(boolean valid){
        if (valid){
            deleteMessageLog = false;
            removeMembers = valid;
            addMembers = false;
            newMessageLog = false;
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
    public MessageLogRequestBuilder setAddMembers(boolean valid){
        if (valid) {
            deleteMessageLog = false;
            removeMembers = false;
            addMembers = valid;
            newMessageLog = false;
        }else{
            addMembers = false;
        }
        return this;
    }

    /**
     * Says if the request is about making a new messagelog.
     * All the other booleans will be set to false if this is set to true.
     * @param valid <code>true</code> if the request is about making a new message log.
     *              <code>false</code> if the request is not about making a new message log.
     * @return this builder object.
     */
    public MessageLogRequestBuilder setNewMessageLog(boolean valid){
        if (valid){
            deleteMessageLog = false;
            removeMembers = false;
            addMembers = false;
            newMessageLog = valid;
        }else {
            newMessageLog = false;
        }
        return this;
    }

    /**
     * Adds a list with usernames to the request object.
     * @param usernames the list with usernames you want to add.
     * @return this builder object.
     */
    public MessageLogRequestBuilder addUsernames(List<String> usernames){
        checkIfObjectIsNull(usernames, "usernames");
        this.usernames = usernames;
        return this;
    }

    /**
     * Adds a message log number to the request.
     * @param messageLogNumber the message log number.
     * @return this builder object.
     */
    public MessageLogRequestBuilder addMessageLogNumber(long messageLogNumber){
        checkIfLongIsAboveZero(messageLogNumber, "messagelog number");
        this.messageLogNumber = messageLogNumber;
        return this;
    }

    /**
     * Says true if the request is an delete message log request.
     * @return <code>true</code> if the message log needs to be deleted.
     *         <code>false</code> if the message log is going to be deleted.
     */
    public boolean isDeleteMessageLog() {
        return deleteMessageLog;
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
     * Says true if this request is a new message log.
     * @return <code>true</code> if this request is a new message log.
     *         <code>false</code> if this request is not a new message log.
     */
    public boolean isNewMessageLog() {
        return newMessageLog;
    }

    /***
     * Gets the list of all usernames.
     * @return a list with all the usernames.
     */
    public List<String> getUsernames() {
        return usernames;
    }

    /**
     * Gets the message log number.
     * @return the message log number.
     */
    public long getMessageLogNumber() {
        return messageLogNumber;
    }

    /**
     * Makes the message log request.
     * @return a request that matches the set values in this builder.
     */
    public MessageLogRequest build(){
        MessageLogRequest messageLogRequest = new MessageLogRequest(this);
        return messageLogRequest;
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
    public void checkIfLongIsAboveZero(long logNumber, String prefix){
        if(logNumber <= 0){
            throw new IllegalArgumentException("The " + prefix + " cannot be negative or null.");
        }
    }
}
