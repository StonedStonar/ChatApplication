package no.stonedstonar.chatapplication.ui.windows;

import javafx.scene.Scene;
import no.stonedstonar.chatapplication.ui.controllers.ChatController;
import no.stonedstonar.chatapplication.ui.controllers.Controller;

/**
 * Represents the chat window.
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class ChatWindow implements Window {

    private String fxmlFileName;

    private Scene scene;

    private Controller controller;

    private String titleOfScene;

    private static volatile ChatWindow chatWindow;

    /**
      * Makes an instance of the ChatWindow class.
      */
    public ChatWindow(){
        titleOfScene = "Chatting";
        controller = new ChatController();
        fxmlFileName = "chatWindow";
    }

    /**
     * Makes an instance of the ChatWindow class.
     * @return the window.
     */
    public static ChatWindow getChatWindow(){
        if (chatWindow == null){
            synchronized (ChatWindow.class){
                chatWindow = new ChatWindow();
            }
        }
        return chatWindow;
    }

    @Override
    public Controller getController() {
        return controller;
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    @Override
    public String getFXMLName() {
        return fxmlFileName;
    }

    @Override
    public String getTitleName() {
        return titleOfScene;
    }

    @Override
    public void setScene(Scene scene) {
        checkIfObjectIsNull(scene, "scene");
        this.scene = scene;
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
