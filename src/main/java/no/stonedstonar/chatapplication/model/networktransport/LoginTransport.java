package no.stonedstonar.chatapplication.model.networktransport;

import no.stonedstonar.chatapplication.model.MessageLog;
import no.stonedstonar.chatapplication.model.User;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class LoginTransport implements Serializable {

    private User user;

    private List<MessageLog> messageLogList;

    /**
      * Makes an instance of the LoginTransport class.
      */
    public LoginTransport(User user, List<MessageLog> messageLogList){
        checkIfObjectIsNull(user, "user");
        checkIfObjectIsNull(messageLogList, "message log list.");
        this.user = user;
        this.messageLogList = messageLogList;
    }

    /**
     * Gets the list that contains all the messages of the user.
     * @return a list that holds all the conversations of this user.
     */
    public List<MessageLog> getMessageLogList() {
        return messageLogList;
    }

    /**
     * Gets the user this transport holds.
     * @return the user this object is transporting.
     */
    public User getUser() {
        return user;
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
