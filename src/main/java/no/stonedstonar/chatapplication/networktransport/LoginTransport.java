package no.stonedstonar.chatapplication.networktransport;

import no.stonedstonar.chatapplication.model.User;
import no.stonedstonar.chatapplication.model.conversationregister.personal.NormalPersonalConversationRegister;

import java.io.Serializable;

/**
 * Transports the details the user needs to login.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class LoginTransport implements Serializable {


    private User user;

    private NormalPersonalConversationRegister personalConversationRegister;

    /**
      * Makes an instance of the LoginTransport class.
      */
    public LoginTransport(User user, NormalPersonalConversationRegister personalConversationRegister){
        checkIfObjectIsNull(user, "user");
        checkIfObjectIsNull(personalConversationRegister, "message log list");
        this.user = user;
        this.personalConversationRegister = personalConversationRegister;
    }

    /**
     * Gets the user from the transport.
     * @return the user.
     */
    public User getUser() {
        return user;
    }

    /**
     * Gets the personal conversation register.
     * @return the personal conversation register.
     */
    public NormalPersonalConversationRegister getPersonalConversationRegister(){
        return personalConversationRegister;
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
