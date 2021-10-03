package no.stonedstonar.chatappliation.model;

import no.stonedstonar.chatapplication.model.MessageLog;
import no.stonedstonar.chatapplication.model.TextMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Text;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 *
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
        }
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
        }
    }

    /**
     * Tests if removeMessage works with valid input.
     */
    @Test
    @DisplayName("Tests if removeMessage works with valid input.")
    public void testIfRemoveMessageWorksWithValidInput(){
        try {
            TextMessage textMessage = messageLog.getMessage("Hello its me");
            messageLog.removeMessage(textMessage);
            assertTrue(true);
        }catch (IllegalArgumentException exception){
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
            assertTrue(true);
        }
    }

    /**
     * Tests if getMessage works with invalid input.
     */
    @Test
    @DisplayName("Tests if getMessage works with invalid input.")
    public void testIfGetMessageWorksWithInvalidInput(){
        try {
            messageLog.getMessage(null);
            fail("Expected to get a exception since the input is null.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if getMessage works with valid input.
     */
    @Test
    @DisplayName("Tests if getMessage works with valid input.")
    public void testIfGetMessageWorksWithValidInput(){
        try {
            TextMessage textMessage = messageLog.getMessage("Hello its me");
            if (textMessage != null){
                assertTrue(true);
            }else {
                fail("Expected to get a message since the input is valid.");
            }
        }catch (IllegalArgumentException exception){
            fail("Expected to get a message since the input is valid.");
        }
    }

    /**
     * Tests if getMessage works with message not in log.
     */
    @Test
    @DisplayName("Tests if getMessage works with message not in log.")
    public void testIfGetMessageWorksWithMessageNotInLog(){
        try {
            TextMessage textMessage = messageLog.getMessage("Hello");
            fail("Exepcted to get a exception since the message is not in the log.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }
    }

}
