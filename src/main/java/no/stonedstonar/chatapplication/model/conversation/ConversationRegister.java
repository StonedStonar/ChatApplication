package no.stonedstonar.chatapplication.model.conversation;

import no.stonedstonar.chatapplication.model.exception.conversation.CouldNotAddConversationException;
import no.stonedstonar.chatapplication.model.exception.conversation.CouldNotGetConversationException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotAddMemberException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotAddMessageLogException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotGetMessageLogException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotRemoveConversationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A class that holds messages from one user to other people.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class ConversationRegister {

    private List<Conversation> conversationList;

    private long lastConversationNumber;

    /**
      * Makes an instance of the MessageRegister class.
      */
    public ConversationRegister(){
        conversationList = new ArrayList<>();
    }

    /**
     * Gets the conversations the username is a part of.
     * @param username the username that wants its conversations.
     * @return the conversations that belongs to this username in a list.
     */
    public List<Conversation> getAllConversationsOfUsername(String username){
        checkString(username, "username");
        return conversationList.stream().filter(conversation -> {
            return conversation.getConversationMembers().checkIfUsernameIsMember(username);
        }).toList();
    }

    /**
     * Adds a new conversation based on a list of names that are in it.
     * @param usernames list with all the usernames that wants to be in the conversation.
     * @param nameOfConversation the name the conversation should have.
     * @return the conversation that was just added to the register.
     * @throws CouldNotAddMessageLogException gets thrown if the conversation is already in the register.
     * @throws CouldNotAddMemberException gets thrown if a member could not be added.
     */
    public Conversation addNewConversationWithUsernames(List<String> usernames, String nameOfConversation) throws CouldNotAddMemberException, CouldNotAddConversationException {
        checkIfObjectIsNull(usernames, "usernames");
        checkIfObjectIsNull(nameOfConversation, "name of messagelog");
        if (!usernames.isEmpty()){
            Conversation conversation = new NormalConversation(makeNewMessageLogNumber(), usernames);
            conversation.getConversationMembers().addAllMembers(usernames);
            if (!nameOfConversation.isEmpty()){
                System.out.println(nameOfConversation);
                conversation.setConversationName(nameOfConversation);
            }
            addConversation(conversation);
            return conversation;
        }else {
            throw new IllegalArgumentException("The size of the usernames must be larger than 0.");
        }
    }

    /**
     * Makes a new log number for each log that is in the list.
     * @return the number that the new message log can have.
     */
    private long makeNewMessageLogNumber(){
        lastConversationNumber = lastConversationNumber + 1;
        return lastConversationNumber;
    }

    /**
     * Gets the conversation that matches the conversation number.
     * @param messageLogNumber the number that the conversation has.
     * @return the conversation that matches this number.
     * @throws CouldNotGetMessageLogException gets thrown if the conversation could not be found.
     */
    public Conversation getConversationByNumber(long messageLogNumber) throws CouldNotGetConversationException {
        checkIfLongIsAboveZero(messageLogNumber, "conversation number");
        Optional<Conversation> optionalMessageLog = conversationList.stream().filter(log -> log.getConversationNumber() == messageLogNumber).findFirst();
        if (optionalMessageLog.isPresent()){
            return optionalMessageLog.get();
        }else {
            throw new CouldNotGetConversationException("The conversation with the log number " + messageLogNumber + " is not a part of this register.");
        }
    }

    /**
     * Adds a conversation to the register.
     * @param conversation the conversation to be added.
     * @throws CouldNotAddConversationException gets thrown if the conversation is already in the register.
     */
    private void addConversation(Conversation conversation) throws CouldNotAddConversationException {
        if (!checkIfMessageLogIsInList(conversation)){
            conversationList.add(conversation);
        }else {
            throw new CouldNotAddConversationException("The conversation is already in the register.");
        }
    }

    /**
     * Gets all the conversations that are in this conversation.
     * @return a list with all the conversations.
     */
    protected List<Conversation> getConversationList(){
        return conversationList;
    }

    /**
     * Removes a conversation from the list.
     * @param conversation the conversation you want to remove.
     * @throws CouldNotRemoveConversationException gets thrown if the conversation could not be removed.
     */
    public void removeMessageLog(Conversation conversation) throws CouldNotRemoveConversationException {
        if (checkIfMessageLogIsInList(conversation)){
            conversationList.remove(conversation);
        }else {
            throw new CouldNotRemoveConversationException("The conversation is not in the system.");
        }
    }

    /**
     * Checks if the conversation is in the system.
     * @param conversation the conversation you want to check.
     * @return <code>true</code> if the conversation is in the register.
     *         <code>false</code> if the conversation is not in the register.
     */
    private boolean checkIfMessageLogIsInList(Conversation conversation){
        checkIfObjectIsNull(conversation, "conversation");
        return conversationList.contains(conversation);
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

    /**
     * Checks if a long is above 0.
     * @param number the number you want to check.
     * @param prefix the prefix the error should have.
     */
    private void checkIfLongIsAboveZero(long number, String prefix){
        if (number <= 0){
            throw new IllegalArgumentException("The " + prefix + " must be above 0.");
        }
    }
}
