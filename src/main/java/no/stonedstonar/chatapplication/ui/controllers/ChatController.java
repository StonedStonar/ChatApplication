package no.stonedstonar.chatapplication.ui.controllers;

import javafx.application.Platform;
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
import no.stonedstonar.chatapplication.model.conversation.ConversationObserver;
import no.stonedstonar.chatapplication.model.conversation.ObservableConversation;
import no.stonedstonar.chatapplication.model.conversationregister.personal.ConversationRegisterObserver;
import no.stonedstonar.chatapplication.model.conversationregister.personal.PersonalConversationRegister;
import no.stonedstonar.chatapplication.model.exception.InvalidResponseException;
import no.stonedstonar.chatapplication.model.exception.conversation.CouldNotGetConversationException;
import no.stonedstonar.chatapplication.model.exception.message.CouldNotAddMessageException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotGetMessageLogException;
import no.stonedstonar.chatapplication.model.member.Member;
import no.stonedstonar.chatapplication.model.membersregister.ObservableMemberRegister;
import no.stonedstonar.chatapplication.model.message.Message;
import no.stonedstonar.chatapplication.model.message.TextMessage;
import no.stonedstonar.chatapplication.ui.ChatApplicationClient;
import no.stonedstonar.chatapplication.ui.windows.AlertTemplates;
import no.stonedstonar.chatapplication.ui.windows.LoginWindow;
import no.stonedstonar.chatapplication.ui.windows.NewConversationWindow;

import java.io.IOException;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * Represents the controller of the chat window.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class ChatController implements Controller, ConversationObserver, ConversationRegisterObserver {

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

    @FXML
    private Button editConversationButton;

    private long activeConversation;

    private Map<Node, Boolean> validFields;


    /**
      * Makes an instance of the ChatController class.
      */
    public ChatController(){
        validFields = new HashMap<>();
        activeConversation = 0;
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
                        "\nPlease try again.");
                textMessageField.textProperty().set("");
                alert.show();
            }
        });

        newContactButton.setOnAction(event -> {
            try {
                ChatApplicationClient.getChatApplication().setNewScene(NewConversationWindow.getNewConversationWindow());
            }catch (IOException exception) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Loading window error.");
                alert.setHeaderText("Loading new conversation window error.");
                alert.setContentText("Could not change window to \"New conversation\". The program will now restart.");
                alert.showAndWait();
                try {
                    ChatApplicationClient.getChatApplication().stopApp();
                } catch (Exception exception1) {
                    AlertTemplates.makeAndShowCriticalErrorAlert(exception1);
                }
            }
        });

        logOutButton.setOnAction(actionEvent -> {
            try {
                unsubscribeToAllConversations();
                emptyContent();
                executorService.submit(chatClient::logOutOfUser);
                ChatApplicationClient.getChatApplication().setNewScene(LoginWindow.getLoginWindow());
            } catch (Exception e) {
                AlertTemplates.makeAndShowCriticalErrorAlert(e);
            }
        });

        editConversationButton.setOnAction(event ->{
            ConversationController controller = (ConversationController) NewConversationWindow.getNewConversationWindow().getController();
            try {
                controller.setEditMode(activeConversation);
                ChatApplicationClient.getChatApplication().setNewScene(NewConversationWindow.getNewConversationWindow());
            } catch (CouldNotGetConversationException e) {
                AlertTemplates.makeAndShowCouldNotGetConversationAlert();
            }catch (IOException exception){
                AlertTemplates.makeAndShowCouldNotConnectToServerAlert();
            }
        });

        ChatApplicationClient.getChatApplication().getExecutor().submit(()-> {
            chatClient.setConversationFocus(activeConversation);
            if (!chatClient.checkIfListeningThreadIsActive()){
                chatClient.startBackgroundThread();
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
        messageBox.getChildren().clear();
        contactsBox.getChildren().clear();
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
            ObservableConversation observableConversation = chatClient.getConversationByNumber(activeConversation);
            chatClient.sendMessage(messageContents, observableConversation);
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
            validFields.put(textMessageField, !newVal.isEmpty());
            checkIfAllFieldsAreValid();
        });
    }

    /**
     * Makes this object a listener of all the objects.
     */
    private void subscribeToAllConversations(){
        Iterator<ObservableConversation> it = ChatApplicationClient.getChatApplication().getChatClient().getPersonalConversationRegister().getIterator();
        it.forEachRemaining(conversation -> {
            if (!conversation.checkIfObjectIsObserver(this)){
                conversation.registerObserver(this);
            }
        });
    }

    /**
     * Unsubscribes to all conversations.
     */
    private void unsubscribeToAllConversations(){
        Iterator<ObservableConversation> it = ChatApplicationClient.getChatApplication().getChatClient().getPersonalConversationRegister().getIterator();
        it.forEachRemaining(conversation -> conversation.removeObserver(this));
    }

    /**
     * Adds all the conversations to the conversation panel.
     */
    private void addAllConversations() {
        contactsBox.getChildren().clear();
        ChatClient chatClient = ChatApplicationClient.getChatApplication().getChatClient();
        subscribeToAllConversations();
        Iterator<ObservableConversation> it = chatClient.getConversationsIterator();
        if (it.hasNext()){
            int i = 0;
            while (it.hasNext()){
                ObservableConversation observableConversation = it.next();
                addNewConversation(observableConversation);
                if(i == 0){
                    showMessagesFromConversation(observableConversation);
                }
                i += 1;
            }
            textMessageField.setDisable(false);
            editConversationButton.setDisable(false);
        } else {
            Label label = new Label("You have none conversations. \nPlease add one.");
            contactsBox.getChildren().add(label);
            textMessageField.setDisable(true);
            editConversationButton.setDisable(true);
        }
    }

    /**
     * Makes a new conversation to select on the left side.
     * @param observableConversation the message log this conversation is about.
     */
    private void addNewConversation(ObservableConversation observableConversation) {
        VBox vBox = new VBox();
        vBox.setMinWidth(Long.MAX_VALUE);
        String nameOfConversation = observableConversation.getConversationName();
        String membersOfConversation = getAllMembersExceptUsernameAsString(observableConversation.getMembers());
        Label membersLabel = new Label(membersOfConversation);
        membersLabel.setFont(Font.font(membersLabel.getFont().getName(), FontWeight.NORMAL, FontPosture.REGULAR, 14));
        long messageLogNumber = observableConversation.getConversationNumber();
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
     * Makes a string of all the usernames in the register.
     * @param observableMemberRegister the members register.
     * @return a string with all the usernames of this group except the applications' user's username.
     */
    private String getAllMembersExceptUsernameAsString(ObservableMemberRegister observableMemberRegister){
        ChatClient chatClient = ChatApplicationClient.getChatApplication().getChatClient();
        String username = chatClient.getUsername();
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Member> it = observableMemberRegister.getIterator();
        it.forEachRemaining(member -> {
            String memberUsername = member.getUsername();
            if (!memberUsername.equals(username)){
                stringBuilder.append(memberUsername);
                stringBuilder.append(" ");
            }
        });
        return stringBuilder.toString();
    }

    /**
     * Adds it so that the pane is interactive and can be used as a button.
     * @param pane the pane you want to make interactive.
     * @param messageLogNumber the message log number this pane is holding.
     */
    private void addInteractionToPane(Pane pane, long messageLogNumber){
        pane.setOnMouseEntered(mouseEvent -> pane.setBackground(new Background(new BackgroundFill(Color.valueOf("#8B7E7E"), CornerRadii.EMPTY, Insets.EMPTY))));

        pane.setOnMouseExited(event -> pane.setBackground(new Background(new BackgroundFill(Color.valueOf("#F4F4F4"), CornerRadii.EMPTY, Insets.EMPTY))));

        pane.setId(Long.toString(messageLogNumber));

        pane.setOnMouseClicked(mouseEvent -> {
            try {
                handleConversationSwitch(messageLogNumber);
                pane.setDisable(true);
                removeAllBordersOnPanes();
                pane.setBorder(new Border(new BorderStroke(Color.BLACK,
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0,0,0,3))));
            }catch (CouldNotGetConversationException exception){
                AlertTemplates.makeAndShowCouldNotGetConversationAlert();
            }
        });
    }

    /**
     * Removes the border around all panes.
     */
    private void removeAllBordersOnPanes(){
        contactsBox.getChildren().stream().filter(Pane.class::isInstance).forEach(pane -> {
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
    private void handleConversationSwitch(long conversationNumber) throws CouldNotGetConversationException {
        if ((conversationNumber != this.activeConversation)){
            ChatClient chatClient = ChatApplicationClient.getChatApplication().getChatClient();
            ObservableConversation observableConversation = chatClient.getConversationByNumber(conversationNumber);
            showMessagesFromConversation(observableConversation);
        }
    }

    /**
     * Loads the messages of this conversation.
     * @param observableConversation the message log you want to load.
     */
    public void showMessagesFromConversation(ObservableConversation observableConversation){
        messageBox.getChildren().clear();
        List<Message> messages = observableConversation.getAllMessagesOfConversationAsList();
        this.activeConversation = observableConversation.getConversationNumber();
        ChatClient chatClient = ChatApplicationClient.getChatApplication().getChatClient();
        chatClient.setConversationFocus(activeConversation);
        if (!messages.isEmpty()){
            messages.forEach(message -> {
                if (message instanceof TextMessage textMessage){
                    addMessage(textMessage);
                }
            });
        }else {
            Label label = new Label("There is no messages in this conversation yet.");
            messageBox.getChildren().add(label);
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
        messageBox.heightProperty().addListener((obs, oldVal, newVal) -> scrollPane.setVvalue(1.0));
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
        addAllConversations();
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
        sendButton.setDisable(valid != validFields.size());
    }

    @Override
    public void updateConversationMessage(Message message, boolean removed, long conversationNumber) {
        Platform.runLater(() ->{
            if ((message instanceof TextMessage textMessage) && (activeConversation == conversationNumber)){
                if (messageBox.getChildren().size() == 1 && messageBox.getChildren().get(0) instanceof Label){
                    messageBox.getChildren().clear();
                }
                addMessage(textMessage);
            }
        });
    }

    @Override
    public void updateMemberInConversation(long conversationNumber) {
        Platform.runLater(() -> changeConversationMembers(conversationNumber));
    }

    @Override
    public void updateConversationName(long conversationNumber) {
        Platform.runLater(() -> {
            try {
                ChatClient chatClient = ChatApplicationClient.getChatApplication().getChatClient();
                VBox vBox = getConversationVBox(conversationNumber);
                long amount = vBox.getChildren().stream().filter(Label.class::isInstance).count();
                ObservableConversation observableConversation = chatClient.getConversationByNumber(conversationNumber);
                if (amount == 1){
                    contactsBox.getChildren().remove(vBox);
                    addNewConversation(observableConversation);
                }else if (amount == 2){
                    Label label = (Label) vBox.getChildren().get(0);
                    label.setText(observableConversation.getConversationName());
                }
            }catch (CouldNotGetConversationException exception){
                contactsBox.getChildren().clear();
                addAllConversations();
            }
        });
    }

    @Override
    public void updateConversation(ObservableConversation observableConversation, boolean removed) {
        Platform.runLater(() -> {
            if (removed){
                if (!contactsBox.getChildren().isEmpty()){
                    contactsBox.getChildren().clear();
                }
                addAllConversations();
            }else{
                if (contactsBox.getChildren().size() == 1 && contactsBox.getChildren().get(0) instanceof Label){
                    contactsBox.getChildren().clear();
                    addAllConversations();
                }else{
                    observableConversation.registerObserver(this);
                    addNewConversation(observableConversation);
                }
            }
        });
    }

    /**
     * Changes the conversation members based on the current input.
     * @param conversationNumber the conversation number.
     */
    private void changeConversationMembers(long conversationNumber){
        try {
            VBox vBox = getConversationVBox(conversationNumber);
            ChatClient chatClient = ChatApplicationClient.getChatApplication().getChatClient();
            ObservableConversation observableConversation = chatClient.getConversationByNumber(conversationNumber);
            long amountOfNodes = vBox.getChildren().stream().filter(Label.class::isInstance).count();
            if (amountOfNodes == 1){
                Label label = (Label) vBox.getChildren().get(0);
                label.setText(getAllMembersExceptUsernameAsString(observableConversation.getMembers()));
            }else if (amountOfNodes == 2){
                Label label = (Label) vBox.getChildren().get(1);
                label.setText(getAllMembersExceptUsernameAsString(observableConversation.getMembers()));
            }
        } catch (CouldNotGetConversationException | NoSuchElementException exception) {
            addAllConversations();
        }
    }

    /**
     * Gets the conversation vbox that has the same id as the input conversation number.
     * @param conversationNumber the conversation number to look for.
     * @return the vbox that matches this number.
     */
    private VBox getConversationVBox(long conversationNumber){
        String conversationNumberAsString = Long.toString(conversationNumber);
        return (VBox) contactsBox.getChildren().stream().filter(node -> node.getId().equals(conversationNumberAsString)).findFirst().get();
    }
}
