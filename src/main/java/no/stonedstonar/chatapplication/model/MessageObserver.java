package no.stonedstonar.chatapplication.model;


/**
 * An interface that represents an observer that waits for new messages to arrive.
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public interface MessageObserver {

    /**
     * Tells the observer that there is a new message.
     * @param textMessage the new text message.
     * @param removed <code>true</code> if the message was removed.
     *                <code>false</code> if the message was added.
     */
    void updateMessage(TextMessage textMessage, boolean removed);
}
