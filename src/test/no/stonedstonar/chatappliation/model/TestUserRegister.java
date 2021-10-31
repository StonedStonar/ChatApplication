package no.stonedstonar.chatappliation.model;

import no.stonedstonar.chatapplication.model.user.EndUser;
import no.stonedstonar.chatapplication.model.user.User;
import no.stonedstonar.chatapplication.model.userregister.NormalUserRegister;
import no.stonedstonar.chatapplication.model.exception.user.CouldNotAddUserException;
import no.stonedstonar.chatapplication.model.exception.user.CouldNotLoginToUserException;
import no.stonedstonar.chatapplication.model.exception.user.CouldNotRemoveUserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Represents a testing class of the UserRegister.
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class TestUserRegister {

    private NormalUserRegister normalUserRegister;

    private EndUser endUser1;

    /**
     * Makes a test user register that is used for testing.
     */
    @BeforeEach
    private void makeTestUserRegister(){
        try {
            normalUserRegister = new NormalUserRegister();
            endUser1 = new EndUser("bjarne21", "pass");
            normalUserRegister.addUser(endUser1);
            normalUserRegister.addUser(new EndUser("lordVoldemort", "password"));
        }catch (IllegalArgumentException exception){
            fail("Expected the test user register to be made without any errors.");
        }catch (CouldNotAddUserException exception){
            fail("Expected the test users to be added since they are not in the register already.");
        }
    }

    /**
     * Tests if addUser works with invalid input.
     */
    @Test
    @DisplayName("Tests if addUser works with invalid input.")
    public void testIfAddUserWorksWithInvalidInput(){
        try {
            normalUserRegister.addUser(null);
            fail("Expected to get a exception since the input is null");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotAddUserException exception){
            fail("Expected to get a IllegalArgumentException since the user is not in the register already.");
        }
    }

    /**
     * Tests if addUser works with valid input.
     */
    @Test
    @DisplayName("Tests if addUser works with valid input.")
    public void testIfAddUserWorksWithValidInput(){
        try {
            normalUserRegister.addUser(new EndUser("frank21", "ass"));
            assertTrue(true);
        }catch (IllegalArgumentException exception){
            fail("Expected to add the user since the input is valid.");
        }catch (CouldNotAddUserException exception){
            fail("Expected the user to be added since they are not in the register already.");
        }
    }

    /**
     * Tests if addUser works with duplicate user.
     */
    @Test
    @DisplayName("Tests if addUser works with duplicate user.")
    public void testIfAddUserWorksWithDuplicateUser(){
        try {
            EndUser endUser = new EndUser("åse20", "ås");
            normalUserRegister.addUser(endUser);
            normalUserRegister.addUser(endUser);
            fail("Expected to get a exception since the user is already in the register.");
        }catch (IllegalArgumentException exception){
            fail("Expected to get a CouldNotAddUserException since the format is valid.");
        }catch (CouldNotAddUserException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if removeUser works with null as input.
     */
    @Test
    @DisplayName("Tests if removeUser works with null as input.")
    public void testIfRemoveUserWorksWithNullAsInput(){
        try {
            normalUserRegister.removeUser(null);
            fail("Expected to get a exception since the input is null");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotRemoveUserException exception){
            fail("Expected to get a IllegalArgumentException sine the format is invalid.");
        }
    }

    /**
     * Tests if removeUser works with valid input.
     */
    @Test
    @DisplayName("Tests if removeUser works with valid input.")
    public void testIfRemoveUserWorksWithValidInput(){
        try{
            normalUserRegister.removeUser(endUser1);
            assertTrue(true);
        }catch (IllegalArgumentException exception){
            fail("Expected the user to be removed since the input is valid.");
        }catch (CouldNotRemoveUserException exception){
            fail("Expected the user to be removed since they are in the register.");
        }
    }

    /**
     * Tests if removeUser works with user not in register.
     */
    @Test
    @DisplayName("Tests if removeUser works with user not in register.")
    public void testIfRemoveUserWorksWithUserNotInRegister(){
        try {
            EndUser endUser = new EndUser("ar", "pass");
            normalUserRegister.removeUser(endUser);
            fail("Expected to get a exception since the input user is not in the register.");
        }catch (IllegalArgumentException exception){
            fail("Expected to get a CouldNotRemoveUserException since the format is valid.");
        }catch (CouldNotRemoveUserException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if login works with invalid username.
     */
    @Test
    @DisplayName("Tests if login works with invalid username.")
    public void testIfLoginWorksWithInvalidUsername(){
        try {
            normalUserRegister.login("", "pass");
            fail("Expected to get a exception since the input username is invalid.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotLoginToUserException exception){
            fail("Expected to get a IllegalArgumentException since the format is invalid.");
        }
    }

    /**
     * Tests if login works with invalid format on password.
     */
    @Test
    @DisplayName("Tests if login works with invalid format on password.")
    public void testIfLoginWorksWithInvalidFormatOnPassword(){
        try {
            normalUserRegister.login("bjarne21", "");
            fail("Expected to get a exception since the input password is invalid.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotLoginToUserException exception){
            fail("Expected to get a IllegalArgumentException since the format is invalid.");
        }
    }

    /**
     * Tests if login works with wrong password.
     */
    @Test
    @DisplayName("Tests if login works with wrong password.")
    public void testIfLoginWorksWithWrongPassword(){
        try {
            normalUserRegister.login("bjarne21", "p");
            fail("Expected to get a exception since the passwords does not match");
        }catch (IllegalArgumentException exception){
            fail("Expected to get a CouldNotLoginToUserException since the format is valid.");
        }catch (CouldNotLoginToUserException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if login works with valid input.
     */
    @Test
    @DisplayName("Tests if login works with valid input.")
    public void testIfLoginWorksWithValidInput(){
        try {
            User endUser = normalUserRegister.login(endUser1.getUsername(), "pass");
            if (endUser != null){
                assertTrue(true);
            }else {
                fail("Expected the user to be logged in since the input is valid.");
            }
        }catch (IllegalArgumentException exception){
            fail("Expected the user to be logged in since the input is valid.");
        }catch (CouldNotLoginToUserException exception){
            fail("Expected the user to be logged in since they are in the register.");
        }
    }

    /**
     * Tests if checkIfUsernameIsTaken works with invalid input.
     */
    @Test
    @DisplayName("Tests if checkIfUsernameIsTaken works with invalid input.")
    public void testIfCheckIfUsernameIsTakenWorksWithInvalidInput(){
        try {
            normalUserRegister.checkIfUsernameIsTaken("");
            fail("Expected to get a exception since the input is invalid.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if checkIfUsernameIsTaken works with valid input.
     */
    @Test
    @DisplayName("Tests if checkIfUsernameIsTaken works with valid input.")
    public void testIfCheckIfUsernameIsTakenWorksWithValidInput(){
        try {
            assertTrue(normalUserRegister.checkIfUsernameIsTaken(endUser1.getUsername()));
        }catch (IllegalArgumentException exception){
            fail("Expected to get a true boolean back since the username is in the register.");
        }
    }
}
