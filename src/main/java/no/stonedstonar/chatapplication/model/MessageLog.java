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

    private ArrayList<TextMessage> textMessageList;

    private MembersOfConversation membersOfConversation;

    private long messageLogNumber;

    //Todo: Vurder en liste som holder alle tingene som vil abonere på dette objektet. Dermed når en ny melding kommer oppdaterer den GUIen eller lignende med en gang.
    /**
      * Makes an instance of the MessageLog class.
      */
    public MessageLog(long messageLogNumber){
        checkIfLongIsNegative(messageLogNumber, "messagelog number");
        textMessageList = new ArrayList<>();
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
     * @param textMessage the message you want to add.
     */
    public void addMessage(TextMessage textMessage){
        checkIfObjectIsNull(textMessage, "message");
        if (!textMessageList.contains(textMessage)){
            textMessageList.add(textMessage);
        }else {
            throw new IllegalArgumentException("The message is already in the register.");
        }
    }

    /**
     * Removes the wanted message form the list.
     * @param textMessage the message you want to remove.
     */
    public void removeMessage(TextMessage textMessage){
        checkIfObjectIsNull(textMessage, "message");
        if (textMessageList.contains(textMessage)){
            textMessageList.remove(textMessage);
        }else {
            throw new IllegalArgumentException("Could not remove the message since its not in the register.");
        }
    }

    /**
     * Gets the list that has all the messages.
     * @return the list with all the messages.
     */
    public List<TextMessage> getMessageList(){
        return textMessageList;
    }

    //Todo: Lag en metode som heller ser på dato og tidspunkt siden samme meldingen kan bli sendt flere ganger.
    /**
     * Gets the message that matches the message contents.
     * @param messageContents the message you want to check for.
     * @return the message that matches the input message.
     */
    public TextMessage getMessage(String messageContents){
        checkString(messageContents, "message");
        Optional<TextMessage> optionalMessage = textMessageList.stream().filter(mess -> mess.getMessage().equals(messageContents)).findFirst();
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
     * Checks if a long is negative or equal to zero.
     * @param number the number to check.
     * @param prefix the prefix the error should have.
     */
    private void checkIfLongIsNegative(long number, String prefix){
        if (number <= 0){
            throw new IllegalArgumentException("Expected the " + prefix + " to be larger than zero.");
        }
    }
}
