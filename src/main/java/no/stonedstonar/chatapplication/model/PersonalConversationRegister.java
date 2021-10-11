package no.stonedstonar.chatapplication.model;

import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotAddMessageLogException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotGetMessageLogException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotRemoveMessageLogException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a personal conversation that is used on the client side.
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class PersonalConversationRegister implements ObservableConversation, Serializable {

    private List<ConversationObserver> conversationObservers;

    private MessageLog messageLog;

    private List<PersonalMessageLog> personalMessageLogs;

    private boolean removed;

    /**
      * Makes an instance of the PersonalConversationRegister class.
      */
    public PersonalConversationRegister(List<MessageLog> messageLogList){
        checkIfObjectIsNull(messageLogList, "message log list");
        conversationObservers = new ArrayList<>();
        personalMessageLogs = new ArrayList<>();
        messageLogList.forEach(log -> {
            PersonalMessageLog personalMessageLog = new PersonalMessageLog(log);
            personalMessageLogs.add(personalMessageLog);
        });
        System.out.println("Done");
    }

    /**
     * Gets all the message logs.
     * @return a list with all the message logs.
     */
    public List<PersonalMessageLog> getMessageLogList() {
        return personalMessageLogs;
    }

    /**
     * Adds a message log to the personal conversation.
     * @param messageLog the new message log.
     * @throws CouldNotAddMessageLogException gets thrown if a message log is already in the register.
     */
    public void addMessageLog(MessageLog messageLog) throws CouldNotAddMessageLogException {
        PersonalMessageLog personalMessageLog = new PersonalMessageLog(messageLog);
        if (!personalMessageLogs.stream().anyMatch(log -> log.getMessageLogNumber() == messageLog.getMessageLogNumber())){
            personalMessageLogs.add(personalMessageLog);
            this.messageLog = messageLog;
            removed = false;
            notifyObservers();
        }else {
            throw new CouldNotAddMessageLogException("The message log is already in the register.");
        }
    }

    /**
     * Gets the personal message log that matches the message log number.
     * @param messageLogNumber the number that the message log has.
     * @return the message log that matches this number.
     * @throws CouldNotGetMessageLogException gets thrown if the message log could not be found.
     */
    public PersonalMessageLog getPersonalMessageLogByNumber(long messageLogNumber) throws CouldNotGetMessageLogException {
        checkIfLongIsAboveZero(messageLogNumber, "message log number");
        Optional<PersonalMessageLog> optionalPersonalMessageLog = personalMessageLogs.stream().filter(log -> log.getMessageLogNumber() == messageLogNumber).findFirst();
        if (optionalPersonalMessageLog.isPresent()){
            return optionalPersonalMessageLog.get();
        }else {
            throw new CouldNotGetMessageLogException("The message log with the log number " + messageLogNumber + " is not a part of this register.");
        }

    }

    @Override
    public void registerObserver(ConversationObserver conversationObserver) {
        checkIfObjectIsNull(conversationObserver, "conversation observer");
        if (!conversationObservers.contains(conversationObserver)){
            conversationObservers.add(conversationObserver);
        }else {
            throw new IllegalArgumentException("The conversation observer " + conversationObserver + " is not a part of this message log.");
        }
    }

    @Override
    public void removeObserver(ConversationObserver conversationObserver) {
        checkIfObjectIsNull(conversationObserver, "conversation observer");
        if (!conversationObservers.contains(conversationObserver)){
            conversationObservers.remove(conversationObserver);
        }else {
            throw new IllegalArgumentException("The conversation observer " + conversationObserver + " is not a part of this message log.");
        }
    }

    @Override
    public void notifyObservers() {
        conversationObservers.forEach(obs -> obs.update(messageLog, removed));
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
