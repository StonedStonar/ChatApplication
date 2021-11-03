package no.stonedstonar.chatappliation.model;

import no.stonedstonar.chatapplication.model.exception.conversation.UsernameNotPartOfConversationException;
import no.stonedstonar.chatapplication.model.member.ConversationMember;
import no.stonedstonar.chatapplication.model.member.Member;
import no.stonedstonar.chatapplication.model.membersregister.ConversationMembersRegister;
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

    private ConversationMembersRegister conversationMembers;

    private String memberUsername;

    private Member removeMember;

    /**
     * Makes a MembersOfConversation that can be used for testing.
     */
    @BeforeEach
    private void makeMembersOfConversation(){
        List<Member> members = new ArrayList<>();
        try{
            memberUsername = "bjarne21";
            removeMember = new ConversationMember("ironman2019");
            members.add(new ConversationMember("bjarne21"));
            members.add(removeMember);
            conversationMembers = new ConversationMembersRegister(members);
        }catch (IllegalArgumentException exception){
            fail("Could not add two members since we get a format error.");
        }
    }

    /**
     * Makes a list with members used in testing.
     * @return a list with members.
     */
    private List<Member> makeTestMembers(){
        List<Member> list = new ArrayList<>();
        list.add(new ConversationMember("lordVader"));
        list.add(new ConversationMember("lordPoop"));
        return list;
    }

    /**
     * Tests if addMember works with invalid member.
     */
    @Test
    @DisplayName("Tests if addMember works with invalid member.")
    public void testIfAddMemberWorksWithInvalidMember(){
        try {
            conversationMembers.addMember(new ConversationMember("bjarne21"), memberUsername);
            fail("Expected to get a exception since the member is already in the register.");
        }catch (CouldNotAddMemberException exception){
            assertTrue(true);
        }catch (IllegalArgumentException | UsernameNotPartOfConversationException exception){
            fail("Expected to get a CouldNotAddMemberException since the format is invalid and not a " + exception.getClass());
        }
    }

    /**
     * Tests if addMember works with invalid username.
     */
    @Test
    @DisplayName("Tests if addMember works with invalid username.")
    public void testIfAddMemberWorksWithInvalidUsername(){
        try {
            Member member = new ConversationMember("fjell");
            conversationMembers.addMember(member, "fjart");
            fail("Expected to get a UsernameNotPartOfConversationException since the format is invalid.");
        }catch (IllegalArgumentException | CouldNotAddMemberException exception){
            fail("Expected to get a UsernameNotPartOfConversationException since the format is invalid and not a " + exception.getClass());
        }catch (UsernameNotPartOfConversationException exception){
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
            Member member = new ConversationMember("fjell");
            conversationMembers.addMember(member, memberUsername);
            assertTrue(true);
        }catch (IllegalArgumentException | CouldNotAddMemberException | UsernameNotPartOfConversationException exception){
            fail("Expected the user to be added since the input is valid and correct format. " + exception.getClass());
        }
    }


    /**
     * Tests if addMember works with user already in the object.
     */
    @Test
    @DisplayName("Tests if addMember works with user already in the object.")
    public void testIfAddMemberWorksWithDuplicate(){
        try {
            Member member = new ConversationMember("ironman2019");
            conversationMembers.addMember(member, memberUsername);
            fail("Expected to get a exception since bjarne21 is already in the register.");
        }catch (IllegalArgumentException | UsernameNotPartOfConversationException exception){
            fail("Expected tog et a CouldNotAddMemberException since the format is valid and not a " + exception.getClass());
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
            conversationMembers.addAllMembers(null, memberUsername);
            fail("Expected to get a exception since the input is null.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotAddMemberException | UsernameNotPartOfConversationException exception){
            fail("Expected to get a IllegalArgumentException since the format is invalid and not a " + exception.getClass());
        }
    }


    /**
     * Tests if addAllMembers works with list size zero.
     */
    @Test
    @DisplayName("Tests if addAllMembers works with list size zero.")
    public void testIfAddAllMembersWorksWithListSizeZero(){
        try {
            conversationMembers.addAllMembers(new ArrayList<>(), memberUsername);
            fail("Expected to get a exception since the input list is size zero.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotAddMemberException | UsernameNotPartOfConversationException exception){
            fail("Expected to get a IllegalArgumentException since the format is invalid and not a " + exception.getClass());
        }
    }

    /**
     * Tests if addAllMembers works with invalid username.
     */
    @Test
    @DisplayName("Tests if addAllMembers works with invalid username.")
    public void testIfAddAllMembersWorksWithInvalidUsername(){
        try {
            conversationMembers.addAllMembers(makeTestMembers(), "");
            fail("Expected to get a IllegalArgumentException since the input is invalid.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotAddMemberException | UsernameNotPartOfConversationException exception){
            fail("Expected to get a IllegalArgumentException since the input username is invalid.");
        }
    }

    /**
     * Tests if addAllMembers works with username not in object.
     */
    @Test
    @DisplayName("Tests if addAllMembers works with username not in object.")
    public void testIfAddAllMembersWorksWithUsernameNotInObject(){
        try {
            conversationMembers.addAllMembers(makeTestMembers(), "testman");
            fail("Expected to get a UsernameNotPartOfConversationException since the input is invalid.");
        }catch (UsernameNotPartOfConversationException exception){
            assertTrue(true);
        }catch (CouldNotAddMemberException | IllegalArgumentException exception){
            fail("Expected to get a UsernameNotPartOfConversationException since the input username is invalid.");
        }
    }

    /**
     * Tests if addAllMembers works with valid input.
     */
    @Test
    @DisplayName("Tests if addAllMembers works with valid input")
    public void testIfAddAllMembersWorksWithValidInput(){
        try {
            conversationMembers.addAllMembers(makeTestMembers(), memberUsername);
            assertTrue(conversationMembers.getAmountOfMembers() == 4);
        }catch (IllegalArgumentException | CouldNotAddMemberException | UsernameNotPartOfConversationException exception){
            fail("Expected the users to be added since the input is valid. " + exception.getClass());
        }
    }

    /**
     * Tests if addAllMembers works with one duplicate member.
     */
    @Test
    @DisplayName("Tests if addAllMembers works with one duplicate member.")
    public void testIfAddAllMembersWorksWithOneDuplicateMember(){
        try {
            List<Member> list = new ArrayList<>();
            list.add(new ConversationMember("lordVader"));
            list.add(new ConversationMember("bjarne21"));
            conversationMembers.addAllMembers(list, memberUsername);
            fail("Expected to get a exception since one member by the name bjarne21 is a part of this conversation already.");
        }catch (IllegalArgumentException  | UsernameNotPartOfConversationException exception){
            fail("Expected to get a CouldNotAddMemberException since the format is correct and not a " + exception.getClass());
        }catch (CouldNotAddMemberException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if removeMember works with invalid member.
     */
    @Test
    @DisplayName("Tests if removeMember works with invalid member.")
    public void testIfRemoveMemberWorksWithInvalidMember(){
        try {
            conversationMembers.removeMember(new ConversationMember(null), memberUsername);
            fail("Expected to get a execution since the input username is invalid format");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotRemoveMemberException | UsernameNotPartOfConversationException exception){
            fail("Expected to get a IllegalArgumentException since the format is invalid and not a " + exception.getClass());
        }
    }

    /**
     * Tests if removeMember works with invalid username.
     */
    @Test
    @DisplayName("Tests if removeMember works with invalid username.")
    public void testIfRemoveMemberWorksWithInvalidUsername(){
        try {
            conversationMembers.removeMember(removeMember, "");
            fail("Expected to get a IllegalArgumentException since the input username is invalid.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotRemoveMemberException | UsernameNotPartOfConversationException exception){
            fail("Expected to get a IllegalArgumentException since the format is invalid and not a " + exception.getClass());
        }
    }

    /**
     * Tests if removeMember works with username not in object.
     */
    @Test
    @DisplayName("Tests if removeMember works with username not in object.")
    public void testIfRemoveMemberWorksWithUsernameNotInObject(){
        try {
            conversationMembers.removeMember(removeMember, "testman");
            fail("Expected to get a UsernameNotPartOfConversationException since the input username is not a part of this object.");
        }catch (UsernameNotPartOfConversationException exception){
            assertTrue(true);
        }catch (CouldNotRemoveMemberException | IllegalArgumentException exception){
            fail("Expected to get a UsernameNotPartOfConversationException since the useranme is not a part of this object. " + exception.getClass());
        }
    }


    /**
     * Tests if removeMember works with valid input.
     */
    @Test
    @DisplayName("Tests if removeMember works with valid input.")
    public void testIfRemoveMemberWorksWithValidInput(){
        try {
            conversationMembers.removeMember(removeMember, memberUsername);
            assertTrue(true);
        }catch (IllegalArgumentException  exception){
            fail("Expected the member to be removed since the format is valid.");
        }catch (CouldNotRemoveMemberException | UsernameNotPartOfConversationException exception){
            fail("Expected the member to be removed since they are a part of this conversation. " + exception.getClass());
        }
    }

    /**
     * Tests if removeMember works with member not in register.
     */
    @Test
    @DisplayName("Tests if removeMember works with member not in register.")
    public void testIfRemoveMemberWorksWithMemberNotInConversation(){
        try {
            conversationMembers.removeMember(new ConversationMember("asdasd"), memberUsername);
            fail("Expected to get a exception since bjarne22 is not a part of the register.");
        }catch (IllegalArgumentException | UsernameNotPartOfConversationException exception){
            fail("Expected to get a CouldNotRemoveMemberException since the format is valid and not a " + exception.getClass());
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
