package no.stonedstonar.chatapplication.model.membersregister;

import no.stonedstonar.chatapplication.model.exception.conversation.UsernameNotPartOfConversationException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotAddMemberException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotGetMemberException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotRemoveMemberException;
import no.stonedstonar.chatapplication.model.member.Member;

import java.util.Iterator;
import java.util.List;

/**
 * Represents a basic group of members in a collection and its methods.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public interface MemberRegister {

    /**
     * Gets the last member that was added.
     * @return the last member that was added.
     */
    long getLastMemberNumber();

    /**
     * Gets the last member that was removed.
     * @return the last member that was removed.
     */
    long getLastDeletedMember();

    /**
     * Adds a user to the conversation.
     * @param member the new member to add.
     * @param username the username of the person in the group that's adding a new member.
     * @throws CouldNotAddMemberException gets thrown if the member could not be added.
     * @throws UsernameNotPartOfConversationException gets thrown if the input username is not a part of this members object.
     */
    void addMember(Member member, String username) throws CouldNotAddMemberException, UsernameNotPartOfConversationException;

    /**
     * Removes a user as a member.
     * @param member the member to be removed.
     * @param username the username of the person in the group that's removing a member.
     * @throws CouldNotRemoveMemberException gets thrown if the member could not be removed.
     * @throws UsernameNotPartOfConversationException gets thrown if the input username is not a part of this members object.
     */
    void removeMember(Member member, String username) throws CouldNotRemoveMemberException, UsernameNotPartOfConversationException;

    /**
     * Adds all the members to the conversation.
     * @param members list with all the new members
     * @param username the username of the person in the group that's adding the new members.
     * @throws CouldNotAddMemberException gets thrown if the member could not be added.
     * @throws UsernameNotPartOfConversationException gets thrown if the input username is not a part of this members object.
     */
    void addAllMembers(List<Member> members, String username) throws CouldNotAddMemberException, UsernameNotPartOfConversationException;

    /**
     * Removes all the members if they are all in the members object.
     * @param members list with all the members to be removed.
     * @param username the username of the person in the group that's removing the members.
     * @throws CouldNotRemoveMemberException gets thrown if one of the usernames cannot be found.
     * @throws UsernameNotPartOfConversationException gets thrown if the input username is not a part of this members object.
     * @throws CouldNotGetMemberException gets thrown if a member could not be located.
     */
    void removeAllMembers(List<Member> members, String username) throws CouldNotRemoveMemberException, UsernameNotPartOfConversationException, CouldNotGetMemberException;

    /**
     * Checks if the user is a part of this conversation.
     * @param username the username.
     * @return <code>true</code> if the user is part of this conversation.
     *         <code>false</code> if the user is not a part of this conversation.
     */
    boolean checkIfUsernameIsMember(String username);

    /**
     * Gets the name of all members in a list.
     * @return a list with all the usernames of the members.
     */
    List<String> getNameOfAllMembers();

    /**
     * Gets the iterator of the members.
     * @return iterator over the members in this object.
     */
    Iterator<Member> getIterator();
}
