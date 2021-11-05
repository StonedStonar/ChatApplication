package no.stonedstonar.chatapplication.ui.windows;

import javafx.application.Platform;
import javafx.scene.control.Alert;

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
        Platform.exit();
    }

    /**
     * Makes an alert that is shown when a username is not on the server.
     */
    public static void makeAndShowCouldNotFindUsernameAlert(){
        makeAndShowAlert(Alert.AlertType.INFORMATION, "Username error", "Username is not a user on the server.", "The username that you are trying to add is not a user on this app. \nPlease try another username.");
    }

    /**
     * Makes an alert that is shown when a conversation could not be added.
     */
    public static void makeAndShowCouldNotAddConversation(){
        makeAndShowAlert(Alert.AlertType.ERROR, "Could not add conversation", "Could not add conversation", "The conversation could not be made. \nPlease try again.");
    }

    /**
     * Makes an alert that is shown when a member could not be removed.
     */
    public static void makeCouldNotRemoveMemberExceptionAlert(){
        makeAndShowAlert(Alert.AlertType.ERROR, "Could not remove member.", "Could not remove member.", "Could not remove member. \nPlease try again.");
    }

    /**
     * Makes an alert that is shown when a message log could not be made.
     */
    public static void makeAndShowCouldNotGetMessageLogExceptionAlert(){
        makeAndShowAlert(Alert.AlertType.ERROR, "Could not change the conversation.", "Could not change conversation.", "A error occurred while trying to switch conversation. \n Please try again or restart the program if the problem persists.");
    }

    /**
     * Makes an alert that is shown when the communication to the server goes bad.
     */
    public static void makeAndShowCouldNotConnectToServerAlert(){
        makeAndShowAlert(Alert.AlertType.WARNING, "Could not connect to server", "Could not connect to the server.", "The program could not connect to the server. \nPlease check that there is a internet connection.");
    }

    /**
     * Makes and shows an alert that is used when the response form the server is invalid.
     */
    public static void makeAndShowInvalidResponseFromTheServer(){
        makeAndShowAlert(Alert.AlertType.WARNING, "Server response problems", "Server responded with wrong input.", "The program got the wrong response from the server. \nPlease try again.\nIf the problem persists please restart the program.");
    }

    /**
     * Makes and shows an alert that is used when the input is invalid.
     */
    public static void makeAndShowInvalidInputAlert(){
        makeAndShowAlert(Alert.AlertType.INFORMATION, "Input is invalid", "The input is invalid.", "The input is invalid. \nPlease try again.");
    }

    /**
     * Makes and shows an alert that is used when a user could not log in.
     */
    public static void makeAndShowCouldNotLoginAlert(){
        makeAndShowAlert(Alert.AlertType.ERROR, "Could not login", "Could not login to user", "Could not login. \nThe username or password does not match any user in the system.");
    }

    /**
     * Makes and shows an alert that is used when a user could not be made.
     */
    public static void makeAndShowCouldNotMakeUserAlert(){
        makeAndShowAlert(Alert.AlertType.INFORMATION, "Could not make user", "Could not make user", "Could not make a user since the username is already taken.");
    }

    /**
     * Makes and shows an alert that is used when a conversation could not be gotten.
     */
    public static void makeAndShowCouldNotGetConversationAlert(){
        makeAndShowAlert(Alert.AlertType.ERROR, "Could not get conversation", "Could not get the conversation", "The conversation could not be gotten. \nPlease restart the program and try again.");
    }

    /**
     * Makes and shows an alert.
     * @param alertType the alert type.
     * @param title the title of the alert.
     * @param header the header of the alert.
     * @param context the context of the alert.
     */
    private static void makeAndShowAlert(Alert.AlertType alertType, String title, String header, String context){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);
        alert.show();
    }
}
