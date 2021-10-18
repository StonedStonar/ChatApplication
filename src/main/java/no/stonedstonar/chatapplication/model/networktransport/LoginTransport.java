package no.stonedstonar.chatapplication.model.networktransport;

import no.stonedstonar.chatapplication.model.conversation.Conversation;
import no.stonedstonar.chatapplication.model.User;

import java.io.Serializable;
import java.util.List;

/**
 * Transports the details the user needs to login.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class LoginTransport implements Serializable {


    private User user;

    private List<Conversation> conversations;

    /**
      * Makes an instance of the LoginTransport class.
      */
    public LoginTransport(User user, List<Conversation> conversations){
        checkIfObjectIsNull(user, "user");
        checkIfObjectIsNull(conversations, "message log list");
        this.user = user;
        this.conversations = conversations;
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
    public List<Conversation> getMessageLogList(){
        return conversations;
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
