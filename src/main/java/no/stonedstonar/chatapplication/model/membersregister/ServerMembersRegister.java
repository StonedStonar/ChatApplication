package no.stonedstonar.chatapplication.model.membersregister;

import no.stonedstonar.chatapplication.model.exception.conversation.UsernameNotPartOfConversationException;
import no.stonedstonar.chatapplication.model.member.Member;

import java.util.List;

/**
 * Represents a basic group of members in a collection on the server and its methods.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public interface ServerMembersRegister extends MembersRegister {

    /**
     * Checks if there are any new users in the members object.
     * @param lastMember the long the last member has.
     * @param username the username of a member in this group.
     * @return a list with all the new users in this members object.
     * @throws UsernameNotPartOfConversationException gets thrown if the input username is not a part of this members object.
     */
    List<Member> checkForNewUsers(long lastMember, String username) throws UsernameNotPartOfConversationException;

    /**
     * Checks for new deleted members.
     * @param lastDeletedMember the last member that was deleted. The number starts with 1.
     * @param username the user's username that wants to check for deleted messages.
     * @return
     * @throws UsernameNotPartOfConversationException
     */
    List<Member> checkForDeletedMembers(long lastDeletedMember,String username) throws UsernameNotPartOfConversationException;
}
