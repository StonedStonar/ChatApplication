package no.stonedstonar.chatapplication.model;

/**
 * An interface that represents what an observable message log should do.
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public interface ObservableMessageLog {

    /**
     * Register a new object as a subscriber.
     * @param messageObserver the new observer.
     */
    void registerObserver(MessageObserver messageObserver);

    /**
     * Removes an observer as a subscriber.
     * @param messageObserver the observer you want to remove.
     */
    void removeObserver(MessageObserver messageObserver);

    /**
     * Notifies all the observers that a change has occurred.
     */
    void notifyObservers();

    /**
     * Checks if the object is a observer.
     * @param messageObserver the message observer you want to check.
     * @return <code>true</code> if the object is an observer.
     *         <code>false</code> if the object is not a observer.
     */
    boolean checkIfObjectIsObserver(MessageObserver messageObserver);
}
