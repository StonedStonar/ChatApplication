package no.stonedstonar.chatapplication.ui.controllers.backend;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import no.stonedstonar.chatapplication.backend.ServerObserver;
import no.stonedstonar.chatapplication.ui.ChatServerApplication;
import no.stonedstonar.chatapplication.ui.controllers.Controller;
import no.stonedstonar.chatapplication.ui.controllers.frontend.AlertTemplates;

import java.util.logging.Level;

/**
 *
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class ServerController implements Controller, ServerObserver {

    @FXML
    private Label serverLabel;

    @FXML
    private VBox logMessagesBox;

    @FXML
    private Button shutdownServerButton;

    /**
      * Makes an instance of the ServerController class.
      */
    public ServerController(){

    }

    /**
     * Sets the functions of all the buttons of this controller.
     */
    private void setButtonFunctions(){
        shutdownServerButton.setOnAction(event -> {
            try {
                ChatServerApplication.getChatServerApplication().stop();
            } catch (Exception e) {
                AlertTemplates.makeAndShowCriticalErrorAlert(e);
            }
        });
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

    /**
     * Adds a message form the server to the message box.
     * @param message the message you want to add.
     * @param level the level of the message.
     */
    private void addMessageToBox(String message, Level level){
        Text text = new Text();
        text.setText(level.toString() + " " + message);
        text.setFont(Font.font(Font.getDefault().getName(), FontWeight.NORMAL, FontPosture.REGULAR, 12));
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(0,5,10,5));
        vBox.setMinWidth(Long.MAX_VALUE);
        vBox.getChildren().add(text);
        logMessagesBox.getChildren().add(vBox);
    }

    @Override
    public void updateContent() {
        setButtonFunctions();
    }

    @Override
    public void updateMessage(String message, Level level) {
        Platform.runLater(()->{
            addMessageToBox(message, level);
        });
    }
}
