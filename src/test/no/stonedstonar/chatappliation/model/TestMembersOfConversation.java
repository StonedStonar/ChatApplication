package no.stonedstonar.chatappliation.model;

import no.stonedstonar.chatapplication.model.MembersOfConversation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class TestMembersOfConversation {

    private MembersOfConversation membersOfConversation;

    /**
     * Makes a MembersOfConversation that can be used for testing.
     */
    @BeforeEach
    private void makeMembersOfConversation(){
        membersOfConversation = new MembersOfConversation();
        try{
            membersOfConversation.addMember("bjarne21");
            membersOfConversation.addMember("ironman2019");
        }catch (IllegalArgumentException exception){
            fail("Could not add two members for testing.");
        }
    }

    /**
     * Tests if addMember works with invalid username.
     */
    @Test
    @DisplayName("Tests if addMember works with invalid username.")
    public void testIfAddMemberWorksWithInvalidUsername(){
        try {
            membersOfConversation.addMember("");
            fail("Expected to get a exception since the input is invalid.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if addMember works with valid input.
     */
    @Test
    @DisplayName("Tests if addMember works with valid input.")
    public void testIfAddMemberWorksWithValidInput(){
        try {
            membersOfConversation.addMember("inge");
            assertTrue(true);
        }catch (IllegalArgumentException exception){
            fail("Expected the user to be added since the input is valid.");
        }
    }


    /**
     * Tests if addMember works with user already in the object.
     */
    @Test
    @DisplayName("Tests if addMember works with user already in the object.")
    public void testIfAddMemberWorksWithDuplicate(){
        try {
            membersOfConversation.addMember("bjarne21");
            fail("Expected to get a exception since bjarne21 is already in the register.");
        }catch (IllegalArgumentException exception){
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
            membersOfConversation.addAllMembers(null);
            fail("Expected to get a exception since the input is null.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if addAllMembers works with list size zero.
     */
    @Test
    @DisplayName("Tests if addAllMembers works with list size zero.")
    public void testIfAddAllMembersWorksWithListSizeZero(){
        try {
            membersOfConversation.addAllMembers(new ArrayList<>());
            fail("Expected to get a exception since the input list is size zero.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
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
            membersOfConversation.addAllMembers(list);
            assertTrue(membersOfConversation.getAmountOfMembers() == 4);
        }catch (IllegalArgumentException exception){
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
            membersOfConversation.addAllMembers(list);
            fail("Expected to get a exception since one member by the name bjarne21 is a part of this conversation already.");
        }catch (IllegalArgumentException exception){
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
            membersOfConversation.removeMember("");
            fail("Expected to get a execption since the input username is invalid format");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if removeMember works with valid input.
     */
    @Test
    @DisplayName("Tests if removeMember works with valid input.")
    public void testIfRemoveMemberWorksWithValidInput(){
        try {
            membersOfConversation.removeMember("bjarne21");
            assertTrue(true);
        }catch (IllegalArgumentException exception){
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
            membersOfConversation.removeMember("bjarne22");
            fail("Expected to get a exception since bjarne22 is not a part of the register.");
        }catch (IllegalArgumentException exception){
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
            membersOfConversation.checkIfUsernamesAreInConversation(null);
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
            membersOfConversation.checkIfUsernamesAreInConversation(list);
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
            boolean valid = membersOfConversation.checkIfUsernamesAreInConversation(list);
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
            boolean valid = membersOfConversation.checkIfUsernamesAreInConversation(list);
            assertFalse(valid);
        }catch (IllegalArgumentException exception){
            fail("Expected to get a false boolean back since one of the members in the input is not a part of this conversation.");
        }
    }
}
