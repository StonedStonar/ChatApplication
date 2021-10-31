package no.stonedstonar.chatappliation.model;

import no.stonedstonar.chatapplication.model.conversation.Conversation;
import no.stonedstonar.chatapplication.model.conversation.NormalServerConversation;
import no.stonedstonar.chatapplication.model.conversationregister.server.NormalConversationRegister;
import no.stonedstonar.chatapplication.model.conversationregister.server.ServerConversationRegister;
import no.stonedstonar.chatapplication.model.exception.conversation.CouldNotAddConversationException;
import no.stonedstonar.chatapplication.model.exception.conversation.CouldNotGetConversationException;
import no.stonedstonar.chatapplication.model.exception.conversation.CouldNotRemoveConversationException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotAddMemberException;
import no.stonedstonar.chatapplication.model.member.ConversationMember;
import no.stonedstonar.chatapplication.model.member.Member;
import no.stonedstonar.chatapplication.model.membersregister.Members;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * A class that tests methods in the ConversationRegister class.
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class TestNormalConversationRegister {

    private ServerConversationRegister conversationRegister;

    private String username;

    /**
     * Makes a conversation used for testing.
     */
    @BeforeEach
    private void makeTestConversation(){
        try {
            conversationRegister = new NormalConversationRegister();
            Member member5 = new ConversationMember("bjarne22");
            username = member5.getUsername();
            Member useranme2 = new ConversationMember("lordVader");
            Member user3 = new ConversationMember("fjell");
            Member user4 = new ConversationMember("pål15");
            List<Member> usernames1 = new ArrayList<>();
            usernames1.add(useranme2);
            usernames1.add(member5);
            List<Member> usernames2 = new ArrayList<>();
            usernames2.add(user3);
            usernames2.add(user4);
            usernames2.add(member5);
            List<Member> usernames3 = new ArrayList<>();
            usernames3.add(useranme2);
            usernames3.add(user4);
            conversationRegister.addNewConversationWithUsernames(usernames1, "");
            conversationRegister.addNewConversationWithUsernames(usernames2, "Full party");
            conversationRegister.addNewConversationWithUsernames(usernames3, "");
        }catch (IllegalArgumentException exception){
            fail("Expected the test data to be added without problems since the input is valid format.");
        }catch (CouldNotAddConversationException exception){
            fail("Expected the test data to be added without problems since the conversations are not already made.");
        }catch (CouldNotAddMemberException exception){
            fail("Expected the test data to be added without problems since none inputs have multiple of the same usernames in the lists.");
        }
    }

    /**
     * Tests if addNewConversationWithUsernames works with invalid usernames.
     */
    @Test
    @DisplayName("Tests if addNewConversationWithUsernames works with invalid usernames.")
    public void testIfAddNewConversationWithUsernamesWorksWithInvalidUsernames(){
        try {
            List<Member> list = new ArrayList<>();
            conversationRegister.addNewConversationWithUsernames(list, "");
            fail("Expected to get a IllegalArgumentException since the list size is zero.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotAddConversationException | CouldNotAddMemberException exception){
            fail("Expected to get a IllegalArgumentException since the list size is zero.");
        }
    }

    /**
     * Tests if addNewConversationWithUsernames works with invalid conversation name.
     */
    @Test
    @DisplayName("Tests if addNewConversationWithUsernames works with invalid conversation name.")
    public void testIfAddNewConversationWithUsernamesWorksWithInvalidConversationName(){
        try {
            List<Member> list = new ArrayList<>();
            list.add(new ConversationMember("bjarne32"));
            list.add(new ConversationMember("bass"));
            conversationRegister.addNewConversationWithUsernames(list, null);
            fail("Expected to get a IllegalArgumentException since the name is set to null.");
        }catch (CouldNotAddConversationException | CouldNotAddMemberException exception){
            fail("Expected to get a IllegalArgumentException since the name is set to null.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if addNewConversationWithUsernames works with valid input.
     */
    @Test
    @DisplayName("Tests if addNewConversationWithUsernames works with valid input.")
    public void testIfAddNewConversationWithUsernamesWorksWithValidInput(){
        try {
            List<Member> list = new ArrayList<>();
            list.add(new ConversationMember("bjarne32"));
            list.add(new ConversationMember("bass"));
            conversationRegister.addNewConversationWithUsernames(list, "");
            assertTrue(true);
        }catch (IllegalArgumentException | CouldNotAddConversationException | CouldNotAddMemberException exception){
            fail("Expected the conversation to be made since the input is valid.");
        }
    }

    /**
     * Tests if getConversationByNumber works with invalid number.
     */
    @Test
    @DisplayName("Tests if getConversationByNumber works with invalid number.")
    public void testIfGetConversationByNumberWorksWithInvalidNumber(){
        try {
            conversationRegister.getConversationByNumber(-1);
            fail("Expected to get a IllegalArgumentException since the input is invalid.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotGetConversationException exception){
            fail("Expected to get a IllegalArgumentException since the input is invalid.");
        }
    }

    /**
     * Tests if getConversationByNumber works with valid input.
     */
    @Test
    @DisplayName("Tests if getConversationByNumber works with valid input.")
    public void testIfGetConversationByNumberWorksWithValidInput(){
        try {
            conversationRegister.getConversationByNumber(1L);
            assertTrue(true);
        }catch (IllegalArgumentException exception){
            fail("Expected to get a conversation back since the input format is valid.");
        }catch (CouldNotGetConversationException exception){
            fail("Expected to get a conversation back since its in the register.");
        }
    }

    /**
     * Tests if getConversationByNumber works with input not in the register.
     */
    @Test
    @DisplayName("Tests if getConversationByNumber works with input not in the register.")
    public void testIfGetConversationByNumberWorksWithInputNotInRegister(){
        try {
            conversationRegister.getConversationByNumber(12);
            fail("Expected to get a CouldNotGetConversationException since there is not 13 conversations in the register.");
        }catch (IllegalArgumentException exception){
            fail("Expected to get a CouldNotGetConversationException since the input is valid format.");
        }catch (CouldNotGetConversationException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if removeConversation works with invalid input.
     */
    @Test
    @DisplayName("Tests if removeConversation works with invalid input.")
    public void testIfRemoveConversationWorksWithInvalidInput(){
        try {
            NormalConversationRegister normalConversationRegister = (NormalConversationRegister) conversationRegister;
            normalConversationRegister.removeConversation(null);
            fail("Expected to get a IllegalArgumentException since the input is invalid format.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotRemoveConversationException exception){
            fail("Expected to get a IllegalArgumentException since the input is invalid format.");
        }
    }

    /**
     * Tests if removeConversation works with valid input.
     */
    @Test
    @DisplayName("Tests if removeConversation works with valid input.")
    public void testIfRemoveConversationWorksWithValidInput(){
        try {
            NormalConversationRegister normalConversationRegister = (NormalConversationRegister) conversationRegister;
            Conversation conversation = normalConversationRegister.getConversationByNumber(1);
            normalConversationRegister.removeConversation(conversation);
            assertTrue(true);
        }catch (IllegalArgumentException exception){
            fail("Expected the conversation to be removed since the input is valid format.");
        }catch (CouldNotGetConversationException | CouldNotRemoveConversationException exception){
            fail("Expected the conversation to be removed since its in the register.");
        }
    }

    /**
     * Tests if removeConversation works with a conversation not in the register.
     */
    @Test
    @DisplayName("Tests if removeConversation works with a conversation not in the register.")
    public void testIfRemoveConversationWorksWithConversationNotInRegister(){
        try {
            NormalConversationRegister normalConversationRegister = (NormalConversationRegister) conversationRegister;
            List<Member> list = new ArrayList<>();
            list.add(new ConversationMember("bjarne32"));
            list.add(new ConversationMember("bass"));
            Conversation conversation = new NormalServerConversation(4L, list);
            normalConversationRegister.removeConversation(conversation);
        } catch (CouldNotAddMemberException exception) {
            fail("Expected the test conversation to be made without problems since there is no duplicate usernames.");
        }catch (CouldNotRemoveConversationException exception){
            assertTrue(true);
        }catch (IllegalArgumentException exception){
            fail("Expected to get a CouldNotRemoveConversationException since the input is valid format.");
        }
    }
}
