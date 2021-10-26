package no.stonedstonar.chatapplication.ui.controllers;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import no.stonedstonar.chatapplication.frontend.ChatClient;
import no.stonedstonar.chatapplication.model.conversation.PersonalConversation;
import no.stonedstonar.chatapplication.model.conversationregister.personal.PersonalConversationRegister;
import no.stonedstonar.chatapplication.model.exception.InvalidResponseException;
import no.stonedstonar.chatapplication.model.exception.conversation.CouldNotGetConversationException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotGetMessageLogException;
import no.stonedstonar.chatapplication.model.exception.message.CouldNotAddMessageException;
import no.stonedstonar.chatapplication.model.conversationregister.personal.ConversationRegisterObserver;
import no.stonedstonar.chatapplication.model.message.Message;
import no.stonedstonar.chatapplication.model.message.TextMessage;
import no.stonedstonar.chatapplication.ui.ChatApplicationClient;
import no.stonedstonar.chatapplication.ui.windows.AlertTemplates;
import no.stonedstonar.chatapplication.ui.windows.LoginWindow;
import no.stonedstonar.chatapplication.ui.windows.NewConversationWindow;

import java.io.IOException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * Represents the controller of the chat window.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class ChatController implements Controller, no.stonedstonar.chatapplication.model.conversation.ConversationObserver, ConversationRegisterObserver {

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
    private Button logOutButton;

    @FXML
    private ScrollPane scrollPane;

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
        ExecutorService executorService = ChatApplicationClient.getChatApplication().getExecutor();
        sendButton.setOnAction(event -> {
            String contents = textMessageField.textProperty().get();
            try{
                sendNewMessage(contents, chatClient);
            }catch (IllegalArgumentException exception){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Could not send message");
                alert.setHeaderText("Could not send empty message");
                alert.setContentText("Could not send text message since the contents are empty. " +
                        "Please try again.");
                textMessageField.textProperty().set("");
                alert.show();
            }
        });

        newContactButton.setOnAction(event -> {
            try {
                PersonalConversation personalConversation = chatClient.getConversationByNumber(activeMessageLog);
                if (personalConversation.checkIfObjectIsObserver(this)){
                    personalConversation.removeObserver(this);
                }
                ChatApplicationClient.getChatApplication().setNewScene(NewConversationWindow.getNewUserWindow());
            }catch (IOException exception){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Loading window error.");
                alert.setHeaderText("Loading new conversation window error.");
                alert.setContentText("Could not change window to \"New conversation\". The program will now restart.");
                alert.showAndWait();
                try {
                    ChatApplicationClient.getChatApplication().stop();
                }catch (Exception exception1){
                    AlertTemplates.makeAndShowCriticalErrorAlert(exception1);
                }
            }catch (CouldNotGetMessageLogException exception){
                AlertTemplates.makeAndShowCouldNotGetMessageLogExceptionAlert();
            } catch (CouldNotGetConversationException exception) {
                //Todo: Fill me in.
            }
        });

        logOutButton.setOnAction(event -> {
            try {
                PersonalConversation personalConversation = chatClient.getConversationByNumber(activeMessageLog);
                if (personalConversation.checkIfObjectIsObserver(this)){
                    personalConversation.removeObserver(this);
                }
                executorService.submit(() -> {
                    chatClient.logOutOfUser();
                });
                ChatApplicationClient.getChatApplication().setNewScene(LoginWindow.getLoginWindow());
            } catch (Exception e) {
                AlertTemplates.makeAndShowCriticalErrorAlert(e);
            }
        });

        sendButton.setDefaultButton(true);
        logOutButton.setCancelButton(true);

    }

    /**
     * Sets all the fields to empty.
     */
    public void setAllFieldsEmpty(){
        textMessageField.textProperty().set("");
    }

    /**
     * Sets all the valid fields and disables buttons.
     */
    private void setAllValidFieldsToFalseAndDisableButtons(){
        validFields.put(textMessageField, false);
        sendButton.setDisable(true);
    }

    /**
     * Sends a new message to the user.
     * @param messageContents the message's contents.
     * @param chatClient the chat client of the application
     */
    private void sendNewMessage(String messageContents, ChatClient chatClient) {
        try {
            PersonalConversation personalConversation = chatClient.getConversationByNumber(activeMessageLog);
            chatClient.sendMessage(messageContents, personalConversation);
            textMessageField.textProperty().set("");
        }catch (IllegalArgumentException  exception){
            AlertTemplates.makeAndShowInvalidInputAlert();
        } catch (CouldNotAddMessageException exception) {
            AlertTemplates.makeAndShowCriticalErrorAlert(exception);
        } catch (CouldNotGetMessageLogException exception) {
            AlertTemplates.makeAndShowCouldNotGetMessageLogExceptionAlert();
        } catch (IOException exception) {
            AlertTemplates.makeAndShowCouldNotConnectToServerAlert();
        } catch (InvalidResponseException e) {
            AlertTemplates.makeAndShowInvalidResponseFromTheServer();
        } catch (CouldNotGetConversationException exception) {
            AlertTemplates.makeAndShowCouldNotGetConversationAlert();
        }
    }

    /**
     * Adds listeners to the control objects that needs it.
     */
    private void addListeners(){
        validFields.put(textMessageField, false);

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
        List<PersonalConversation> messageLogList = chatClient.getMessageLogs();
        if (!messageLogList.isEmpty()){
            messageLogList.forEach(this::addNewConversation);
            PersonalConversation personalConversation = messageLogList.get(0);
            showMessagesFromConversation(personalConversation);
        } else {
            VBox vBox = new VBox();
            vBox.setMinWidth(Long.MAX_VALUE);
            Text text = new Text("You have none conversations. \nPlease add one.");
            vBox.getChildren().add(text);
            contactsBox.getChildren().add(vBox);
        }
    }

    /**
     * Makes a new conversation to select on the left side.
     * @param personalConversation the message log this conversation is about.
     */
    private void addNewConversation(PersonalConversation personalConversation){
        System.out.println("Making conversation for " + personalConversation.getConversationNumber());
        VBox vBox = new VBox();
        vBox.setMinWidth(Long.MAX_VALUE);
        String nameOfConversation = personalConversation.getConversationName();
        String membersOfConversation = personalConversation.getConversationMembers().getAllMembersExceptUsernameAsString(ChatApplicationClient.getChatApplication().getChatClient().getUsername());
        Label membersLabel = new Label(membersOfConversation);
        membersLabel.setFont(Font.font(membersLabel.getFont().getName(), FontWeight.NORMAL, FontPosture.REGULAR, 14));
        long messageLogNumber = personalConversation.getConversationNumber();
        if (!nameOfConversation.isEmpty()){
            Label textName = new Label(nameOfConversation);
            vBox.getChildren().add(textName);
            membersLabel.setTextFill(Color.valueOf("#666666"));
            textName.setFont(Font.font(textName.getFont().getName(), FontWeight.NORMAL, FontPosture.REGULAR, 14));
            membersLabel.setFont(Font.font(textName.getFont().getName(), FontWeight.NORMAL, FontPosture.REGULAR, 12));
        }
        vBox.getChildren().add(membersLabel);
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
                pane.setDisable(true);
                removeAllBordersOnPanes();
                pane.setBorder(new Border(new BorderStroke(Color.BLACK,
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0,0,0,3))));
            }catch (CouldNotGetMessageLogException exception){
                AlertTemplates.makeAndShowCouldNotGetMessageLogExceptionAlert();
            }catch (CouldNotGetConversationException exception){
                //Todo: Fix this.
            }
        });
    }

    /**
     * Removes the border around all panes.
     */
    private void removeAllBordersOnPanes(){
        contactsBox.getChildren().stream().filter(node -> node instanceof Pane).forEach(pane -> {
            Pane pane1 = (Pane) pane;
            pane1.setBorder(Border.EMPTY);
            pane1.setDisable(false);
        });
    }

    /**
     * Handles the switch from one conversation to another.
     * @param conversationNumber the message log number this conversation has.
     * @throws CouldNotGetConversationException gets thrown if the conversation could not be found.
     */
    private void handleConversationSwitch(long conversationNumber) throws CouldNotGetMessageLogException, CouldNotGetConversationException {
        if ((conversationNumber != this.activeMessageLog)){
            ChatClient chatClient = ChatApplicationClient.getChatApplication().getChatClient();
            PersonalConversation lastPersonalConversation = chatClient.getConversationByNumber(this.activeMessageLog);
            if (lastPersonalConversation.checkIfObjectIsObserver(this)){
                System.out.println("The chat controller is " + lastPersonalConversation.checkIfObjectIsObserver(this));
                lastPersonalConversation.removeObserver(this);
            }
            PersonalConversation personalConversation = chatClient.getConversationByNumber(conversationNumber);
            showMessagesFromConversation(personalConversation);
        }
    }

    /**
     * Loads the messages of this conversation.
     * @param personalConversation the message log you want to load.
     */
    public void showMessagesFromConversation(PersonalConversation personalConversation){
        messageBox.getChildren().clear();
        personalConversation.registerObserver(this);
        List<Message> messages = personalConversation.getAllMessagesOfConversationAsList();
        activeMessageLog = personalConversation.getConversationNumber();
        ChatApplicationClient.getChatApplication().getExecutor().submit(()-> {
            ChatApplicationClient.getChatApplication().getChatClient().setMessageLogFocus(activeMessageLog);
        });
        if (!messages.isEmpty()){
            messages.forEach(message -> {
                if (message instanceof TextMessage textMessage){
                    addMessage(textMessage);
                }
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
    private void addMessage(TextMessage message) {
        Text text = new Text();
        LocalTime timeOfMessage = message.getTime();
        Label label = new Label(message.getFromUsername() + " " + addZeroUntilLengthIsValid(timeOfMessage.getHour()) + ":" + addZeroUntilLengthIsValid(timeOfMessage.getMinute()));
        label.setTextFill(Color.valueOf("#666666"));
        label.setFont(Font.font(label.getFont().getName(), FontWeight.NORMAL, FontPosture.REGULAR, 12));
        text.setFont(Font.font(label.getFont().getName(), FontWeight.NORMAL, FontPosture.REGULAR, 12));
        text.setText(message.getMessage());
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(0, 5, 10, 5));
        vBox.setMinWidth(Long.MAX_VALUE);
        vBox.getChildren().add(label);
        vBox.getChildren().add(text);
        messageBox.getChildren().add(vBox);
        messageBox.heightProperty().addListener((obs, oldVal, newVal) -> {
            scrollPane.setVvalue(1.0);
        });
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

    @Override
    public void updateContent() {
        emptyContent();
        setButtonFunctions();
        addListeners();
        try {
            addAllConversations();
        }catch (Exception exception){
            System.out.println(exception.getClass() + " " + exception.getMessage());
        }
        PersonalConversationRegister personalConversationRegister = ChatApplicationClient.getChatApplication().getChatClient().getPersonalConversationRegister();
        if(!personalConversationRegister.checkIfObjectIsObserver(this)){
            personalConversationRegister.registerObserver(this);
        }
    }

    /**
     * Sets the logged in label to the wanted name.
     */
    private void setLoggedInLabel(){
        String string = "Logged in as";
        string += " " + ChatApplicationClient.getChatApplication().getChatClient().getUsername();
        loggedInLabel.textProperty().set(string);
    }
    @Override
    public void emptyContent() {
        setAllFieldsEmpty();
        setAllValidFieldsToFalseAndDisableButtons();
        setLoggedInLabel();
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

    @Override
    public void updateConversationMessage(Message message, boolean removed) {
        Platform.runLater(() ->{
            System.out.println("New message");
            if (message instanceof TextMessage textMessage){
                addMessage(textMessage);
            }
        });
    }

    @Override
    public void updateConversation(PersonalConversation personalConversation, boolean removed) {
        Platform.runLater(() -> {
            addNewConversation(personalConversation);
        });
    }
}
