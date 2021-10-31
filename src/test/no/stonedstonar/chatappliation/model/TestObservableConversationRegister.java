package no.stonedstonar.chatappliation.model;

import no.stonedstonar.chatapplication.model.conversation.NormalObservableConversation;
import no.stonedstonar.chatapplication.model.conversation.NormalServerConversation;
import no.stonedstonar.chatapplication.model.conversation.ObservableConversation;
import no.stonedstonar.chatapplication.model.conversation.ServerConversation;
import no.stonedstonar.chatapplication.model.conversationregister.ConversationRegister;
import no.stonedstonar.chatapplication.model.conversationregister.personal.NormalPersonalConversationRegister;
import no.stonedstonar.chatapplication.model.conversationregister.personal.PersonalConversationRegister;
import no.stonedstonar.chatapplication.model.exception.conversation.CouldNotAddConversationException;
import no.stonedstonar.chatapplication.model.exception.conversation.CouldNotGetConversationException;
import no.stonedstonar.chatapplication.model.exception.conversation.UsernameNotPartOfConversationException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotAddMemberException;
import no.stonedstonar.chatapplication.model.exception.message.CouldNotAddMessageException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotGetMessageLogException;
import no.stonedstonar.chatapplication.model.member.ConversationMember;
import no.stonedstonar.chatapplication.model.member.Member;
import no.stonedstonar.chatapplication.model.message.TextMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * A class that tests the personal conversation register.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class TestObservableConversationRegister {

    private PersonalConversationRegister personalConversationRegister;

    private String username;

    /**
     * Makes the personal test conversation.
     */
    @BeforeEach
    private void makeTestPersonalConversation(){
        personalConversationRegister = new NormalPersonalConversationRegister(makeServerConversation(), "bjarne21");
    }

    /**
     * Makes a server conversation. Can be used for testing.
     * @return the list with a server conversation.
     */
    private List<ServerConversation> makeServerConversation(){
        List<ServerConversation> serverConversations = new ArrayList<>();
        try {
            ServerConversation testConversation = new NormalServerConversation(1L, makeMembers());
            testConversation.addNewMessage(new TextMessage("Hello its me", "bjarne21"));
            testConversation.addNewMessage(new TextMessage("Hey bjarne21 its time to join the darkside.", "lordVader"));
            serverConversations.add(testConversation);
        }catch (IllegalArgumentException exception){
            System.out.println(exception.getMessage());
            fail("Expected the test message log to be made since all the input is valid.");
        }catch (CouldNotAddMessageException exception){
            fail("Expected all the text messages for the test message log to be added since they are valid.");
        }catch (CouldNotAddMemberException exception){
            fail("Expected all the members for the test message log to be added since the format is valid.");
        } catch (CouldNotGetMessageLogException exception) {
            fail("Expected the message log to be gotten since the date is valid.");
        } catch (UsernameNotPartOfConversationException e) {
            fail("Expected the messages to be added since they are a part of the conversation.");
        }
        return serverConversations;
    }



    /**
     * Makes a basic list with members of a conversation.
     * @return a list with some usernames.
     */
    private List<Member> makeMembers(){
        List<Member> usernames = new ArrayList<>();
        username = "bjarne21";
        usernames.add(new ConversationMember(username));
        usernames.add(new ConversationMember("lordVader"));
        return usernames;
    }

    /**
     * Tests if constructor works with invalid normalMessageLogList.
     */
    @Test
    @DisplayName("Tests if constructor works with invalid normalMessageLogList.")
    public void testIfConstructorWorksWithInvalidNormalMessageLogList(){
        try{
            ConversationRegister conversationRegister = new NormalPersonalConversationRegister(null, "bjarne22");
            fail("Expected to get a IllegalArgumentException since the input is invalid.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if constructor works with invalid username.
     */
    @Test
    @DisplayName("Tests if constructor works with invalid username.")
    public void testIfConstructorWorksWithInvalidUsername(){
        try {
            PersonalConversationRegister personalConversationRegister1 = new NormalPersonalConversationRegister(makeServerConversation(), "");
            fail("Expected to get a IllegalArgumentException since the input is invalid format.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if constructor works with valid input.
     */
    @Test
    @DisplayName("Tests if constructor works with valid input.")
    public void testIfConstructorWorksWithValidInput(){
        try {
            PersonalConversationRegister personalConversationRegister = new NormalPersonalConversationRegister(makeServerConversation(), username);
            assertTrue(true);
        }catch (IllegalArgumentException exception){
            fail("Expected the conversation register to be made since its valid format.");
        }
    }

    /**
     * Tests if addConversation works with invalid conversation.
     */
    @Test
    @DisplayName("Tests if addConversation works with invalid conversation.")
    public void testIfAddConversationWorksWithInvalidConversation(){
        try{
            personalConversationRegister.addConversation(null);
            fail("Expected to get a IllegalArgumentException since the input is invalid format.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotAddConversationException exception){
            fail("Expected to get a IllegalArgumentException since the input is invalid format.");
        }
    }

    /**
     * Tests if addConversation works with valid username.
     */
    @Test
    @DisplayName("Tests if addConversation works with valid username.")
    public void testIfAddConversationWorksWithValidUsername(){
        try {
            PersonalConversationRegister personalConversationRegister = new NormalPersonalConversationRegister(new ArrayList<>(), "bjarne22");
            ObservableConversation observableConversation = new NormalObservableConversation(makeServerConversation().get(0), "bjarne21");
            personalConversationRegister.addConversation(observableConversation);
            assertTrue(true);
        } catch (CouldNotAddConversationException | IllegalArgumentException exception) {
            fail("Expected the personal conversation to be added since the input is valid.");
        }
    }

    /**
     * Tests if getConversationByNumber works with invalid conversation number.
     */
    @Test
    @DisplayName("Tests if getConversationByNumber works with invalid conversation number.")
    public void testIfGetConversationByNumberWorksWithInvalidConversationNumber(){
        try {
            personalConversationRegister.getConversationByNumber(-1L );
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
            personalConversationRegister.getConversationByNumber(1L);
            assertTrue(true);
        }catch (IllegalArgumentException | CouldNotGetConversationException exception){
            fail("Expected to get a conversation since the input is valid.");
        }
    }

    /**
     * Tests if getConversationByNumber works with conversation number not in register.
     */
    @Test
    @DisplayName("Tests if getConversationByNumber works with conversation number not in register.")
    public void testIfGetConversationByNumberWorksWithConversationNotInRegister(){
        try {
            personalConversationRegister.getConversationByNumber(3L);
            fail("Expected to get a CouldNotGetConversationException since the input conversation number is above original value.");
        }catch (IllegalArgumentException exception) {
            fail("Expected to get a CouldNotGetConversationException since the input conversation number is above original value.");
        }catch (CouldNotGetConversationException exception){
            assertTrue(true);
        }
    }

}
