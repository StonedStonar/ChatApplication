package no.stonedstonar.chatapplication.model.membersregister;

/**
 * Represents a basic group of members in a collection and its methods.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public interface Members {

    /**
     * Gets the last member that was added.
     * @return the last member that was added.
     */
    long getLastMemberNumber();
}
