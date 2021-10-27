package no.stonedstonar.chatapplication.model.conversationregister.personal;

import no.stonedstonar.chatapplication.model.conversation.Conversation;
import no.stonedstonar.chatapplication.model.conversation.NormalObservableConversation;
import no.stonedstonar.chatapplication.model.conversation.ObservableConversation;
import no.stonedstonar.chatapplication.model.conversation.ServerConversation;
import no.stonedstonar.chatapplication.model.exception.conversation.CouldNotAddConversationException;
import no.stonedstonar.chatapplication.model.exception.conversation.CouldNotGetConversationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a personal conversation that is used on the client side.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class NormalPersonalConversationRegister implements PersonalConversationRegister {

    private final List<ConversationRegisterObserver> conversationRegisterObservers;

    private final List<ObservableConversation> observableConversations;

    private ObservableConversation newlyAddedNormalObservableConversation;

    private boolean removed;

    /**
      * Makes an instance of the PersonalConversationRegister class.
      * @param messageLogList the list of all the conversations of a user.
     *  @param username the username of the user that is making the personal conversation register.
      */
    public NormalPersonalConversationRegister(List<ServerConversation> messageLogList, String username){
        checkIfObjectIsNull(messageLogList, "message log list");
        checkString(username, "username");
        conversationRegisterObservers = new ArrayList<>();
        observableConversations = new ArrayList<>();
        messageLogList.forEach(log -> {
            NormalObservableConversation personalMessageLog = new NormalObservableConversation(log, username);
            observableConversations.add(personalMessageLog);
        });
    }

    /**
     * Gets all the conversations.
     * @return a list with all the conversations.
     */
    public List<ObservableConversation> getMessageLogList() {
        return observableConversations;
    }

    @Override
    public void addConversation(ObservableConversation observableConversation) throws CouldNotAddConversationException {
        checkIfObjectIsNull(observableConversation, "personal conversation");
        if (!checkIfConversationIsInRegister(observableConversation)){
            observableConversations.add(observableConversation);
            newlyAddedNormalObservableConversation = observableConversation;
            removed = false;
            notifyObservers();
        }else {
            throw new CouldNotAddConversationException("The conversation is already in the register.");
        }
    }

    @Override
    public List<Long> getAllConversationNumbers() {
        return observableConversations.stream().map(convo -> convo.getConversationNumber()).toList();
    }

    /**
     * Checks if the conversation is in the register based on the conversation number.
     * @param conversation the conversation to check for.
     * @return <code>true</code> if the conversation is in the register.
     *         <code>false</code> if the conversation is not in the register.
     */
    private boolean checkIfConversationIsInRegister(Conversation conversation){
        return observableConversations.stream().anyMatch(con -> con.getConversationNumber() == conversation.getConversationNumber());
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
        conversationRegisterObservers.forEach(obs -> obs.updateConversation(newlyAddedNormalObservableConversation, removed));
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

    @Override
    public ObservableConversation getConversationByNumber(long conversationNumber) throws CouldNotGetConversationException {
        checkIfLongIsAboveZero(conversationNumber, "conversation number");
        Optional<ObservableConversation> optionalPersonalConversation = observableConversations.stream().filter(conv -> conv.getConversationNumber() == conversationNumber).findFirst();
        if (optionalPersonalConversation.isPresent()){
             return optionalPersonalConversation.get();
        }else {
            throw new CouldNotGetConversationException("The conversation with the conversation number " + conversationNumber + " is not in this register.");
        }
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
}
