package no.stonedstonar.chatapplication.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import no.stonedstonar.chatapplication.frontend.ChatClient;
import no.stonedstonar.chatapplication.ui.ChatApplicationClient;
import no.stonedstonar.chatapplication.ui.windows.ChatWindow;
import no.stonedstonar.chatapplication.ui.windows.LoginWindow;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class NewUserController implements Controller{

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
    
    }

    private void setButtonFunctions(){
        makeUserButton.setOnAction(actionEvent -> {
            try {
                String username = usernameField.textProperty().get();
                String password = passwordField.textProperty().get();
                ChatClient chatClient = ChatApplicationClient.getSortingApp().getChatClient();
                chatClient.makeNewUser(username, password);
                ChatApplicationClient.getSortingApp().setNewScene(ChatWindow.getChatWindow());
            }catch (IllegalArgumentException exception){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("The user with the name " + usernameField.textProperty().get() + " is already in the register.");
                alert.setTitle("Could not make new user");
                alert.setHeaderText("Error making new user");
                alert.show();
            }catch (IOException exception){

            }
        });
        cancelButton.setOnAction(actionEvent -> {
            try {
                ChatApplicationClient.getSortingApp().setNewScene(LoginWindow.getLoginWindow());
            }catch (IOException exception){
                //Todo: Insert error handeling.
            }
        });
    }

    private void makeListeners(){
        String username3Letters = "The username must be more than 3 letters long.";
        String password3Letters = "The password must be more than 3 letters long.";
        passwordText.setText(password3Letters);
        usernameText.setText(username3Letters);
        makeUserButton.setDisable(true);
        validFields.put(passwordText, false);
        validFields.put(passwordField, false);
        validFields.put(usernameField, false);
        validFields.put(passwordFieldCheck, false);
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
                validFields.put(passwordField, true);
            }else {
                passwordText.textProperty().set(password3Letters);
                validFields.put(passwordField, false);
            }
            checkIfPasswordsMatch(password3Letters);
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
     * Checks if all the fields are filled in. Disables the login button if password and username is not filled in.
     */
    private void checkIfAllFieldsAreValid(){
        long valid = validFields.values().stream().filter(aBoolean ->  aBoolean).count();
        if (valid == validFields.size()){
            makeUserButton.setDisable(false);
        }else {
            makeUserButton.setDisable(true);
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
        validFields = new HashMap<>();
        setButtonFunctions();
        makeListeners();
    }
}
