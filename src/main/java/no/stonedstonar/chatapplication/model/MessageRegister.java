package no.stonedstonar.chatapplication.model;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

/**
 * A class that holds messages 
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class MessageRegister {

    private Map<String, MessageLog> messageLogMap;

    /**
      * Makes an instance of the MessageRegister class.
      */
    public MessageRegister(){
        messageLogMap = new HashMap<>();
    }

    /**
     * Gets the message log that matches this username. Makes a new message log if the user don't have one.
     * @param username the username that the message log is for.
     * @return the message log that belongs to this username.
     */
    public MessageLog getMessageLog(String username){
        MessageLog messageLog;
        if (checkIfUsernameHasMessageLog(username)){
            messageLog =  messageLogMap.get(username);
        }else {
            messageLog = new MessageLog();
            messageLogMap.put(username, messageLog);
        }
        return messageLog;
    }

    /**
     * Removes a message log from the system.
     * @param username the username the message log is with.
     */
    public void removeMessageLog(String username){
        if (checkIfUsernameHasMessageLog(username)){
            messageLogMap.remove(username);
        }else {
            throw new IllegalArgumentException("The user with the name " + username + " does not have a messagelog.");
        }
    }

    /**
     * Checks if the username has a message log.
     * @param username the username you want to check.
     * @return <code>true</code> if the username has a message log.
     *         <code>false</code> if the username does not have a messagelog.
     */
    private boolean checkIfUsernameHasMessageLog(String username){
        checkString(username, "username");
        return messageLogMap.keySet().stream().anyMatch(name -> name.equals(username));
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
