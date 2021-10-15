package no.stonedstonar.chatapplication.model;

import no.stonedstonar.chatapplication.model.exception.textmessage.CouldNotAddTextMessageException;
import no.stonedstonar.chatapplication.model.exception.textmessage.CouldNotRemoveTextMessageException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a personal message log that is used by the client side.
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class PersonalMessageLog extends MessageLog implements ObservableMessageLog, Serializable {

    private volatile List<MessageObserver> messageObservers;

    private volatile TextMessage textMessageObs;

    private volatile boolean removed;

    /**
      * Makes an instance of the PersonalMessageLog class.
      * @param messageLog the message log this Personal message log is going to take over.
      */
    public PersonalMessageLog(MessageLog messageLog){
        super(messageLog.getMessageLogNumber());
        messageObservers = new ArrayList<>();
        List<TextMessage> textMessages = getMessageList();
        textMessages.addAll(messageLog.getMessageList());
        super.setMembersRegister(messageLog.getMembersOfConversation());
    }

    @Override
    public void addMessage(TextMessage textMessage) throws CouldNotAddTextMessageException {
        super.addMessage(textMessage);
        this.textMessageObs = textMessage;
        removed = false;
        notifyObservers();
    }

    @Override
    public void addAllMessages(List<TextMessage> textMessages) throws CouldNotAddTextMessageException {
        checkIfListIsValid(textMessages, "text message");
        if (!checkIfAllMessagesAreNewMessages(textMessages)){
            List<TextMessage> textMessageList = getMessageList();
            textMessages.forEach(message -> {
                textMessageList.add(textMessageObs);
                this.textMessageObs = message;
                removed = false;
                notifyObservers();
            });
        }else {
            throw new CouldNotAddTextMessageException("Could not add the new messages since one of them are already in the reigster.");
        }
    }

    @Override
    public void removeMessage(TextMessage textMessage) throws CouldNotRemoveTextMessageException {
        super.removeMessage(textMessage);
        this.textMessageObs = textMessage;
        removed = true;
        notifyObservers();
    }


    @Override
    public void registerObserver(MessageObserver messageObserver) {
        checkIfObjectIsNull(messageObserver, "message observer");
        if (!messageObservers.contains(messageObserver)){
            messageObservers.add(messageObserver);
        }else {
            throw new IllegalArgumentException("The message observer " + messageObserver + " is not a part of this message log.");
        }
    }

    @Override
    public void removeObserver(MessageObserver messageObserver) {
        checkIfObjectIsNull(messageObserver, "message observer");
        if (!messageObservers.contains(messageObserver)){
            messageObservers.remove(messageObserver);
        }else {
            throw new IllegalArgumentException("The message observer " + messageObserver + " is not a part of this message log.");
        }
    }

    @Override
    public void notifyObservers() {
        for (MessageObserver obs : messageObservers) {
            obs.updateMessage(textMessageObs, removed);
        }
    }

    @Override
    public boolean checkIfObjectIsObserver(MessageObserver messageObserver) {
        return messageObservers.stream().anyMatch(obs -> obs == messageObserver);
    }
}
