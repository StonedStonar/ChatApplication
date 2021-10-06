package no.stonedstonar.chatapplication.frontend;

import no.stonedstonar.chatapplication.model.networktransport.LoginTransport;
import no.stonedstonar.chatapplication.model.networktransport.MessageTransport;
import no.stonedstonar.chatapplication.model.networktransport.UserRequest;
import no.stonedstonar.chatapplication.model.TextMessage;
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

    /**
     * Gets the username of the user.
     * @return the username of the logged in user.
     */
    public String getUsername(){
        if (user != null){
            return user.getUsername();
        }else {
            throw new IllegalArgumentException("The user cannot be null.");
        }
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
            TextMessage textMessage = new TextMessage(messageContents, user.getUsername());
            messageLog.addMessage(textMessage);
            MessageTransport messageTransport = new MessageTransport(textMessage, messageLog);
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
            }else if (object instanceof IllegalArgumentException){
                socket.close();
                throw (IllegalArgumentException) object;
            }else {
                throw new IllegalArgumentException("The returned input is not a user.");
            }
        } catch (IOException e) {

        }

    }

    public void logOutOfUser(){

    }

    public boolean checkUsername(String username){
        try {
            Socket socket = new Socket("localhost", 1380);
            checkString(username, "username");
            sendObject(username, socket);
            Object object = getObject(socket);
            if (object instanceof Boolean b){
                return b;
            }else {
                //Todo: Fiks this.
                throw new IllegalArgumentException("Could not check username.");
            }
        }catch (IOException exception){
            //Todo: Also fiks this
            throw new IllegalArgumentException("Facka you");
        }
    }

    /**
     * Contacts the server and makes a new user if the username is not taken.
     * @param username the username the user wants.
     * @param password the password of the user.
     */
    public void makeNewUser(String username, String password){
        try {
            Socket socket = new Socket("localhost", 1380);
            checkString(username, "username");
            checkString(password, "password");
            UserRequest userRequest = new UserRequest.UserRequestBuilder().setNewUser(true).setUsername(username).setPassword(password).build();
            sendObject(userRequest, socket);
            Object object = getObject(socket);
            if (object instanceof LoginTransport loginTransport){
                user = loginTransport.getUser();
                messageLogs = loginTransport.getMessageLogList();
            }else if (object instanceof IllegalArgumentException exception){
                System.out.println("Execption from server");
                throw exception;
            }
        }catch (IOException exception){
            //Todo: lag en handler
        }
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
