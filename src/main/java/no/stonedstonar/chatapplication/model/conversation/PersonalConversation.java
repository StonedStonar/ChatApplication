package no.stonedstonar.chatapplication.model.conversation;

import no.stonedstonar.chatapplication.model.exception.conversation.UsernameNotPartOfConversationException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotGetMessageLogException;
import no.stonedstonar.chatapplication.model.message.Message;
import no.stonedstonar.chatapplication.model.messagelog.PersonalMessageLog;

import java.time.LocalDate;
import java.util.List;

/**
 * A basic
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public interface PersonalConversation extends Conversation, ObservableConversation{

    @Override
    PersonalMessageLog getMessageLogForDate(LocalDate localDate, String username) throws CouldNotGetMessageLogException, UsernameNotPartOfConversationException;

    /**
     * Gets all the messages of the conversation as a list.
     * @return a list with all the messages.
     */
    List<Message> getAllMessagesOfConversationAsList();
}
