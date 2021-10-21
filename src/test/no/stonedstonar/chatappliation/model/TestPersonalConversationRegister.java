package no.stonedstonar.chatappliation.model;

import no.stonedstonar.chatapplication.model.conversation.ServerConversation;
import no.stonedstonar.chatapplication.model.conversationregister.ConversationRegister;
import no.stonedstonar.chatapplication.model.conversationregister.NormalPersonalConversationRegister;
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
public class TestPersonalConversationRegister {

    /**
     * Tests if constructor works with invalid normalMessageLogList.
     */
    @Test
    @DisplayName("Tests if constructor works with invalid normalMessageLogList.")
    public void testIfConstructorWorksWithInvalidNormalMessageLogList(){
        try{
            List<ServerConversation> serverConversationList = new ArrayList<>();
            ConversationRegister conversationRegister = new NormalPersonalConversationRegister(serverConversationList, "");
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

    }

    /**
     * Tests if constructor works with valid input.
     */
    @Test
    @DisplayName("Tests if constructor works with valid input.")
    public void testIfConstructorWorksWithValidInput(){

    }

    /**
     * Tests if addConversation works with invalid conversation.
     */
    @Test
    @DisplayName("Tests if addConversation works with invalid conversation.")
    public void testIfAddConversationWorksWithInvalidConversation(){

    }

    /**
     * Tests if addConversation works with invalid username.
     */
    @Test
    @DisplayName("Tests if addConversation works with invalid username.")
   public void testIfAddConversationWorksWithInvalidUsername(){

    }

    /**
     * Tests if addConversation works with valid username.
     */
    @Test
    @DisplayName("Tests if addConversation works with valid username.")
    public void testIfAddConversationWorksWithValidUsername(){

    }

    /**
     * Tests if getConversationByNumber works with invalid conversation number.
     */
    @Test
    @DisplayName("Tests if getConversationByNumber works with invalid conversation number.")
    public void testIfGetConversationByNumberWorksWithInvalidConversationNumber(){

    }

    /**
     * Tests if getConversationByNumber works with valid input.
     */
    @Test
    @DisplayName("Tests if getConversationByNumber works with valid input.")
    public void testIfGetConversationByNumberWorksWithValidInput(){

    }

    /**
     * Tests if getConversationByNumber works with conversation number not in register.
     */
    @Test
    @DisplayName("Tests if getConversationByNumber works with conversation number not in register.")
    public void testIfGetConversationByNumberWorksWithConversationNotInRegister(){
        
    }

}
