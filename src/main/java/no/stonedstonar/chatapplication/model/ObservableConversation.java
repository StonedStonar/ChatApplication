package no.stonedstonar.chatapplication.model;
/**
 *
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public interface ObservableConversation {

    /**
     * Register a new object as a subscriber.
     * @param conversationObserver the new observer.
     */
    void registerObserver(ConversationObserver conversationObserver);

    /**
     * Removes an observer as a subscriber.
     * @param conversationObserver the observer you want to remove.
     */
    void removeObserver(ConversationObserver conversationObserver);

    /**
     * Notifies all the observers that a change has occurred.
     */
    void notifyObservers();
}
