package no.stonedstonar.chatapplication.model.membersregister;

import no.stonedstonar.chatapplication.model.exception.conversation.UsernameNotPartOfConversationException;
import no.stonedstonar.chatapplication.model.member.Member;

/**
 * Represents a class that is used on the client side for observing members.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public interface ObservableMemberRegister extends MemberRegister {

    /**
     * Adds a new observer to the object.
     * @param membersRegisterObserver the new observer.
     */
    void addObserver(MembersRegisterObserver membersRegisterObserver);

    /**
     * Removes a observer from the object.
     * @param membersRegisterObserver the observer to remove.
     */
    void removeObserver(MembersRegisterObserver membersRegisterObserver);

    /**
     * Notifies the observers about a change.
     * @param member the member that has been added or removed.
     * @param removed <code>true</code> if the member has been removed.
     *                <code>false</code> if the member has been added.
     */
    void notifyObservers(Member member, boolean removed);

    /**
     * Checks if a object is a observer.
     * @param membersRegisterObserver the observer to check for.
     * @return <code>true</code> if the object is an observer.
     *         <code>false</code> if the object is not an observer.
     */
    boolean checkIfObjectIsObserver(MembersRegisterObserver membersRegisterObserver);

    /**
     * Gets the amount of members stored.
     * @return the amount of members stored.
     */
    int getAmountOfMembers();
}
