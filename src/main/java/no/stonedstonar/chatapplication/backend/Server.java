package no.stonedstonar.chatapplication.backend;

import no.stonedstonar.chatapplication.model.*;
import no.stonedstonar.chatapplication.model.networktransport.LoginTransport;
import no.stonedstonar.chatapplication.model.networktransport.MessageTransport;
import no.stonedstonar.chatapplication.model.networktransport.UserRequest;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents the logic that the server class should hold.
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class Server {

    private ServerSocket welcomeSocket;

    private volatile UserRegister userRegister;

    private volatile ConversationRegister conversationRegister;

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.run();
        }catch (IllegalArgumentException exception){
            System.out.println("Exception in main run method: " + exception.getMessage());
        }
    }

    /**
      * Makes an instance of the Server class.
      */
    public Server(){
        userRegister = new UserRegister();
        conversationRegister = new ConversationRegister();

        User user = new User("bjarne22", "passr");
        User user1 = new User("fjell", "passord");
        User user3 = new User("bass", "thepass");
        userRegister.addUser(user);
        userRegister.addUser(user1);
        userRegister.addUser(user3);
        List<String> twoMembers = new ArrayList<>();
        twoMembers.add(user.getUsername());
        twoMembers.add(user1.getUsername());
        List<String> threeMembers = new ArrayList<>();
        threeMembers.add(user.getUsername());
        threeMembers.add(user1.getUsername());
        threeMembers.add(user3.getUsername());
        conversationRegister.addNewMessageLogWithUsernames(threeMembers);
        conversationRegister.addNewMessageLogWithUsernames(twoMembers);
        List<MessageLog> messageLogs = conversationRegister.getAllMessageLogsOfUsername("bjarne22");
        messageLogs.get(0).addMessage(new TextMessage("Haha", "bjarne22"));
        messageLogs.get(0).addMessage(new TextMessage("Nope", "fjell"));
        messageLogs.get(0).addMessage(new TextMessage("So funny bjarne", "bass"));

        try {
            welcomeSocket = new ServerSocket(1380);
        }catch (IOException exception){
            System.out.println("Could not open a socket. Please try again.");
        }
    }

    /**
     * Makes the server run and accept incoming communication.
     */
    public void run(){
        boolean run = true;
        try {
            while (run){
                Socket client = welcomeSocket.accept();
                System.out.println("Message recived from a new client.");
                Thread clientThread = new Thread(() -> {
                    handleConnection(client);
                });
                clientThread.start();
            }
            System.out.println("Server shutting down");
        }catch (IOException exception){
            System.out.println("FUCK");
        }
    }

    /**
     * Handles the connection of a client.
     * @param socket the socket that this connection is about.
     */
    private void handleConnection(Socket socket){
        try {
            Object object = getObject(socket);
            if(object instanceof MessageTransport messageTransport){
                handleMessage(messageTransport);
            }else if (object instanceof UserRequest userRequest){
                handleUserInteraction(userRequest, socket);
            }else {
                throw new IllegalArgumentException("NONE IS A VALID OBJECT");
            }
            try {
                socket.close();
            }catch (IOException exception) {
                exception.printStackTrace();
            }
        }catch (IllegalArgumentException exception){
            sendObject(exception, socket);
        }

    }

    /**
     * Handles a message when it comes in.
     * @param messageTransport the message transport the message comes in.
     */
    private void handleMessage(MessageTransport messageTransport){
        System.out.print("New message");
        MessageLog messageLog = conversationRegister.getMessageLogByLogNumber(messageTransport.getMessageLogNumber());
        messageLog.addMessage(messageTransport.getMessage());
        System.out.print("The message log is now " + messageLog.getMessageList().size() + " messages long.");
    }



    /**
     *
     * @param userRequest
     */
    private void handleUserInteraction(UserRequest userRequest, Socket socket){
        //Todo: Skal gjøre det mulig å logge inn med brukeren.
        try {
            if (userRequest.isLogin()){
                User user = userRegister.login(userRequest.getUsername(), userRequest.getPassword());
                List<MessageLog> messageLogs = conversationRegister.getAllMessageLogsOfUsername(user.getUsername());
                LoginTransport loginTransport = new LoginTransport(user, messageLogs);
                sendObject(loginTransport, socket);
            }else if (userRequest.isNewUser()){
                User user = new User(userRequest.getUsername(), userRequest.getPassword());
                userRegister.addUser(user);
                List<MessageLog> messageLogs = conversationRegister.getAllMessageLogsOfUsername(user.getUsername());
                LoginTransport loginTransport = new LoginTransport(user, messageLogs);
                sendObject(loginTransport, socket);
            } else if (userRequest.isCheckUsername()){
                sendObject(userRegister.checkIfUsernameIsTaken(userRequest.getUsername()), socket);
            }
        }catch (IllegalArgumentException exception){
            sendObject(exception, socket);
        }
    }

    /**
     * Sends an object through the socket.
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
