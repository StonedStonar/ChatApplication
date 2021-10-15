package no.stonedstonar.chatapplication.ui.windows.backend;

import javafx.scene.Scene;

import no.stonedstonar.chatapplication.ui.controllers.Controller;
import no.stonedstonar.chatapplication.ui.controllers.backend.ServerController;
import no.stonedstonar.chatapplication.ui.windows.Window;


/**
 * Represents the server window.
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class ServerWindow implements Window {

    private String fxmlFileName;

    private Scene scene;

    private Controller controller;

    private String titleOfScene;

    private static volatile ServerWindow serverWindow;

    /**
      * Makes an instance of the ServerWindow class.
      */
    public ServerWindow(){
        controller = new ServerController();
        titleOfScene = "Server";
        fxmlFileName = "serverWindow";
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

    /**
     * Makes an instance of the ServerWindow class.
     * @return the window.
     */
    public static ServerWindow getServerWindow(){
        if (serverWindow == null){
            synchronized (ServerWindow.class){
                serverWindow = new ServerWindow();
            }
        }
        return serverWindow;
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
