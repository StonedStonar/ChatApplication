package no.stonedstonar.chatapplication.frontend;

import no.stonedstonar.chatapplication.backend.LoginTransport;
import no.stonedstonar.chatapplication.backend.MessageTransport;
import no.stonedstonar.chatapplication.backend.UserRequest;
import no.stonedstonar.chatapplication.model.Message;
import no.stonedstonar.chatapplication.model.MessageLog;
import no.stonedstonar.chatapplication.model.User;

import java.io.*;
import java.net.Socket;
import java.util.List;

/**
 * A class that represents the logic that the chat client should hold.
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class ChatClient {

    private User user;

    private List<MessageLog> messageLogs;

    public static void main(String[] args) {
        ChatClient chatClient = new ChatClient();
        chatClient.run();
    }

    /**
      * Makes an instance of the ChatClient class.
      */
    public ChatClient(){

    }

    public void run(){
        String username = "fjell";
        String password =  "passord";
        loginToUser(username, password);
        System.out.println("You are now logged into " + user.getUsername());
        System.out.println("Your conversations are " + messageLogs.toString());
        MessageLog messageLog = messageLogs.get(0);
        System.out.println("Before adding a message: " + messageLog.getMessageList().size());
        System.out.println();
        sendMessage("fuck", messageLog);
        System.out.print("After adding a message: " + messageLog.getMessageList().size());
    }

    /**
     * Sends a message to a person if this client is logged in.
     * @param messageContents the contents of the message.
     * @param messageLog the message log that holds the message log number.
     */
    public void sendMessage(String messageContents, MessageLog messageLog){
        Socket socket;
        try {
            socket = new Socket("localhost", 1380);
            checkString(messageContents, "message");
            Message message = new Message(messageContents, user.getUsername());
            messageLog.addMessage(message);
            MessageTransport messageTransport = new MessageTransport(message, messageLog);
            sendObject(messageTransport, socket);
        } catch (IOException e) {
            System.out.println("The socket failed to be made.");
        }
    }

    /**
     * Logs the client in as a user.
     * @param username the username of the user.
     * @param password the password that the user has.
     */
    public void loginToUser(String username, String password){
        Socket socket;
        try {
            socket = new Socket("localhost", 1380);

            checkString(username, "username");
            checkString(password, "password");
            UserRequest userRequest = new UserRequest.UserRequestBuilder().setLogin(true).setUsername(username).setPassword(password).build();
            sendObject(userRequest, socket);
            Object object = getObject(socket);
            if (object instanceof LoginTransport loginTransport){
                user = loginTransport.getUser();
                messageLogs = loginTransport.getMessageLogList();
            }else {
                throw new IllegalArgumentException("The returned input is not a user.");
            }
        } catch (IOException e) {
            System.out.println("The socket failed to be made.");
        }

    }

    public void logOutOfUser(){

    }

    public void checkUsername(String username){
        checkString(username, "username");

    }

    public void makeNewUser(String username, String password){
        checkString(username, "username");
        checkString(password, "password");
    }



    /**
     * Sends a object through the socket.
     * @param object the object you want to send.
     * @param socket the socket the object should go through.
     */
    private void sendObject(Object object, Socket socket){
        try {
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(object);
        }catch (IOException exception){
            System.out.println("Kunne ikke sende objektet.");
        }
    }

    /**
     * Gets the message that is sent through the socket.
     * @param socket the socket that this message is coming through.
     * @return the message this socket contians.
     */
    private Object getObject(Socket socket){
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            Object object = objectInputStream.readObject();
            checkIfObjectIsNull(object, "object");
            return object;
        }catch (IOException | ClassNotFoundException exception){
            throw new IllegalArgumentException(exception.getMessage());
        }
    }

    /**
     * Checks if a string is of a valid format or not.
     * @param stringToCheck the string you want to check.
     * @param errorPrefix the error the exception should have if the string is invalid.
     */
    public void checkString(String stringToCheck, String errorPrefix){
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
    public void checkIfObjectIsNull(Object object, String error){
        if (object == null){
            throw new IllegalArgumentException("The " + error + " cannot be null.");
        }
    }
}
