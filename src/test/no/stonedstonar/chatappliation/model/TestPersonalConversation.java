package no.stonedstonar.chatappliation.model;

import no.stonedstonar.chatapplication.backend.Server;
import no.stonedstonar.chatapplication.model.User;
import no.stonedstonar.chatapplication.model.conversation.NormalPersonalConversation;
import no.stonedstonar.chatapplication.model.conversation.NormalServerConversation;
import no.stonedstonar.chatapplication.model.conversation.PersonalConversation;
import no.stonedstonar.chatapplication.model.conversation.ServerConversation;
import no.stonedstonar.chatapplication.model.exception.conversation.UsernameNotPartOfConversationException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotAddMemberException;
import no.stonedstonar.chatapplication.model.exception.message.CouldNotAddMessageException;
import no.stonedstonar.chatapplication.model.exception.message.CouldNotRemoveMessageException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotGetMessageLogException;
import no.stonedstonar.chatapplication.model.message.Message;
import no.stonedstonar.chatapplication.model.message.TextMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * A class that tests methods in the PersonalConversation class.
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class TestPersonalConversation {


    private PersonalConversation personalConversation;

    private String username;

    private Message message;

    /**
     * Makes a personal conversation for testing.
     */
    @BeforeEach
    private void makePersonalConversation(){
        personalConversation = new NormalPersonalConversation(makeServerConversation(), username);
    }

    /**
     * Makes a server conversation used for testing.
     * @return a server conversation.
     */
    private ServerConversation makeServerConversation(){
        ServerConversation testConversation = null;
        try {
            testConversation = new NormalServerConversation(301L, makeUsernames());
            message = new TextMessage("Hello its me", "bjarne21");
            testConversation.addNewMessage(message);
            testConversation.addNewMessage(new TextMessage("Hey bjarne21 its time to join the darkside.", "lordVader"));

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
        return testConversation;
    }

    /**
     * Makes a basic list with members of a conversation.
     * @return a list with some usernames.
     */
    private List<String> makeUsernames(){
        List<String> usernames = new ArrayList<>();
        username = "bjarne21";
        usernames.add(username);
        usernames.add("lordVader");
        return usernames;
    }
    /**
     * Tests if the constructor works with invalid ServerConversation.
     */
    @Test
    @DisplayName("Tests if the constructor works with invalid ServerConversation.")
    public void testIfConstructorWorksWithInvalidServerConversation(){
        try {
            PersonalConversation personalConversation = new NormalPersonalConversation(null, username);
            fail("Expected to get a IllegalArgumentException since the server conversation is null.");
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
            PersonalConversation personalConversation = new NormalPersonalConversation(makeServerConversation(), "");
            fail("Expected to get a IllegalArgumentException since the username is null.");
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
            PersonalConversation personalConversation = new NormalPersonalConversation(makeServerConversation(), username);
            assertTrue(true);
        }catch (IllegalArgumentException exception){
            fail("Expected the conversation to be made since the format is valid.");
        }
    }

    /**
     * Tests if setConversationName works with invalid input.
     */
    @Test
    @DisplayName("Tests if setConversationName works with invalid input.")
    public void testIfSetConversationNameWorksWithInvalidInput(){
        try {
            personalConversation.setConversationName("");
            fail("Expected to get a IllegalArgumentException since the format is invalid.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if setConversationName works with valid input.
     */
    @Test
    @DisplayName("Tests if setConversationName works with valid input.")
    public void testIfSetConversationNameWorksWithValidInput(){
        try {
            personalConversation.setConversationName("test2");
            assertTrue(true);
        }catch (IllegalArgumentException exception){
            fail("Expected the name to be changed since the format is invalid.");
        }
    }

    /**
     * Tests if checkForMessageLogByDate works with invalid input.
     */
    @Test
    @DisplayName("Tests if checkForMessageLogByDate works with invalid input.")
    public void testIfCheckForMessageLogByDateWorksWithInvalidInput(){
        try {
            personalConversation.checkForMessageLogByDate(null);
            fail("Expected to get a IllegalArgumentException since the input is invalid.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if checkForMessageLogByDate works with valid input.
     */
    @Test
    @DisplayName("Tests if checkForMessageLogByDate works with valid input.")
    public void testIfCheckForMessageLogByDateWorksWithValidInput(){
        try {
            personalConversation.checkForMessageLogByDate(LocalDate.now());
            assertTrue(true);
        }catch (IllegalArgumentException exception){
            fail("Expected to get a value back since the input is valid.");
        }
    }

    /**
     * Tests if addNewMessage works with invalid input.
     */
    @Test
    @DisplayName("Tests if addNewMessage works with invalid input")
    public void testIfAddNewMessageWorksWithInvalidInput(){
        try {
            personalConversation.addNewMessage(null);
            fail("Expected to get a IllegalArgumentException since the input is invalid format.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        } catch (UsernameNotPartOfConversationException | CouldNotAddMessageException | CouldNotGetMessageLogException e) {
            fail("Expected to get a IllegalArgumentException since the input is invalid format.");
        }
    }

    /**
     * Tests if addNewMessage works with valid message.
     */
    @Test
    @DisplayName("Tests if addNewMessage works with valid message.")
    public void testIfAddNewMessageWorksWithValidMessage(){
        try {
            personalConversation.addNewMessage(new TextMessage("asd", username));
            assertTrue(true);
        }catch (IllegalArgumentException | UsernameNotPartOfConversationException | CouldNotAddMessageException | CouldNotGetMessageLogException exception){
            fail("Expected the masse to be added since its valid format.");
        }
    }

    /**
     * Tests if addNewMessage works with duplicate messages.
     */
    @Test
    @DisplayName("Tests if addNewMessage works with duplicate messages.")
    public void testIfAddNewMessageWorksWithDuplicateMessages(){
        try {
            Message message = new TextMessage("Test", username);
            personalConversation.addNewMessage(message);
            personalConversation.addNewMessage(message);
            fail("Expected to get a CouldNotAddMessageException since the message is already in the register.");
        }catch (CouldNotAddMessageException exception){
            assertTrue(true);
        }catch (IllegalArgumentException | UsernameNotPartOfConversationException | CouldNotGetMessageLogException exception){
            fail("Expected to get a CouldNotAddMessageException since the message is already in the register.");
        }
    }

    /**
     * Tests if removeMessage works with invalid input.
     */
    @Test
    @DisplayName("Tests if removeMessage works with invalid input.")
    public void testIfRemoveMessageWorksWithInvalidInput(){
        try {
            personalConversation.removeMessage(null);
            fail("Expected to get a IllegalArgumentException since the input is valid.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotRemoveMessageException | CouldNotGetMessageLogException | UsernameNotPartOfConversationException exception){
            fail("Expected to get a IllegalArgumentException since the input is valid.");
        }
    }

    /**
     * Tests if removeMessage works with valid input.
     */
    @Test
    @DisplayName("Tests if removeMessage works with valid input.")
    public void testIfRemoveMessageWorksWithValidInput(){
        try {
            personalConversation.removeMessage(message);
            assertTrue(true);
        }catch (IllegalArgumentException | CouldNotRemoveMessageException | UsernameNotPartOfConversationException | CouldNotGetMessageLogException exception){
            fail("Expected the message to removed since the input is valid.");
        }
    }

    /**
     * Tests if removeMessage works with message not in conversation.
     */
    @Test
    @DisplayName("Tests if removeMessage works with message not in conversation.")
    public void testIfRemoveMessageWorksWithMessageNotInConversation(){
        try {
            Message messageToRemove = new TextMessage("ASASD", "bjarne21");
            personalConversation.removeMessage(messageToRemove);
            fail("Expected to get a CouldNotRemoveMessageException since the message is not in the conversation.");
        }catch (CouldNotRemoveMessageException exception){
            assertTrue(true);
        }catch (IllegalArgumentException | UsernameNotPartOfConversationException | CouldNotGetMessageLogException exception){
            fail("Expected to get a CouldNotRemoveMessageException since the message is not in the conversation.");
        }
    }

    /**
     * Tests if addMessagesWithSameDate works with invalid list.
     */
    @Test
    @DisplayName("Tests if addMessagesWithSameDate works with invalid list.")
    public void testIfAddMessageWithSameDateWorksWithInvalidList(){
        try {
            personalConversation.addAllMessagesWithSameDate(new ArrayList<>());
            fail("Expected to get a IllegalArgumentException since the input is invalid.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotAddMessageException | UsernameNotPartOfConversationException | CouldNotGetMessageLogException exception){
            fail("Expected to get a IllegalArgumentException since the input is invalid.");
        }
    }

    /**
     * Tests if addMessagesWithSameDate works with valid input.
     */
    @Test
    @DisplayName("Tests if addMessagesWithSameDate works with valid input.")
    public void testIfAddMessagesWithSameDateWorksWithValidInput(){
        try {
            List<Message> messageList = new ArrayList<>();
            Message mess = new TextMessage("TEst2", "bjarne21");
            Message mess2 = new TextMessage("Test3", "bjarne21");
            messageList.add(mess);
            messageList.add(mess2);
            personalConversation.addAllMessagesWithSameDate(messageList);
            assertTrue(true);
        }catch (IllegalArgumentException | CouldNotAddMessageException | UsernameNotPartOfConversationException | CouldNotGetMessageLogException exception){
            fail("Expected the messages to be added since the input is valid.");
        }
    }

    /**
     * Tests if addMessagesWithSameDate works with one invalid date.
     */
    @Test
    @DisplayName("Tests if addMessagesWithSameDate works with one invalid date.")
    public void testIfAddMessagesWithSameDateWorksWithInvalidDate(){
        try {
            List<Message> messageList = new ArrayList<>();
            Message mess = new TextMessage("TEst2", "bjarne21");
            Message mess2 = new TextMessage("Test3", "bjarne21", LocalDate.of(2019, 01, 01));
            messageList.add(mess);
            messageList.add(mess2);
            personalConversation.addAllMessagesWithSameDate(messageList);
            fail("Expected to get a CouldNotAddMessageException since one date is invalid.");
        }catch (IllegalArgumentException | CouldNotGetMessageLogException | UsernameNotPartOfConversationException exception){
            fail("Expected to get a CouldNotAddMessageException since one date is invalid.");
        }catch (CouldNotAddMessageException exception){
            assertTrue(true);
        }
    }

}
