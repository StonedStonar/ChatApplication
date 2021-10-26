package no.stonedstonar.chatapplication.model.conversation;

/**
 *
 * @version 0.2
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
     * Notifies all the observers that a new message has come.
     */
    void notifyObserversAboutNewMessage();

    /**
     * Notifies all the observers that a new member is added.
     */
    void notifyObserversAboutNewMember();

    /**
     * Checks if the object is a observer.
     * @param conversationObserver the message observer you want to check.
     * @return <code>true</code> if the object is an observer.
     *         <code>false</code> if the object is not a observer.
     */
    boolean checkIfObjectIsObserver(ConversationObserver conversationObserver);
}
