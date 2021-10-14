package no.stonedstonar.chatapplication.ui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
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
import no.stonedstonar.chatapplication.model.*;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotGetMessageLogException;
import no.stonedstonar.chatapplication.model.exception.textmessage.CouldNotAddTextMessageException;
import no.stonedstonar.chatapplication.ui.ChatApplicationClient;
import no.stonedstonar.chatapplication.ui.windows.NewConversationWindow;

import java.io.IOException;
import java.net.SocketException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class ChatController implements Controller, ConversationObserver, MessageObserver {

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

    @FXML
    private Button newContactButton;

    @FXML
    private Button testButton;

    private long activeMessageLog;

    private Map<Node, Boolean> validFields;


    /**
      * Makes an instance of the ChatController class.
      */
    public ChatController(){
        validFields = new HashMap<>();
    }

    /**
     * Sets the functions of all the buttons in the window.
     */
    private void setButtonFunctions(){
        ChatClient chatClient = ChatApplicationClient.getChatApplication().getChatClient();
        sendButton.setOnAction(event -> {
            try {
                String contents = textMessageField.textProperty().get();

                PersonalMessageLog personalMessageLog= chatClient.getMessageLogByLongNumber(activeMessageLog);
                chatClient.sendMessage(contents, personalMessageLog);
                textMessageField.textProperty().set("");
            }catch (IllegalArgumentException  exception){
                //Todo: Gjør noe annet her.
                System.out.println("Could not send the message." + exception.getMessage());
                exception.printStackTrace();
            } catch (CouldNotAddTextMessageException exception) {
                //Todo: Fix all the fucking exceptions here.
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle();
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (CouldNotGetMessageLogException exception) {
                exception.printStackTrace();
            } catch (IOException exception) {
                exception.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        newContactButton.setOnAction(event -> {
            try {
                ChatApplicationClient.getChatApplication().setNewScene(NewConversationWindow.getNewUserWindow());
            }catch (IOException exception){

            }
        });
        testButton.setOnAction(event -> {
            chatClient.setMessageLogFocus(activeMessageLog);
        });
    }

    private void makeErrorAlert(){
        Alert alert = new Alert()
    }

    /**
     * Adds listeners to the control objects that needs it.
     */
    private void addListeners(){
        validFields.put(textMessageField, false);
        sendButton.setDisable(true);
        textMessageField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty()){
                validFields.put(textMessageField, true);
            }else {
                validFields.put(textMessageField, false);
            }
            checkIfAllFieldsAreValid();
        });
    }



    /**
     * Adds all the conversations to the conversation panel.
     */
    private void addAllConversations(){
        contactsBox.getChildren().clear();
        ChatClient chatClient = ChatApplicationClient.getChatApplication().getChatClient();
        List<PersonalMessageLog> messageLogList = chatClient.getMessageLogs();
        if (!messageLogList.isEmpty()){
            messageLogList.forEach(this::addNewConversation);
            PersonalMessageLog firstPersonalMessageLog = messageLogList.get(0);
            showMessagesFromMessageLog(firstPersonalMessageLog);
        }
        if (!messageLogList.isEmpty()){

        }else {
            VBox vBox = new VBox();
            vBox.setMinWidth(Long.MAX_VALUE);
            Text text = new Text("You have none conversations. \nPlease add one.");
            vBox.getChildren().add(text);
            contactsBox.getChildren().add(vBox);
        }
    }

    /**
     * Makes a new conversation to select on the left side.
     * @param personalMessageLog the message log this conversation is about.
     */
    private void addNewConversation(PersonalMessageLog personalMessageLog){
        VBox vBox = new VBox();
        vBox.setMinWidth(Long.MAX_VALUE);
        String nameOfConversation = personalMessageLog.getMembersOfConversation().getAllMembersExceptUsernameAsString(ChatApplicationClient.getChatApplication().getChatClient().getUsername());
        Text text = new Text(nameOfConversation);
        long messageLogNumber = personalMessageLog.getMessageLogNumber();
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
            try {
                handleConversationSwitch(messageLogNumber);
            }catch (CouldNotGetMessageLogException exception){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Could not change the conversation.");
                alert.setHeaderText("Could not change conversation.");
                alert.setContentText("A error occurred while trying to switch conversation. \n Please try again or restart the program if the problem persists.");
            }
        });
    }

    /**
     * Handles the switch from one conversation to another.
     * @param messageLogNumber the message log number this conversation has.
     */
    private void handleConversationSwitch(long messageLogNumber) throws CouldNotGetMessageLogException {
        ChatClient chatClient = ChatApplicationClient.getChatApplication().getChatClient();
        PersonalMessageLog lastPersonalMessageLog = chatClient.getMessageLogByLongNumber(this.activeMessageLog);
        if (lastPersonalMessageLog.checkIfObjectIsObserver(this)){
            lastPersonalMessageLog.removeObserver(this);
        }
        PersonalMessageLog messageLog = chatClient.getMessageLogByLongNumber(messageLogNumber);
        showMessagesFromMessageLog(messageLog);
    }

    /**
     * Loads the messages of this conversation.
     * @param personalMessageLog the message log you want to load.
     */
    public void showMessagesFromMessageLog(PersonalMessageLog personalMessageLog){
        messageBox.getChildren().clear();
        personalMessageLog.registerObserver(this);
        List<TextMessage> messages = personalMessageLog.getMessageList();
        activeMessageLog = personalMessageLog.getMessageLogNumber();
        Thread thread = new Thread(()-> {
            ChatApplicationClient.getChatApplication().getChatClient().setMessageLogFocus(activeMessageLog);
        });
        thread.start();
        if (!messages.isEmpty()){
            personalMessageLog.getMessageList().forEach(message -> {
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
     * Adds a message to the conversation box.
     * @param message the message you want to add to the gui.
     */
    private void addMessage(TextMessage message){
        Text text = new Text();
        LocalTime timeOfMessage = message.getTime();
        Label label = new Label(message.getFromUsername() + " " + addZeroUntilLengthIsValid(timeOfMessage.getHour()) + ":" + addZeroUntilLengthIsValid(timeOfMessage.getMinute()));
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
     * Adds a zero if the number input is under 10, so we get the right clock format.
     * If the input is 3 then you get a string that is "03" instead of "3".
     * @param number the number you want to convert to string.
     * @return a string that is two digits in length.
     */
    private String addZeroUntilLengthIsValid(int number){
        StringBuilder stringBuilder = new StringBuilder();
        if (number < 10){
            stringBuilder.append("0");
        }
        stringBuilder.append(number);
        return stringBuilder.toString();
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
        setButtonFunctions();
        addListeners();
        try {
            addAllConversations();
        }catch (Exception exception){
            System.out.println(exception.getClass() + " " + exception.getMessage());
        }
    }

    /**
     * Checks if all the fields are filled in. Disables the login button if password and username is not filled in.
     */
    private void checkIfAllFieldsAreValid(){
        long valid = validFields.values().stream().filter(aBoolean ->  aBoolean).count();
        if (valid == validFields.size()){
            sendButton.setDisable(false);
        }else {
            sendButton.setDisable(true);
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
    public void updateMessageLog(MessageLog messageLog, boolean removed) {

    }

    @Override
    public void updateMessage(TextMessage textMessage, boolean removed) {
        Platform.runLater(() ->{
            System.out.println("New message");
            addMessage(textMessage);
        });
    }
}
