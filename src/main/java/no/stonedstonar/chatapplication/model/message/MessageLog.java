package no.stonedstonar.chatapplication.model.message;

import no.stonedstonar.chatapplication.model.MembersRegister;
import no.stonedstonar.chatapplication.model.exception.textmessage.CouldNotAddTextMessageException;
import no.stonedstonar.chatapplication.model.exception.textmessage.CouldNotGetTextMessageException;
import no.stonedstonar.chatapplication.model.exception.textmessage.CouldNotRemoveTextMessageException;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a class that holds messages.
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class MessageLog implements Serializable {

    private ArrayList<TextMessage> textMessageList;

    private MembersRegister membersRegister;

    private final long messageLogNumber;

    private String nameOfMessageLog;

    private long lastMessageNumber;

    /**
      * Makes an instance of the MessageLog class.
     * @param messageLogNumber the number this message log should have.
      */
    public MessageLog(long messageLogNumber){
        checkIfLongIsNegative(messageLogNumber, "messagelog number");
        textMessageList = new ArrayList<>();
        membersRegister = new MembersRegister();
        this.messageLogNumber = messageLogNumber;
        lastMessageNumber = 0;
        nameOfMessageLog = "";
    }

    /**
     * Sets the name of the message log.
     * @param nameOfMessageLog the name the message log should have.
     */
    public void setNameOfMessageLog(String nameOfMessageLog) {
        checkIfObjectIsNull(nameOfMessageLog, "name of message log");
        this.nameOfMessageLog = nameOfMessageLog;
    }

    /**
     * Gets the name of the message log.
     * @return the name of the message log. Its empty if the message log does not have a name.
     */
    public String getNameOfMessageLog() {
        return nameOfMessageLog;
    }

    /**
     * Sets the members register object to be another.
     * @param membersRegister the members register you want it to be.
     */
    protected void setMembersRegister(MembersRegister membersRegister){
        checkIfObjectIsNull(membersRegister, "members register");
        this.membersRegister = membersRegister;
    }


    /**
     * Gets the message log number.
     * @return the number that this message log is assigned.
     */
    public long getMessageLogNumber() {
        return messageLogNumber;
    }

    /**
     * Gets the members object.
     * @return the object that holds all the members of this conversation.
     */
    public MembersRegister getMembersOfConversation(){
        return membersRegister;
    }

    /**
     * Adds a message to the list.
     * @param textMessage the message you want to add.
     * @throws CouldNotAddTextMessageException gets thrown if the text message is already in the message log or the message already has a message number.
     */
    public void addMessage(TextMessage textMessage) throws CouldNotAddTextMessageException {
        checkIfObjectIsNull(textMessage, "message");
        if (!checkIfMessageIsInMessageLog(textMessage)){
            lastMessageNumber += 1;
            try {
                textMessage.setMessageNumber(lastMessageNumber);
            }catch (IllegalArgumentException exception){
                throw new CouldNotAddTextMessageException("The message has already gotten a message number and cannot be added.");
            }
            textMessageList.add(textMessage);
        }else {
            throw new CouldNotAddTextMessageException("The message " + textMessage + " is already in the system.");
        }
    }

    /**
     * Checks if the message is in the message log already.
     * @param textMessage the text message you want to check.
     * @return <code>true</code> if the message matches a message on time, date, contents and from username.
     *         <code>false</code> if the message does not match any messages in the log.
     */
    private boolean checkIfMessageIsInMessageLog(TextMessage textMessage){
        return textMessageList.stream().anyMatch(message -> message.getTime().equals(textMessage.getTime()) && message.getDate().equals(textMessage.getDate()) && message.getMessage().equals(textMessage.getMessage()) && message.getFromUsername().equals(textMessage.getFromUsername()));
    }

    /**
     * Adds all the input messages to the message log.
     * @param textMessages the list with the text messages.
     * @throws CouldNotAddTextMessageException gets thrown if one text message is already in the list.
     */
    public void addAllMessages(List<TextMessage> textMessages) throws CouldNotAddTextMessageException {
        checkIfListIsValid(textMessages, "text message");
        if (!checkIfAllMessagesAreNewMessages(textMessages)){
            textMessageList.addAll(textMessages);
        }else {
            throw new CouldNotAddTextMessageException("Could not add the new messages since one of them are already in the register.");
        }
    }

    /**
     * Gets the newest messages from the message log.
     * @param sizeOfList the size of the other message log.
     * @return a list with the new text messages.
     */
    public List<TextMessage> checkForNewMessages(int sizeOfList){
        List<TextMessage> newTextMessages = new ArrayList<>();
        if (sizeOfList < 0){
            checkIfLongIsNegative(sizeOfList, "size of list");
        }
        if (textMessageList.size() > sizeOfList){
            int sizeOfTextMessages = textMessageList.size();
            long counter = sizeOfList;
            while (counter < sizeOfTextMessages){
                newTextMessages.add(textMessageList.get((int) counter));
                counter += 1;
            }
        }
        return newTextMessages;
    }

    /**
     * Checks if all the messages in the list is new messages to this message log.
     * @param textMessages the list of text messages you want to check.
     * @return <code>true</code> if one of the input messages matches one in the list.
     *         <code>false</code> if none of the input messages are in the message log.
     */
    protected boolean checkIfAllMessagesAreNewMessages(List<TextMessage> textMessages){
        return textMessages.stream().anyMatch(this::checkIfMessageIsInMessageLog);
    }

    /**
     * Removes the wanted message form the list.
     * @param textMessage the message you want to remove.
     * @throws CouldNotRemoveTextMessageException gets thrown if the text message could not be removed.
     */
    public void removeMessage(TextMessage textMessage) throws CouldNotRemoveTextMessageException {
        checkIfObjectIsNull(textMessage, "message");
        if (textMessageList.contains(textMessage)){
            textMessageList.remove(textMessage);
        }else {
            throw new CouldNotRemoveTextMessageException("Could not remove the message since its not in the register.");
        }
    }

    /**
     * Gets the list that has all the messages.
     * @return the list with all the messages.
     */
    public List<TextMessage> getMessageList(){
        return textMessageList;
    }

    /**
     * Gets the message that matches the message contents.
     * @param fromUsername the username this message is from.
     * @param localDate the local date object this message has.
     * @param localTime the local time object this message has.
     * @return the message that matches the input message.
     * @throws CouldNotGetTextMessageException gets thrown when a message could not be found.
     */
    public TextMessage getMessage(String fromUsername, LocalTime localTime, LocalDate localDate) throws CouldNotGetTextMessageException {
        checkString(fromUsername, "from username");
        checkIfObjectIsNull(localDate, "local date");
        checkIfObjectIsNull(localTime, "local time");
        Optional<TextMessage> optionalMessage = textMessageList.stream().filter(mess -> mess.getTime().equals(localTime)).filter(message -> message.getDate().equals(localDate)).filter(message -> message.getFromUsername().equals(fromUsername)).findFirst();
        if (optionalMessage.isPresent()){
            return optionalMessage.get();
        }else {
            throw new CouldNotGetTextMessageException("The message that has the date and time \"" + localDate + " " + localTime+ "\" is not a part of this messages.");
        }
    }
    
    /**
     * Checks if a string is of a valid format or not.
     * @param stringToCheck the string you want to check.
     * @param errorPrefix the error the exception should have if the string is invalid.
     */
    protected void checkString(String stringToCheck, String errorPrefix){
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
    protected void checkIfObjectIsNull(Object object, String error){
       if (object == null){
           throw new IllegalArgumentException("The " + error + " cannot be null.");
       }
    }

    /**
     * Checks if a long is negative or equal to zero.
     * @param number the number to check.
     * @param prefix the prefix the error should have.
     */
    protected void checkIfLongIsNegative(long number, String prefix){
        if (number <= 0){
            throw new IllegalArgumentException("Expected the " + prefix + " to be larger than zero.");
        }

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

}
