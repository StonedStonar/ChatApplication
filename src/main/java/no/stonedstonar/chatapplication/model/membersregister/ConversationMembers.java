package no.stonedstonar.chatapplication.model.membersregister;

import no.stonedstonar.chatapplication.model.exception.member.CouldNotAddMemberException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotGetMemberException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotRemoveMemberException;
import no.stonedstonar.chatapplication.model.member.ConversationMember;
import no.stonedstonar.chatapplication.model.user.User;

import java.io.Serializable;
import java.util.*;

/**
 * A object that holds all the members of a object.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class ConversationMembers implements Serializable, ServerMembers {

    //Todo: Endre denne til map slik at vi kan lagre hvem som var siste medlem.
    private final Map<Long, ConversationMember> memberMap;

    private long lastMember;

    /**
      * Makes an instance of the MembersOfConversation class.
      */
    public ConversationMembers(){
        memberMap = new HashMap<>();
    }

    /**
     * Gets all the members from the object if the username is a part of the group.
     * @param user the user that is a member in the conversation.
     * @return a list with all the members.
     */
    public List<String> getNameOfAllMembers(User user){
        checkIfObjectIsNull(user, "user");
        String username = user.getUsername();
        if (checkIfUsernameIsMember(username)){
            return memberMap.values().stream().map(ConversationMember::getUsername).toList();
        }else {
            throw new IllegalArgumentException("The user with the username " + username + " is not a part of this members object.");
        }
    }

    @Override
    public void addMember(String username) throws CouldNotAddMemberException {
        if (!checkIfUsernameIsMember(username)){
            makeNewMemberNumber();
            ConversationMember conversationMember = new ConversationMember(username, lastMember);
            memberMap.put(conversationMember.getMemberNumber(), conversationMember);
        }else {
            throw new CouldNotAddMemberException("The user is already in the register.");
        }
    }

    /**
     * Makes a new member number.
     */
    private void makeNewMemberNumber(){
        lastMember += 1;
    }

    @Override
    public void addAllMembers(List<String> usernames) throws CouldNotAddMemberException{
        checkIfListIsValid(usernames, "usernames");
        if (!usernames.isEmpty()){
            if (!checkIfNoneUsernamesAreInConversation(usernames)){
                Iterator<String> it = usernames.iterator();
                while (it.hasNext()){
                    String name = it.next();
                    addMember(name);
                }
            }else {
                throw new CouldNotAddMemberException("One person in the list is a member.");
            }
        }else {
            throw new IllegalArgumentException("All the usernames could not be added since the list is zero in size.");
        }
    }

    @Override
    public long getLastMemberNumber() {
        return lastMember;
    }

    @Override
    public void removeMember(String username) throws CouldNotRemoveMemberException {
        try {
            ConversationMember conversationMember = getMemberByUsername(username);
            memberMap.remove(conversationMember.getMemberNumber());
        }catch (CouldNotGetMemberException exception){
            throw new CouldNotRemoveMemberException("The member by the username " + username + " is not a part of this conversation.");
        }
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
     * @return <code>true</code> if all the names matches everyone in the group.
     *         <code>false</code> if one name or all of the names does not match any in the conversation.
     */
    public boolean checkIfUsernamesAreInConversation(List<String> usernames){
        boolean valid = false;
        checkIfObjectIsNull(usernames, "usernames");
        if (!usernames.isEmpty()){
            long amount = memberMap.values().stream().filter(conversationMember -> usernames.stream().anyMatch(username -> username.equals(conversationMember.getUsername()))).count();
            if ((amount == usernames.size()) && (memberMap.size() == amount)){
                valid = true;
            }
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
    private ConversationMember getMemberByUsername(String username) throws CouldNotGetMemberException {
        Optional<ConversationMember> optionalMember = memberMap.values().stream().filter(mem -> mem.getUsername().equals(username)).findFirst();
        if (optionalMember.isPresent()){
            return optionalMember.get();
        }else {
            throw new CouldNotGetMemberException("The member by the username " + username  + " is not in the register.");
        }
    }

    /**
     * Checks if one username is in the group already.
     * @param usernames the list with all the usernames to be checked.
     * @return <code>true</code> if one of the usernames are in the register.
     *         <code>false</code> if none of the usernames are in the register.
     */
    private boolean checkIfNoneUsernamesAreInConversation(List<String> usernames){
        if (!usernames.isEmpty()){
            return memberMap.values().stream().anyMatch(conversationMember -> usernames.stream().anyMatch(name -> name.equals(conversationMember.getUsername())));
        }else {
            throw new IllegalArgumentException("The list must have some usernames in it.");
        }
    }

    /**
     * Builds a string that contains the name of all members in the register except the input username.
     * @param username the username you don't want in the string.
     * @return all the usernames that does not match the input username in a string.
     */
    public String getAllMembersExceptUsernameAsString(String username){
        checkString(username, "username");
        if (!checkIfUsernameIsMember(username)){
            throw new IllegalArgumentException("The username " + username + " is not in the conversation.");
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (ConversationMember conversationMember : memberMap.values()){
            if (!conversationMember.getUsername().equals(username)){
                stringBuilder.append(" ");
                stringBuilder.append(conversationMember);
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public List<ConversationMember> checkForNewUsers(long lastMember, String username){
        checkIfLongIsNegative(lastMember, "last member");
        List<ConversationMember> newUsers = new ArrayList<>();
        if (lastMember < this.lastMember){
            List<Long> newKeys = memberMap.keySet().stream().filter(num -> num > lastMember).toList();
            newKeys.forEach(key -> newUsers.add(memberMap.get(key)));
        }
        return newUsers;
    }

    /\\
    
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
