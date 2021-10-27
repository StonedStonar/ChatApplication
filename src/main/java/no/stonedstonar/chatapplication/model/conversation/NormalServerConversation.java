package no.stonedstonar.chatapplication.model.conversation;

import no.stonedstonar.chatapplication.model.membersregister.ConversationMembers;
import no.stonedstonar.chatapplication.model.exception.conversation.UsernameNotPartOfConversationException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotAddMemberException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotRemoveMemberException;
import no.stonedstonar.chatapplication.model.exception.message.CouldNotAddMessageException;
import no.stonedstonar.chatapplication.model.exception.message.CouldNotRemoveMessageException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotGetMessageLogException;
import no.stonedstonar.chatapplication.model.message.Message;
import no.stonedstonar.chatapplication.model.messagelog.MessageLog;
import no.stonedstonar.chatapplication.model.messagelog.NormalServerMessageLog;
import no.stonedstonar.chatapplication.model.messagelog.ServerMessageLog;

import java.time.LocalDate;
import java.util.*;

/**
 * Represents a normal conversation that uses objects and lists to store its contents.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class NormalServerConversation implements ServerConversation {

    private LocalDate conversationDateMade;

    private String conversationName;

    private List<ServerMessageLog> messageLogList;

    private long conversationNumber;

    private ConversationMembers conversationMembers;

    //Todo: Lag metoder så vi kan spørre etter de siste 100 meldingene eller lignende.
    // Kanskje heller lage metoder som ser på en dato.

    /**
      * Makes an instance of the conversation class.
      * @param conversationNumber the number this conversation is.
      * @throws CouldNotAddMemberException gets thrown if the same username is written twice in the list.
      */
    public NormalServerConversation(long conversationNumber, List<String> usernames) throws CouldNotAddMemberException {
        checkIfLongIsNegative(conversationNumber, "conversation number");
        checkIfListIsValid(usernames, "usernames");
        conversationDateMade = LocalDate.now();
        messageLogList = new ArrayList<>();
        conversationMembers = new ConversationMembers();
        conversationMembers.addAllMembers(usernames);
        this.conversationNumber = conversationNumber;
        conversationName = "";
    }

    /**
     * Makes an instance of the conversation class.
     * @param conversationNumber the conversation number this class is going to have.
     * @param conversationMembers the members of the class.
     * @param dateMade the date this class was made.
     * @param conversationName the name of the conversation.
     */
    protected NormalServerConversation(long conversationNumber, ConversationMembers conversationMembers, LocalDate dateMade, String conversationName){
        checkIfLongIsNegative(conversationNumber, "conversation number");
        checkIfObjectIsNull(conversationMembers, "members");
        checkIfDateIsValid(dateMade);
        checkString(conversationName, "conversation name");
        this.conversationName = conversationName;
        this.conversationNumber = conversationNumber;
        this.conversationMembers = conversationMembers;
        this.conversationDateMade = dateMade;
    }

    /**
     * Makes an instance of the conversation class.
     * @param conversationName the name of the conversation.
     * @param conversationNumber the number this conversation is.
     */
    public NormalServerConversation(String conversationName, long conversationNumber, ConversationMembers conversationMembers) {
        checkString(conversationName, "conversation name");
        checkIfLongIsNegative(conversationNumber, "conversation number");
        checkIfObjectIsNull(conversationMembers, "members");
        this.conversationName = conversationName;
        this.conversationNumber = conversationNumber;
        this.conversationMembers = conversationMembers;
    }

    /**
     * Checks if the username is a part of this conversation.
     * @param username the username of the member.
     * @throws UsernameNotPartOfConversationException gets thrown if the user is not a part of this conversation.
     */
    private void checkIfUsernameIsMemberAndThrowExceptionIfNot(String username) throws UsernameNotPartOfConversationException {
        if (!checkIfUsernameIsMember(username)){
            throw new UsernameNotPartOfConversationException("The user by the useranme " + username + " is not a part of this conversation.");
        }
    }

    @Override
    public boolean checkIfUsernameIsMember(String username) {
        return conversationMembers.checkIfUsernameIsMember(username);
    }

    @Override
    public String getConversationName() {
        return conversationName;
    }

    @Override
    public ConversationMembers getConversationMembers() {
        return conversationMembers;
    }

    @Override
    public LocalDate getDateMade() {
        return conversationDateMade;
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
    public ServerMessageLog getMessageLogForDate(LocalDate localDate, String username) throws CouldNotGetMessageLogException, UsernameNotPartOfConversationException {
        checkString(username, "username");
        checkIfUsernameIsMemberAndThrowExceptionIfNot(username);
        if (checkForMessageLogByDate(localDate)){
            return getMessageLogByTheDate(localDate);
        }else {
            ServerMessageLog messageLog = new NormalServerMessageLog(localDate);
            addNewMessageLog(messageLog);
            return messageLog;
        }
    }

    @Override
    public List<ServerMessageLog> getMessageLogs(String username) {
        checkString(username, "user");
        if (checkIfUsernameIsMember(username)){
            return messageLogList;
        }else {
            throw new IllegalArgumentException("The user with the username " + username + " is not a part of this conversation.");
        }
    }

    @Override
    public boolean checkForMessageLogByDate(LocalDate localDate) {
        checkIfDateIsValid(localDate);
        return messageLogList.stream().anyMatch(log -> log.getDateMade().isEqual(localDate));
    }

    @Override
    public void addNewMessage(Message message) throws CouldNotGetMessageLogException, CouldNotAddMessageException, UsernameNotPartOfConversationException {
        checkIfObjectIsNull(message, "message");
        checkIfDateIsValid(message.getDate());
        checkIfUsernameIsMemberAndThrowExceptionIfNot(message.getFromUsername());
        ServerMessageLog messageLog = getMessageLogForDate(message.getDate(), message.getFromUsername());
        addMessageToMessageLog(message, messageLog);
    }

    @Override
    public void removeMessage(Message message) throws CouldNotGetMessageLogException, CouldNotRemoveMessageException, UsernameNotPartOfConversationException {
        checkIfObjectIsNull(message, "message");
        checkIfDateIsValid(message.getDate());
        checkIfUsernameIsMemberAndThrowExceptionIfNot(message.getFromUsername());
        MessageLog messageLog = getMessageLogByTheDate(message.getDate());
        messageLog.removeMessage(message);
    }

    /**
     * Adds a new message to the message log.
     * @param message the new message.
     * @param messageLog the message log this message is going into.
     * @throws CouldNotAddMessageException gets thrown if the message could not be added.
     */
    private void addMessageToMessageLog(Message message, ServerMessageLog messageLog) throws CouldNotAddMessageException {
        messageLog.addMessage(message);
    }

    @Override
    public void addAllMessagesWithSameDate(List<Message> newMessageList) throws CouldNotAddMessageException, CouldNotGetMessageLogException {
        checkIfListIsValid(newMessageList, "new message list");
        LocalDate testDateFromOneMessage = newMessageList.get(0).getDate();
        boolean validDate = newMessageList.stream().allMatch(message -> message.getDate().isEqual(testDateFromOneMessage));
        if (validDate){
            ServerMessageLog messageLog = getMessageLogByTheDate(testDateFromOneMessage);
            if (!messageLog.checkIfAllMessagesAreNewMessages(newMessageList)){
                Iterator<Message> it = newMessageList.iterator();
                while (it.hasNext()){
                    Message message = it.next();
                    addMessageToMessageLog(message, messageLog);
                }
            }else {
                throw new CouldNotAddMessageException("One of the messages in the list is already in the message log. " + messageLog.getDateMade() + " and the conversation is "  + conversationNumber);
            }
        }else {
            throw new CouldNotAddMessageException("The dates all of the messages are not the same.");
        }
    }

    @Override
    public void addMember(String username) throws CouldNotAddMemberException {
        conversationMembers.addMember(username);
    }

    @Override
    public void removeMember(String username) throws CouldNotRemoveMemberException {
        conversationMembers.removeMember(username);
    }

    @Override
    public void addAllMembers(List<String> usernames) throws CouldNotAddMemberException {
        conversationMembers.addAllMembers(usernames);

    }

    @Override
    public List<Message> checkForNewMessagesOnDate(LocalDate localDate, long lastMessage, String username) throws CouldNotGetMessageLogException, UsernameNotPartOfConversationException {
        checkIfDateIsValid(localDate);
        checkIfLongIsNegative(lastMessage, "last message");
        try {
            ServerMessageLog messageLog;
            if (localDate.isEqual(LocalDate.now())){
                messageLog = getMessageLogForDate(localDate, username);
            }else{
                messageLog = getMessageLogByTheDate(localDate);
            }
            return messageLog.checkForNewMessages(lastMessage);
        }catch (CouldNotGetMessageLogException exception){
            if ((lastMessage == 0) && localDate.isEqual(LocalDate.now())){
                ServerMessageLog messageLog = getMessageLogForDate(localDate, username);
                return messageLog.checkForNewMessages(lastMessage);
            }else {
                throw new CouldNotGetMessageLogException("There is no message log for the date " + localDate.toString() + " and the last message is not zero.");
            }
        }
    }

    /**
     * Adds a new message log to the conversation.
     * @param messageLog the new message log to be added.
     */
    private void addNewMessageLog(ServerMessageLog messageLog){
        messageLogList.add(messageLog);
    }

    /**
     * Gets the message log that matches that date.
     * @param localDate the local date the message log was made.
     * @throws CouldNotGetMessageLogException gets thrown if there is no message log that matches the date.
     * @return the message log that matches that date.
     */
    private ServerMessageLog getMessageLogByTheDate(LocalDate localDate) throws CouldNotGetMessageLogException {
        checkIfObjectIsNull(localDate, "local date");
        Optional<ServerMessageLog> optionalMessageLog = messageLogList.stream().filter(log -> log.getDateMade().isEqual(localDate)).findFirst();
        if (optionalMessageLog.isPresent()){
            return optionalMessageLog.get();
        }else {
            throw new CouldNotGetMessageLogException("The message log by the local date " + localDate.toString() + " is not in this conversation.");
        }
    }

    /**
     * Gets the message log list.
     * @return a list with all the message logs for each day.
     */
    public List<ServerMessageLog> getMessageLogList(){
        return messageLogList;
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

    /**
     * Checks if a long is negative or equal to zero.
     * @param number the number to check.
     * @param prefix the prefix the error should have.
     */
    private void checkIfLongIsNegative(long number, String prefix){
        if (number < 0){
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
