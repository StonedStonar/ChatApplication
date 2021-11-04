package no.stonedstonar.chatapplication.model.conversationregister.personal;

import javafx.beans.Observable;
import no.stonedstonar.chatapplication.model.conversation.ObservableConversation;
import no.stonedstonar.chatapplication.model.conversationregister.ConversationRegister;
import no.stonedstonar.chatapplication.model.exception.conversation.CouldNotAddConversationException;
import no.stonedstonar.chatapplication.model.exception.conversation.CouldNotGetConversationException;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a basic template for what a personal conversation register should hold and implements the observable design pattern.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public interface PersonalConversationRegister extends ConversationRegister, Serializable {

    @Override
    ObservableConversation getConversationByNumber(long messageLogNumber) throws CouldNotGetConversationException;

    /**
     * Adds a conversation to the personal conversation.
     * @param observableConversation the new conversation.
     * @throws CouldNotAddConversationException gets thrown if a conversation is already in the register.
     */
    void addConversation(ObservableConversation observableConversation) throws CouldNotAddConversationException;

    /**
     * Gets all the conversation numbers of the personal register.
     * @return a list with all the conversation numbers.
     */
    List<Long> getAllConversationNumbers();

    /**
     * Register a new object as a subscriber.
     * @param conversationRegisterObserver the new observer.
     */
    void registerObserver(ConversationRegisterObserver conversationRegisterObserver);

    /**
     * Removes an observer as a subscriber.
     * @param conversationRegisterObserver the observer you want to remove.
     */
    void removeObserver(ConversationRegisterObserver conversationRegisterObserver);

    /**
     * Notifies all the observers that a change has occurred.
     */
    void notifyObservers();

    /**
     * Checks if the object is a observer.
     * @param conversationRegisterObserver the message observer you want to check.
     * @return <code>true</code> if the object is an observer.
     *         <code>false</code> if the object is not a observer.
     */
    boolean checkIfObjectIsObserver(ConversationRegisterObserver conversationRegisterObserver);

    /**
     * Gets the iterator of this personal register.
     * @return the iterator of this register.
     */
    Iterator<ObservableConversation> getIterator();
}
