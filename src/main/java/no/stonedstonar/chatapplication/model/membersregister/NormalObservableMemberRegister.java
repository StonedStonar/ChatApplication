package no.stonedstonar.chatapplication.model.membersregister;

import no.stonedstonar.chatapplication.model.exception.conversation.UsernameNotPartOfConversationException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotAddMemberException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotGetMemberException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotRemoveMemberException;
import no.stonedstonar.chatapplication.model.member.Member;

import java.io.Serializable;
import java.util.*;

/**
 * Represents an observable conversation members register. That is used on the client side.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class NormalObservableMemberRegister implements ObservableMemberRegister, Serializable {

    private final Map<Long, Member> memberMap;

    private final List<MembersRegisterObserver> membersRegisterObserverList;

    private long lastMemberNumber;

    private long lastDeletedMember;

    /**
      * Makes an instance of the ObservableConversationMembersRegister class.
      */
    public NormalObservableMemberRegister(MemberRegister memberRegister){
        memberMap = new HashMap<>();
        checkIfObjectIsNull(memberRegister, "members register");
        membersRegisterObserverList = new ArrayList<>();
        Iterator<Member> it = memberRegister.getIterator();
        it.forEachRemaining(member -> {
            memberMap.put(member.getMemberNumber(), member);
        });
        this.lastMemberNumber = memberRegister.getLastMemberNumber();
        this.lastDeletedMember = memberRegister.getLastDeletedMember();
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

    /**
     * Checks if all the usernames in the list is a part of the conversation.
     * 9/10 names can be correct but the 1/10 will result in a false return value.
     * So this method requires all the names to match one person in the conversation.
     * @param usernames the usernames of the users.
     * @return <code>true</code> if all the names matches someone in the group.
     *         <code>false</code> if one name or all of the names does not match any in the conversation.
     */
    private boolean checkIfUsernamesAreInConversation(List<String> usernames){
        boolean valid = false;
        checkIfObjectIsNull(usernames, "usernames");
        if (!usernames.isEmpty()){
            valid = usernames.stream().allMatch(this::checkIfUsernameIsMember);
            System.out.println(memberMap.values().stream().map(mem -> mem.getUsername()).toList());
        }else {
            throw new IllegalArgumentException("The list must have some usernames in it.");
        }
        return valid;
    }

    /**
     * Checks if one username is in the group already.
     * @param members the list with all the members to be checked.
     * @return <code>true</code> if one of the usernames are in the register.
     *         <code>false</code> if none of the usernames are in the register.
     */
    private boolean checkIfNoneUsernamesAreInConversation(List<Member> members){
        if (!members.isEmpty()){
            return members.stream().anyMatch(mem -> memberMap.values().stream().anyMatch(mem2 -> mem2.getUsername().equals(mem.getUsername())));
        }else {
            throw new IllegalArgumentException("The list must have some usernames in it.");
        }
    }

    /**
     * Adds a new member to the collection.
     * @param memberToAdd the member you want to add.
     */
    private void addNewMember(Member memberToAdd){
        memberMap.put(memberToAdd.getMemberNumber(), memberToAdd);
        lastMemberNumber = memberToAdd.getMemberNumber();
        notifyObservers(memberToAdd, false);
    }

    /**
     * Removes a member from the map.
     * @param member the member you want to remove.
     */
    private void removeMember(Member member){
        memberMap.remove(member.getMemberNumber());
        lastDeletedMember += 1;
        notifyObservers(member, true);
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
            addNewMember(member);
        }else {
            throw new CouldNotAddMemberException("The member with the name " + member + " is already a part of this members register.");
        }
    }

    @Override
    public void removeMember(Member member, String username) throws CouldNotRemoveMemberException, UsernameNotPartOfConversationException {
        checkIfObjectIsNull(member, "member");
        checkIfUserIsMemberIfNotThrowException(username);
        if (checkIfUsernameIsMember(member.getUsername())){
            removeMember(member);
        }else {
            throw new CouldNotRemoveMemberException("The member with the username " + member.getUsername() + " is not a part of this members object.");
        }
    }

    @Override
    public void addAllMembers(List<Member> members, String username) throws CouldNotAddMemberException, UsernameNotPartOfConversationException {
        checkIfListIsValid(members, "members to add");
        checkIfUserIsMemberIfNotThrowException(username);
        if (!checkIfNoneUsernamesAreInConversation(members)){
            members.forEach(this::addNewMember);
        }else {
            throw new CouldNotAddMemberException("One of the members is already in the register.");
        }
    }

    @Override
    public void removeAllMembers(List<Member> members, String username) throws CouldNotRemoveMemberException, UsernameNotPartOfConversationException, CouldNotGetMemberException {
        checkIfListIsValid(members, "members to remove");
        checkIfUserIsMemberIfNotThrowException(username);
        if (checkIfUsernamesAreInConversation(members.stream().map(member -> member.getUsername()).toList())){
            for (Member member : members) {
                removeMember(member);
            }
        }else {
            throw new CouldNotRemoveMemberException("There are one or more members that are not in the register.");
        }
    }

    @Override
    public boolean checkIfUsernameIsMember(String username) {
        checkString(username, "username");
        return memberMap.values().stream().anyMatch(member -> member.getUsername().equals(username));
    }

    @Override
    public List<String> getNameOfAllMembers() {
        return memberMap.values().stream().map(Member::getUsername).toList();
    }

    @Override
    public int getAmountOfMembers() {
        return memberMap.size();
    }

    @Override
    public Iterator<Member> getIterator() {
        return memberMap.values().iterator();
    }

    @Override
    public void addObserver(MembersRegisterObserver membersRegisterObserver) {
        checkIfObjectIsNull(membersRegisterObserver, "observer");
        if (!checkIfObjectIsObserver(membersRegisterObserver)){
            membersRegisterObserverList.add(membersRegisterObserver);
        }else {
            throw new IllegalArgumentException("The observer is already in the observer register.");
        }
    }

    @Override
    public void removeObserver(MembersRegisterObserver membersRegisterObserver) {
        checkIfObjectIsNull(membersRegisterObserver, "observer");
        if (checkIfObjectIsObserver(membersRegisterObserver)){
            membersRegisterObserverList.remove(membersRegisterObserver);
        }else {
            throw new IllegalArgumentException("The observer object is not subscribed to this object.");
        }
    }

    @Override
    public void notifyObservers(Member member, boolean removed) {
        checkIfObjectIsNull(member, "member");
        membersRegisterObserverList.forEach(obs -> obs.updateMember(member, removed));
    }

    @Override
    public boolean checkIfObjectIsObserver(MembersRegisterObserver membersRegisterObserver) {
        checkIfObjectIsNull(membersRegisterObserver, "observer");
        return membersRegisterObserverList.stream().anyMatch(observer -> observer.equals(membersRegisterObserver));
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
     * Checks if a list is of a valid format.
     * @param list   the list you want to check.
     * @param prefix the prefix the error should have.
     */
    private void checkIfListIsValid(List list, String prefix) {
        checkIfObjectIsNull(list, prefix);
        if (list.isEmpty()) {
            throw new IllegalArgumentException("The " + prefix + " list cannot be zero in size.");
        }
    }
}
