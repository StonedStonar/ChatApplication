package no.stonedstonar.chatapplication.model.membersregister;

import no.stonedstonar.chatapplication.model.exception.member.CouldNotAddMemberException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotRemoveMemberException;
import no.stonedstonar.chatapplication.model.member.ConversationMember;

import java.util.List;

/**
 * Represents a basic group of members in a collection on the server and its methods.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public interface ServerMembers extends Members {

    /**
     * Adds a user to the conversation.
     * @param username the username.
     * @throws CouldNotAddMemberException gets thrown if the member could not be added.
     */
    void addMember(String username) throws CouldNotAddMemberException;

    /**
     * Removes a user as a member.
     * @param username the username.
     * @throws CouldNotRemoveMemberException gets thrown if the member could not be removed.
     */
    void removeMember(String username) throws CouldNotRemoveMemberException;

    /**
     * Adds all the members to the conversation.
     * @param usernames the usernames of all the members of the conversation.
     * @throws CouldNotAddMemberException gets thrown if the member could not be added.
     */
    void addAllMembers(List<String> usernames) throws CouldNotAddMemberException;

    /**
     * Removes all the members if they are all in the members object.
     * @param usernames the usernames to be removed.
     * @throws CouldNotRemoveMemberException gets thrown if one of the usernames cannot be found.
     */
    void removeAllMembers(List<String> usernames) throws CouldNotRemoveMemberException;

    /**
     * Checks if there are any new users in the members object.
     * @param lastMember the long the last member has.
     * @param username the username of a member in this group.
     * @return a list with all the new users in this members object.
     */
    List<ConversationMember> checkForNewUsers(long lastMember, String username);

}
