package no.stonedstonar.chatapplication.model.networktransport;

import no.stonedstonar.chatapplication.model.networktransport.builder.MessageLogRequestBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a message log request that can do different operations based on the input values.
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class MessageLogRequest implements Serializable {

    private boolean deleteMessageLog;

    private boolean removeMembers;

    private boolean addMembers;

    private boolean newMessageLog;

    private List<String> usernames;

    private long messageLogNumber;

    /**
      * Makes an instance of the MessageLogRequest class.
      */
    public MessageLogRequest(MessageLogRequestBuilder messageLogRequestBuilder){
        deleteMessageLog = messageLogRequestBuilder.isDeleteMessageLog();
        removeMembers = messageLogRequestBuilder.isRemoveMembers();
        addMembers = messageLogRequestBuilder.isAddMembers();
        newMessageLog = messageLogRequestBuilder.isNewMessageLog();
        usernames = messageLogRequestBuilder.getUsernames();
        messageLogNumber = messageLogRequestBuilder.getMessageLogNumber();
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
