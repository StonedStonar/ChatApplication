package no.stonedstonar.chatapplication.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a class that holds messages.
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class MessageLog implements Serializable {

    private ArrayList<Message> messageList;

    private MembersOfConversation membersOfConversation;

    private long messageLogNumber;

    //Todo: Vurder en liste som holder alle tingene som vil abonere på dette objektet. Dermed når en ny melding kommer oppdaterer den GUIen eller lignende med en gang.
    /**
      * Makes an instance of the MessageLog class.
      */
    public MessageLog(long messageLogNumber){
        messageList = new ArrayList<>();
        membersOfConversation = new MembersOfConversation();
        this.messageLogNumber = messageLogNumber;
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
    public MembersOfConversation getMembersOfConversation(){
        return membersOfConversation;
    }

    /**
     * Adds a message to the list.
     * @param message the message you want to add.
     */
    public void addMessage(Message message){
        checkIfObjectIsNull(message, "message");
        if (!messageList.contains(message)){
            messageList.add(message);
        }else {
            throw new IllegalArgumentException("The message is already in the register.");
        }
    }

    /**
     * Removes the wanted message form the list.
     * @param message the message you want to remove.
     */
    public void removeMessage(Message message){
        checkIfObjectIsNull(message, "message");
        if (messageList.contains(message)){
            messageList.remove(message);
        }else {
            throw new IllegalArgumentException("Could not remove the message since its not in the register.");
        }
    }

    /**
     * Gets the list that has all the messages.
     * @return the list with all the messages.
     */
    public List<Message> getMessageList(){
        return messageList;
    }

    //Todo: Lag en metode som heller ser på dato og tidspunkt siden samme meldingen kan bli sendt flere ganger.
    /**
     * Gets the message that matches the message contents.
     * @param messageContents the message you want to check for.
     * @return the message that matches the input message.
     */
    public Message getMessage(String messageContents){
        checkString(messageContents, "message");
        Optional<Message> optionalMessage = messageList.stream().filter(mess -> mess.getMessage().equals(messageContents)).findFirst();
        if (optionalMessage.isPresent()){
            return optionalMessage.get();
        }else {
            throw new IllegalArgumentException("The message with the contents \"" + messageContents + "\" is not a part of this messagelist.");
        }
    }
    
    /**
     * Checks if a string is of a valid format or not.
     * @param stringToCheck the string you want to check.
     * @param errorPrefix the error the exception should have if the string is invalid.
     */
    public void checkString(String stringToCheck, String errorPrefix){
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
    public void checkIfObjectIsNull(Object object, String error){
       if (object == null){
           throw new IllegalArgumentException("The " + error + " cannot be null.");
       }
    }
}
