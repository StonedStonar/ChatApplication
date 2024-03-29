package no.stonedstonar.chatapplication.ui.controllers;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import no.stonedstonar.chatapplication.frontend.ChatClient;
import no.stonedstonar.chatapplication.model.conversation.ObservableConversation;
import no.stonedstonar.chatapplication.model.exception.InvalidResponseException;
import no.stonedstonar.chatapplication.model.exception.conversation.CouldNotAddConversationException;
import no.stonedstonar.chatapplication.model.exception.conversation.CouldNotGetConversationException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotAddMemberException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotRemoveMemberException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotAddMessageLogException;
import no.stonedstonar.chatapplication.ui.ChatApplicationClient;
import no.stonedstonar.chatapplication.ui.windows.AlertTemplates;
import no.stonedstonar.chatapplication.ui.windows.ChatWindow;

import java.io.IOException;
import java.util.*;

/**
 * Represents the controller of the conversation window.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class ConversationController implements Controller {

    @FXML
    private Label usernameLabel;

    @FXML
    private TextField usernameField;

    @FXML
    private Text usernameText;

    @FXML
    private Button addUsernameButton;

    @FXML
    private Button removeUsernameButton;

    @FXML
    private VBox membersBox;

    @FXML
    private Label conversationNameLabel;

    @FXML
    private TextField conversationField;

    @FXML
    private Text conversationText;

    @FXML
    private Button makeConversationButton;

    @FXML
    private Button cancelButton;

    private final List<String> usernames;

    private final List<String> removedUsernames;

    private final Map<Node, Boolean> validFields;

    private boolean editConversation;

    private ObservableConversation conversationToEdit;

    private String standardConversationText;

    /**
      * Makes an instance of the ConversationController class.
      */
    public ConversationController(){
        validFields = new HashMap<>();
        usernames = new ArrayList<>();
        removedUsernames = new ArrayList<>();
        standardConversationText = "If the conversation is given a name it must be 3 characters or longer.";
    }

    /**
     * Sets all the fields to empty.
     */
    public void setAllFieldsEmpty(){
        if (usernameField != null){
            usernameField.textProperty().set("");
        }
        if (conversationField != null){
            conversationField.textProperty().set("");
        }
        removedUsernames.clear();
        if (!editConversation){
            usernames.clear();
            removedUsernames.clear();
            if (membersBox != null){
                membersBox.getChildren().clear();
            }
        }
    }

    /**
     * Sets all the functions of the buttons.
     */
    private void setButtonFunctions(){
        ChatApplicationClient chatApplicationClient = ChatApplicationClient.getChatApplication();
        ChatClient chatClient = ChatApplicationClient.getChatApplication().getChatClient();

        cancelButton.setOnAction(event -> {
            try {
                editConversation = false;
                conversationToEdit = null;
                setAllFieldsEmpty();
                ChatApplicationClient.getChatApplication().setNewScene(ChatWindow.getChatWindow());
            } catch (IOException e) {
                AlertTemplates.makeAndShowCouldNotConnectToServerAlert();
            }
        });

        addUsernameButton.setOnAction(event -> {
            String username = usernameField.textProperty().get();
            try {
                if (chatClient.checkUsername(username)){
                    addUsername(username);
                    usernameField.textProperty().set("");
                    validFields.put(usernameField, false);
                }else {
                    AlertTemplates.makeAndShowCouldNotFindUsernameAlert();
                }
            } catch (IOException e) {
                AlertTemplates.makeAndShowCouldNotConnectToServerAlert();
            } catch (InvalidResponseException e) {
                AlertTemplates.makeAndShowInvalidResponseFromTheServer();
            }
            checkIfRequiredFieldsAreOk();
            checkIfUsernameCanBeAdded();
        });

        removeUsernameButton.setOnAction(event -> {
            String username = usernameField.textProperty().get();
            String usernameOfClient = chatClient.getUsername();
            if (username.equals(usernameOfClient)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error removing username");
                alert.setHeaderText("Could not remove username");
                alert.setContentText("You cannot remove yourself from this new conversation.");
                alert.show();
            }else {
                removeUsername(username);
                usernameField.textProperty().set("");
                validFields.put(usernameField, false);
            }
            checkIfRequiredFieldsAreOk();
        });

        makeConversationButton.setOnAction(event -> {
            String nameOfConversation = conversationField.textProperty().get();
            try {
                if (editConversation){
                    List<String> originalMembers = conversationToEdit.getMembers().getNameOfAllMembers();
                    List<String> newMembers = usernames.stream().filter(name -> originalMembers.stream().noneMatch(user -> user.equals(name))).toList();
                    chatClient.editConversation(newMembers, removedUsernames, conversationField.getText(), conversationToEdit);
                }else {
                    chatClient.makeNewConversation(usernames, nameOfConversation);
                }
                editConversation = false;
                conversationToEdit = null;
                setAllFieldsEmpty();
                chatApplicationClient.setNewScene(ChatWindow.getChatWindow());
            } catch (CouldNotAddMessageLogException exception) {
                AlertTemplates.makeAndShowCouldNotGetMessageLogExceptionAlert();
            } catch (IOException exception) {
                AlertTemplates.makeAndShowCouldNotConnectToServerAlert();
            } catch (CouldNotAddMemberException exception) {
                AlertTemplates.makeAndShowCriticalErrorAlert(exception);
            } catch (InvalidResponseException e) {
                AlertTemplates.makeAndShowInvalidResponseFromTheServer();
            } catch (CouldNotAddConversationException exception) {
                AlertTemplates.makeAndShowCouldNotAddConversation();
            } catch (CouldNotGetConversationException exception) {
                AlertTemplates.makeAndShowCouldNotGetConversationAlert();
            } catch (CouldNotRemoveMemberException e) {
                AlertTemplates.makeCouldNotRemoveMemberExceptionAlert();
            }
        });

        makeConversationButton.setDefaultButton(true);
        cancelButton.setCancelButton(true);
    }

    /**
     * Sets all the valid fields and disables buttons.
     */
    private void setAllValidFieldsToFalseAndDisableButtons(){
        validFields.put(usernameField, false);
        validFields.put(conversationField, true);
        validFields.put(usernameText, false);
        addUsernameButton.setDisable(true);
        removeUsernameButton.setDisable(true);
        makeConversationButton.setDisable(true);
    }

    /**
     * Adds all the listeners that is needed.
     */
    private void addListeners(){
        usernameText.setText("The username must be 3 letters long.");
        conversationText.setText(standardConversationText);

        usernameField.textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                Long.parseLong(newVal);
                usernameText.textProperty().set("Username cannot start with a number or only be numbers.");
                validFields.put(usernameField, false);
                removeUsernameButton.setDisable(true);
            }catch (NumberFormatException exception){
                if (newVal.length() >= 3){
                    if (!checkIfUsernameIsInConversation(newVal)){
                        usernameText.setText("The username is a valid format.");
                        validFields.put(usernameField, true);
                        removeUsernameButton.setDisable(true);
                    }else {
                        usernameText.setText("The username is already in the conversation.");
                        validFields.put(usernameField, false);
                        removeUsernameButton.setDisable(false);
                    }
                }else {
                    usernameText.textProperty().set("The username must be 3 letters long.");
                    validFields.put(usernameField, false);
                    removeUsernameButton.setDisable(true);
                }
            }
            checkIfRequiredFieldsAreOk();
            checkIfUsernameCanBeAdded();
        });

        conversationField.textProperty().addListener((obs, oldVal, newVal) -> {
            StringProperty convoText = conversationText.textProperty();
            if (!newVal.isEmpty()){
                if (newVal.length() >= 3){
                    validFields.put(conversationField, true);
                    convoText.set("The conversation name is valid.");
                }else {
                    validFields.put(conversationField, false);
                    convoText.set(standardConversationText);
                }
            }else {
                validFields.put(conversationField, true);
                convoText.set(standardConversationText);
            }
            checkIfRequiredFieldsAreOk();
        });
    }


    /**
     * Checks if the username is in the conversation already.
     * @param username the username you want to check.
     * @return <code>true</code> if the username is already in the conversation.
     *         <code>false</code> if the username is not already in the conversation.
     */
    private boolean checkIfUsernameIsInConversation(String username){
        boolean valid = false;
        if (!usernames.isEmpty()){
            valid = usernames.stream().anyMatch(name -> name.equals(username));
        }
        return valid;
    }

    /**
     * Sets the conversation window to edit mode.
     * @param conversationNumber the conversation number that you want to edit.
     * @throws CouldNotGetConversationException gets thrown if the conversation could not be gotten.
     */
    public void setEditMode(long conversationNumber) throws CouldNotGetConversationException {
        ChatClient chatClient = ChatApplicationClient.getChatApplication().getChatClient();
        conversationToEdit = chatClient.getConversationByNumber(conversationNumber);
        List<String> names = conversationToEdit.getMembers().getNameOfAllMembers();
        emptyContent();
        usernames.addAll(names);
        editConversation = true;
    }

    /**
     * Adds the user that is logged in to the group.
     */
    private void addUserLoggedInToConversation(){
        addUsername(ChatApplicationClient.getChatApplication().getChatClient().getUsername());
    }

    /**
     * Adds a username to the list of conversation members.
     * @param username the username you want to add.
     */
    private void addUsername(String username){
        Text text = new Text();
        text.setText(username);
        membersBox.getChildren().add(text);
        if (!usernames.contains(username)){
            usernames.add(username);
        }
        if(removedUsernames.stream().anyMatch(name -> name.equals(username))){
            String usernameToRemove = removedUsernames.stream().filter(name -> name.equals(username)).toList().get(0);
            removedUsernames.remove(usernameToRemove);
        }
    }

    /**
     * Removes a username from the list and the gui.
     * @param username the username you want to remove.
     */
    private void removeUsername(String username){
        Text text = (Text) membersBox.getChildren().stream().filter(Text.class::isInstance).filter(texts -> ((Text) texts).textProperty().get().equals(username)).toList().get(0);
        membersBox.getChildren().remove(text);
        String nameToRemove = usernames.stream().filter(string -> string.equals(username)).toList().get(0);
        usernames.remove(nameToRemove);
        removedUsernames.add(nameToRemove);
    }

    /**
     * Checks if the username can be added or removed. If so it
     */
    private void checkIfUsernameCanBeAdded(){
        if (Boolean.TRUE.equals(validFields.get(usernameField))){
            addUsernameButton.setDisable(false);
        }else {
            addUsernameButton.setDisable(true);
        }
    }

    /**
     * Checks if all the fields are filled in. Disables the login button if password and username is not filled in.
     */
    private void checkIfRequiredFieldsAreOk(){
        if (Boolean.TRUE.equals((validFields.get(conversationField))) && (usernames.size() > 1)){
            makeConversationButton.setDisable(false);
        }else {
            makeConversationButton.setDisable(true);
        }
    }

    @Override
    public void updateContent() {
        emptyContent();
        if (!editConversation){
            addUserLoggedInToConversation();
        }
        setButtonFunctions();
        addListeners();
    }

    @Override
    public void emptyContent(){
        if (cancelButton != null){
            setAllFieldsEmpty();
            setAllValidFieldsToFalseAndDisableButtons();
            membersBox.getChildren().clear();
            if(editConversation){
                makeConversationButton.setText("Edit conversation");
                usernames.forEach(this::addUsername);
                conversationField.setText(conversationToEdit.getConversationName());
            }else {
                usernames.clear();
                if (makeConversationButton != null){
                    makeConversationButton.setText("Make conversation");
                }
            }
        }

    }
}
