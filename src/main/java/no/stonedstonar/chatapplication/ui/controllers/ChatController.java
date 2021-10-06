package no.stonedstonar.chatapplication.ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import no.stonedstonar.chatapplication.ui.ChatApplicationClient;

import java.io.IOException;

/**
 *
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class ChatController implements Controller{

    @FXML
    private Label loggedInLabel;

    @FXML
    private VBox vBox;

    /**
      * Makes an instance of the ChatController class.
      */
    public ChatController(){
    
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

    public void test(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("contactBox.fxml"));
        try {
            //Todo: Use what you found out here to take stuff out of a fxml file. Let the games begin
            // ( ͡• ͜ʖ ͡•)
            Scene scene = new Scene(loader.load());
            if (scene.getRoot() instanceof Pane pane){
                Label label = (Label) pane.getChildren().stream().filter(thing -> {
                    return thing instanceof Label;
                }).findFirst().get();
                System.out.println(label.textProperty().get());
            }
        }catch (IOException exception){

        }
    }

    @Override
    public void updateContent() {
        String string = loggedInLabel.textProperty().get();
        string += " " + ChatApplicationClient.getSortingApp().getChatClient().getUsername();
        loggedInLabel.textProperty().set(string);
        test();
    }
}
