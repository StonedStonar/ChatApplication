package no.stonedstonar.chatapplication.ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import no.stonedstonar.chatapplication.frontend.ChatClient;
import no.stonedstonar.chatapplication.model.MessageLog;
import no.stonedstonar.chatapplication.model.TextMessage;
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
    private VBox contactsBox;

    @FXML
    private VBox messageBox;

    @FXML
    private Button sendButton;

    @FXML
    private TextField textMessageField;

    private long activeMessageLog;


    /**
      * Makes an instance of the ChatController class.
      */
    public ChatController(){
    
    }

    /**
     * Sets the functions of all the buttons in the window.
     */
    private void setButtonFunctions(){
        sendButton.setOnAction(event -> {
            try {
                String contents = textMessageField.textProperty().get();
                ChatClient chatClient = ChatApplicationClient.getChatApplication().getChatClient();
                MessageLog messageLog = chatClient.getMessageLogByLongNumber(activeMessageLog);
                TextMessage textMessage = chatClient.sendMessage(contents, messageLog);
                addMessage(textMessage);
                textMessageField.textProperty().set("");
            }catch (IllegalArgumentException exception){
                //Todo: Gjør noe annet her.
                System.out.println("Could not send the message.");
            }

        });
    }

    private void addListeners(){

    }



    /**
     * Adds all the conversations to the conversation panel.
     */
    private void addAllConversations(){
        ChatClient chatClient = ChatApplicationClient.getChatApplication().getChatClient();
        List<MessageLog> messageLogList = chatClient.getMessageLogs();
        if (!messageLogList.isEmpty()){
            messageLogList.forEach(this::addNewConversation);
        }
        showMessagesFromMessageLog(messageLogList.get(0));
    }

    /**
     * Makes a new conversation to select on the right side.
     * @param messageLog the message log this conversation is about.
     */
    private void addNewConversation(MessageLog messageLog){
        VBox vBox = new VBox();
        vBox.setMinWidth(Long.MAX_VALUE);
        String nameOfConversation = messageLog.getMembersOfConversation().getAllMembersExceptUsernameAsString(ChatApplicationClient.getChatApplication().getChatClient().getUsername());
        Text text = new Text(nameOfConversation);
        long messageLogNumber = messageLog.getMessageLogNumber();
        vBox.getChildren().add(text);
        vBox.setPadding(new Insets(3,3,3,3));
        VBox.setMargin(vBox, new Insets(5,5,5,5));
        addInteractionToPane(vBox, messageLogNumber);
        contactsBox.getChildren().add(vBox);
    }

    /**
     * Adds it so that the pane is interactive and can be used as a button.
     * @param pane the pane you want to make interactive.
     * @param messageLogNumber the message log number this pane is holding.
     */
    private void addInteractionToPane(Pane pane, long messageLogNumber){
        pane.setOnMouseEntered(mouseEvent -> {
            pane.setBackground(new Background(new BackgroundFill(Color.valueOf("#8B7E7E"), CornerRadii.EMPTY, Insets.EMPTY)));
        });

        pane.setOnMouseExited(event -> {
            pane.setBackground(new Background(new BackgroundFill(Color.valueOf("#F4F4F4"), CornerRadii.EMPTY, Insets.EMPTY)));
        });

        pane.setOnMouseClicked(mouseEvent -> {
            handleConversationSwitch(messageLogNumber);
        });
    }

    /**
     * Handles the switch from one conversation to another.
     * @param messageLogNumber the message log number this conversation has.
     */
    private void handleConversationSwitch(long messageLogNumber){
        ChatClient chatClient = ChatApplicationClient.getChatApplication().getChatClient();
        MessageLog messageLog = chatClient.getMessageLogByLongNumber(messageLogNumber);
        showMessagesFromMessageLog(messageLog);
    }

    /**
     * Loads the messages of this conversation.
     * @param messageLog the messagelog you want to load.
     */
    public void showMessagesFromMessageLog(MessageLog messageLog){
        messageBox.getChildren().clear();
        ChatClient chatClient = ChatApplicationClient.getChatApplication().getChatClient();
        String username = chatClient.getUsername();
        List<TextMessage> messages = messageLog.getMessageList();
        activeMessageLog = messageLog.getMessageLogNumber();
        if (messages.size() > 0){
            messageLog.getMessageList().forEach(message -> {
                addMessage(message);
            });
        }else {
            Label label = new Label("There is no messages in this conversation yet.");
            VBox vBox = new VBox();
            vBox.setMinWidth(Long.MAX_VALUE);
            vBox.getChildren().add(label);
            messageBox.getChildren().add(vBox);
        }

    }

    /**
     * Adds a message to the conversation.
     * @param message the message you want to add to the gui.
     */
    private void addMessage(TextMessage message){
        Text text = new Text();
        Label label = new Label(message.getFromUsername());
        label.setTextFill(Color.valueOf("#666666"));
        label.setFont(Font.font(label.getFont().getName(), FontWeight.NORMAL, FontPosture.REGULAR, 12));
        text.setFont(Font.font(label.getFont().getName(), FontWeight.NORMAL, FontPosture.REGULAR, 12));
        text.setText(message.getMessage());
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(0,5,10,5));
        vBox.setMinWidth(Long.MAX_VALUE);
        vBox.getChildren().add(label);
        vBox.getChildren().add(text);
        //vBox.setBackground(new Background(new BackgroundFill(Color.valueOf("#dbdbdb"), CornerRadii.EMPTY, Insets.EMPTY)));
        messageBox.getChildren().add(vBox);
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
        string += " " + ChatApplicationClient.getChatApplication().getChatClient().getUsername();
        loggedInLabel.textProperty().set(string);
        addAllConversations();
        setButtonFunctions();
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
}
