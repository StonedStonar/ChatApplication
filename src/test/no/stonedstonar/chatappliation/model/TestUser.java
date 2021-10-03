package no.stonedstonar.chatappliation.model;

import no.stonedstonar.chatapplication.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Represents a testing class for user.
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class TestUser {

    private User testUser;

    /**
     * Makes a user that is used for testing.
     */
    @BeforeEach
    private void makeTestUser(){
        try {
            testUser = new User("bjarne21", "pass");
        }catch (IllegalArgumentException exception){
            fail("Expected the test user to be made since the input is valid.");
        }
    }

    /**
     * Tests if constructor works with invalid username.
     */
    @Test
    @DisplayName("Tests if constructor works with invalid username.")
    private void testIfConstructorWorksWithInvalidUsername(){
        try {
            User user = new User("", "pass");
            fail("Expected to get a exception since the username is invalid.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if constructor works with invalid password.
     */
    @Test
    @DisplayName("Tests if constructor works with invalid password.")
    public void testIfConstructorWorksWithInvalidPassword(){
        try {
            User user = new User("bjarne21", null);
            fail("Expected to get a exception since the password is null.");
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
            User user = new User("bjarne21", "pass");
            assertTrue(true);
        }catch (IllegalArgumentException exception){
            fail("Expected the user to be made since the input is valid.");
        }
    }

    /**
     * Tests if setUsername works with invalid username.
     */
    @Test
    @DisplayName("Tests if setUsername works with invalid input.")
    public void testIfSetNameWorksWithInvalidUsername(){
        try {
            testUser.setUsername("", "pass");
            fail("Expected to get a exception since the input is invalid.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if setName works with invalid password.
     */
    @Test
    @DisplayName("Tests if setUsername works with invalid password.")
    public void testIfSetNameWorksWithInvalidPassword(){
        try {
            testUser.setUsername("bjarne22", "bjarne21");
            fail("Expected to get a exception since the password is wrong format.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if setName works with valid input.
     */
    @Test
    @DisplayName("Tests if setUsername works with valid input.")
    public void testIfSetNameWorksWithValidInput(){
        try {
            testUser.setUsername("bjarne22", "pass");
            assertTrue(true);
        }catch (IllegalArgumentException exception){
            fail("Expected the username to be set to a new value since the input is valid.");
        }
    }

    /**
     * Tests if setPassword works with invalid new password.
     */
    @Test
    @DisplayName("Tests if setPassword works with invalid new password.")
    public void testIfSetPasswordWorksWithInvalidNewPassword(){
        try {
            testUser.setPassword("", "pass");
            fail("Expected to get a exception since the new password is invalid format.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if setPassword works with wrong old password.
     */
    @Test
    @DisplayName("Tests if setPassword works with wrong old password.")
    public void testIfSetPasswordWorksWithWrongOldPassword(){
        try {
            testUser.setPassword("password", "passs");
            fail("Expected to get a exception since the input old password is wrong.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if setPassword works with valid input.
     */
    @Test
    @DisplayName("Tests if setPassword works with valid input.")
    public void testIfSetPasswordWorksWithValidInput(){
        try {
            testUser.setPassword("password", "pass");
            assertTrue(true);
        }catch (IllegalArgumentException exception){
            fail("Expected the password to be changed since the input is valid.");
        }
    }
}
