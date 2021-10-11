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
import no.stonedstonar.chatapplication.model.MessageLog;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotAddMemberException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotAddMessageLogException;
import no.stonedstonar.chatapplication.ui.ChatApplicationClient;
import no.stonedstonar.chatapplication.ui.windows.ChatWindow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class ConversationController implements Controller{

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

    private List<String> usernames;

    private Map<Node, Boolean> validFields;

    /**
      * Makes an instance of the ConversationController class.
      */
    public ConversationController(){
        validFields = new HashMap<>();
        usernames = new ArrayList<>();
    }

    /**
     * Sets all the functions of the buttons.
     */
    private void setButtonFunctions(){
        ChatClient chatClient = ChatApplicationClient.getChatApplication().getChatClient();

        cancelButton.setOnAction(event -> {
            try {
                ChatApplicationClient.getChatApplication().setNewScene(ChatWindow.getChatWindow());
            } catch (IOException e) {
                //Todo: Sett inn respons if it goes wrong.
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
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Username error");
                    alert.setHeaderText("Username is not a user on the server.");
                    alert.setContentText("The username that you are trying to add is not a user on this app. " +
                            "\nPlease try another username.");
                    alert.show();
                }
            } catch (IOException e) {
                //Todo: Fix all these exceptions
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            checkIfRequiredFieldsAreOk();
            checkIfUsernameCanBeAdded();
        });

        removeUsernameButton.setOnAction(event -> {
            String username = usernameField.textProperty().get();
            String usernameOfClient = ChatApplicationClient.getChatApplication().getChatClient().getUsername();
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
            try {
                long messageLogNumber = chatClient.makeNewConversation(usernames);
                ChatApplicationClient.getChatApplication().setNewScene(ChatWindow.getChatWindow());
            } catch (CouldNotAddMessageLogException exception) {
                //Todo: Fix all exceptions
                exception.printStackTrace();
            } catch (IOException exception) {
                exception.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (CouldNotAddMemberException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Sets all the valid fields and disables buttons.
     */
    private void setAllValidFieldsToFalseAndDisableButtons(){
        validFields.put(usernameField, false);
        validFields.put(conversationField, true);
        validFields.put(usernameText, false);
        addUsernameButton.setDisable(true);
        makeConversationButton.setDisable(true);
    }

    /**
     * Adds all the listeners that is needed.
     */
    private void addListeners(){
        usernameText.setText("The username must be 3 letters long.");
        conversationText.setText("If the conversation is given a name it must be 3 characters or longer.");

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
                    convoText.set("If the conversation is given a name it must be 3 characters or longer.");
                }
            }else {
                validFields.put(conversationField, true);
                convoText.set("If the conversation is given a name it must be 3 characters or longer.");
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
        usernames.add(username);
    }

    /**
     * Removes a username from the list and the gui.
     * @param username the username you want to remove.
     */
    private void removeUsername(String username){
        Text text = (Text) membersBox.getChildren().stream().filter(node -> node instanceof Text).filter(texts -> ((Text) texts).textProperty().get().equals(username)).findFirst().get();
        membersBox.getChildren().remove(text);
        String nameToRemove = usernames.stream().filter(string -> string.equals(username)).findFirst().get();
        usernames.remove(nameToRemove);
    }

    /**
     * Checks if the username can be added or removed. If so it
     */
    private void checkIfUsernameCanBeAdded(){
        if (validFields.get(usernameField)){
            addUsernameButton.setDisable(false);
        }else {
            addUsernameButton.setDisable(true);
        }
    }

    /**
     * Checks if all the fields are filled in. Disables the login button if password and username is not filled in.
     */
    private void checkIfRequiredFieldsAreOk(){
        if ((validFields.get(conversationField)) && (usernames.size() > 1)){
            makeConversationButton.setDisable(false);
        }else {
            makeConversationButton.setDisable(true);
        }
    }
    
    /**
     * Checks if a string is of a valid format or not.
     * @param stringToCheck the string you want to check.
     * @param errorPrefix the error the exception should have if the string is invalid.
     */
    private void checkString(String stringToCheck, String errorPrefix){
        checkIfObjectIsNull(stringToCheck, errorPrefix);
        if (stringToCheck.isEmpty()){
            throw new IllegalArgumentException("The " + errorPrefix + " cannot be empty.");
        }
    }
    
    /**
     * Checks if an object is null.
     * @param object the object you want to check.
     * @param error the error message the exception should have.
     */
    private void checkIfObjectIsNull(Object object, String error){
       if (object == null){
           throw new IllegalArgumentException("The " + error + " cannot be null.");
       }
    }

    @Override
    public void updateContent() {
        setAllValidFieldsToFalseAndDisableButtons();
        addUserLoggedInToConversation();
        setButtonFunctions();
        addListeners();
    }
}
