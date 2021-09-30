package no.stonedstonar.chatapplication.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class MembersOfConversation implements Serializable {

    private List<String> memberList;

    /**
      * Makes an instance of the MembersOfConversation class.
      */
    public MembersOfConversation(){
        memberList = new ArrayList<>();
    }

    /**
     * Adds a user to the conversation.
     * @param username the username.
     */
    public void addMember(String username){
        if (!checkIfUsernameIsMember(username)){
            memberList.add(username);
        }else {
            throw new IllegalArgumentException("The user is already in the register.");
        }
    }

    /**
     * Adds all the members to the conversation.
     * @param usernames the usernames of all the members of the conversation.
     */
    public void addAllMembers(List<String> usernames){
        checkIfObjectIsNull(usernames, "usernames");
        if (usernames.size() > 0){
            usernames.forEach((String username) -> {
                addMember(username);
            });
        }else {
            throw new IllegalArgumentException("All the usernames could not be added since the list is zero in size.");
        }
    }

    /**
     * Removes a user as a member.
     * @param username the username.
     */
    public void removeMember(String username){
        if (checkIfUsernameIsMember(username)){
            memberList.remove(username);
        }else {
            throw new IllegalArgumentException("The member by the username " + username + " is not a part of this conversation.");
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
        return memberList.stream().anyMatch(mem -> mem.equals(username));
    }

    /**
     * Checks if all the usernames in the list is a part of the conversation.
     * @param usernames the usernames of the users.
     * @return <code>true</code> if all the names matches everyone in the group.
     */
    public boolean checkIfUsernamesAreInConversation(List<String> usernames){
        boolean valid = false;
        checkIfObjectIsNull(usernames, "usernames");
        long amount = memberList.stream().filter(name -> {
            return usernames.stream().anyMatch(username -> username.equals(name));
        }).count();
        if ((amount == usernames.size()) && (memberList.size() == amount)){
            valid = true;
        }
        return valid;
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
}
