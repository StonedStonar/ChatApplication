package no.stonedstonar.chatapplication.ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import no.stonedstonar.chatapplication.frontend.ChatClient;
import no.stonedstonar.chatapplication.model.MessageLog;
import no.stonedstonar.chatapplication.ui.ChatApplicationClient;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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

    @FXML
    private VBox contactsBox;

    @FXML
    private VBox messageBox;

    @FXML
    private Button sendButton;

    @FXML
    private TextField textMessageField;


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

    public void addAllMessageLogs(){
        ChatClient chatClient = ChatApplicationClient.getSortingApp().getChatClient();
        List<MessageLog> messageLogList = chatClient.getMessageLogs();
        AtomicInteger errors = new AtomicInteger();
        if (!messageLogList.isEmpty()){
            messageLogList.forEach(messageLog -> {
                try {
                    Pane conversationPane = getPaneFromFXMLFile("contactBox");
                    Text contactLabel = (Text) conversationPane.getChildren().stream().filter(thing -> thing instanceof Text).findFirst().get();
                    contactLabel.textProperty().set(messageLog.getMembersOfConversation().getAllMembersInAString());
                    addInteractionToPane(conversationPane);
                    contactsBox.getChildren().add(conversationPane);
                }catch (IOException exception){
                    errors.addAndGet(1);
                }
            });
        }
        if (errors.get() > 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Could not load messagebox");
            alert.setHeaderText("Could not load messagebox.");
            alert.setContentText("Something went wrong in the loading of your contacts. Please try again.");
            alert.show();
        }
        showMessagesFromMessageLog(messageLogList.get(0));
    }

    private void addInteractionToPane(Pane pane){
        pane.setOnMouseEntered(mouseEvent -> {
            pane.setBackground(new Background(new BackgroundFill(Color.valueOf("#8B7E7E"), CornerRadii.EMPTY, Insets.EMPTY)));
        });

        pane.setOnMouseExited(event -> {
            pane.setBackground(new Background(new BackgroundFill(Color.valueOf("#F4F4F4"), CornerRadii.EMPTY, Insets.EMPTY)));
        });

        pane.setOnMouseClicked(mouseEvent -> {

        });

    }

    /**
     *
     * @param messageLog
     */
    public void showMessagesFromMessageLog(MessageLog messageLog){
        ChatClient chatClient = ChatApplicationClient.getSortingApp().getChatClient();
        String username = chatClient.getUsername();

        messageLog.getMessageList().forEach(message -> {
            Text text = new Text();
            text.setText(message.getMessage());
            String side = "left";
            if(message.getFromUsername().equals(username)){
                side = "right";
            }
            text.setStyle("-fx-halignment: right;");
            messageBox.getChildren().add(text);
        });
    }

    /**
     * Gets the contents of a FXML file and makes it usable.
     * @param fxmlFileName the name of the FXML file's name without the ".fxml" part.
     * @return a pane that can be used.
     * @throws IOException gets thrown if something goes wrong in loading the file.
     */
    private Pane getPaneFromFXMLFile(String fxmlFileName) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName + ".fxml"));
        Scene scene = new Scene(loader.load());
        if (scene.getRoot() instanceof Pane){
            Pane pane = (Pane) scene.getRoot();
            return pane;
        }else {
            throw new IllegalArgumentException("The contents of this FXML file is not a pane and cannot be loaded.");
        }
    }

    @Override
    public void updateContent() {
        String string = loggedInLabel.textProperty().get();
        string += " " + ChatApplicationClient.getSortingApp().getChatClient().getUsername();
        loggedInLabel.textProperty().set(string);
        addAllMessageLogs();
    }
}
