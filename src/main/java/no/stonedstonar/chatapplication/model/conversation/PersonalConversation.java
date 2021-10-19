package no.stonedstonar.chatapplication.model.conversation;

import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotGetMessageLogException;
import no.stonedstonar.chatapplication.model.message.Message;
import no.stonedstonar.chatapplication.model.messagelog.PersonalMessageLog;
import no.stonedstonar.chatapplication.model.messagelog.ServerMessageLog;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public interface PersonalConversation extends Conversation, ObservableConversation{

    @Override
    PersonalMessageLog getMessageLogForDate(LocalDate localDate) throws CouldNotGetMessageLogException;

    /**
     * Gets all the messages of the conversation as a list.
     * @return a list with all the messages.
     */
    List<Message> getAllMessagesOfConversationAsList();
}
