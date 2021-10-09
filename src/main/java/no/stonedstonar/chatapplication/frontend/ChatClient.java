package no.stonedstonar.chatapplication.frontend;

import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotGetMessageLogException;
import no.stonedstonar.chatapplication.model.exception.textmessage.CouldNotAddTextMessageException;
import no.stonedstonar.chatapplication.model.exception.textmessage.CouldNotGetTextMessageException;
import no.stonedstonar.chatapplication.model.networktransport.LoginTransport;
import no.stonedstonar.chatapplication.model.networktransport.MessageTransport;
import no.stonedstonar.chatapplication.model.networktransport.UserRequest;
import no.stonedstonar.chatapplication.model.TextMessage;
import no.stonedstonar.chatapplication.model.MessageLog;
import no.stonedstonar.chatapplication.model.User;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class that represents the logic that the chat client should hold.
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class ChatClient {

    private User user;

    private List<MessageLog> messageLogs;

    private Logger logger;

    public static void main(String[] args) {
        ChatClient chatClient = new ChatClient();
    }

    /**
      * Makes an instance of the ChatClient class.
      */
    public ChatClient(){
        logger = Logger.getLogger(getClass().toString());
    }

    /**
     * Returns the message log of the user.
     * @return the message log of the user.
     */
    public List<MessageLog> getMessageLogs(){
        return messageLogs;
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

    /**
     * Sends a message to a person if this client is logged in.
     * @param messageContents the contents of the message.
     * @param messageLog the message log that holds the message log number.
     * @return the message that was just sent.
     * @throws SocketException gets thrown if the socket could not be made.
     * @throws CouldNotGetTextMessageException gets thrown if the server could not add the message.
     * @throws CouldNotGetMessageLogException gets thrown if the server could not get the message log.
     */
    public TextMessage sendMessage(String messageContents, MessageLog messageLog) throws SocketException, CouldNotAddTextMessageException, CouldNotGetMessageLogException {
        Socket socket;
        //Todo: Add a function to check if the message was added successfully.And alter the documentation.
        try {
            socket = new Socket("localhost", 1380);
            checkString(messageContents, "message");
            TextMessage textMessage = new TextMessage(messageContents, user.getUsername());
            MessageTransport messageTransport = new MessageTransport(textMessage, messageLog);
            sendObject(messageTransport, socket);
            Object object = getObject(socket);
            if (object instanceof CouldNotAddTextMessageException || object instanceof CouldNotGetMessageLogException){
                if (object instanceof CouldNotGetMessageLogException){
                    throw (CouldNotGetMessageLogException) object;
                }else {
                    throw (CouldNotAddTextMessageException) object;
                }
            }
            messageLog.addMessage(textMessage);
            return textMessage;
        } catch (IOException e) {
            //Todo: Kanskje istedet for å kaste en exception så burde programmet håndtere denne erroren selv.
            logger.log(Level.WARNING, "The socket failed to be made.");
            throw new SocketException("The socket failed to be made.");
        }catch (CouldNotAddTextMessageException | CouldNotGetMessageLogException exception){
            String message = "Something has gone wrong on the serverside " + exception.getClass() + " exception content: " + exception.getMessage();
            logger.log(Level.WARNING, message);
            throw exception;
        }
    }

    /**
     * Gets the message log that matches that long number.
     * @param messageLogNumber the number that message log has.
     * @return the message log that matches that number.
     */
    public MessageLog getMessageLogByLongNumber(long messageLogNumber){
        try {
            MessageLog messageLog = messageLogs.stream().filter(log -> log.getMessageLogNumber() == messageLogNumber).findFirst().get();
            return messageLog;
        }catch (NoSuchElementException exception){
            throw new IllegalArgumentException("The messagelog with the log number " + messageLogNumber + " is not in the list.");
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
            UserRequest userRequest = new UserRequest.UserRequestBuilder().setUsername(username).setCheckUsername(true).build();
            sendObject(userRequest, socket);
            Object object = getObject(socket);
            if (object instanceof Boolean valid){
                return valid;
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
