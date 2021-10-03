package no.stonedstonar.chatapplication.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import no.stonedstonar.chatapplication.ui.controllers.Controller;
import no.stonedstonar.chatapplication.ui.windows.Window;

import java.io.IOException;

/**
 *
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class ChatApplicationClient {

    private static volatile ChatApplicationClient chatApplicationClient;

    /**
     * Makes an instance of the ChatApplicationGUI app.
     */
    private ChatApplicationClient(){

    }

    /**
     * Gets the sorting app object.
     * @return the sorting app.
     */
    public static ChatApplicationClient getSortingApp(){
        if (chatApplicationClient == null){
            synchronized (ChatApplicationClient.class){
                chatApplicationClient = new ChatApplicationClient();
            }
        }
        return chatApplicationClient;
    }

    /**
     * Changes the scene to a new scene.
     * @param window the window you want to change the scene to.
     */
    public void setNewScene(Window window){
        checkIfObjectIsNull(window, "window");
        
    }

    /**
     * Loads a scene and returns the scene after its loaded.
     * @param fullNameOfFile the full name of the FXML file you want to load with the .fxml part.
     * @param controller the controller you want this scene to have.
     * @return a scene that is loaded and ready to be displayed.
     * @throws IOException gets thrown if the scene is not loaded correctly or is missing.
     */
    private Scene loadScene(String fullNameOfFile, Controller controller) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource( fullNameOfFile));
        loader.setController(controller);
        Scene newScene = new Scene(loader.load());
        return newScene;
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
