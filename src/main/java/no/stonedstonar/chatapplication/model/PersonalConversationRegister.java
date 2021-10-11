package no.stonedstonar.chatapplication.model;
/**
 *
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class PersonalConversationRegister extends ConversationRegister {

    //Todo: Make this observable så jeg kan få gjort ferdig dritt.
    /**
      * Makes an instance of the PersonalConversationRegister class.
      */
    public PersonalConversationRegister(){
        super();
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
