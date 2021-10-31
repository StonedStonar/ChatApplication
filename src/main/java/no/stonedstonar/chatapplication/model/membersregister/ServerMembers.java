package no.stonedstonar.chatapplication.model.membersregister;

import no.stonedstonar.chatapplication.model.exception.conversation.UsernameNotPartOfConversationException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotAddMemberException;
import no.stonedstonar.chatapplication.model.member.Member;

import java.util.List;

/**
 * Represents a basic group of members in a collection on the server and its methods.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public interface ServerMembers extends Members {

    /**
     * Checks if there are any new users in the members object.
     * @param lastMember the long the last member has.
     * @param username the username of a member in this group.
     * @return a list with all the new users in this members object.
     * @throws UsernameNotPartOfConversationException gets thrown if the input username is not a part of this members object.
     */
    List<Member> checkForNewUsers(long lastMember, String username) throws UsernameNotPartOfConversationException;
}
