package no.stonedstonar.chatapplication.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.Scene;
import javafx.stage.Stage;
import no.stonedstonar.chatapplication.backend.Server;
import no.stonedstonar.chatapplication.ui.controllers.Controller;
import no.stonedstonar.chatapplication.ui.controllers.frontend.ChatController;
import no.stonedstonar.chatapplication.ui.windows.Window;
import no.stonedstonar.chatapplication.ui.windows.backend.ServerWindow;

import java.io.IOException;

/**
 *
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class ChatServerApplication extends Application {

    private Stage stage;

    private Server server;

    private static volatile ChatServerApplication chatServerApplication;

    /**
      * Makes an instance of the ChatServerApplication class.
      */
    public ChatServerApplication(){
        server = new Server();
        chatServerApplication = this;
    }

    /**
     * Gets the chat server app object.
     * @return the chat server app.
     */
    public static ChatServerApplication getChatServerApplication() {
        return chatServerApplication;
    }

    /**
     * Gets the server from the server app.
     * @return the server this app holds.
     */
    public Server getServer(){
        return server;
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        server.stopServer();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            stage = primaryStage;
            setNewScene(ServerWindow.getServerWindow());
            //server.run();
            stage.show();
        }catch (LoadException exception){
            System.out.println(exception.getClass() + " " + exception.getMessage());
        }
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
        return new Scene(loader.load());
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
