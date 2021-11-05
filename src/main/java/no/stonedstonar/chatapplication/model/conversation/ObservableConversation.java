package no.stonedstonar.chatapplication.model.conversation;

import no.stonedstonar.chatapplication.model.exception.conversation.UsernameNotPartOfConversationException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotGetMessageLogException;
import no.stonedstonar.chatapplication.model.member.Member;
import no.stonedstonar.chatapplication.model.membersregister.ObservableMemberRegister;
import no.stonedstonar.chatapplication.model.message.Message;
import no.stonedstonar.chatapplication.model.messagelog.PersonalMessageLog;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents an observable conversation that is used on the client side.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public interface ObservableConversation extends Conversation{

    /**
     * Gets all the messages of the conversation as a list.
     * @return a list with all the messages.
     */
    List<Message> getAllMessagesOfConversationAsList();

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
     * Notifies the observers about a new name of the conversation.
     * @param name the new name.
     */
    void notifyAboutNewName(String name);

    /**
     * Notifies the observers about a change.
     * @param member the member that has been added or removed.
     * @param removed <code>true</code> if the member has been removed.
     *                <code>false</code> if the member has been added.
     */
    void notifyObserversOfNewMember(Member member, boolean removed);

    /**
     * Checks if the object is a observer.
     * @param conversationObserver the message observer you want to check.
     * @return <code>true</code> if the object is an observer.
     *         <code>false</code> if the object is not a observer.
     */
    boolean checkIfObjectIsObserver(ConversationObserver conversationObserver);

    @Override
    ObservableMemberRegister getMembers();

    @Override
    PersonalMessageLog getMessageLogForDate(LocalDate localDate, String username) throws CouldNotGetMessageLogException, UsernameNotPartOfConversationException;
}
