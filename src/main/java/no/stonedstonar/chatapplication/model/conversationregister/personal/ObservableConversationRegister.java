package no.stonedstonar.chatapplication.model.conversationregister.personal;

/**
 * Represents a conversation register that can be observed.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public interface ObservableConversationRegister {

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
}
