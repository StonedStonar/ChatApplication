package no.stonedstonar.chatapplication.backend;
/**
 *
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class ServerTestData {

    public static void makeTestDataForServer(){

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
