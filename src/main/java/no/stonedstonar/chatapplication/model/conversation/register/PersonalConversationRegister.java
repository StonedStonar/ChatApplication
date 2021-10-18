package no.stonedstonar.chatapplication.model.conversation.register;

import no.stonedstonar.chatapplication.model.conversation.Conversation;
import no.stonedstonar.chatapplication.model.conversation.PersonalConversation;
import no.stonedstonar.chatapplication.model.conversation.register.ConversationRegisterObserver;
import no.stonedstonar.chatapplication.model.conversation.register.ObservableConversationRegister;
import no.stonedstonar.chatapplication.model.exception.conversation.CouldNotAddConversationException;
import no.stonedstonar.chatapplication.model.exception.conversation.CouldNotGetConversationException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a personal conversation that is used on the client side.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class PersonalConversationRegister implements ObservableConversationRegister, Serializable {

    private final List<ConversationRegisterObserver> conversationRegisterObservers;

    private final List<PersonalConversation> personalConversations;

    private PersonalConversation newlyAddedPersonalConversation;

    private boolean removed;

    /**
      * Makes an instance of the PersonalConversationRegister class.
      * @param normalMessageLogList the list of all the conversations of a user.
      */
    public PersonalConversationRegister(List<Conversation> normalMessageLogList){
        checkIfObjectIsNull(normalMessageLogList, "message log list");
        conversationRegisterObservers = new ArrayList<>();
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
        if (!checkIfConversationIsInRegister(conversation)){
            personalConversations.add(personalMessageLog);
            this.newlyAddedPersonalConversation = personalMessageLog;
            removed = false;
            notifyObservers();
        }else {
            throw new CouldNotAddConversationException("The conversation is already in the register.");
        }
    }

    /**
     * Checks if a conversation is already in the register.
     * @param conversation the conversation you want to check.
     * @return <code>true</code> if the conversation is in the register.
     *         <code>false</code> if the conversation is not in this register.
     */
    private boolean checkIfConversationIsInRegister(Conversation conversation){
        return personalConversations.stream().anyMatch(log -> log.getConversationNumber() == conversation.getConversationNumber());
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
    public void registerObserver(ConversationRegisterObserver conversationRegisterObserver) {
        checkIfObjectIsNull(conversationRegisterObserver, "conversation observer");
        if (!conversationRegisterObservers.contains(conversationRegisterObserver)){
            conversationRegisterObservers.add(conversationRegisterObserver);
        }else {
            throw new IllegalArgumentException("The conversation observer " + conversationRegisterObserver + " is not a part of this message log.");
        }
    }

    @Override
    public void removeObserver(ConversationRegisterObserver conversationRegisterObserver) {
        checkIfObjectIsNull(conversationRegisterObserver, "conversation observer");
        if (!conversationRegisterObservers.contains(conversationRegisterObserver)){
            conversationRegisterObservers.remove(conversationRegisterObserver);
        }else {
            throw new IllegalArgumentException("The conversation observer " + conversationRegisterObserver + " is not a part of this message log.");
        }
    }

    @Override
    public void notifyObservers() {
        conversationRegisterObservers.forEach(obs -> obs.updateConversation(newlyAddedPersonalConversation, removed));
    }

    @Override
    public boolean checkIfObjectIsObserver(ConversationRegisterObserver conversationRegisterObserver) {
        checkIfObjectIsNull(conversationRegisterObserver, "conversation observer");
        return conversationRegisterObservers.stream().anyMatch(obs -> obs.equals(conversationRegisterObserver));
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
