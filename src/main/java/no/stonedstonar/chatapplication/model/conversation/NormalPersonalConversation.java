package no.stonedstonar.chatapplication.model.conversation;

import no.stonedstonar.chatapplication.model.Members;
import no.stonedstonar.chatapplication.model.User;
import no.stonedstonar.chatapplication.model.exception.message.CouldNotAddMessageException;
import no.stonedstonar.chatapplication.model.exception.message.CouldNotRemoveMessageException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotGetMessageLogException;
import no.stonedstonar.chatapplication.model.message.Message;
import no.stonedstonar.chatapplication.model.messagelog.MessageLog;
import no.stonedstonar.chatapplication.model.messagelog.PersonalMessageLog;
import no.stonedstonar.chatapplication.model.messagelog.ServerMessageLog;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a personal conversation that is on the client side.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class NormalPersonalConversation implements PersonalConversation, Serializable {

    private LocalDate dateMade;

    private String conversationName;

    private List<PersonalMessageLog> personalMessageLogs;

    private long conversationNumber;

    private Members members;

    private final List<ConversationObserver> conversationObservers;

    private volatile Message newlyAddedMessage;

    private volatile boolean removed;


    //Todo: Vurder å ikke ta imot hele samtalen men at dette objektet må spørre etter deler av den.
    // Finish this tomorrow and start on the testing part.
    /**
     * Makes an instance of the PersonalNormalConversation class.
     * @param serverConversation the conversation this personal conversation is going to imitate.
     * @param username the username of the person who is a part of this conversation.
     */
    public NormalPersonalConversation(ServerConversation serverConversation, String username) {
        checkIfObjectIsNull(serverConversation, "conversation");
        conversationObservers = new ArrayList<>();
        this.conversationName = serverConversation.getConversationName();
        this.conversationNumber = serverConversation.getConversationNumber();
        this.dateMade = serverConversation.getDateMade();
        this.personalMessageLogs = new ArrayList<>();
        this.members = serverConversation.getConversationMembers();
        List<ServerMessageLog> messageLogList = serverConversation.getMessageLogs(username);
        messageLogList.forEach(messageLog -> {
            PersonalMessageLog personalMessageLog = new PersonalMessageLog(messageLog);
            personalMessageLogs.add(personalMessageLog);
        });
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

    @Override
    public String getConversationName() {
        return conversationName;
    }

    @Override
    public Members getConversationMembers() {
        return members;
    }

    @Override
    public LocalDate getDateMade() {
        return dateMade;
    }

    @Override
    public long getConversationNumber() {
        return conversationNumber;
    }

    @Override
    public void setConversationName(String conversationName) {
        checkString(conversationName, "conversation name");
        this.conversationName = conversationName;
    }

    @Override
    public boolean checkForMessageLogByDate(LocalDate localDate) {
        return personalMessageLogs.stream().anyMatch(log -> log.getDateMade().isEqual(localDate));
    }

    @Override
    public void addNewMessage(Message message) throws CouldNotGetMessageLogException, CouldNotAddMessageException {
        checkIfObjectIsNull(message, "message");
        checkIfDateIsValid(message.getDate());
        PersonalMessageLog personalMessageLog = getMessageLogForDate(message.getDate());
        personalMessageLog.addMessage(message);
        this.newlyAddedMessage = message;
        this.removed = false;
        notifyObservers();
    }

    @Override
    public void removeMessage(Message message) throws CouldNotRemoveMessageException, CouldNotGetMessageLogException {
        checkIfObjectIsNull(message, "message");
        checkIfDateIsValid(message.getDate());
        PersonalMessageLog personalMessageLog = getMessageLogForDate(message.getDate());
        personalMessageLog.removeMessage(message);
        this.newlyAddedMessage = message;
        this.removed = true;
        notifyObservers();
    }

    /**
     * Adds a new message to the message log.
     * @param message the new message.
     * @param messageLog the message log this message is going into.
     * @throws CouldNotAddMessageException gets thrown if the message could not be added.
     */
    private void addMessageToMessageLog(Message message, MessageLog messageLog) throws CouldNotAddMessageException {
        messageLog.addMessage(message);
    }

    @Override
    public void addAllMessagesWithSameDate(List<Message> newMessageList) throws CouldNotAddMessageException, CouldNotGetMessageLogException {
        checkIfListIsValid(newMessageList, "new message list");
        LocalDate testDateFromOneMessage = newMessageList.get(0).getDate();
        newMessageList.forEach(message -> checkIfDateIsValid(message.getDate()));
        boolean validDate = newMessageList.stream().allMatch(message -> message.getDate().isEqual(testDateFromOneMessage));
        if (validDate){
            PersonalMessageLog messageLog = getMessageLogByTheDate(testDateFromOneMessage);
            if (messageLog.checkIfAllMessagesAreNewMessages(newMessageList)){
                for (Message message : newMessageList) {
                    addMessageToMessageLog(message, messageLog);
                    this.newlyAddedMessage = message;
                    this.removed = false;
                    notifyObservers();
                }
            }else {
                throw new CouldNotAddMessageException("One of the messages in the list is already in the message log. " + messageLog.getDateMade() + " and the conversation is "  + conversationNumber);
            }
        }else {
            throw new CouldNotAddMessageException("The dates all of the messages are not the same.");
        }
    }

    /**
     * Gets the message log that matches that date.
     * @param localDate the local date the message log was made.
     * @throws CouldNotGetMessageLogException gets thrown if there is no message log that matches the date.
     * @return the message log that matches that date.
     */
    private PersonalMessageLog getMessageLogByTheDate(LocalDate localDate) throws CouldNotGetMessageLogException {
        checkIfObjectIsNull(localDate, "local date");
        Optional<PersonalMessageLog> optionalMessageLog = personalMessageLogs.stream().filter(log -> log.getDateMade().isEqual(localDate)).findFirst();
        if (optionalMessageLog.isPresent()){
            return optionalMessageLog.get();
        }else {
            throw new CouldNotGetMessageLogException("The message log by the local date " + localDate.toString() + " is not in this conversation.");
        }
    }

    @Override
    public PersonalMessageLog getMessageLogForDate(LocalDate localDate) throws CouldNotGetMessageLogException {
        return null;
    }

    @Override
    public List<Message> getAllMessagesOfConversationAsList() {
        List<Message> messages = new ArrayList<>();
        personalMessageLogs.forEach(messageLog -> messages.addAll(messageLog.getMessages()));
        return messages;
    }

    /**
     * Checks if the date is valid. That it is before current date or the current date.
     * @param localDate the local date to be checked.
     */
    private void checkIfDateIsValid(LocalDate localDate){
        checkIfObjectIsNull(localDate, "local date");
        if (localDate.isAfter(LocalDate.now())){
            throw new IllegalArgumentException("The input date must be before or at this current date.");
        }
    }
}
