package no.stonedstonar.chatapplication.model;

import no.stonedstonar.chatapplication.model.exception.member.CouldNotAddMemberException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotRemoveMemberException;
import no.stonedstonar.chatapplication.model.user.User;

import java.io.Serializable;
import java.util.*;

/**
 * A object that holds all the members of a object.
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class Members implements Serializable {

    //Todo: Endre denne til map slik at vi kan lagre hvem som var siste medlem.
    private final Map<Long, String> memberMap;

    private long lastMember;

    /**
      * Makes an instance of the MembersOfConversation class.
      */
    public Members(){
        memberMap = new HashMap<>();
    }

    /**
     * Gets all the members from the object if the username is a part of the group.
     * @param user the user that is a member in the conversation.
     * @return a list with all the members.
     */
    public List<String> getAllMembers(User user){
        checkIfObjectIsNull(user, "user");
        String username = user.getUsername();
        if (checkIfUsernameIsMember(username)){
            return memberMap.values().stream().toList();
        }else {
            throw new IllegalArgumentException("The user with the username " + username + " is not a part of this members object.");
        }
    }

    /**
     * Adds a user to the conversation.
     * @param username the username.
     * @throws CouldNotAddMemberException gets thrown if the member could not be added.
     */
    public void addMember(String username) throws CouldNotAddMemberException {
        if (!checkIfUsernameIsMember(username)){
            makeNewMemberNumber();
            memberMap.put(lastMember, username);
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

    /**
     * Adds all the members to the conversation.
     * @param usernames the usernames of all the members of the conversation.
     * @throws CouldNotAddMemberException gets thrown if the member could not be added.
     */
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

    /**
     * Removes a user as a member.
     * @param username the username.
     * @throws CouldNotRemoveMemberException gets thrown if the member could not be removed.
     */
    public void removeMember(String username) throws CouldNotRemoveMemberException {
        if (checkIfUsernameIsMember(username)){
            memberMap.remove(username);
        }else {
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
        return memberMap.values().stream().anyMatch(mem -> mem.equals(username));
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
            long amount = memberMap.values().stream().filter(name -> {
                return usernames.stream().anyMatch(username -> username.equals(name));
            }).count();
            if ((amount == usernames.size()) && (memberMap.size() == amount)){
                valid = true;
            }
        }else {
            throw new IllegalArgumentException("The list must have some usernames in it.");
        }
        return valid;
    }

    /**
     * Checks if one username is in the group already.
     * @param usernames the list with all the usernames to be checked.
     * @return <code>true</code> if one of the usernames are in the register.
     *         <code>false</code> if none of the usernames are in the register.
     */
    private boolean checkIfNoneUsernamesAreInConversation(List<String> usernames){
        if (!usernames.isEmpty()){
            return memberMap.values().stream().anyMatch(username -> {
               return usernames.stream().anyMatch(name -> name.equals(username));
            });
        }else {
            System.out.println(usernames.size());
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

        for (String member : memberMap.values()){
            if (!member.equals(username)){
                stringBuilder.append(" ");
                stringBuilder.append(member);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Checks if there are any new users in the members object.
     * @param lastMember the long the last member has.
     * @return a list with all the new users in this members object.
     */
    public List<String> checkForNewUsers(long lastMember){
        checkIfLongIsNegative(lastMember, "last member");
        List<String> newUsers = new ArrayList<>();
        if (lastMember < this.lastMember){
            List<Long> newKeys = memberMap.keySet().stream().filter(num -> num > lastMember).toList();
            newKeys.forEach(key -> newUsers.add(memberMap.get(key)));
        }
        return newUsers;
    }

    /**
     * Gets the size of the conversation.
     * @return the amount of users in the conversation.
     */
    public int getAmountOfMembers(){
        return memberMap.size();
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
