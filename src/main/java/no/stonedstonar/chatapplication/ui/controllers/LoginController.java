package no.stonedstonar.chatapplication.ui.controllers;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import no.stonedstonar.chatapplication.frontend.ChatClient;
import no.stonedstonar.chatapplication.ui.ChatApplicationClient;
import no.stonedstonar.chatapplication.ui.windows.ChatWindow;
import no.stonedstonar.chatapplication.ui.windows.NewUserWindow;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class LoginController implements Controller{

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

    }

    /**
     * Sets the functions of all the buttons.
     */
    private void setButtonFunctions(){
        loginButton.setOnAction(actionEvent -> {
            try {
                String username = usernameField.textProperty().get();
                String password = passwordField.textProperty().get();
                ChatApplicationClient.getSortingApp().getChatClient().loginToUser(username, password);
                ChatApplicationClient.getSortingApp().setNewScene(ChatWindow.getChatWindow());
            }catch (IOException exception){
                //Todo: Sett inn error melding
            }catch (IllegalArgumentException exception){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error logging in.");
                alert.setHeaderText("Error logging in.");
                alert.setContentText("The username or password does not match a user. \nPlease try again.");
                alert.show();
            }
        });
        cancelButton.setOnAction(actionEvent -> {

        });

        newUserButton.setOnAction(actionEvent -> {
            try {
                ChatApplicationClient.getSortingApp().setNewScene(NewUserWindow.getNewUserWindow());
            } catch (IOException e) {
                //Todo: Sett inn en exception response.
            }
        });
    }

    private void makeListeners(){
        String username3Letters = "The username must be more than 3 letters long.";
        String password3Letters = "The password must be more than 3 letters long.";
        loginButton.setDisable(true);
        usernameText.textProperty().set(username3Letters);
        passwordText.textProperty().set(password3Letters);
        fields.put(usernameField, false);
        fields.put(passwordField, false);
        usernameField.textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                Long.parseLong(newVal);
                usernameText.textProperty().set("Username cannot start with a number or only be numbers.");
                fields.put(usernameField, false);
            }catch (NumberFormatException exception){
                if (newVal.length() >= 3){
                    usernameText.textProperty().set("The username is valid format.");
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
        fields = new HashMap<>();
        setButtonFunctions();
        makeListeners();
    }
}
