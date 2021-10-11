package no.stonedstonar.chatappliation.model;

import no.stonedstonar.chatapplication.model.User;
import no.stonedstonar.chatapplication.model.UserRegister;
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

    private UserRegister userRegister;

    private User user1;

    /**
     * Makes a test user register that is used for testing.
     */
    @BeforeEach
    private void makeTestUserRegister(){
        try {
            userRegister = new UserRegister();
            user1 = new User("bjarne21", "pass");
            userRegister.addUser(user1);
            userRegister.addUser(new User("lordVoldemort", "password"));
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
            userRegister.addUser(null);
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
            userRegister.addUser(new User("frank21", "ass"));
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
            User user = new User("åse20", "ås");
            userRegister.addUser(user);
            userRegister.addUser(user);
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
            userRegister.removeUser(null);
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
            userRegister.removeUser(user1);
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
            User user = new User("ar", "pass");
            userRegister.removeUser(user);
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
            userRegister.login("", "pass");
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
            userRegister.login("bjarne21", "");
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
            userRegister.login("bjarne21", "p");
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
            User user = userRegister.login(user1.getUsername(), "pass");
            if (user != null){
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
            userRegister.checkIfUsernameIsTaken("");
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
            assertTrue(userRegister.checkIfUsernameIsTaken(user1.getUsername()));
        }catch (IllegalArgumentException exception){
            fail("Expected to get a true boolean back since the username is in the register.");
        }
    }
}
