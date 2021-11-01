package no.stonedstonar.chatapplication.network.requests.builder;

import no.stonedstonar.chatapplication.network.requests.MembersRequest;
import no.stonedstonar.chatapplication.network.transport.MemberTransport;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a builder class for the members request.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class MembersRequestBuilder {

    private boolean checkForNewMembers;

    private long conversationNumber;

    private List<MemberTransport> memberList;

    private String username;

    private long lastMember;

    private long lastDeletedMember;

    /**
      * Makes an instance of the MembersRequestBuilder class.
      */
    public MembersRequestBuilder(){
        checkForNewMembers = false;
        memberList = new ArrayList<>();
        username = "";
    }

    /**
     * Says if the request wants to check for new members.
     * All the other booleans will be set to false if this is set to true.
     * @param valid <code>true</code> if the list with usernames are going to be added.
     *              <code>false</code> if the usernames should not be added.
     * @return this builder object.
     */
    public MembersRequestBuilder setCheckForNewMembers(boolean valid){
        if (valid){
            checkForNewMembers = true;
        }else {
            checkForNewMembers = false;
        }
        return this;
    }

    /**
     * Sets the last deleted member number.
     * @param lastDeletedMember the last member that was deleted.
     * @return this builder object.
     */
    public MembersRequestBuilder setLastDeletedMember(long lastDeletedMember){
        checkIfLongIsAboveZero(lastDeletedMember, "last deleted member", false);
        this.lastDeletedMember = lastDeletedMember;
        return this;
    }

    /**
     * Adds a list with usernames to the request object.
     * @param membersToTransport the list with usernames you want to add.
     * @return this builder object.
     */
    public MembersRequestBuilder addMemberTransports(List<MemberTransport> membersToTransport){
        checkIfObjectIsNull(membersToTransport, "usernames");
        this.memberList = membersToTransport;
        return this;
    }

    /**
     * Adds a conversation number to the request.
     * @param conversationNumber the conversation number.
     * @return this builder object.
     */
    public MembersRequestBuilder addConversationNumber(long conversationNumber){
        checkIfLongIsAboveZero(conversationNumber, "messagelog number", false);
        this.conversationNumber = conversationNumber;
        return this;
    }

    /**
     * Adds the last member long.
     * @param lastMember the last member.
     * @return this builder object.
     */
    public MembersRequestBuilder addLastMember(long lastMember){
        checkIfLongIsAboveZero(lastMember, "last member", false);
        this.lastMember = lastMember;
        return this;
    }

    /**
     * Adds the username of the end user to the request.
     * @param username the username.
     * @return this builder object.
     */
    public MembersRequestBuilder addUsername(String username){
        checkString(username, "username");
        this.username = username;
        return this;
    }

    /**
     * Gets the username that sent this request.
     * @return username of the end user.
     */
    public String getUsername(){
        return username;
    }

    /**
     * Gets the last deleted member.
     * @return gets the last deleted member.
     */
    public long getLastDeletedMember(){
        return lastDeletedMember;
    }
    /**
     * Gets the last member long.
     * @return the last member long.
     */
    public long getLastMember(){
        return lastMember;
    }

    /**
     * Gets the conversation number this request is about.
     * @return the conversation number.
     */
    public long getConversationNumber(){
        return conversationNumber;
    }

    /**
     * Says true if the request is a checkForNewMembers request.
     * @return <code>true</code> if this is supposed to check for new members.
     *         <code>false</code> if this is not supposed to check for new members.
     */
    public boolean isCheckForNewMembers() {
        return checkForNewMembers;
    }



    /***
     * Gets the list of all the members.
     * @return a list with all the members.
     */
    public List<MemberTransport> getMembers() {
        return memberList;
    }

    /**
     * Makes the members request.
     * @return a members request with the input values.
     */
    public MembersRequest build(){
        return new MembersRequest(this);
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
     * Checks if a string is of a valid format or not.
     * @param stringToCheck the string you want to check.
     * @param errorPrefix the error the exception should have if the string is invalid.
     */
    public void checkString(String stringToCheck, String errorPrefix){
        checkIfObjectIsNull(stringToCheck, errorPrefix);
        if (stringToCheck.isEmpty()){
            throw new IllegalArgumentException("The " + errorPrefix + " cannot be empty.");
        }
    }

    /**
     * Checks if the long is above zero.
     * @param logNumber the log number you want to check.
     * @param prefix the error the exception should mention.
     */
    private void checkIfLongIsAboveZero(long logNumber, String prefix, boolean equalZero){
        if (equalZero){
            if (logNumber < 0){
                throw new IllegalArgumentException("The " + prefix + " cannot be negative or null.");
            }
        }else {
            if(logNumber <= 0){
                throw new IllegalArgumentException("The " + prefix + " cannot be negative or null.");
            }
        }
    }
}
