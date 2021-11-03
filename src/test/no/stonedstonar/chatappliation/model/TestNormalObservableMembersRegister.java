package no.stonedstonar.chatappliation.model;

import no.stonedstonar.chatapplication.model.exception.conversation.UsernameNotPartOfConversationException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotAddMemberException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotGetMemberException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotRemoveMemberException;
import no.stonedstonar.chatapplication.model.member.ConversationMember;
import no.stonedstonar.chatapplication.model.member.Member;
import no.stonedstonar.chatapplication.model.membersregister.NormalMembersRegister;
import no.stonedstonar.chatapplication.model.membersregister.NormalObservableMemberRegister;
import no.stonedstonar.chatapplication.model.membersregister.ObservableMemberRegister;
import no.stonedstonar.chatapplication.model.membersregister.ServerMemberRegister;
import no.stonedstonar.chatapplication.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Represents a test class for ObservableConversationMembersRegister
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class TestNormalObservableMembersRegister {

    private ObservableMemberRegister observableMemberRegister;

    private String memberUsername;

    private Member removeMember;

    /**
     * Makes a MembersOfConversation that can be used for testing.
     */
    @BeforeEach
    private void makeMembersOfConversation(){
        observableMemberRegister = new NormalObservableMemberRegister(makeNormalConversationMemberRegister());
    }

    /**
     * Makes a new normal conversation members register.
     * @return a server members register.
     */
    private ServerMemberRegister makeNormalConversationMemberRegister(){
        NormalMembersRegister normalMembersRegister = null;
        try{
            List<Member> members = new ArrayList<>();
            memberUsername = "bjarne21";
            removeMember = new ConversationMember("ironman2019");
            members.add(new ConversationMember(memberUsername));
            members.add(removeMember);
            normalMembersRegister = new NormalMembersRegister(members);
        }catch (IllegalArgumentException exception){
            fail("Could not add two members since we get a format error.");
        }
        return normalMembersRegister;
    }

    /**
     * Makes a list with members used in testing.
     * @return a list with members.
     */
    private List<Member> makeTestMembers(){
        List<Member> list = new ArrayList<>();
        list.add(new ConversationMember("lordVader", 3));
        list.add(new ConversationMember("lordPoop", 4));
        return list;
    }

    /**
     * Makes and adds members to the members register.
     * @return the list with all the members that was added.
     */
    private List<Member> makeAndAddTestMembers(){
        List<Member> members = makeTestMembers();
        try {
            observableMemberRegister.addAllMembers(members, memberUsername);
        } catch (CouldNotAddMemberException | UsernameNotPartOfConversationException | IllegalArgumentException e) {
            fail("Expected all the members to be added since the input is valid.");
            System.out.println("EXCEPTION");
        }
        return members;
    }

    /**
     * Tests if the constructor works with invalid member register.
     */
    @Test
    @DisplayName("Tests if the constructor works with invalid member register.")
    public void testIfConstructorWorksWithInvalidMembersRegister(){
        try {
            ObservableMemberRegister observableMemberRegister = new NormalObservableMemberRegister(null);
            fail("Expected to get a IllegalArgumentException since the input is invalid.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if the constructor works with valid input.
     */
    @Test
    @DisplayName("Tests if the constructor works with valid input.")
    public void testIfConstructorWorksWithValidInput(){
        try {
            ObservableMemberRegister observableMemberRegister = new NormalObservableMemberRegister(makeNormalConversationMemberRegister());
            assertTrue(true);
        }catch (IllegalArgumentException exception){
            fail("Expected the members register to be made since the input is valid.");
        }
    }

    /**
     * Tests if addMember works with invalid member.
     */
    @Test
    @DisplayName("Tests if addMember works with invalid member.")
    public void testIfAddMemberWorksWithInvalidMember(){
        try {
            observableMemberRegister.addMember(null, memberUsername);
            fail("Expected to get a IllegalArgumentException since the input is invalid.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotAddMemberException | UsernameNotPartOfConversationException exception){
            fail("Expected to get a IllegalArgumentException since the input is invalid and not " + exception.getClass());
        }
    }

    /**
     * Tests if addMember works with invalid username.
     */
    @Test
    @DisplayName("Tests if addMember works with invalid username.")
    public void testIfAddMemberWorksWithInvalidUsername(){
        try {
            observableMemberRegister.addMember(new ConversationMember("fjells"), "");
            fail("Expected to get a IllegalArgumentException since the input is invalid.");
        }catch (IllegalArgumentException exception) {
            assertTrue(true);
        }catch (CouldNotAddMemberException | UsernameNotPartOfConversationException exception){
            fail("Expected to get a IllegalArgumentException since the input is invalid and not " + exception.getClass());
        }
    }

    /**
     * Tests if addMember works with username not in register.
     */
    @Test
    @DisplayName("Tests if addMember works with username not in register.")
    public void testIfAddMemberWorksWithUsernameNotInRegister(){
        try {
            observableMemberRegister.addMember(new ConversationMember("fjells"), "test2");
            fail("Expected to get a IllegalArgumentException since the input is invalid.");
        }catch (UsernameNotPartOfConversationException exception){
            assertTrue(true);
        }catch (IllegalArgumentException | CouldNotAddMemberException exception){
            fail("Expected to get a IllegalArgumentException since the input is invalid and not " + exception.getClass());
        }
    }

    /**
     * Tests if addMember works with valid input.
     */
    @Test
    @DisplayName("Tests if addMember works with valid input.")
    public void testIfAddMemberWorksWithValidInput(){
        try {
            observableMemberRegister.addMember(new ConversationMember("fjells"), memberUsername);
            assertTrue(true);
        }catch (IllegalArgumentException | UsernameNotPartOfConversationException | CouldNotAddMemberException exception){
            fail("Expected the member to be added since the input is valid " + exception.getClass());
        }
    }

    /**
     * Tests if addMember works with duplicate member.
     */
    @Test
    @DisplayName("Tests if addMember works with duplicate member.")
    public void testIfAddMemberWorksWithDuplicateMembers(){
        try {
            Member member = new ConversationMember("Fjells");
            observableMemberRegister.addMember(member, memberUsername);
            observableMemberRegister.addMember(member, memberUsername);
            fail("Expected to get a CouldNotAddMemberException since the member is already in the register.");
        }catch (CouldNotAddMemberException exception){
            assertTrue(true);
        }catch (IllegalArgumentException | UsernameNotPartOfConversationException exception){
            fail("Expected to get a CouldNotAddMemberException since the member is already in the register and not " + exception.getClass());
        }
    }

    /**
     * Tests if removeMember works with invalid member.
     */
    @Test
    @DisplayName("Tests if removeMember works with invalid member.")
    public void testIfRemoveMemberWorksWithInvalidMember(){
        try {
            observableMemberRegister.removeMember(null, memberUsername);
            fail("Expected to get a IllegalArgumentException since the input is invalid.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotRemoveMemberException | UsernameNotPartOfConversationException exception){
            fail("Expected to get a IllegalArgumentException since the input is invalid and not " + exception.getClass());
        }
    }

    /**
     * Tests if removeMember works with invalid username.
     */
    @Test
    @DisplayName("Tests if removeMember works with invalid username.")
    public void testIfRemoveMemberWorksWithInvalidUsername(){
        try {
            observableMemberRegister.removeMember(removeMember, "");
            fail("Expected to get a IllegalArgumentException since the input is invalid.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotRemoveMemberException | UsernameNotPartOfConversationException exception){
            fail("Expected to get a IllegalArgumentException since the input is invalid and not " + exception.getClass());
        }
    }

    /**
     * Tests if removeMember works with username not in register.
     */
    @Test
    @DisplayName("Tests if removeMember works with username not in register.")
    public void testIfRemoveMemberWorksWithUsernameNotInRegister(){
        try {
            observableMemberRegister.removeMember(removeMember, "bjarne55");
            fail("Expected to get a UsernameNotPartOfConversationException since the user is not a part of this register.");
        }catch (UsernameNotPartOfConversationException exception){
            assertTrue(true);
        }catch (IllegalArgumentException | CouldNotRemoveMemberException exception){
            fail("Expected to get a UsernameNotPartOfConversationException since the user is not a part of this register and not " + exception.getClass());
        }
    }

    /**
     * Tests if removeMember works with valid input.
     */
    @Test
    @DisplayName("Tests if removeMember works with valid input.")
    public void testIfRemoveMemberWorksWithValidInput(){
        try {
            observableMemberRegister.removeMember(removeMember, memberUsername);
            assertTrue(true);
        }catch (IllegalArgumentException | UsernameNotPartOfConversationException | CouldNotRemoveMemberException exception){
            fail("Expected the member to be removed since the input member is in the register and not a " + exception.getClass());
        }
    }

    /**
     * Tests if removeMember works with user not in register.
     */
    @Test
    @DisplayName("Tests if removeMember works with user not in register.")
    public void testIfRemoveMemberWorksWithUserNotInRegister(){
        try {
            observableMemberRegister.removeMember(new ConversationMember("fjells"), memberUsername);
            fail("Expected to get a CouldNotRemoveMemberException since the member is not in the register.");
        }catch (CouldNotRemoveMemberException exception){
            assertTrue(true);
        }catch (IllegalArgumentException | UsernameNotPartOfConversationException exception){
            fail("Expected to get a CouldNotRemoveMemberException since the member is not in the register and not " + exception.getClass());
        }
    }

    /**
     * Tests if addAllMembers works with invalid list.
     */
    @Test
    @DisplayName("Tests if addAllMembers works with invalid list.")
    public void testIfAddAllMembersWorksWithInvalidList(){
        try {
            observableMemberRegister.addAllMembers(new ArrayList<>(), memberUsername);
            fail("Expected to get a IllegalArgumentException since the input is invalid.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotAddMemberException | UsernameNotPartOfConversationException exception){
            fail("Expected to get a IllegalArgumentException since the input is invalid and not " + exception.getClass());;
        }
    }

    /**
     * Tests if addAllMembers works with invalid username.
     */
    @Test
    @DisplayName("Tests if addAllMembers works with invalid username.")
    public void testIfAddAllMEmbersWorksWithInvalidUsername(){
        try {
            observableMemberRegister.addAllMembers(makeTestMembers(), null);
            fail("Expected to get a IllegalArgumentException since the input is invalid.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotAddMemberException | UsernameNotPartOfConversationException exception){
            fail("Expected to get a IllegalArgumentException since the input is invalid and not " + exception.getClass());;
        }
    }

    /**
     * Tests if addAllMembers works with username not in register.
     */
    @Test
    @DisplayName("Tests if addAllMembers works with username not in register.")
    public void testIfAddAllMembersWorksWithUsernameNotInRegister(){
        try {
            observableMemberRegister.addAllMembers(makeTestMembers(), "fjells2");
            fail("Expected to get a UsernameNotPartOfConversationException since the input username is not part of the register.");
        }catch (IllegalArgumentException | CouldNotAddMemberException exception){
            fail("Expected to get a UsernameNotPartOfConversationException since the input username is not part of the register and not " + exception.getClass());
        }catch (UsernameNotPartOfConversationException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if addAllMembers works with valid input.
     */
    @Test
    @DisplayName("Tests if addAllMembers works with valid input.")
    public void testIfAddAllMembersWorksWithValidInput(){
        try {
            observableMemberRegister.addAllMembers(makeTestMembers(), memberUsername);
            assertTrue(true);
        }catch (IllegalArgumentException | UsernameNotPartOfConversationException | CouldNotAddMemberException exception){
            fail("Expected the members to be added since the input is valid and not " + exception.getClass());
        }
    }

    /**
     * Tests if addAllMembers works with one member already in the register.
     */
    @Test
    @DisplayName("Tests if addAllMembers works with one member already in the register.")
    public void testIfAddAllMembersWorksWithOneMemberAlreadyInRegister(){
        try {
            List<Member> members = makeTestMembers();
            members.add(removeMember);
            observableMemberRegister.addAllMembers(members, memberUsername);
            fail("Expected to get a CouldNotAddMemberException since one member is already in the register.");
        }catch (CouldNotAddMemberException exception){
            assertTrue(true);
        }catch (IllegalArgumentException | UsernameNotPartOfConversationException exception){
            fail("Expected to get a CouldNotAddMemberException since one member is already in the register and not " + exception.getClass());
        }
    }

    /**
     * Tests if removeAllMembers works with invalid list.
     */
    @Test
    @DisplayName("Tests if removeAllMembers works with invalid list.")
    public void testIfRemoveAllMembersWorksWithInvalidList(){
        try {
            observableMemberRegister.removeAllMembers(new ArrayList<>(), memberUsername);
            fail("Expected to get a IllegalArgumentException since the input is invalid.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotRemoveMemberException | UsernameNotPartOfConversationException | CouldNotGetMemberException exception){
            fail("Expected to get a IllegalArgumentException since the input is invalid and not " + exception.getClass());
        }
    }

    /**
     * Tests if removeAllMembers works with invalid username.
     */
    @Test
    @DisplayName("Tests if removeAllMembers works with invalid username.")
    public void testIfRemoveAllMembersWorksWithInvalidUsername(){
        try {
            observableMemberRegister.removeAllMembers(makeAndAddTestMembers(), "");
            fail("Expected to get a IllegalArgumentException since the input is invalid.");
        }catch (IllegalArgumentException exception){
            assertTrue(true);
        }catch (CouldNotRemoveMemberException | UsernameNotPartOfConversationException | CouldNotGetMemberException exception){
            fail("Expected to get a IllegalArgumentException since the input is invalid and not " + exception.getClass());
        }
    }

    /**
     * Tests if removeAllMembers works with username not in register.
     */
    @Test
    @DisplayName("Tests if removeAllMembers works with username not in register")
    public void testIfRemoveAllMembersWorksWithUsernameNotInRegister(){
        try {
            observableMemberRegister.removeAllMembers(makeAndAddTestMembers(), "fjells2");
            fail("Expected to get a UsernameNotPartOfconversationException since the input name is not in the register.");
        }catch (CouldNotGetMemberException | CouldNotRemoveMemberException | IllegalArgumentException exception){
            fail("Expected to get a UsernameNotPartOfconversationException since the input name is not in the register and not " + exception.getClass());
        }catch (UsernameNotPartOfConversationException exception){
            assertTrue(true);
        }
    }

    /**
     * Tests if removeAllMembers works with valid input.
     */
    @Test
    @DisplayName("Tests if removeAllMembers works with valid input.")
    public void testIfRemoveAllMembersWorkWithValidInput(){
        try {
            observableMemberRegister.removeAllMembers(makeAndAddTestMembers(), memberUsername);
            assertTrue(true);
        }catch (IllegalArgumentException | CouldNotRemoveMemberException | CouldNotGetMemberException | UsernameNotPartOfConversationException exception){
            fail("Expected the members to be removed since the input is valid and not get a " + exception.getClass());
        }
    }

    /**
     * Tests if removeAllMembers works with one member not in register.
     */
    @Test
    @DisplayName("Tests if removeAllMembers works with one member not in register.")
    public void testIfRemoveAllMembersWorksWithOneNotInRegister(){
        try {
            List<Member> members = makeAndAddTestMembers();
            members.add(new ConversationMember("Fjells222"));
            observableMemberRegister.removeAllMembers(members, memberUsername);
            fail("Expected to get a CouldNotRemoveMemberException since one member is not in register.");
        }catch (IllegalArgumentException | UsernameNotPartOfConversationException | CouldNotGetMemberException exception){
            fail("Expected to get a CouldNotRemoveMemberException since one member is not in register.");
        }catch (CouldNotRemoveMemberException exception){
            assertTrue(true);
        }
    }
}
