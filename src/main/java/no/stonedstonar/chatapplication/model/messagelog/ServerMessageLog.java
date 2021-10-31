package no.stonedstonar.chatapplication.model.messagelog;

import no.stonedstonar.chatapplication.model.message.Message;

import java.util.List;

/**
 *
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public interface ServerMessageLog extends MessageLog{

    /**
     * Gets the newest messages from the message log.
     * @param lastMessageNumber the last message the other log has gotten.
     * @return a list with the new messages.
     */
    List<Message> checkForNewMessages(long lastMessageNumber);

}
