package no.stonedstonar.chatappliation.model;

import no.stonedstonar.chatapplication.model.exception.message.CouldNotAddMessageException;
import no.stonedstonar.chatapplication.model.exception.message.CouldNotRemoveMessageException;
import no.stonedstonar.chatapplication.model.message.Message;
import no.stonedstonar.chatapplication.model.message.TextMessage;
import no.stonedstonar.chatapplication.model.messagelog.MessageLog;
import no.stonedstonar.chatapplication.model.messagelog.NormalServerMessageLog;
import no.stonedstonar.chatapplication.model.messagelog.ServerMessageLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests the normal message log.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class TestNormalServerMessageLog {

    private ServerMessageLog testMessageLog;

    private Message testMessage;

    /**
     * Makes a test message log.
     */
    @BeforeEach
    private void makeTestMessageLog(){
        try {
            testMessageLog = new NormalServerMessageLog(LocalDate.now());
            Message message = new TextMessage("Hei", "bjarne21");
            testMessage= new TextMessage("Hello", "lordVader");
            testMessageLog.addMessage(message);
            testMessageLog.addMessage(testMessage);
        }catch (IllegalArgumentException exception){
            fail("Expected the messages to be added since the input is valid.");
        }catch (CouldNotAddMessageException exception){
            fail("Expected the messages to be added since they are not already in the register.");
        }
    }

    /**
     * Tests if constructor works with invalid date format.
     */
    @Test
    @DisplayName("Tests if constructor works with invalid date format.")
    public void testIfConstructorWorksWithInvalidDateFormat(){
        try {
            MessageLog messageLog = new NormalServerMessageLog(null);
            fail("Expected to get a IllegalLArgumentException since the input is invalid format.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if constructor works with valid date.
     */
    @Test
    @DisplayName("Tests if constructor works with valid date.")
    public void testIfConstructorWorksWithValidInput(){
        try {
            MessageLog messageLog = new NormalServerMessageLog(LocalDate.now());
            assertTrue(true);
        }catch (IllegalArgumentException exception){
            fail("Expected the message log to be made since the input is valid.");
        }
    }

    /**
     * Tests if addMessage works with invalid input.
     */
    @Test
    @DisplayName("Tests if addMessage works with invalid input.")
   public void testIfAddMessageWorksWithInvalidInput(){
        try {
            testMessageLog.addMessage(null);
            fail("Expected to get a IllegalArgumentException since the input is invalid format.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotAddMessageException exception){
            fail("Expected to get a IllegalArgumentException since the input is invalid.");
        }
    }

    /**
     * Tests if addMessage works with valid input.
     */
    @Test
    @DisplayName("Tests if addMessage works with valid input.")
    public void testIfAddMessageWorksWithValidInput(){
        try {
            Message message = new TextMessage("Jadda", "bjarne21");
            testMessageLog.addMessage(message);
            assertTrue(true);
        }catch (IllegalArgumentException | CouldNotAddMessageException exception){
            fail("Expected the message to be added since the input is valid format and not already in the log.");
        }
    }

    /**
     * Tests if addMessage works with duplicate messages.
     */
    @Test
    @DisplayName("Tests if addMessage works with duplicate messages.")
    public void testIfAddMessageWorksWithDuplicateMessages(){
        try {
            Message message = new TextMessage("Jadda", "bjarne21");
            testMessageLog.addMessage(message);
            testMessageLog.addMessage(message);
            fail("Expected to get a CouldNotAddMessageException since the text message is already in the log.");
        }catch (IllegalArgumentException exception){
            fail("Expected to get a CouldNotAddMessageException since format is valid.");
        }catch (CouldNotAddMessageException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if removeMessage works with invalid input.
     */
    @Test
    @DisplayName("Tests if removeMessage works with invalid input.")
    public void testIfRemoveMessageWorksWithInvalidInput(){
        try {
            testMessageLog.removeMessage(null);
            fail("Expected to get a IllegalArgumentException since the format is invalid.");
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
           testMessageLog.removeMessage(testMessage);
           assertTrue(true);
        }catch (IllegalArgumentException | CouldNotRemoveMessageException exception){
            fail("Expected the message to be removed since the input is valid and the message is in the log.");
        }
    }

    /**
     * Tests if removeMessage works with message not in message log.
     */
    @Test
    @DisplayName("Tests if removeMessage works with message not in message log.")
    public void testIfRemoveMessageWorksWithMessageNotInMessageLog(){
         try {
             Message message = new TextMessage("Hei", "bjarne21");
             testMessageLog.removeMessage(message);
             fail("Expected to get a CouldNotRemoveMessageException since the message is not in the log.");
         }catch (IllegalArgumentException exception){
             fail("Expected to get a CouldNotRemoveMessageException since the format is valid.");
         }catch (CouldNotRemoveMessageException exception){
             assertTrue(true);
         }
    }

    /**
     * Tests if checkForNewMessages works with invalid input.
     */
    @Test
    @DisplayName("Tests if checkForNewMessages works with invalid input.")
    public void testIfCheckForMessagesWorksWithInvalidInput(){
        try {
            testMessageLog.checkForNewMessages(-1);
            fail("Expected to get a IllegalArgumentException since the input is negative.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if checkForNewMessages works with valid input.
     */
    @Test
    @DisplayName("Tests if checkForNewMessages works with valid input.")
    public void testIfCheckForNewMessagesWorksWithValidInput(){
        try {
            Message message = new TextMessage("ss", "bjarne21");
            testMessageLog.addMessage(message);
            List<Message> newMessages = testMessageLog.checkForNewMessages(1L);
            if (newMessages.size() == 2){
                assertTrue(true);
            }else {
                fail("Expected the size of the new messages to be 2 and not " + newMessages.size());
            }
        }catch (IllegalArgumentException exception){
            fail("Expected the methods to work since the inputs are valid.");
        }catch (CouldNotAddMessageException exception){
            fail("Expected the message to be added since its not already in the log.");
        }
    }
}
