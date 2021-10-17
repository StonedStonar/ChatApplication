package no.stonedstonar.chatapplication.model.conversation;

import no.stonedstonar.chatapplication.model.Members;
import no.stonedstonar.chatapplication.model.exception.message.CouldNotAddMessageException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotGetMessageLogException;
import no.stonedstonar.chatapplication.model.message.Message;
import no.stonedstonar.chatapplication.model.messagelog.ConversationObserver;
import no.stonedstonar.chatapplication.model.messagelog.MessageLog;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a personal conversation that is on the client side.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class PersonalConversation implements ObservableConversation, Serializable {

    private List<MessageLog> messageLogList;

    private String conversationName;

    private LocalDate dateMade;

    private long conversationNumber;

    private Members members;

    private volatile List<no.stonedstonar.chatapplication.model.messagelog.ConversationObserver> conversationObservers;

    private volatile Message newlyAddedMessage;

    private volatile boolean removed;


    /**
     * Makes an instance of the PersonalNormalConversation class.
     * @param conversation the conversation this personal conversation is going to imitate.
     */
    public PersonalConversation(Conversation conversation) {
        checkIfObjectIsNull(conversation, "conversation");
        this.conversationName = conversation.getConversationName();
        this.conversationNumber = conversation.getConversationNumber();
        this.dateMade = conversation.getDateMade();
        this.members = conversation.getConversationMembers();
        //Todo: Vurder å ikke ta imot hele samtalen men at dette objektet må spørre etter deler av den.
        messageLogList = new ArrayList<>();
    }

    /**
     * Adds a new message to the conversation.
     * @param message the new message you want to add.
     * @throws CouldNotAddMessageException gets thrown if the message could not be added.
     * @throws CouldNotGetMessageLogException gets thrown if the message log could not be located.
     */
    public void addMessage(Message message) throws CouldNotAddMessageException, CouldNotGetMessageLogException {
        conversation.addNewMessage(message);
        newlyAddedMessage = message;
        removed = false;
        notifyObservers();
    }

    /**
     * Gets the conversation number.
     * @return the conversation number.
     */
    public long getConversationNumber() {
        return conversationNumber;
    }

    @Override
    public void registerObserver(no.stonedstonar.chatapplication.model.conversation.ConversationObserver conversationObserver) {

    }

    @Override
    public void removeObserver(no.stonedstonar.chatapplication.model.conversation.ConversationObserver conversationObserver) {

    }

    @Override
    public void notifyObservers() {

    }

    @Override
    public boolean checkIfObjectIsObserver(no.stonedstonar.chatapplication.model.conversation.ConversationObserver conversationObserver) {
        return false;
    }

    @Override
    public boolean checkIfObjectIsObserver(ConversationObserver conversationObserver) {
        return false;
    }


    /**
     * Checks if a list is of a valid format.
     * @param list the list you want to check.
     * @param prefix the prefix the error should have.
     */
    protected void checkIfListIsValid(List list, String prefix){
        checkIfObjectIsNull(list, prefix);
        if (list.isEmpty()){
            throw new IllegalArgumentException("The " + prefix + " list cannot be zero in size.");
        }
    }

    /**
     * Checks if a string is of a valid format or not.
     * @param stringToCheck the string you want to check.
     * @param errorPrefix the error the exception should have if the string is invalid.
     */
    private void checkString(String stringToCheck, String errorPrefix){
        checkIfObjectIsNull(stringToCheck, errorPrefix);
        if (stringToCheck.isEmpty()){
            throw new IllegalArgumentException("The " + errorPrefix + " cannot be empty.");
        }
    }

    /**
     * Checks if an object is null.
     * @param object the object you want to check.
     * @param error the error message the exception should have.
     */
    private void checkIfObjectIsNull(Object object, String error){
        if (object == null){
            throw new IllegalArgumentException("The " + error + " cannot be null.");
        }
    }
}
