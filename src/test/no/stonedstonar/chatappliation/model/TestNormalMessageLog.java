package no.stonedstonar.chatappliation.model;

import no.stonedstonar.chatapplication.model.message.Message;
import no.stonedstonar.chatapplication.model.messagelog.NormalMessageLog;
import no.stonedstonar.chatapplication.model.message.TextMessage;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotAddMemberException;
import no.stonedstonar.chatapplication.model.exception.message.CouldNotAddMessageException;
import no.stonedstonar.chatapplication.model.exception.message.CouldNotGetMessageException;
import no.stonedstonar.chatapplication.model.exception.message.CouldNotRemoveMessageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Represents a testing class for MessageLog.
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class TestNormalMessageLog {

    private NormalMessageLog normalMessageLog;

    /**
     * Makes a test message log that can be used for testing.
     */
    @BeforeEach
    private void makeTestMessageLog(){
        try {
            normalMessageLog = new NormalMessageLog(301L);
            normalMessageLog.getMembersOfConversation().addMember("bjarne21");
            normalMessageLog.getMembersOfConversation().addMember("lordVader");
            normalMessageLog.addMessage(new TextMessage("Hello its me", "bjarne21"));
            normalMessageLog.addMessage(new TextMessage("Hey bjarne21 its time to join the darkside.", "lordVader"));
        }catch (IllegalArgumentException exception){
            fail("Expected the test message log to be made since all the input is valid.");
        }catch (CouldNotAddMessageException exception){
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
            normalMessageLog.addMessage(textMessage);
        }catch (IllegalArgumentException exception){
            fail("Expected the test message log to be made since all the input is valid.");
        }catch (CouldNotAddMessageException exception){
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
            NormalMessageLog normalMessageLog = new NormalMessageLog(-1L);
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
            NormalMessageLog normalMessageLog = new NormalMessageLog(1L);
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
            normalMessageLog.addMessage(null);
            fail("Expected to get a exception since the input is invalid.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotAddMessageException exception){
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
            normalMessageLog.addMessage(textMessage);
            normalMessageLog.addMessage(textMessage);
            fail("Expected to get a exception since the input is duplicated.");
        }catch (IllegalArgumentException exception){
            fail("Expected to get a CouldNotAddTextMessageException since the format is valid.");
        }catch (CouldNotAddMessageException exception){
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
            normalMessageLog.removeMessage(null);
            fail("Expected to get a exception since the input is null.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotRemoveMessageException exception){
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
            normalMessageLog.removeMessage(textMessage);
            assertTrue(true);
        }catch (IllegalArgumentException exception){
            fail("Expected the message to be removed since the input format is valid.");
        }catch (CouldNotRemoveMessageException exception) {
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
            normalMessageLog.removeMessage(message);
            fail("Expected to get a exception since the message is not a part of the message log.");
        }catch (IllegalArgumentException exception){
            fail("Expected to get a CouldNotRemoveTextMessageException since the format is valid.");
        }catch (CouldNotRemoveMessageException exception){
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
            normalMessageLog.getMessage(null, LocalTime.now(), LocalDate.now());
            fail("Expected to get a exception since the input is null.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotGetMessageException exception){
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
            normalMessageLog.getMessage("bjarne22", LocalTime.now(), null);
            fail("Expected to get a exception since the input is null.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotGetMessageException exception){
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
            normalMessageLog.getMessage("bjarne22", null, LocalDate.now());
            fail("Expected to get a exception since the time is null.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotGetMessageException exception){
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
            Message textMessage = normalMessageLog.getMessage(testMessage.getFromUsername(), testMessage.getTime(), testMessage.getDate());
            if (textMessage != null){
                assertTrue(true);
            }else {
                fail("Expected to get a message since the input is valid.");
            }
        }catch (IllegalArgumentException exception){
            fail("Expected to get a message since the input format is valid.");
        }catch (CouldNotGetMessageException exception){
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
            Message textMessage = normalMessageLog.getMessage(testMessage.getFromUsername(), testMessage.getTime(), testMessage.getDate());
            fail("Expected to get a exception since the message is not in the log.");
        }catch (IllegalArgumentException exception){
            fail("Expected to get a CouldNotGetTextMessageException since the format is valid.");
        }catch (CouldNotGetMessageException exception){
            assertTrue(true);
        }
    }

}
