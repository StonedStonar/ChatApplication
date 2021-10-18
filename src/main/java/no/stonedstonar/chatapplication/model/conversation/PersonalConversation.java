package no.stonedstonar.chatapplication.model.conversation;

import javafx.beans.Observable;
import no.stonedstonar.chatapplication.model.exception.message.CouldNotAddMessageException;
import no.stonedstonar.chatapplication.model.exception.message.CouldNotRemoveMessageException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotGetMessageLogException;
import no.stonedstonar.chatapplication.model.message.Message;
import no.stonedstonar.chatapplication.model.messagelog.MessageLog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a personal conversation that is on the client side.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class PersonalConversation extends NormalConversation implements ObservableConversation, Serializable {

    private final List<ConversationObserver> conversationObservers;

    private volatile Message newlyAddedMessage;

    private volatile boolean removed;


    //Todo: Vurder å ikke ta imot hele samtalen men at dette objektet må spørre etter deler av den.
    // Finish this tomorrow and start on the testing part.
    /**
     * Makes an instance of the PersonalNormalConversation class.
     * @param conversation the conversation this personal conversation is going to imitate.
     */
    public PersonalConversation(Conversation conversation) {
        super(conversation.getConversationNumber(), conversation.getConversationMembers(), conversation.getDateMade(), conversation.getConversationName());
        conversationObservers = new ArrayList<>();
        if (conversation instanceof NormalConversation normalConversation){
            List<MessageLog> messageLogList = normalConversation.getMessageLogList();
            messageLogList.forEach(messageLog -> {
                getMessageLogList().add(messageLog);
            });
        }
    }

    @Override
    public void addNewMessage(Message message) throws CouldNotAddMessageException, CouldNotGetMessageLogException {
        super.addNewMessage(message);
        newlyAddedMessage = message;
        removed = false;
        notifyObservers();
    }

    @Override
    public void removeMessage(Message message) throws CouldNotGetMessageLogException, CouldNotRemoveMessageException{
        super.removeMessage(message);
        newlyAddedMessage = message;
        removed = true;
        notifyObservers();
    }


    @Override
    public void registerObserver(ConversationObserver conversationObserver) {
        if (!checkIfObjectIsObserver(conversationObserver)){
            conversationObservers.add(conversationObserver);
        }else {
            throw new IllegalArgumentException("The observer " + conversationObserver + " is already a observer of this object " + this);
        }
    }

    @Override
    public void removeObserver(ConversationObserver conversationObserver) {
        if (checkIfObjectIsObserver(conversationObserver)){
            conversationObservers.remove(conversationObserver);
        }else {
            throw new IllegalArgumentException("The observer " + conversationObserver + " is not a observer of this conversation " + this);
        }
    }

    @Override
    public void notifyObservers() {
        conversationObservers.forEach(obs -> obs.updateConversationMessage(newlyAddedMessage, removed));
    }

    @Override
    public boolean checkIfObjectIsObserver(ConversationObserver conversationObserver) {
        checkIfObjectIsNull(conversationObserver, "conversation observer");
        return conversationObservers.stream().anyMatch(obs -> obs.equals(conversationObserver));
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
