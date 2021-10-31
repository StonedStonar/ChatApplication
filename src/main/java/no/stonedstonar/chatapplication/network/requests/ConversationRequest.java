package no.stonedstonar.chatapplication.network.requests;

import no.stonedstonar.chatapplication.model.member.Member;
import no.stonedstonar.chatapplication.network.requests.builder.ConversationRequestBuilder;
import no.stonedstonar.chatapplication.network.transport.MemberTransport;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a conversation request that can do different operations based on the input values.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class ConversationRequest implements Serializable {

    private boolean deleteConversation;

    private boolean newConversation;

    private String nameOfConversation;

    private List<Long> conversationNumberList;

    private boolean checkForNewConversation;

    private String username;

    private List<Member> memberList;

    /**
      * Makes an instance of the MessageLogRequest class.
      */
    public ConversationRequest(ConversationRequestBuilder conversationRequestBuilder){
        deleteConversation = conversationRequestBuilder.isDeleteConversation();
        newConversation = conversationRequestBuilder.isNewConversation();
        nameOfConversation = conversationRequestBuilder.getNameOfConversation();
        checkForNewConversation = conversationRequestBuilder.isCheckForNewConversation();
        conversationNumberList = conversationRequestBuilder.getConversationNumberList();
        username = conversationRequestBuilder.getUsername();
        memberList = conversationRequestBuilder.getMemberList();
    }

    /**
     * Gets the username of the end user.
     * @return the username of the end user.
     */
    public String getUsername(){
        return username;
    }

    /**
     * The list with all the first members if this is a new conversation.
     * @return the member list.
     */
    public List<Member> getMemberList(){
        return memberList;
    }

    /**
     * Gets the list with all the conversation numbers.
     * @return the list with all the conversation numbers.
     */
    public List<Long> getConversationNumberList(){
        return conversationNumberList;
    }

    /**
     * Says true if the request is a check for new conversations.
     * @return <code>true</code> if the request wants to check for new conversations.
     *         <code>false</code> if the does not want to check for new conversations.
     */
    public boolean isCheckForNewConversation(){
        return checkForNewConversation;
    }


    /**
     * Says true if the request is a remove conversation request.
     * @return <code>true</code> if the conversation needs to be deleted.
     *         <code>false</code> if the conversation is going to be deleted.
     */
    public boolean isDeleteConversation() {
        return deleteConversation;
    }


    /**
     * Says true if this request is a new conversation.
     * @return <code>true</code> if this request is a new conversation.
     *         <code>false</code> if this request is not a new conversation.
     */
    public boolean isNewConversation() {
        return newConversation;
    }

    /**
     * Gets the name of the conversation.
     * @return the name of the conversation.
     */
    public String getNameOfConversation() {
        return nameOfConversation;
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
     * Checks if an object is null.
     * @param object the object you want to check.
     * @param error the error message the exception should have.
     */
    public void checkIfObjectIsNull(Object object, String error){
       if (object == null){
           throw new IllegalArgumentException("The " + error + " cannot be null.");
       }
    }
}
