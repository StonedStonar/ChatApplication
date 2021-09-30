package no.stonedstonar.chatapplication.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A class that holds messages from one user to other people.
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class ConversationRegister {

    private List<MessageLog> messageLogList;

    //Todo: Lage det slik at mange kan være del i en samtale.
    //Todo: Lage det slik at kjennetegnet på gruppen er alle brukernavn i en lang streng. (Medlemmer kan sikkert være i en liste på eget objekt.)

    /**
      * Makes an instance of the MessageRegister class.
      */
    public ConversationRegister(){
        messageLogList = new ArrayList<>();
    }

    /**
     * Gets the message logs that matches this username.
     * @param username the username that the message log is for.
     * @return the message logs that belongs to this username in a list.
     */
    public List<MessageLog> getAllMessageLogsOfUsername(String username){
        checkString(username, "username");
        return messageLogList.stream().filter(messageLog -> {
            return messageLog.getMembersOfConversation().checkIfUsernameIsMember(username);
        }).toList();
    }

    /**
     * Gets the message log that matches the message log number.
     * @param messageLogNumber the number that the message log has.
     * @return the message log that matches this number.
     */
    public MessageLog getMessageLogByLogNumber(long messageLogNumber){
        checkIfLongIsAboveZero(messageLogNumber, "message log number");
        Optional<MessageLog> optionalMessageLog = messageLogList.stream().filter(log -> log.getMessageLogNumber() == messageLogNumber).findFirst();
        if (optionalMessageLog.isPresent()){
            return optionalMessageLog.get();
        }else {
            throw new IllegalArgumentException("The message log with the log number " + messageLogNumber + " is not a part of this register.");
        }
    }

    /**
     * Adds a message log to the list.
     * @param messageLog the message log thats going to be added.
     */
    private void addMessageLog(MessageLog messageLog){
        if (!checkIfMessageLogIsInList(messageLog)){
            messageLogList.add(messageLog);
        }else {
            throw new IllegalArgumentException("The message log is already in the register.");
        }
    }

    /**
     * Adds a new message log based on a list of names that are in it.
     * @param usernames a list with all the usernames that are part of this message log.
     */
    public void addNewMessageLogWithUsernames(List<String> usernames){
        checkIfObjectIsNull(usernames, "usernames");
        if (usernames.size() > 0){
            MessageLog messageLog = new MessageLog(makeNewMessageLogNumber());
            messageLog.getMembersOfConversation().addAllMembers(usernames);
            addMessageLog(messageLog);
        }else {
            throw new IllegalArgumentException("The size of the usernames must be larger than 0.");
        }
    }

    /**
     * Removes a message log from the list.
     * @param messageLog the message log you want to remove.
     */
    public void removeMessageLog(MessageLog messageLog){
        if (checkIfMessageLogIsInList(messageLog)){
            messageLogList.remove(messageLog);
        }else {
            throw new IllegalArgumentException("The message log is not in the system.");
        }
    }

    /**
     * Makes a new log number for each log that is in the list.
     * @return the number that the new message log can have.
     */
    private long makeNewMessageLogNumber(){
        long number = 1;
        if (messageLogList.size() > 0){
            number = (messageLogList.get(messageLogList.size() - 1).getMessageLogNumber() + 1);
        }
        return number;
    }

    /**
     * Checks if the message log is in the system.
     * @param messageLog the message log you want to check.
     * @return <code>true</code> if the message log is in the system.
     *         <code>false</code> if the message log is not in the system.
     */
    private boolean checkIfMessageLogIsInList(MessageLog messageLog){
        checkIfObjectIsNull(messageLog, "messagelog");
        return messageLogList.contains(messageLog);
    }

    /**
     * Checks if a string is of a valid format or not.
     * @param stringToCheck the string you want to check.
     * @param errorPrefix the error the exception should have if the string is invalid.
     */
    private void checkString(String stringToCheck, String errorPrefix){
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
