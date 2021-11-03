package no.stonedstonar.chatapplication.model.membersregister;

import no.stonedstonar.chatapplication.model.exception.conversation.UsernameNotPartOfConversationException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotAddMemberException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotGetMemberException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotRemoveMemberException;
import no.stonedstonar.chatapplication.model.member.Member;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Represents an observable conversation members register. That is used on the client side.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class ObservableConversationMembersRegister implements ObservableMembersRegister{

    private Map<Long, Member> memberMap;

    private long lastMemberNumber;

    private long lastDeletedMember;

    /**
      * Makes an instance of the ObservableConversationMembersRegister class.
      */
    public ObservableConversationMembersRegister(MembersRegister membersRegister){
        memberMap = new HashMap<>();
        Iterator<Member> it = membersRegister.getIterator();
        it.forEachRemaining(member -> {
            memberMap.put(member.getMemberNumber(), member);
        });
        this.lastMemberNumber = membersRegister.getLastMemberNumber();
        this.lastDeletedMember = membersRegister.getLastDeletedMember();
    }


    
    /**
     * Checks if a string is of a valid format or not.
     * @param stringToCheck the string you want to check.
     * @param errorPrefix the error the exception should have if the string is invalid.
     */
    private void checkString(String stringToCheck, String errorPrefix){
        checkIfObjectIsNull(stringToCheck, errorPrefix);
        if (stringToCheck.isEmpty()){
            throw new IllegalArgumentException("The " + errorPrefix + " cannot be empty.");
        }
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

    /**
     * Checks if the user is a member and if not throws an exception.
     * @param username the username to check if they are member.
     * @throws UsernameNotPartOfConversationException gets thrown if the username is not a part of this members object.
     */
    private void checkIfUserIsMemberIfNotThrowException(String username) throws UsernameNotPartOfConversationException {
        checkString(username, "username");
        if (!checkIfUsernameIsMember(username)){
            throw new UsernameNotPartOfConversationException("The user by the username \"" + username  + "\" is not a part of this members object.");
        }
    }

    @Override
    public long getLastMemberNumber() {
        return lastMemberNumber;
    }

    @Override
    public long getLastDeletedMember() {
        return lastDeletedMember;
    }

    @Override
    public void addMember(Member member, String username) throws CouldNotAddMemberException, UsernameNotPartOfConversationException {
        checkIfObjectIsNull(member, "member");
        checkIfUserIsMemberIfNotThrowException(username);
        if (!checkIfUsernameIsMember(member.getUsername())){

        }else {
            throw new CouldNotAddMemberException("The member with the name " + member + " is already a part of this members register.");
        }

    }

    @Override
    public void removeMember(Member member, String username) throws CouldNotRemoveMemberException, UsernameNotPartOfConversationException {

    }

    @Override
    public void addAllMembers(List<Member> members, String username) throws CouldNotAddMemberException, UsernameNotPartOfConversationException {

    }

    @Override
    public void removeAllMembers(List<Member> members, String username) throws CouldNotRemoveMemberException, UsernameNotPartOfConversationException, CouldNotGetMemberException {

    }

    @Override
    public boolean checkIfUsernameIsMember(String username) {
        return false;
    }

    @Override
    public String getAllMembersExceptUsernameAsString(String username) {
        return null;
    }

    @Override
    public List<String> getNameOfAllMembers(String username) {
        return null;
    }

    @Override
    public int getAmountOfMembers() {
        return 0;
    }

    @Override
    public Iterator<Member> getIterator() {
        return null;
    }

    @Override
    public void addObserver() {

    }
}
