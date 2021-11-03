package no.stonedstonar.chatapplication.model.membersregister;

import no.stonedstonar.chatapplication.model.member.Member;

/**
 * Represents an object that is a observer of a Members register.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public interface MembersRegisterObserver {

    /**
     * Tells if there is a new member to update.
     * @param member the member to add or remove.
     * @param removed <code>true</code> if the member is removed.
     *                <code>false</code> if the member is added.
     */
    void updateMember(Member member, boolean removed);
}
