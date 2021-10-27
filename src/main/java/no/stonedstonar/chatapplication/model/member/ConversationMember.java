package no.stonedstonar.chatapplication.model.member;

import java.io.Serializable;

/**
 * A member of a conversation.
 * @author Steinar Hjelle Midthus
 * @version 0.2
 */
public final class ConversationMember implements Serializable, Member {

    private final String username;

    private final long memberNumber;

    /**
     * Makes an instance of the Member class.
     * @param username the username of the member.
     * @param memberNumber the member number.
     */
    public ConversationMember(String username, long memberNumber) {
        checkIfLongIsNegative(memberNumber, "member number");
        checkString(username, "username");
        this.username = username;
        this.memberNumber = memberNumber;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public long getMemberNumber() {
        return memberNumber;
    }

    /**
     * Checks if a long is negative or equal to zero.
     *
     * @param number the number to check.
     * @param prefix the prefix the error should have.
     */
    private void checkIfLongIsNegative(long number, String prefix) {
        if (number < 0) {
            throw new IllegalArgumentException("Expected the " + prefix + " to be larger than zero.");
        }
    }

    /**
     * Checks if a string is of a valid format or not.
     *
     * @param stringToCheck the string you want to check.
     * @param errorPrefix   the error the exception should have if the string is invalid.
     */
    private void checkString(String stringToCheck, String errorPrefix) {
        checkIfObjectIsNull(stringToCheck, errorPrefix);
        if (stringToCheck.isEmpty()) {
            throw new IllegalArgumentException("The " + errorPrefix + " cannot be empty.");
        }
    }

    /**
     * Checks if an object is null.
     *
     * @param object the object you want to check.
     * @param error  the error message the exception should have.
     */
    private void checkIfObjectIsNull(Object object, String error) {
        if (object == null) {
            throw new IllegalArgumentException("The " + error + " cannot be null.");
        }
    }
}
