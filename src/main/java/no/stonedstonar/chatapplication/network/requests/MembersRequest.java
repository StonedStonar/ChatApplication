package no.stonedstonar.chatapplication.network.requests;

import no.stonedstonar.chatapplication.network.requests.builder.MembersRequestBuilder;
import no.stonedstonar.chatapplication.network.transport.MemberTransport;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a request that is sent when a conversations members are going to be changed.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class MembersRequest implements Serializable {

    private final boolean checkForNewMembers;

    private final long conversationNumber;

    private final List<MemberTransport> memberTransportList;

    private final String username;

    private final long lastMember;

    /**
      * Makes an instance of the MembersRequest class.
      */
    public MembersRequest(MembersRequestBuilder membersRequestBuilder){
        checkIfObjectIsNull(membersRequestBuilder, "members request builder");
        checkForNewMembers = membersRequestBuilder.isCheckForNewMembers();
        conversationNumber = membersRequestBuilder.getConversationNumber();
        memberTransportList = membersRequestBuilder.getMembers();
        username = membersRequestBuilder.getUsername();
        lastMember = membersRequestBuilder.getLastMember();
    }

    /**
     * Gets the username that sent this request.
     * @return username of the end user.
     */
    public String getUsername(){
        return username;
    }

    /**
     * Gets the conversation number this request is about.
     * @return the conversation number.
     */
    public long getConversationNumber(){
        return conversationNumber;
    }

    /**
     * Gets the last member long.
     * @return the last member long.
     */
    public long getLastMember(){
        return lastMember;
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
        return memberTransportList;
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
