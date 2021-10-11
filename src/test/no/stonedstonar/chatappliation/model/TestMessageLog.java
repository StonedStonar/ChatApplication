package no.stonedstonar.chatappliation.model;

import no.stonedstonar.chatapplication.model.MessageLog;
import no.stonedstonar.chatapplication.model.TextMessage;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotAddMemberException;
import no.stonedstonar.chatapplication.model.exception.textmessage.CouldNotAddTextMessageException;
import no.stonedstonar.chatapplication.model.exception.textmessage.CouldNotGetTextMessageException;
import no.stonedstonar.chatapplication.model.exception.textmessage.CouldNotRemoveTextMessageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Represents a testing class for MessageLog.
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class TestMessageLog {

    private MessageLog messageLog;

    /**
     * Makes a test message log that can be used for testing.
     */
    @BeforeEach
    private void makeTestMessageLog(){
        try {
            messageLog = new MessageLog(301L);
            messageLog.getMembersOfConversation().addMember("bjarne21");
            messageLog.getMembersOfConversation().addMember("lordVader");
            messageLog.addMessage(new TextMessage("Hello its me", "bjarne21"));
            messageLog.addMessage(new TextMessage("Hey bjarne21 its time to join the darkside.", "lordVader"));
        }catch (IllegalArgumentException exception){
            fail("Expected the test message log to be made since all the input is valid.");
        }catch (CouldNotAddTextMessageException exception){
            fail("Expected all the text messages for the test message log to be added since they are valid.");
        }catch (CouldNotAddMemberException exception){
            fail("Expected all the members for the test message log to be added since the format is valid.");
        }
    }

    /**
     * Adds a text message that can be used for testing.
     * @return the text message that matches that message.
     */
    private TextMessage addTextMessageTextAndReturnIt(){
        TextMessage textMessage = new TextMessage("Hello", "bjarne22");
        try {
            messageLog.addMessage(textMessage);
        }catch (IllegalArgumentException exception){
            fail("Expected the test message log to be made since all the input is valid.");
        }catch (CouldNotAddTextMessageException exception){
            fail("Expected all the text messages for the test message log to be added since its valid.");
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
            MessageLog messageLog = new MessageLog(-1L);
            fail("Expected to get a exception since the input is negative.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if the constructor works as intended with valid input.
     */
    @Test
    @DisplayName("Tests if the constructor works as intended with valid input.")
    public void testIfConstructorWorksWithValidInput(){
        try {
            MessageLog messageLog = new MessageLog(1L);
            assertTrue(true);
        }catch (IllegalArgumentException exception){
            fail("Expected to make a messagelog since the input is valid.");
        }
    }

    /**
     * Tests if addMessage works as intended with invalid input.
     */
    @Test
    @DisplayName("Tests if addMessage works as intended with invalid input.")
    public void testIfAddMessageWorksWithInvalidInput(){
        try {
            messageLog.addMessage(null);
            fail("Expected to get a exception since the input is invalid.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotAddTextMessageException exception){
            fail("Expected to get a IllegalArgumentException since the input format is invalid.");
        }
    }

    /**
     * Tests if addMessage works with duplicate messages (the same object twice.)
     */
    @Test
    @DisplayName("Tests if addMessage works with duplicate messages (the same object twice.)")
    public void testIfAddMessageWorksWithDuplicateMessages(){
        try {
            TextMessage textMessage = new TextMessage("Testing", "bjarne21");
            messageLog.addMessage(textMessage);
            messageLog.addMessage(textMessage);
            fail("Expected to get a exception since the input is duplicated.");
        }catch (IllegalArgumentException exception){
            fail("Expected to get a CouldNotAddTextMessageException since the format is valid.");
        }catch (CouldNotAddTextMessageException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if removeMessage works with invalid text message.
     */
    @Test
    @DisplayName("Tests if removeMessage works with invalid text message.")
    public void testIfRemoveMessageWorksWithInvalidMessage(){
        try {
            messageLog.removeMessage(null);
            fail("Expected to get a exception since the input is null.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotRemoveTextMessageException exception){
            fail("Expected to get a IllegalArgumentException since the format is invalid.");
        }
    }

    /**
     * Tests if removeMessage works with valid input.
     */
    @Test
    @DisplayName("Tests if removeMessage works with valid input.")
    public void testIfRemoveMessageWorksWithValidInput(){
        try {
            TextMessage textMessage = addTextMessageTextAndReturnIt();
            messageLog.removeMessage(textMessage);
            assertTrue(true);
        }catch (IllegalArgumentException exception){
            fail("Expected the message to be removed since the input format is valid.");
        }catch (CouldNotRemoveTextMessageException exception) {
            fail("Expected the message to be removed since the input is valid.");
        }
    }

    /**
     * Tests if removeMessage works with message not in log.
     */
    @Test
    @DisplayName("Tests if removeMessage works with message not in log.")
    public void testIfRemoveMessageWorksWithMessageNotInLog(){
        try {
            TextMessage message = new TextMessage("Hello vader", "bjarne21");
            messageLog.removeMessage(message);
            fail("Expected to get a exception since the message is not a part of the message log.");
        }catch (IllegalArgumentException exception){
            fail("Expected to get a CouldNotRemoveTextMessageException since the format is valid.");
        }catch (CouldNotRemoveTextMessageException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if getMessage works with invalid form username.
     */
    @Test
    @DisplayName("Tests if getMessage works with invalid input.")
    public void testIfGetMessageWorksWithInvalidFromUsername(){
        try {
            messageLog.getMessage(null, LocalTime.now(), LocalDate.now());
            fail("Expected to get a exception since the input is null.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotGetTextMessageException exception){
            fail("Expected to get a IllegalArgumentException since the format is invalid.");
        }
    }

    /**
     * Tests if getMessage works with invalid date.
     */
    @Test
    @DisplayName("Tests if getMessage works with invalid date.")
    public void testIfGetMessageWorksWithInvalidDate(){
        try {
            messageLog.getMessage("bjarne22", LocalTime.now(), null);
            fail("Expected to get a exception since the input is null.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotGetTextMessageException exception){
            fail("Expected to get a IllegalArgumentException since the format is invalid.");
        }
    }

    /**
     * Tests if getMessage works with invalid time.
     */
    @Test
    @DisplayName("Tests if getMessage works with invalid time.")
    public void testIfGetMessageWorksWithInvalidTime(){
        try {
            messageLog.getMessage("bjarne22", null, LocalDate.now());
            fail("Expected to get a exception since the time is null.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotGetTextMessageException exception){
            fail("Expected to get a IllegalArgumentException since the format is invalid.");
        }
    }

    /**
     * Tests if getMessage works with valid input.
     */
    @Test
    @DisplayName("Tests if getMessage works with valid input.")
    public void testIfGetMessageWorksWithValidInput(){
        try {
            TextMessage testMessage = addTextMessageTextAndReturnIt();
            TextMessage textMessage = messageLog.getMessage(testMessage.getFromUsername(), testMessage.getTime(), testMessage.getDate());
            if (textMessage != null){
                assertTrue(true);
            }else {
                fail("Expected to get a message since the input is valid.");
            }
        }catch (IllegalArgumentException exception){
            fail("Expected to get a message since the input format is valid.");
        }catch (CouldNotGetTextMessageException exception){
            fail("Expected to get the message since the input is valid and the message is in the log.");
        }
    }

    /**
     * Tests if getMessage works with message not in log.
     */
    @Test
    @DisplayName("Tests if getMessage works with message not in log.")
    public void testIfGetMessageWorksWithMessageNotInLog(){
        try {
            TextMessage testMessage = new TextMessage("Jadda", "bjarne22");
            TextMessage textMessage = messageLog.getMessage(testMessage.getFromUsername(), testMessage.getTime(), testMessage.getDate());
            fail("Expected to get a exception since the message is not in the log.");
        }catch (IllegalArgumentException exception){
            fail("Expected to get a CouldNotGetTextMessageException since the format is valid.");
        }catch (CouldNotGetTextMessageException exception){
            assertTrue(true);
        }
    }

}
