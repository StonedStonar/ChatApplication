package no.stonedstonar.chatapplication.ui.windows;

import javafx.scene.Scene;
import no.stonedstonar.chatapplication.ui.controllers.Controller;
import no.stonedstonar.chatapplication.ui.controllers.LoginController;
import no.stonedstonar.chatapplication.ui.controllers.NewUserController;

/**
 *
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class NewUserWindow implements Window{

    private String fxmlFileName;

    private Scene scene;

    private Controller controller;

    private String titleOfScene;

    private static NewUserWindow newUserWindow;

    /**
      * Makes an instance of the NewUserWindow class.
      */
    private NewUserWindow(){
        titleOfScene = "New user";
        controller = new NewUserController();
        fxmlFileName = "newUserWindow";
    }

    /**
     * Gets the login window.
     * @return the login window.
     */
    public static NewUserWindow getNewUserWindow(){
        if (newUserWindow == null){
            synchronized (NewUserWindow.class){
                newUserWindow = new NewUserWindow();
            }
        }
        return newUserWindow;
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
}
