package no.stonedstonar.chatappliation.model;

import no.stonedstonar.chatapplication.model.user.BasicEndUser;
import no.stonedstonar.chatapplication.model.userregister.NormalEndUserRegister;
import no.stonedstonar.chatapplication.model.exception.user.CouldNotAddUserException;
import no.stonedstonar.chatapplication.model.exception.user.CouldNotLoginToUserException;
import no.stonedstonar.chatapplication.model.exception.user.CouldNotRemoveUserException;
import no.stonedstonar.chatapplication.model.user.EndUser;
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
public class TestBasicEndNormalEndUserRegister {

    private NormalEndUserRegister normalEndUserRegister;

    private BasicEndUser basicEndUser1;

    /**
     * Makes a test user register that is used for testing.
     */
    @BeforeEach
    private void makeTestUserRegister(){
        try {
            normalEndUserRegister = new NormalEndUserRegister();
            basicEndUser1 = new BasicEndUser("bjarne21", "pass");
            normalEndUserRegister.addUser(basicEndUser1);
            normalEndUserRegister.addUser(new BasicEndUser("lordVoldemort", "password"));
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
            normalEndUserRegister.addUser(null);
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
            normalEndUserRegister.addUser(new BasicEndUser("frank21", "ass"));
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
            BasicEndUser basicEndUser = new BasicEndUser("åse20", "ås");
            normalEndUserRegister.addUser(basicEndUser);
            normalEndUserRegister.addUser(basicEndUser);
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
            normalEndUserRegister.removeUser(null);
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
            normalEndUserRegister.removeUser(basicEndUser1);
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
            BasicEndUser basicEndUser = new BasicEndUser("ar", "pass");
            normalEndUserRegister.removeUser(basicEndUser);
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
            normalEndUserRegister.login("", "pass");
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
            normalEndUserRegister.login("bjarne21", "");
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
            normalEndUserRegister.login("bjarne21", "p");
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
            EndUser endUser = normalEndUserRegister.login(basicEndUser1.getUsername(), "pass");
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
            normalEndUserRegister.checkIfUsernameIsTaken("");
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
            assertTrue(normalEndUserRegister.checkIfUsernameIsTaken(basicEndUser1.getUsername()));
        }catch (IllegalArgumentException exception){
            fail("Expected to get a true boolean back since the username is in the register.");
        }
    }
}
