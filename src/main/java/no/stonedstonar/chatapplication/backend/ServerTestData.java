package no.stonedstonar.chatapplication.backend;

import no.stonedstonar.chatapplication.model.conversation.ServerConversation;
import no.stonedstonar.chatapplication.model.conversationregister.server.ServerConversationRegister;
import no.stonedstonar.chatapplication.model.exception.conversation.CouldNotAddConversationException;
import no.stonedstonar.chatapplication.model.exception.conversation.UsernameNotPartOfConversationException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotAddMemberException;
import no.stonedstonar.chatapplication.model.exception.message.CouldNotAddMessageException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotGetMessageLogException;
import no.stonedstonar.chatapplication.model.exception.user.CouldNotAddUserException;
import no.stonedstonar.chatapplication.model.member.ConversationMember;
import no.stonedstonar.chatapplication.model.member.Member;
import no.stonedstonar.chatapplication.model.message.TextMessage;
import no.stonedstonar.chatapplication.model.user.EndUser;
import no.stonedstonar.chatapplication.model.userregister.UserRegister;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents methods to fill the server with test data.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class ServerTestData {

    /**
     * Makes test data and fills a conversation register and user register with the data.
     * @param serverConversationRegister the servers conversation register to fill.
     * @param userRegister the user register to fill with test data.
     * @throws CouldNotAddConversationException gets thrown if the conversation could not be added.
     * @throws CouldNotAddMemberException gets thrown if the member could not be added.
     * @throws UsernameNotPartOfConversationException gets thrown if the user is not a part of this conversation.
     * @throws CouldNotAddMessageException gets thrown if a message could not be added.
     * @throws CouldNotGetMessageLogException gets thrown if a message log could not be added.
     * @throws CouldNotAddUserException gets thrown if the test user could not be added.
     */
    public static void makeTestDataForServer(ServerConversationRegister serverConversationRegister, UserRegister userRegister) throws CouldNotAddConversationException, CouldNotAddMemberException, UsernameNotPartOfConversationException, CouldNotAddMessageException, CouldNotGetMessageLogException, CouldNotAddUserException {
        EndUser endUser = new EndUser("bjarne22", "passr");
        EndUser endUser1 = new EndUser("fjell", "passord");
        EndUser endUser3 = new EndUser("bass", "thepass");
        userRegister.addUser(endUser);
        userRegister.addUser(endUser1);
        userRegister.addUser(endUser3);
        List<Member> twoMembers = new ArrayList<>();
        twoMembers.add(new ConversationMember(endUser.getUsername()));
        twoMembers.add(new ConversationMember(endUser1.getUsername()));
        List<Member> threeMembers = new ArrayList<>();
        threeMembers.add(new ConversationMember(endUser.getUsername()));
        threeMembers.add(new ConversationMember(endUser1.getUsername()));
        threeMembers.add(new ConversationMember(endUser3.getUsername()));
        serverConversationRegister.addNewConversationWithUsernames(threeMembers, "");
        serverConversationRegister.addNewConversationWithUsernames(twoMembers, "");
        List<ServerConversation> normalMessageLogs = serverConversationRegister.getAllConversationsOfUsername("bjarne22");
        normalMessageLogs.get(0).addNewMessage(new TextMessage("Haha", "bjarne22"));
        normalMessageLogs.get(0).addNewMessage(new TextMessage("Nope", "fjell"));
        normalMessageLogs.get(0).addNewMessage(new TextMessage("So funny bjarne", "bass"));
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
