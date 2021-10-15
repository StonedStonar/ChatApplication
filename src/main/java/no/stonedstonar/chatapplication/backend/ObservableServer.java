package no.stonedstonar.chatapplication.backend;

import no.stonedstonar.chatapplication.model.message.MessageObserver;

/**
 * Represents a server that is observable by the errors that occur on it.
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public interface ObservableServer {

    /**
     * Register a new object as a subscriber.
     * @param serverObserver the new observer.
     */
    void registerObserver(ServerObserver serverObserver);

    /**
     * Removes an observer as a subscriber.
     * @param serverObserver the observer you want to remove.
     */
    void removeObserver(ServerObserver serverObserver);

    /**
     * Notifies all the observers that a change has occurred.
     */
    void notifyObservers();

    /**
     * Checks if the object is an observer.
     * @param serverObserver the message observer you want to check.
     * @return <code>true</code> if the object is an observer.
     *         <code>false</code> if the object is not an observer.
     */
    boolean checkIfObjectIsObserver(ServerObserver serverObserver);
}
