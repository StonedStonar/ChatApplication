package no.stonedstonar.chatappliation.model;

import no.stonedstonar.chatapplication.model.membersregister.ConversationMembers;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotAddMemberException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotRemoveMemberException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Represents a testing class for the Members of a conversation.
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class TestConversationMembers {

    private ConversationMembers conversationMembers;

    /**
     * Makes a MembersOfConversation that can be used for testing.
     */
    @BeforeEach
    private void makeMembersOfConversation(){
        conversationMembers = new ConversationMembers();
        try{
            conversationMembers.addMember("bjarne21");
            conversationMembers.addMember("ironman2019");
        }catch (IllegalArgumentException exception){
            fail("Could not add two members since we get a format error.");
        }catch (CouldNotAddMemberException exception){
            fail("Could not add two members since the add method does not work.");
        }
    }

    /**
     * Tests if addMember works with invalid username.
     */
    @Test
    @DisplayName("Tests if addMember works with invalid username.")
    public void testIfAddMemberWorksWithInvalidUsername(){
        try {
            conversationMembers.addMember("");
            fail("Expected to get a exception since the input is invalid.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotAddMemberException exception){
            fail("Expected to get a IllegalArgumentException since the format is invalid and not a " + exception.getClass());
        }
    }

    /**
     * Tests if addMember works with valid input.
     */
    @Test
    @DisplayName("Tests if addMember works with valid input.")
    public void testIfAddMemberWorksWithValidInput(){
        try {
            conversationMembers.addMember("inge");
            assertTrue(true);
        }catch (IllegalArgumentException | CouldNotAddMemberException exception){
            fail("Expected the user to be added since the input is valid and correct format.");
        }
    }


    /**
     * Tests if addMember works with user already in the object.
     */
    @Test
    @DisplayName("Tests if addMember works with user already in the object.")
    public void testIfAddMemberWorksWithDuplicate(){
        try {
            conversationMembers.addMember("bjarne21");
            fail("Expected to get a exception since bjarne21 is already in the register.");
        }catch (IllegalArgumentException exception){
            fail("Expected tog et a CouldNotAddMemberException since the format is valid.");
        }catch (CouldNotAddMemberException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if addAllMembers works with invalid input.
     */
    @Test
    @DisplayName("Tests if addAllMembers works with invalid input.")
    public void testIfAddAllMembersWorksWithInvalidInput(){
        try {
            conversationMembers.addAllMembers(null);
            fail("Expected to get a exception since the input is null.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotAddMemberException exception){
            fail("Expected to get a IllegalArgumentException since the format is invalid.");
        }
    }

    /**
     * Tests if addAllMembers works with list size zero.
     */
    @Test
    @DisplayName("Tests if addAllMembers works with list size zero.")
    public void testIfAddAllMembersWorksWithListSizeZero(){
        try {
            conversationMembers.addAllMembers(new ArrayList<>());
            fail("Expected to get a exception since the input list is size zero.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotAddMemberException exception){
            fail("Expected to get a IllegalArgumentException since the format is invalid.");
        }
    }

    /**
     * Tests if addAllMembers works with valid input.
     */
    @Test
    @DisplayName("Tests if addAllMembers works with valid input")
    public void testIfAddAllMembersWorksWithValidInput(){
        try {
            List<String> list = new ArrayList<>();
            list.add("lordVader");
            list.add("lordPoop");
            conversationMembers.addAllMembers(list);
            assertTrue(conversationMembers.getAmountOfMembers() == 4);
        }catch (IllegalArgumentException | CouldNotAddMemberException exception){
            fail("Expected the users to be added since the input is valid.");
        }
    }

    /**
     * Tests if addAllMembers works with one duplicate member.
     */
    @Test
    @DisplayName("Tests if addAllMembers works with one duplicate member.")
    public void testIfAddAllMembersWorksWithOneDuplicateMember(){
        try {
            List<String> list = new ArrayList<>();
            list.add("lordVader");
            list.add("bjarne21");
            conversationMembers.addAllMembers(list);
            fail("Expected to get a exception since one member by the name bjarne21 is a part of this conversation already.");
        }catch (IllegalArgumentException exception){
            fail("Expected to get a CouldNotAddMemberException since the format is correct.");
        }catch (CouldNotAddMemberException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if removeMember works with invalid username.
     */
    @Test
    @DisplayName("Tests if removeMember works with invalid username.")
    public void testIfRemoveMemberWorksWithInvalidUsername(){
        try {
            conversationMembers.removeMember("");
            fail("Expected to get a execption since the input username is invalid format");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotRemoveMemberException exception){
            fail("Expected to get a IllegalArgumentException since the format is invalid and not a " + exception.getClass());
        }
    }

    /**
     * Tests if removeMember works with valid input.
     */
    @Test
    @DisplayName("Tests if removeMember works with valid input.")
    public void testIfRemoveMemberWorksWithValidInput(){
        try {
            conversationMembers.removeMember("bjarne21");
            assertTrue(true);
        }catch (IllegalArgumentException  exception){
            fail("Expected the member to be removed since the format is valid.");
        }catch (CouldNotRemoveMemberException exception){
            fail("Expected the member to be removed since they are a part of this conversation.");
        }
    }

    /**
     * Tests if removeMember works with member not in register.
     */
    @Test
    @DisplayName("Tests if removeMember works with member not in register.")
    public void testIfRemoveMemberWorksWithMemberNotInConversation(){
        try {
            conversationMembers.removeMember("bjarne22");
            fail("Expected to get a exception since bjarne22 is not a part of the register.");
        }catch (IllegalArgumentException exception){
            fail("Expected to get a CouldNotRemoveMemberException since the format is valid.");
        }catch (CouldNotRemoveMemberException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if checkIfUsernamesAreInConversation works with invalid input.
     */
    @Test
    @DisplayName("Tests if checkIfUsernamesAreInConversation works with invalid input.")
    public void testIfCheckIfUsernamesAreInConversationWorksWithInvalidInput(){
        try {
            conversationMembers.checkIfUsernamesAreInConversation(null);
            fail("Expected to get a exception since the input value is null.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if checkIfUsernamesAreInConversation works with list with zero objects.
     */
    @Test
    @DisplayName("Tests if checkIfUsernamesAreInConversation works with list with zero objects.")
    public void testIfCheckIfUsernamesAreInConversationWorksWithListWithZeroObjects(){
        try {
            List<String> list = new ArrayList<>();
            conversationMembers.checkIfUsernamesAreInConversation(list);
            fail("Expected to get a exception since the list is empty.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if checkIfUsernamesAreInConversation works with valid input.
     */
    @Test
    @DisplayName("Tests if checkIfUsernamesAreInConversation works with valid input.")
    public void testIfCheckIfUsernamesAreInConversationWorksWithValidInput(){
        try {
            List<String> list = new ArrayList<>();
            list.add("bjarne21");
            list.add("ironman2019");
            boolean valid = conversationMembers.checkIfUsernamesAreInConversation(list);
            assertTrue(valid);
        }catch (IllegalArgumentException exception){
            fail("Expected to get a boolean value that is true since the input is valid.");
        }
    }

    /**
     * Tests if checkIfUsernamesAreInConversation works with members not in register.
     */
    @Test
    @DisplayName("Tests if checkIfUsernamesAreInConversation works with members not in register.")
    public void testIfCheckIfUsernamesAreInConversationWorksWithMembersNotInRegister(){
        try {
            List<String> list = new ArrayList<>();
            list.add("bjarne21");
            list.add("thor11");
            boolean valid = conversationMembers.checkIfUsernamesAreInConversation(list);
            assertFalse(valid);
        }catch (IllegalArgumentException exception){
            fail("Expected to get a false boolean back since one of the members in the input is not a part of this conversation.");
        }
    }
}
