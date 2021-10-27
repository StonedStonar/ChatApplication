package no.stonedstonar.chatapplication.networktransport;

import no.stonedstonar.chatapplication.model.conversation.ObservableConversation;

import java.io.Serializable;
import java.util.List;

/**
 * Represents transport that can be used to send personal conversations.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class PersonalConversationTransport implements Serializable {

    List<ObservableConversation> observableConversationList;

    /**
      * Makes an instance of the PersonalConversationTransport class.
     * @param observableConversations the list with all the conversation.
      */
    public PersonalConversationTransport(List<ObservableConversation> observableConversations){
        checkIfObjectIsNull(observableConversations, "the list with all the personal conversations");
        observableConversationList = observableConversations;
    }

    /**
     * Gets the personal conversation list this object transports.
     * @return the personal conversation list.
     */
    public List<ObservableConversation> getPersonalConversationList() {
        return observableConversationList;
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
