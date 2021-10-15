package no.stonedstonar.chatapplication.backend;

import no.stonedstonar.chatapplication.model.message.TextMessage;

import java.util.logging.Level;

/**
 * An observer that observes a servers running time.
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public interface ServerObserver {
    /**
     * Tells the observer that there is a new message.
     * @param message the new text message.
     * @param level the level this message log is at.
     */
    void updateMessage(String message, Level level);
}
