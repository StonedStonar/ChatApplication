package no.stonedstonar.chatapplication.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import no.stonedstonar.chatapplication.frontend.ChatClient;
import no.stonedstonar.chatapplication.ui.controllers.Controller;
import no.stonedstonar.chatapplication.ui.windows.LoginWindow;
import no.stonedstonar.chatapplication.ui.windows.Window;

import java.io.IOException;

/**
 *
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class ChatApplicationClient extends Application {

    private static volatile ChatApplicationClient chatApplicationClient;

    private ChatClient chatClient;

    private Stage stage;

    /**
     * Makes an instance of the ChatApplicationGUI app.
     */
    public ChatApplicationClient(){
        chatClient = new ChatClient();
        chatApplicationClient = this;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        try {
            setNewScene(LoginWindow.getLoginWindow());
            primaryStage.show();
        }catch (Exception exception){
            //Todo: Something
            System.out.print("Exception hell. " +  exception.getClass() + " " + exception.getMessage());
        }
    }

    /**
     * Gets the sorting app object.
     * @return the sorting app.
     */
    public static ChatApplicationClient getChatApplication(){
        if (chatApplicationClient == null){
            synchronized (ChatApplicationClient.class){
                chatApplicationClient = new ChatApplicationClient();
            }
        }
        return chatApplicationClient;
    }

    /**
     * Gets the chat client and can call methods from it.
     * @return the chat client of this application.
     */
    public ChatClient getChatClient(){
        return chatClient;
    }

    /**
     * Changes the scene to a new scene.
     * @param window the window you want to change the scene to.
     * @throws IOException gets thrown if the scene could not be loaded.
     */
    public void setNewScene(Window window) throws IOException {
        checkIfObjectIsNull(window, "window");
        Controller controller = window.getController();
        checkIfObjectIsNull(controller, "controller");
        Scene scene = window.getScene();
        if (scene == null){
            String fileName = window.getFXMLName();
            checkString(fileName, "file name");
            String fullFilename = fileName + ".fxml";
            scene = loadScene(fullFilename , controller);
            window.setScene(scene);
        }
        String title = window.getTitleName();
        checkString(title, "title");
        String windowTitle = "Chat application 0.1v - " + title;
        controller.updateContent();
        stage.setTitle(windowTitle);
        stage.setScene(scene);
    }

    /**
     * Loads a scene and returns the scene after its loaded.
     * @param fullNameOfFile the full name of the FXML file you want to load with the .fxml part.
     * @param controller the controller you want this scene to have.
     * @return a scene that is loaded and ready to be displayed.
     * @throws IOException gets thrown if the scene is not loaded correctly or is missing.
     */
    private Scene loadScene(String fullNameOfFile, Controller controller) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fullNameOfFile));
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

    @Override
    public void stop() throws Exception {
        super.stop();
        chatClient.stopThread();
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
