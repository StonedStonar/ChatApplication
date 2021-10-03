package no.stonedstonar.chatappliation.model;

import no.stonedstonar.chatapplication.model.MessageLog;
import no.stonedstonar.chatapplication.model.TextMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Text;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Represents a testing class for text message.
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class TestTextMessage {

    /**
     * Tests if a message can be made with invalid message.
     */
    @Test
    @DisplayName("Tests if a message can be made with invalid message.")
    public void testIfAMessageCanBeMadeWithInvalidMessage(){
        try {
            TextMessage message = new TextMessage("", "bjarne21");
            fail("Expected to get a IllegalArgumentException since the input is valid.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if a massage can be made with invalid fromUsername.
     */
    @Test
    @DisplayName("Tests if a massage can be made with invalid fromUsername.")
    public void testIfAMessageCanBeMadeWithInvalidFromUsername(){
        try {
            TextMessage message = new TextMessage("", "bjarne21");
            fail("Expected to get a IllegalArgumentException since the input is valid.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if a message can be made with valid input.
     */
    @Test
    @DisplayName("Tests if a message can be made with valid input.")
    public void testIfMessageCanBeMadeWithValidInput(){
        try {
            TextMessage textMessage = new TextMessage("Hello", "bjarne21");
            assertTrue(true);
        }catch (IllegalArgumentException exception){
            fail("Expected the message to be made since the input is valid.");
        }
    }
}
