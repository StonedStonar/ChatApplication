package no.stonedstonar.chatapplication.networktransport;

import no.stonedstonar.chatapplication.model.user.BasicEndUser;
import no.stonedstonar.chatapplication.model.conversationregister.personal.NormalPersonalConversationRegister;
import no.stonedstonar.chatapplication.model.user.EndUser;

import java.io.Serializable;

/**
 * Transports the details the user needs to login.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class LoginTransport implements Serializable {


    private EndUser endUser;

    private NormalPersonalConversationRegister personalConversationRegister;

    /**
      * Makes an instance of the LoginTransport class.
      */
    public LoginTransport(EndUser endUser, NormalPersonalConversationRegister personalConversationRegister){
        checkIfObjectIsNull(endUser, "end user");
        checkIfObjectIsNull(personalConversationRegister, "message log list");
        this.endUser = endUser;
        this.personalConversationRegister = personalConversationRegister;
    }

    /**
     * Gets the user from the transport.
     * @return the user.
     */
    public EndUser getUser() {
        return endUser;
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
