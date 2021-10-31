package no.stonedstonar.chatapplication.network.transport;

import no.stonedstonar.chatapplication.model.member.Member;

import java.io.Serializable;

/**
 * Represents a member when it's transported over the internet. Also says if it's going to be added or deleted.
 * If addMember is set to false then the member is "removed".
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class MemberTransport implements Serializable {

    private final boolean addMember;

    private final Member member;

    /**
      * Makes an instance of the MemberTransport class.
      * @param member the member you want to transport.
      * @param addMember <code>true</code> if the member is going to be added.
      *                  <code>false</code> if the member is going to be removed.
      */
    public MemberTransport(Member member, boolean addMember){
        checkIfObjectIsNull(member, "member");
        this.member = member;
        this.addMember = addMember;
    }

    /**
     * Gets the member.
     * @return the member.
     */
    public Member getMember() {
        return member;
    }

    /**
     * Gets if the member transport is adding a member.
     * @return <code>true</code> if the member is going to be added.
     *         <code>false</code> if the member is going to be removed.
     */
    public boolean isAddMember(){
        return addMember;
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
