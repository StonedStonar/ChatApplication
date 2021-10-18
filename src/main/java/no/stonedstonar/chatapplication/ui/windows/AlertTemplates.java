package no.stonedstonar.chatapplication.ui.windows;

import javafx.scene.control.Alert;
import no.stonedstonar.chatapplication.ui.ChatApplicationClient;

/**
 * A basic class that holds methods to make often used alerts.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class AlertTemplates {

    /**
     * Makes a critical error alert and stops the program.
     */
    public static void makeAndShowCriticalErrorAlert(Exception exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Critical error");
        alert.setHeaderText("A critical error has occured.");
        alert.setContentText("A unexpected error has occured. Please report this to the application developer.\n" +exception.getMessage() + "\nAnd restart the program.");
        alert.show();
        try {
            ChatApplicationClient.getChatApplication().stop();
        }catch (Exception exception1){

        }
    }

    /**
     * Makes an alert that is shown when a message log could not be made.
     */
    public static void makeAndShowCouldNotGetMessageLogExceptionAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Could not change the conversation.");
        alert.setHeaderText("Could not change conversation.");
        alert.setContentText("A error occurred while trying to switch conversation. \n Please try again or restart the program if the problem persists.");
        alert.show();
    }

    /**
     * Makes an alert that is shown when the communication to the server goes bad.
     */
    public static void makeAndShowCouldNotConnectToServerAlert(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Could not connect to server");
        alert.setHeaderText("Could not connect to the server.");
        alert.setContentText("The program could not connect to the server. \nPlease check that there is a internet connection.");
        alert.show();
    }

    /**
     * Makes and shows an alert that is used when the response form the server is invalid.
     */
    public static void makeAndShowInvalidResponseFromTheServer(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Server response problems");
        alert.setHeaderText("Server responded with wrong input.");
        alert.setContentText("The program got the wrong response from the server. \nPlease try again.\nIf the problem persists please restart the program.");
        alert.show();
    }

    /**
     * Makes and shows an alert that is used when the input is invalid.
     */
    public static void makeAndShowInvalidInputAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Input is invalid");
        alert.setHeaderText("The input is invalid.");
        alert.setContentText("The input is invalid. \nPlease try again.");
        alert.show();
    }

    /**
     * Makes and shows an alert that is used when a user could not log in.
     */
    public static void makeAndShowCouldNotLoginAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Could not login");
        alert.setHeaderText("Could not login to user");
        alert.setContentText("Could not login. \nThe username or password does not match any user in the system.");
        alert.show();
    }

    /**
     * Makes and shows an alert that is used when a user could not be made.
     */
    public static void makeAndShowCouldNotMakeUserAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Could not make user");
        alert.setHeaderText("Could not make user");
        alert.setContentText("Could not make a user since the username is already taken.");
        alert.show();
    }
}
