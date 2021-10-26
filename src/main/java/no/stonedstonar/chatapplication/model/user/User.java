package no.stonedstonar.chatapplication.model.user;

import java.io.Serializable;

/**
 * Represents basic methods that a user should have.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public interface User extends Serializable {

    /**
     * Sets the username to a new value.
     * @param username the new useranme of the person.
     */
    void setUsername(String username, String password);

    /**
     * Gets the username of this user.
     * @return the username of this user.
     */
    String getUsername();

    /**
     * Sets the password to a new password if the old one matches the set password.
     * @param newPassword the new password of the user.
     * @param oldPassword the old password of the user.
     */
    void setPassword(String newPassword, String oldPassword);

    /**
     * Checks if the password is correct.
     * @param password the password you want to check.
     * @return <code>true</code> if the passwords match.
     *         <code>false</code> if the passwords mismatch.
     */
    boolean checkPassword(String password);
}
