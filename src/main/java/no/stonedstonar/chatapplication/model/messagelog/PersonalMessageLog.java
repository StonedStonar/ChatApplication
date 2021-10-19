package no.stonedstonar.chatapplication.model.messagelog;

import no.stonedstonar.chatapplication.model.exception.message.CouldNotAddMessageException;
import no.stonedstonar.chatapplication.model.exception.message.CouldNotRemoveMessageException;
import no.stonedstonar.chatapplication.model.message.Message;
import no.stonedstonar.chatapplication.model.message.TextMessage;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * Represents a message log that is on the client side.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class PersonalMessageLog implements MessageLog, Serializable {

    private List<Message> messageList;

    private LocalDate dateMade;

    private long lastMessageNumber;

    /**
      * Makes an instance of the PersonalMessageLog class.
      */
    public PersonalMessageLog(ServerMessageLog serverMessageLog){
        checkIfObjectIsNull(serverMessageLog, "normal message log.");
        messageList = serverMessageLog.getMessages();
        dateMade = serverMessageLog.getDateMade();
        lastMessageNumber = serverMessageLog.getLastMessageNumber();
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

    /**
     * Gets the date this personal message log was made.
     * @return the date this personal message log was made.
     */
    public LocalDate getDateMade() {
        return dateMade;
    }

    /**
     * Gets all the messages.
     * @return a list with all the messages.
     */
    public List<Message> getMessages() {
        return messageList;
    }

    @Override
    public long getLastMessageNumber() {
        return lastMessageNumber;
    }

    @Override
    public void addMessage(Message message) throws CouldNotAddMessageException {
        checkIfObjectIsNull(message, "message");
        if (!checkIfMessageIsInMessageLog(message)){
            messageList.add(message);
            this.lastMessageNumber = message.getMessageNumber();
        }else {
            throw new CouldNotAddMessageException("The message is already in the message log.");
        }
    }

    @Override
    public void removeMessage(Message message) throws CouldNotRemoveMessageException {
        checkIfObjectIsNull(message, "message");
        if (checkIfMessageIsInMessageLog(message)){
            messageList.remove(message);
        }else {
            throw new CouldNotRemoveMessageException("The message is not in the message log");
        }
    }

    @Override
    public boolean checkIfAllMessagesAreNewMessages(List<Message> messageList) {
        return messageList.stream().anyMatch(this::checkIfMessageIsInMessageLog);
    }

    /**
     * Checks if the message is in the message log.
     * @param message the message to check.
     * @return <code>true</code> if the message is in the message log.
     *         <code>false</code>
     */
    private boolean checkIfMessageIsInMessageLog(Message message){
        return messageList.stream().anyMatch(mess -> {
            boolean valid = false;
            if (mess instanceof TextMessage textMessage && textMessage.checkIfMessageContentsAreEqual(message)){
                valid = true;
            }
            return valid;
        });
    }

}
