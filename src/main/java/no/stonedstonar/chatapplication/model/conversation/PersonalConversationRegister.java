package no.stonedstonar.chatapplication.model.conversation;

import no.stonedstonar.chatapplication.model.exception.conversation.CouldNotAddConversationException;
import no.stonedstonar.chatapplication.model.exception.conversation.CouldNotGetConversationException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotAddMessageLogException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotGetMessageLogException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a personal conversation that is used on the client side.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class PersonalConversationRegister implements ObservableConversation, Serializable {

    private final List<ConversationObserver> conversationObservers;

    private final List<PersonalConversation> personalConversations;

    private PersonalConversation newlyAddedPersonalConversation;

    private boolean removed;

    /**
      * Makes an instance of the PersonalConversationRegister class.
      * @param normalMessageLogList the list of all the conversations of a user.
      */
    public PersonalConversationRegister(List<Conversation> normalMessageLogList){
        checkIfObjectIsNull(normalMessageLogList, "message log list");
        conversationObservers = new ArrayList<>();
        personalConversations = new ArrayList<>();
        normalMessageLogList.forEach(log -> {
            PersonalConversation personalMessageLog = new PersonalConversation(log);
            personalConversations.add(personalMessageLog);
        });
    }

    /**
     * Gets all the conversations.
     * @return a list with all the conversations.
     */
    public List<PersonalConversation> getMessageLogList() {
        return personalConversations;
    }

    /**
     * Adds a conversation to the personal conversation.
     * @param conversation the new conversation.
     * @throws CouldNotAddConversationException gets thrown if a conversation is already in the register.
     */
    public void addConversation(Conversation conversation) throws CouldNotAddConversationException {
        PersonalConversation personalMessageLog = new PersonalConversation(conversation);
        if (!personalConversations.stream().anyMatch(log -> log.getConversationNumber() == conversation.getConversationNumber())){
            personalConversations.add(personalMessageLog);
            this.newlyAddedPersonalConversation = personalMessageLog;
            removed = false;
            notifyObservers();
        }else {
            throw new CouldNotAddConversationException("The conversation is already in the register.");
        }
    }

    /**
     * Gets the personal conversation that matches the conversation number.
     * @param conversationNumber the number that the conversation has.
     * @return the conversation that matches this number.
     * @throws CouldNotGetConversationException gets thrown if the conversation could not be found.
     */
    public PersonalConversation getPersonalConversation(long conversationNumber) throws CouldNotGetConversationException {
        checkIfLongIsAboveZero(conversationNumber, "conversation number");
        Optional<PersonalConversation> optionalPersonalMessageLog = personalConversations.stream().filter(log -> log.getConversationNumber() == conversationNumber).findFirst();
        if (optionalPersonalMessageLog.isPresent()){
            return optionalPersonalMessageLog.get();
        }else {
            throw new CouldNotGetConversationException("The conversation with the log number " + conversationNumber + " is not a part of this register.");
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
        conversationObservers.forEach(obs -> obs.updateMessageLog(newlyAddedPersonalConversation, removed));
    }

    @Override
    public boolean checkIfObjectIsObserver(ConversationObserver conversationObserver) {
        checkIfObjectIsNull(conversationObserver, "conversation observer");
        return conversationObservers.stream().anyMatch(obs -> obs.equals(conversationObserver));
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
