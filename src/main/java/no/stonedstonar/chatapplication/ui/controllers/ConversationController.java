package no.stonedstonar.chatapplication.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import no.stonedstonar.chatapplication.ui.ChatApplicationClient;
import no.stonedstonar.chatapplication.ui.windows.ChatWindow;

import java.io.IOException;
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
    private Button usernameButton;

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

    private Map<Node, Boolean> validFields;

    /**
      * Makes an instance of the ConversationController class.
      */
    public ConversationController(){
    
    }

    private void setButtonFunctions(){
        cancelButton.setOnAction(event -> {
            try {
                ChatApplicationClient.getChatApplication().setNewScene(ChatWindow.getChatWindow());
            } catch (IOException e) {
                //Todo: Sett inn respons if it goes wrong.
            }
        });
    }

    /**
     * 
     */
    private void addListeners(){

    }

    private void addUsername(){

    }

    private void removeUsername(){

    }

    /**
     * Checks if all the fields are filled in. Disables the login button if password and username is not filled in.
     */
    private void checkIfAllFieldsAreValid(){
        long valid = validFields.values().stream().filter(aBoolean ->  aBoolean).count();
        if (valid == validFields.size()){
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
        setButtonFunctions();
        addListeners();
    }
}
