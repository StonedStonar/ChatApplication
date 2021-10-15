package no.stonedstonar.chatapplication.model.networktransport;

import no.stonedstonar.chatapplication.model.message.MessageLog;
import no.stonedstonar.chatapplication.model.User;

import java.io.Serializable;
import java.util.List;

/**
 * Transports the details the user needs to login.
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
        checkIfObjectIsNull(messageLogList, "message log list");
        this.user = user;
        this.messageLogList = messageLogList;
    }

    /**
     * Gets the user from the transport.
     * @return the user.
     */
    public User getUser() {
        return user;
    }

    /**
     * Gets the list with all the message logs.
     * @return list with all the message logs.
     */
    public List<MessageLog> getMessageLogList(){
        return messageLogList;
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
