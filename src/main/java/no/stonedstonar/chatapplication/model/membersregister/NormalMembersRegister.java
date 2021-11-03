package no.stonedstonar.chatapplication.model.membersregister;

import no.stonedstonar.chatapplication.model.exception.conversation.UsernameNotPartOfConversationException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotAddMemberException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotGetMemberException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotRemoveMemberException;
import no.stonedstonar.chatapplication.model.member.ConversationMember;
import no.stonedstonar.chatapplication.model.member.Member;

import java.io.Serializable;
import java.util.*;

/**
 * A class that represents a conversations members.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class NormalMembersRegister implements Serializable, ServerMemberRegister {

    //Todo: Endre denne til map slik at vi kan lagre hvem som var siste medlem.
    private final Map<Long, Member> memberMap;

    private final Map<Long, Member> deletedMap;

    private long lastDeletedMember;

    private long lastMember;

    /**
      * Makes an instance of the MembersOfConversation class.
      */
    public NormalMembersRegister(List<Member> conversationMembers){
        memberMap = new HashMap<>();
        deletedMap = new HashMap<>();
        lastMember = 0;
        checkIfListIsValid(conversationMembers, "conversation members");
        conversationMembers.forEach(this::addNewMember);
    }

    @Override
    public void addMember(Member userToAdd, String username) throws CouldNotAddMemberException, UsernameNotPartOfConversationException {
        checkIfUserIsMemberIfNotThrowException(username);
        if (!checkIfUsernameIsMember(userToAdd.getUsername())){
            addNewMember(userToAdd);
        }else {
            throw new CouldNotAddMemberException("The user is already in the register.");
        }
    }

    /**
     * Adds a new member to the collection.
     * @param memberToAdd the member you want to add.
     */
    private void addNewMember(Member memberToAdd){
        makeNewMemberNumber();
        Member member = new ConversationMember(memberToAdd.getUsername(), lastMember);
        memberMap.put(member.getMemberNumber(), member);
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
     * Makes a new member number.
     */
    private void makeNewMemberNumber(){
        lastMember += 1;
    }

    @Override
    public void addAllMembers(List<Member> newMembers, String username) throws CouldNotAddMemberException, UsernameNotPartOfConversationException {
        checkIfUserIsMemberIfNotThrowException(username);
        checkIfListIsValid(newMembers, "new members");
        if (!newMembers.isEmpty()){
            if (!checkIfNoneUsernamesAreInConversation(newMembers)){
                Iterator<Member> it = newMembers.iterator();
                while (it.hasNext()){
                    Member member = it.next();
                    addNewMember(member);
                }
            }else {
                throw new CouldNotAddMemberException("One person in the list is a member.");
            }
        }else {
            throw new IllegalArgumentException("All the usernames could not be added since the list is zero in size.");
        }
    }

    @Override
    public void removeAllMembers(List<Member> members, String username) throws CouldNotRemoveMemberException, UsernameNotPartOfConversationException, CouldNotGetMemberException {
        checkIfUserIsMemberIfNotThrowException(username);
        checkIfListIsValid(members, "members");
        boolean valid = checkIfUsernamesAreInConversation(members.stream().map(Member::getUsername).toList());
        if (valid){
            Iterator<Member> it = members.iterator();
            while (it.hasNext()){
                Member member = it.next();
                removeMemberFromMap(member.getUsername());
            }
        }else {
            throw new CouldNotRemoveMemberException("The members in the list could not be removed since one or more of them is not a part of the conversation object.");
        }
    }

    @Override
    public long getLastMemberNumber() {
        return lastMember;
    }

    @Override
    public long getLastDeletedMember() {
        return lastDeletedMember;
    }

    @Override
    public void removeMember(Member member, String username) throws CouldNotRemoveMemberException, UsernameNotPartOfConversationException {
        checkIfUserIsMemberIfNotThrowException(username);
        try {
            removeMemberFromMap(member.getUsername());
        }catch (CouldNotGetMemberException exception){
            throw new CouldNotRemoveMemberException("The member by the username " + member.getUsername() + " is not a part of this conversation.");
        }
    }

    /**
     * Removes a member from the map if they are in it.
     * @param usernameToRemove the username of the member to be removed.
     * @throws CouldNotGetMemberException gets thrown if the member could not be found.
     */
    private void removeMemberFromMap(String usernameToRemove) throws CouldNotGetMemberException {
        Member member = getMemberByUsername(usernameToRemove);
        memberMap.remove(member.getMemberNumber());
        lastDeletedMember += 1;
        deletedMap.put(lastDeletedMember, member);
    }


    /**
     * Checks if the user is a part of this conversation.
     * @param username the username.
     * @return <code>true</code> if the user is part of this conversation.
     *         <code>false</code> if the user is not a part of this conversation.
     */
    public boolean checkIfUsernameIsMember(String username){
        checkString(username, "username");
        return memberMap.values().stream().anyMatch(mem -> mem.getUsername().equals(username));
    }

    /**
     * Checks if all the usernames in the list is a part of the conversation.
     * 9/10 names can be correct but the 1/10 will result in a false return value.
     * So this method requires all the names to match one person in the conversation.
     * @param usernames the usernames of the users.
     * @return <code>true</code> if all the names matches someone in the group.
     *         <code>false</code> if one name or all of the names does not match any in the conversation.
     */
    public boolean checkIfUsernamesAreInConversation(List<String> usernames){
        boolean valid = false;
        checkIfObjectIsNull(usernames, "usernames");
        if (!usernames.isEmpty()){
            valid = usernames.stream().allMatch(this::checkIfUsernameIsMember);
        }else {
            throw new IllegalArgumentException("The list must have some usernames in it.");
        }
        return valid;
    }

    /**
     * Gets the member that matches the input name.
     * @param username the username that it should look for.
     * @return the member that matches that username.
     * @throws CouldNotGetMemberException gets thrown if there is no members with that username.
     */
    private Member getMemberByUsername(String username) throws CouldNotGetMemberException {
        Optional<Member> optionalMember = memberMap.values().stream().filter(mem -> mem.getUsername().equals(username)).findFirst();
        if (optionalMember.isPresent()){
            return optionalMember.get();
        }else {
            throw new CouldNotGetMemberException("The member by the username " + username  + " is not in the register.");
        }
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


    @Override
    public List<String> getNameOfAllMembers() {
        return memberMap.values().stream().map(Member::getUsername).toList();
    }

    @Override
    public Iterator<Member> getIterator() {
        return memberMap.values().iterator();
    }

    @Override
    public List<Member> checkForNewUsers(long lastMember, String username) throws UsernameNotPartOfConversationException{
        checkIfUserIsMemberIfNotThrowException(username);
        checkIfLongIsNegative(lastMember, "last member");
        List<Member> newUsers = new ArrayList<>();
        if (lastMember < this.lastMember){
            List<Long> newKeys = memberMap.keySet().stream().filter(num -> num > lastMember).toList();
            newKeys.forEach(key -> newUsers.add(memberMap.get(key)));
        }
        return newUsers;
    }

    @Override
    public List<Member> checkForDeletedMembers(long lastDeletedMember, String username) throws UsernameNotPartOfConversationException {
        List<Member> deletedMembers = new ArrayList<>();
        checkIfUserIsMemberIfNotThrowException(username);
        if (this.lastDeletedMember > lastDeletedMember){
            long firstLong = lastDeletedMember;
            do {
                deletedMembers.add(deletedMap.get(firstLong));
                firstLong += 1;
            }while (this.lastDeletedMember < firstLong);
        }
        return deletedMembers;
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
     * Checks if a long is negative or equal to zero.
     * @param number the number to check.
     * @param prefix the prefix the error should have.
     */
    private void checkIfLongIsNegative(long number, String prefix){
        if (number < 0){
            throw new IllegalArgumentException("Expected the " + prefix + " to be larger than zero.");
        }
    }

    /**
     * Checks if a list is of a valid format.
     * @param list the list you want to check.
     * @param prefix the prefix the error should have.
     */
    private void checkIfListIsValid(List list, String prefix){
        checkIfObjectIsNull(list, prefix);
        if (list.isEmpty()){
            throw new IllegalArgumentException("The " + prefix + " list cannot be zero in size.");
        }
    }
}
