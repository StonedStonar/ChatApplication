package no.stonedstonar.chatapplication.ui.windows;

import javafx.scene.Scene;
import no.stonedstonar.chatapplication.ui.controllers.Controller;
import no.stonedstonar.chatapplication.ui.controllers.ConversationController;

/**
 * Represents the conversation window.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class NewConversationWindow implements Window {

    private String fxmlFileName;

    private Scene scene;

    private Controller controller;

    private String titleOfScene;

    private static volatile NewConversationWindow newConversationWindow;

    /**
      * Makes an instance of the NewConversationWindow class.
      */
    private NewConversationWindow(){
        fxmlFileName = "newConversationWindow";
        controller = new ConversationController();
        titleOfScene = "New conversation";
    }

    /**
     * Gets the new conversation window.
     * @return gets the new conversation window.
     */
    public static NewConversationWindow getNewConversationWindow(){
        if (newConversationWindow == null){
            synchronized (NewUserWindow.class){
                newConversationWindow = new NewConversationWindow();
            }
        }
        return newConversationWindow;
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
