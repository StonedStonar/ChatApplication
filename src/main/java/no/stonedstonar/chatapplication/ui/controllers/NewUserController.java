package no.stonedstonar.chatapplication.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import no.stonedstonar.chatapplication.frontend.ChatClient;
import no.stonedstonar.chatapplication.model.exception.InvalidResponseException;
import no.stonedstonar.chatapplication.model.exception.user.CouldNotAddUserException;
import no.stonedstonar.chatapplication.ui.ChatApplicationClient;
import no.stonedstonar.chatapplication.ui.windows.AlertTemplates;
import no.stonedstonar.chatapplication.ui.windows.LoginWindow;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the controller for new user window.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class NewUserController implements Controller {

    @FXML
    private Label loginLabel;

    @FXML
    private Label usernameText;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label versionLabel;

    @FXML
    private PasswordField passwordFieldCheck;

    @FXML
    private Label passwordText;

    @FXML
    private Button makeUserButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField usernameField;

    private Map<Node, Boolean> validFields;

    /**
      * Makes an instance of the NewUserController class.
      */
    public NewUserController(){
        validFields = new HashMap<>();
    }

    /**
     * Sets all the fields to empty.
     */
    public void setAllFieldsEmpty(){
        usernameField.textProperty().set("");
        passwordField.textProperty().set("");
        passwordFieldCheck.textProperty().set("");
    }

    /**
     * Sets all the valid fields and disables buttons.
     */
    private void setAllValidFieldsToFalseAndDisableButtons(){
        makeUserButton.setDisable(true);
        validFields.put(passwordText, false);
        validFields.put(passwordField, false);
        validFields.put(usernameField, false);
        validFields.put(passwordFieldCheck, false);
    }


    /**
     * Sets the functions of all the buttons in this window.
     */
    private void setButtonFunctions(){
        ChatApplicationClient chatApplicationClient = ChatApplicationClient.getChatApplication();
        ChatClient chatClient = chatApplicationClient.getChatClient();
        makeUserButton.setOnAction(actionEvent -> {
            try {
                String username = usernameField.textProperty().get();
                String password = passwordField.textProperty().get();
                chatClient.makeNewUser(username, password);
                chatApplicationClient.setNewScene(LoginWindow.getLoginWindow());
            }catch (IllegalArgumentException exception){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("The user with the name " + usernameField.textProperty().get() + " is already in the register.");
                alert.setTitle("Could not make new user");
                alert.setHeaderText("Error making new user");
                alert.show();
            }catch (IOException exception){
                AlertTemplates.makeAndShowCouldNotConnectToServerAlert();
            }catch (CouldNotAddUserException exception) {
                AlertTemplates.makeAndShowCouldNotMakeUserAlert();
            }catch (InvalidResponseException e) {
                AlertTemplates.makeAndShowInvalidResponseFromTheServer();
            }
        });
        cancelButton.setOnAction(actionEvent -> {
            try {
                chatApplicationClient.setNewScene(LoginWindow.getLoginWindow());
            }catch (IOException exception){
                AlertTemplates.makeAndShowCouldNotConnectToServerAlert();
            }
        });

        makeUserButton.setDefaultButton(true);
        cancelButton.setCancelButton(true);
    }

    /**
     * Sets the listeners of all the controls that needs them.
     */
    private void makeListeners(){
        String username3Letters = "The username must be more than 3 letters long.";
        String password3Letters = "The password must be more than 3 letters long.";
        passwordText.setText(password3Letters);
        usernameText.setText(username3Letters);
        passwordField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.length() >= 3){
                validFields.put(passwordField, true);
            }else {
                passwordText.textProperty().set(password3Letters);
                validFields.put(passwordField, false);
            }
            checkIfPasswordsMatch(password3Letters);
        });

        passwordFieldCheck.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.length() >= 3){
                validFields.put(passwordFieldCheck, true);
            }else {
                passwordText.textProperty().set(password3Letters);
                validFields.put(passwordFieldCheck, false);
            }
            checkIfPasswordsMatch(password3Letters);
        });

        ChatClient chatClient = ChatApplicationClient.getChatApplication().getChatClient();

        usernameField.textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                Long.parseLong(newVal);
                usernameText.textProperty().set("Username cannot start with a number or only be numbers.");
            }catch (NumberFormatException exception){
                if (newVal.length() >= 3){
                    usernameText.setText("The username is of a valid format.");
                }else {
                    usernameText.textProperty().set(username3Letters);
                }
            }
            checkIfAllFieldsAreValid();
        });

        usernameField.focusedProperty().addListener((valid) -> {
            String username = usernameField.textProperty().get();
            if ((username != null) && (!username.isEmpty()) && (username.length() >= 3)){
                try {
                    if (chatClient.checkUsername(usernameField.textProperty().get())){
                        usernameText.setText("The username is taken.");
                        validFields.put(usernameField, false);
                    }else {
                        validFields.put(usernameField, true);
                        usernameText.setText("The username is not taken.");
                    }
                } catch (IOException e) {
                    AlertTemplates.makeAndShowCouldNotConnectToServerAlert();
                } catch (InvalidResponseException e) {
                    AlertTemplates.makeAndShowInvalidResponseFromTheServer();
                }
            }
            checkIfAllFieldsAreValid();
        });
    }

    /**
     * Checks if the passwords match or not.
     * @param error the error that should show when password fields are empty.
     */
    private void checkIfPasswordsMatch(String error){
        String password1 = passwordField.textProperty().get();
        String password2 = passwordFieldCheck.textProperty().get();
        if (password1.isBlank() | password2.isBlank()){
            passwordText.setText(error);
            validFields.put(passwordText, false);
        }else {
            if (password1.equals(password2)){
                passwordText.setText("The passwords match.");
                validFields.put(passwordText, true);
            }else {
                passwordText.setText("The passwords does not match.");
                validFields.put(passwordText, false);
            }
        }
        checkIfAllFieldsAreValid();
    }
    /**
     * Checks if all the fields are filled in. Disables the login button if password the passwords does not match and username is not filled in.
     */
    private void checkIfAllFieldsAreValid(){
        long valid = validFields.values().stream().filter(aBoolean ->  aBoolean).count();
        if (valid == validFields.size()){
            makeUserButton.setDisable(false);
        }else {
            makeUserButton.setDisable(true);
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
