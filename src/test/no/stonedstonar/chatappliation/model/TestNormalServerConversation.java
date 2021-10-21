package no.stonedstonar.chatappliation.model;

import no.stonedstonar.chatapplication.model.conversation.Conversation;
import no.stonedstonar.chatapplication.model.conversation.NormalServerConversation;
import no.stonedstonar.chatapplication.model.conversation.ServerConversation;
import no.stonedstonar.chatapplication.model.exception.conversation.UsernameNotPartOfConversationException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotGetMessageLogException;
import no.stonedstonar.chatapplication.model.message.TextMessage;
import no.stonedstonar.chatapplication.model.message.Message;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotAddMemberException;
import no.stonedstonar.chatapplication.model.exception.message.CouldNotAddMessageException;
import no.stonedstonar.chatapplication.model.exception.message.CouldNotRemoveMessageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Represents a testing class for MessageLog.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class TestNormalServerConversation {

    private ServerConversation testConversation;

    private String username;

    /**
     * Makes a test message log that can be used for testing.
     */
    @BeforeEach
    private void makeTestConversation(){
        try {
            testConversation = new NormalServerConversation(301L, makeUsernames());
            testConversation.addNewMessage(new TextMessage("Hello its me", "bjarne21"));
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
     * Adds a text message that can be used for testing.
     * @return the text message that matches that message.
     */
    private Message addTextMessageTextAndReturnIt(){
        Message textMessage = new TextMessage("Hello", "bjarne21");
        try {
            testConversation.addNewMessage(textMessage);
        }catch (IllegalArgumentException exception){
            fail("Expected the test message log to be made since all the input is valid.");
        }catch (CouldNotAddMessageException exception){
            fail("Expected all the text messages for the test message log to be added since its valid.");
        } catch (CouldNotGetMessageLogException exception) {
            fail("Expected the message log to be found since the date is valid.");
        } catch (UsernameNotPartOfConversationException e) {
            fail("Expected the username to be valid since they are part of the conversation.");
        }
        return textMessage;
    }

    /**
     * Tests if the constructor works with a negative input.
     */
    @Test
    @DisplayName("Tests if the constructor works with a negative input.")
    public void testIfConstructorWorksWithNegativeInput(){
        try {
            Conversation conversation = new NormalServerConversation(-1L, makeUsernames());
            fail("Expected to get a exception since the input is negative.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotAddMemberException exception){;
            fail("Expected to get a IllegalArgumentException since the input is invalid.");
        }
    }

    /**
     * Tests if constructor works with invalid list input.
     */
    @Test
    @DisplayName("Tests if constructor works with invalid list input.")
    public void testIfConstructorWorksWithInvalidUsernames(){
        try {
            Conversation conversation = new NormalServerConversation(1L, new ArrayList<>());
            fail("Expected to get a exception since the input list is zero in size.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotAddMemberException exception){
            fail("Expected to get a IllegalArgumentException since the input is invalid.");
        }
    }

    /**
     * Tests if the constructor works as intended with valid input.
     */
    @Test
    @DisplayName("Tests if the constructor works as intended with valid input.")
    public void testIfConstructorWorksWithValidInput(){
        try {
            Conversation conversation = new NormalServerConversation(1L, makeUsernames());
            assertTrue(true);
        }catch (IllegalArgumentException | CouldNotAddMemberException exception){
            fail("Expected to make a messagelog since the input is valid.");
        }
    }


    /**
     * Tests if addNewMessage works as intended with invalid input.
     */
    @Test
    @DisplayName("Tests if addMessage works as intended with invalid input.")
    public void testIfAddMessageWorksWithInvalidInput(){
        try {
            testConversation.addNewMessage(null);
            fail("Expected to get a exception since the input is invalid.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotAddMessageException | UsernameNotPartOfConversationException | CouldNotGetMessageLogException exception){
            fail("Expected to get a IllegalArgumentException since the input format is invalid.");
        }
    }

    /**
     * Tests if addNewMessage works with duplicate messages (the same object twice.)
     */
    @Test
    @DisplayName("Tests if addMessage works with duplicate messages (the same object twice.)")
    public void testIfAddMessageWorksWithDuplicateMessages(){
        try {
            Message textMessage = new TextMessage("Testing", "bjarne21");
            testConversation.addNewMessage(textMessage);
            testConversation.addNewMessage(textMessage);
            fail("Expected to get a exception since the input is duplicated.");
        }catch (IllegalArgumentException exception){
            fail("Expected to get a CouldNotAddTextMessageException since the format is valid.");
        }catch (CouldNotAddMessageException exception){
            assertTrue(true);
        } catch (CouldNotGetMessageLogException | UsernameNotPartOfConversationException exception) {
            fail("Expected the message log to be found since its a invalid date.");
        }
    }

    /**
     * Tests if addNewMessage works with valid input.
     */
    @Test
    @DisplayName("Tests if addNewMessage works with invalid date.")
    public void testIfAddNewMessageWorksWithValidInput(){
        try {
            Message message = new TextMessage("Testing", "bjarne21");
            testConversation.addNewMessage(message);
            assertTrue(true);
        } catch (CouldNotAddMessageException e) {
            fail("Expected the message to be added since its not already in the conversation.");
        } catch (CouldNotGetMessageLogException exception) {
            fail("Expected the message log to be found sine the format is valid.");
        }catch (IllegalArgumentException exception){
            fail("Expected the message to be added since the input format is valid. ");
        } catch (UsernameNotPartOfConversationException e) {
            fail("Expected the message to be added since the user is a part of the conversation.");
        }
    }

    /**
     * Tests if addAllMessagesWithSameDate works with invalid input dates.
     */
    @Test
    @DisplayName("Tests if addAllMessagesWithSameDate works with invalid input dates.")
    public void testIfAddAllMessagesWithSameDateWorksWithInvalidInput(){
        try {
            testConversation.addAllMessagesWithSameDate(null);
            fail("Expected to get a IllegalArgumentException since the input is invalid format.");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (CouldNotGetMessageLogException | UsernameNotPartOfConversationException |CouldNotAddMessageException exception) {
            fail("Expected to get a IllegalArgumentException since the input is invalid format.");
        }
    }

    /**
     * Tests if addAllMessagesWithSameDate works with one date invalid.
     */
    @Test
    @DisplayName("Tests if addAllMessagesWithSameDate works with one date invalid.")
    public void testIfAddAllMessagesWithSameDateWorksWithOneInvalidDate(){
        try {
            Message mess1 = new TextMessage("Hello", "bjarne21");
            Message mess2 = new TextMessage("Hi", "lordVader", LocalDate.of(2100, 1, 1));
            List<Message> messageList = new ArrayList<>();
            messageList.add(mess1);
            messageList.add(mess2);
            testConversation.addAllMessagesWithSameDate(messageList);
            fail("Expected to get a CouldNotAddMessageException since the dates are wrong.");
        }catch (IllegalArgumentException | CouldNotGetMessageLogException exception){
            System.out.println(exception.getMessage());
            fail("Expected to get a CouldNotAddMessageException since the dates are wrong.");
        }catch (CouldNotAddMessageException exception){
            assertTrue(true);
        } catch (UsernameNotPartOfConversationException e) {
            fail("Expected to get a CouldNotAddMessageException since the username is a part of the covnersation.");
        }
    }

    /**
     * Tests if addAllMessagesWithSameDate works with valid input.
     */
    @Test
    @DisplayName("Tests if addAllMessagesWithSameDate works with valid input.")
    public void testIfAddAllMessagesWithSameDateWorksWithValidInput(){
        try {
            Message mess1 = new TextMessage("Hello", "bjarne21");
            Message mess2 = new TextMessage("Hi", "lordVader");
            List<Message> messageList = new ArrayList<>();
            messageList.add(mess1);
            messageList.add(mess2);
            testConversation.addAllMessagesWithSameDate(messageList);
            assertTrue(true);
        }catch (IllegalArgumentException | CouldNotAddMessageException | CouldNotGetMessageLogException | UsernameNotPartOfConversationException exception){
            fail("Expected the messages to be added since the format is valid." + exception.getClass());
        }
    }

    /**
     * Tests if removeMessage works with invalid text message.
     */
    @Test
    @DisplayName("Tests if removeMessage works with invalid text message.")
    public void testIfRemoveMessageWorksWithInvalidMessage(){
        try {
            testConversation.removeMessage(null);
            fail("Expected to get a exception since the input is null.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotRemoveMessageException | UsernameNotPartOfConversationException | CouldNotGetMessageLogException exception){
            fail("Expected to get a IllegalArgumentException since the format is invalid.");
        }
    }

    /**
     * Tests if removeMessage works with message from one not in the conversation.
     */

    /**
     * Tests if removeMessage works with valid input.
     */
    @Test
    @DisplayName("Tests if removeMessage works with valid input.")
    public void testIfRemoveMessageWorksWithValidInput(){
        try {
            Message textMessage = addTextMessageTextAndReturnIt();
            testConversation.removeMessage(textMessage);
            assertTrue(true);
        }catch (IllegalArgumentException exception){
            fail("Expected the message to be removed since the input format is valid.");
        }catch (CouldNotRemoveMessageException | UsernameNotPartOfConversationException | CouldNotGetMessageLogException exception) {
            fail("Expected the message to be removed since the input is valid and the user is valid.");
        }
    }

    /**
     * Tests if removeMessage works with message not in log.
     */
    @Test
    @DisplayName("Tests if removeMessage works with message not in log.")
    public void testIfRemoveMessageWorksWithMessageNotInLog(){
        try {
            Message message = new TextMessage("Hello vader", "bjarne21");
            testConversation.removeMessage(message);
            fail("Expected to get a exception since the message is not a part of the message log.");
        }catch (IllegalArgumentException | CouldNotGetMessageLogException | UsernameNotPartOfConversationException exception){
            fail("Expected to get a CouldNotRemoveTextMessageException since the format is valid.");
        }catch (CouldNotRemoveMessageException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if checkForNewMessagesOnDate works with invalid format on date.
     */
    @Test
    @DisplayName("Tests if checkForNewMessagesOnDate works with invalid format on date.")
    public void testIfCheckForNewMessagesOnDateWorksWithInvalidFormatOnDate(){
        try {
            testConversation.checkForNewMessagesOnDate(null, 2L, username);
            fail("Expected to get a IllegalArgumentException since the input format is invalid.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotGetMessageLogException | UsernameNotPartOfConversationException exception){
            fail("Expected to get a IllegalArgumentException since the input format is invalid.");
        }
    }

    /**
     * Tests if checkForNewMessagesOnDate works with invalid date.
     */
    @Test
    @DisplayName("Tests if checkForNewMessagesOnDate works with invalid date.")
    public void testIfCheckForNewMessagesOnDateWorksWithInvalidDate(){
        try {
            testConversation.checkForNewMessagesOnDate(LocalDate.of(2100, 1, 1), 2L, username);
            fail("Expected to get a IllegalArgumentException since the input format is invalid.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotGetMessageLogException | UsernameNotPartOfConversationException exception){
            fail("Expected to get a IllegalArgumentException since the input format is invalid.");
        }
    }

    /**
     * Tests if checkForNewMessagesOnDate works with invalid long.
     */
    @Test
    @DisplayName("Tests if checkForNewMessagesOnDate works with invalid long.")
    public void testIfCheckForNewMessagesOnDateWorksWithInvalidLong(){
        try {
            testConversation.checkForNewMessagesOnDate(LocalDate.now(), -1L, username);
            fail("Expected to get a IllegalArgumentException since the input format is invalid.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotGetMessageLogException | UsernameNotPartOfConversationException exception){
            fail("Expected to get a IllegalArgumentException since the input format is invalid.");
        }
    }

    /**
     * Tests if checkForNewMessagesOnDate works with valid input.
     */
    @Test
    @DisplayName("Tests if checkForNewMessagesOnDate works with valid input.")
    public void testIfCheckForNewMessagesOnDateWorksWithValidInput(){
        try {
            List<Message> newMessages = testConversation.checkForNewMessagesOnDate(LocalDate.now(), 0L, username);
            if (newMessages.size() == 2){
                assertTrue(true);
            }else {
                fail("Expected the size of the returning map to be 2 and not " + newMessages.size());
            }
        }catch (IllegalArgumentException exception){
            fail("Expected the method to be executed since the input format is valid.");
        }catch (CouldNotGetMessageLogException | UsernameNotPartOfConversationException exception){
            fail("Expected to get a IllegalArgumentException since the input format is invalid.");
        }
    }
}
