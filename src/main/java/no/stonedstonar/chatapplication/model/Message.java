package no.stonedstonar.chatapplication.model;
/**
 *
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class Message {

    private String message;

    private String fromUsername;

    private String toUsername;


    /**
      * Makes an instance of the Message class.
     * @param message the message this object should contain.
     * @param fromUsername the username this message was sent form.
     * @param toUsername the username this message was meant for.
      */
    public Message(String message, String fromUsername, String toUsername){
        checkString(message, "Message");
        checkString(fromUsername, "From username");
        checkString(toUsername, "To username");
        this.message = message;
        this.fromUsername = fromUsername;
        this.toUsername = toUsername;
    }

    /**
     * Gets the message.
     * @return the message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the username that this message is from.
     * @return the username of the person who sent the message.
     */
    public String getFromUsername() {
        return fromUsername;
    }

    /**
     * Gets the username the message was meant for.
     * @return gets the username of the person who this message was meant for.
     */
    public String getToUsername() {
        return toUsername;
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
