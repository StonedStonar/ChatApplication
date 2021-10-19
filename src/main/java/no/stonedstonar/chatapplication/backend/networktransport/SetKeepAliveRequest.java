package no.stonedstonar.chatapplication.backend.networktransport;

import java.io.Serializable;

/**
 * A request that says if the socket is going to be kept alive or not.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class SetKeepAliveRequest implements Serializable {

    private boolean keepAlive;

    /**
      * Makes an instance of the SetKeepAliveRequest class.
      */
    public SetKeepAliveRequest(boolean valid){
        keepAlive = valid;
    }

    /**
     * Says if it's going to be kept alive or not.
     * @return <code>true</code> if the socket must be kept alive.
     *         <code>false</code> if the socket dont need to be kept alive.
     */
    public boolean isKeepAlive() {
        return keepAlive;
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
