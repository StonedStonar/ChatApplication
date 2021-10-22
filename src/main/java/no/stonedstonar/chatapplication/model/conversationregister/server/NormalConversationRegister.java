package no.stonedstonar.chatapplication.model.conversationregister.server;

import no.stonedstonar.chatapplication.backend.Server;
import no.stonedstonar.chatapplication.model.conversation.*;
import no.stonedstonar.chatapplication.model.conversationregister.personal.NormalPersonalConversationRegister;
import no.stonedstonar.chatapplication.model.exception.conversation.CouldNotAddConversationException;
import no.stonedstonar.chatapplication.model.exception.conversation.CouldNotGetConversationException;
import no.stonedstonar.chatapplication.model.exception.conversation.CouldNotRemoveConversationException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotAddMemberException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A class that holds messages from one user to other people.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class NormalConversationRegister implements ServerConversationRegister {

    private List<ServerConversation> conversationList;

    private long lastConversationNumber;

    /**
      * Makes an instance of the MessageRegister class.
      */
    public NormalConversationRegister(){
        conversationList = new ArrayList<>();
    }

    @Override
    public List<ServerConversation> getAllConversationsOfUsername(String username){
        checkString(username, "username");
        return conversationList.stream().filter(con -> con.getConversationMembers().checkIfUsernameIsMember(username)).toList();
    }

    @Override
    public NormalPersonalConversationRegister getAllConversationsUserHasAndMakePersonalRegister(String username){
        List<ServerConversation> conversations = getAllConversationsOfUsername(username);
        return new NormalPersonalConversationRegister(conversations, username);
    };

    @Override
    public ServerConversation addNewConversationWithUsernames(List<String> usernames, String nameOfConversation) throws CouldNotAddMemberException, CouldNotAddConversationException {
        checkIfObjectIsNull(usernames, "usernames");
        checkIfObjectIsNull(nameOfConversation, "name of conversation");
        if (!usernames.isEmpty()){
            makeNewConversationNumber();
            ServerConversation conversation = new NormalServerConversation(lastConversationNumber, usernames);
            if (!nameOfConversation.isEmpty()){
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
    private void makeNewConversationNumber(){
        lastConversationNumber += 1;
    }

    @Override
    public ServerConversation getConversationByNumber(long conversationNumber) throws CouldNotGetConversationException {
        checkIfLongIsAboveZero(conversationNumber, "conversation number");
        Optional<ServerConversation> optionalMessageLog = conversationList.stream().filter(log -> log.getConversationNumber() == conversationNumber).findFirst();
        if (optionalMessageLog.isPresent()){
            return optionalMessageLog.get();
        }else {
            throw new CouldNotGetConversationException("The conversation with the log number " + conversationNumber + " is not a part of this register.");
        }
    }

    /**
     * Adds a conversation to the register.
     * @param serverConversation the conversation to be added.
     * @throws CouldNotAddConversationException gets thrown if the conversation is already in the register.
     */
    private void addConversation(ServerConversation serverConversation) throws CouldNotAddConversationException {
        if (!checkIfConversationIsInRegister(serverConversation)){
            conversationList.add(serverConversation);
        }else {
            throw new CouldNotAddConversationException("The conversation is already in the register.");
        }
    }

    //Todo: Endre denne slik at samtalen blir slettet om den er tom forlenge.
    /**
     * Removes a conversation from the list.
     * @param conversation the conversation you want to remove.
     * @throws CouldNotRemoveConversationException gets thrown if the conversation could not be removed.
     */
    public void removeConversation(Conversation conversation) throws CouldNotRemoveConversationException {
        if (checkIfConversationIsInRegister(conversation)){
            conversationList.remove(conversation);
        }else {
            throw new CouldNotRemoveConversationException("The conversation is not in the system.");
        }
    }

    /**
     * Checks if the conversation matches one in the list.
     * @param conversation the conversation you want to check.
     * @return <code>true</code> if the conversation is in the register.
     *         <code>false</code> if the conversation is not in the register.
     */
    private boolean checkIfConversationIsInRegister(Conversation conversation){
        checkIfObjectIsNull(conversation, "conversation");
        return conversationList.stream().anyMatch(con -> con.equals(conversation));
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
