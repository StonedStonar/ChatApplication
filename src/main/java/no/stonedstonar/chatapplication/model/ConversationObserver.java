package no.stonedstonar.chatapplication.model;
/**
 *
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public interface ConversationObserver {

    /**
     * Tells the observer that there is a new message log.
     * @param messageLog the new message log.
     * @param removed <code>true</code> if the message log was removed.
     *                <code>false</code> if the message log was added.
     */
    void updateMessageLog(MessageLog messageLog, boolean removed);
}
