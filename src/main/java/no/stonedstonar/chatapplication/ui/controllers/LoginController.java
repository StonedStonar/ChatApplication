package no.stonedstonar.chatapplication.ui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import no.stonedstonar.chatapplication.frontend.ChatClient;
import no.stonedstonar.chatapplication.model.exception.InvalidResponseException;
import no.stonedstonar.chatapplication.model.exception.user.CouldNotLoginToUserException;
import no.stonedstonar.chatapplication.ui.ChatApplicationClient;
import no.stonedstonar.chatapplication.ui.windows.AlertTemplates;
import no.stonedstonar.chatapplication.ui.windows.ChatWindow;
import no.stonedstonar.chatapplication.ui.windows.NewUserWindow;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the controller for the login window.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class LoginController implements Controller {

    @FXML
    private Label loginLabel;

    @FXML
    private Label usernameText;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label passwordText;

    @FXML
    private TextField usernameField;

    @FXML
    private Label versionLabel;

    @FXML
    private Button loginButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button newUserButton;

    private Map<TextField, Boolean> fields;
    /**
      * Makes an instance of the LoginController class.
      */
    public LoginController(){
        fields = new HashMap<>();
    }

    /**
     * Sets all the fields to empty.
     */
    public void setAllFieldsEmpty(){
        usernameField.textProperty().set("");
        passwordField.textProperty().set("");
    }

    /**
     * Sets all the valid fields and disables buttons.
     */
    private void setAllValidFieldsToFalseAndDisableButtons(){
        fields.put(usernameField, false);
        fields.put(passwordField, false);
        loginButton.setDisable(true);
    }

    /**
     * Sets the functions of all the buttons.
     */
    private void setButtonFunctions(){
        ChatApplicationClient chatApplicationClient = ChatApplicationClient.getChatApplication();
        ChatClient chatClient = chatApplicationClient.getChatClient();

        loginButton.setOnAction(actionEvent -> {
            try {
                String username = usernameField.textProperty().get();
                String password = passwordField.textProperty().get();
                chatClient.loginToUser(username, password);
                chatApplicationClient.setNewScene(ChatWindow.getChatWindow());
            }catch (IOException exception){
                AlertTemplates.makeAndShowCouldNotConnectToServerAlert();
            }catch (IllegalArgumentException exception){
                AlertTemplates.makeAndShowInvalidInputAlert();
            } catch (CouldNotLoginToUserException exception) {
                AlertTemplates.makeAndShowCouldNotLoginAlert();
            }  catch (InvalidResponseException e) {
                AlertTemplates.makeAndShowInvalidResponseFromTheServer();
            }
        });
        cancelButton.setOnAction(actionEvent -> {
            Platform.exit();
        });

        newUserButton.setOnAction(actionEvent -> {
            try {
                chatApplicationClient.setNewScene(NewUserWindow.getNewUserWindow());
            } catch (IOException e) {
                AlertTemplates.makeAndShowCriticalErrorAlert(e);
            }
        });

        loginButton.setDefaultButton(true);
        cancelButton.setCancelButton(true);
    }

    /**
     * Makes the listeners needed for fields.
     */
    private void makeListeners(){
        String username3Letters = "The username must be more than 3 letters long.";
        String password3Letters = "The password must be more than 3 letters long.";
        usernameText.textProperty().set(username3Letters);
        passwordText.textProperty().set(password3Letters);
        usernameField.textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                Long.parseLong(newVal);
                usernameText.textProperty().set("Username cannot start with a number or only be numbers.");
                fields.put(usernameField, false);
            }catch (NumberFormatException exception){
                if (newVal.length() >= 3){
                    usernameText.setText("The username is a valid format.");
                    fields.put(usernameField, true);
                }else {
                    usernameText.textProperty().set(username3Letters);
                    fields.put(usernameField, false);
                }
            }
            checkIfAllFieldsAreValid();

        });
        passwordField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.length() >= 3){
                passwordText.textProperty().set("Password is valid format.");
                fields.put(passwordField, true);
            }else {
                passwordText.textProperty().set(password3Letters);
                fields.put(passwordField, false);
            }
            checkIfAllFieldsAreValid();
        });
    }

    /**
     * Checks if all the fields are filled in. Disables the login button if password and username is not filled in.
     */
    private void checkIfAllFieldsAreValid(){
        long valid = fields.values().stream().filter(aBoolean ->  aBoolean).count();
        if (valid == fields.size()){
            loginButton.setDisable(false);
        }else {
            loginButton.setDisable(true);
        }
    }

    @Override
    public void updateContent() {
        emptyContent();
        setButtonFunctions();
        makeListeners();
    }

    @Override
    public void emptyContent() {
        setAllFieldsEmpty();
        setAllValidFieldsToFalseAndDisableButtons();
    }
}
