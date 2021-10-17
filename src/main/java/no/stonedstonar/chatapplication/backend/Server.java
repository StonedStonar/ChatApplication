package no.stonedstonar.chatapplication.backend;

import javafx.application.Platform;
import no.stonedstonar.chatapplication.model.conversation.ConversationRegister;
import no.stonedstonar.chatapplication.model.exception.InvalidResponseException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotAddMemberException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotAddMessageLogException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotGetMessageLogException;
import no.stonedstonar.chatapplication.model.exception.message.CouldNotAddMessageException;
import no.stonedstonar.chatapplication.model.exception.user.CouldNotAddUserException;
import no.stonedstonar.chatapplication.model.exception.user.CouldNotLoginToUserException;
import no.stonedstonar.chatapplication.model.messagelog.NormalMessageLog;
import no.stonedstonar.chatapplication.model.message.TextMessage;
import no.stonedstonar.chatapplication.model.networktransport.*;
import no.stonedstonar.chatapplication.model.User;
import no.stonedstonar.chatapplication.model.UserRegister;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class that represents the logic that the server class should hold.
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class Server{

    private ServerSocket welcomeSocket;

    private volatile UserRegister userRegister;

    private volatile ConversationRegister conversationRegister;

    private volatile Logger logger;

    private boolean run;

    private ExecutorService executors;

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }

    /**
      * Makes an instance of the Server class.
      */
    public Server(){
        logger = Logger.getLogger(getClass().toString());
        userRegister = new UserRegister();
        conversationRegister = new ConversationRegister();
        run = true;
        executors = Executors.newFixedThreadPool(8);
        try {
            welcomeSocket = new ServerSocket(1380);
        }catch (IOException exception){
            logEvent(Level.SEVERE, "Could not open the server socket. Please restart the server.");
        }
    }

    /**
     * Adds all the test data needed.
     */
    private void addTestData(){
        try {
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
            conversationRegister.addNewConversationWithUsernames(threeMembers, "");
            conversationRegister.addNewConversationWithUsernames(twoMembers, "");
            List<NormalMessageLog> normalMessageLogs = conversationRegister.getAllConversationsOfUsername("bjarne22");
            normalMessageLogs.get(0).addMessage(new TextMessage("Haha", "bjarne22"));
            normalMessageLogs.get(0).addMessage(new TextMessage("Nope", "fjell"));
            normalMessageLogs.get(0).addMessage(new TextMessage("So funny bjarne", "bass"));
        }catch (Exception exception){
            String message = "The test data could not be added " + exception.getClass() + " exception message: " + exception.getMessage();
            logEvent(Level.SEVERE, message);
        }
    }

    /**
     * Makes the server run and accept incoming communication.
     */
    public void run(){
        addTestData();
        try {
            while (run){
                Socket client = welcomeSocket.accept();
                logEvent(Level.FINE, "Message received from a new client.");
                executors.submit(()->{
                    try {
                        handleConnection(client);
                    }catch (IOException | InvalidResponseException exception){
                        if (!client.isClosed()){
                            try {
                                client.close();
                            } catch (IOException e) {
                                logEvent(Level.SEVERE, "A client connection cannot be closed.");
                            }
                        }
                    }
                });

            }
        }catch (IOException exception){
            String message = "The server has crashed and gotten the following exception class: " + exception.getClass() + " and message: " + exception.getMessage();
            logEvent(Level.SEVERE, message);
        }
    }

    /**
     * Stops the server
     */
    public synchronized void stopServer(){
        Platform.runLater(()->{
            run = false;
            executors.shutdown();
            while (!executors.isTerminated()){
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {

                }
            }
            logEvent(Level.FINE, "Server is shutting down.");
        });
    }

    /**
     * Handles the connection of a client.
     * @param socket the socket that this connection is about.
     * @throws IOException gets thrown if the socket closes or cannot finish its task.
     * @throws InvalidResponseException gets thrown if the class cannot be found.
     */
    private void handleConnection(Socket socket) throws IOException, InvalidResponseException {
        do{
            Object object = getObject(socket);
            if(object instanceof MessageTransport messageTransport){
                handleIncomingMessage(messageTransport, socket);
            }else if (object instanceof UserRequest userRequest){
                handleUserInteraction(userRequest, socket);
            }else if (object instanceof MessageLogRequest messageLogRequest){
                handleMessageLogInteraction(messageLogRequest, socket);
            }else if (object instanceof SetKeepAliveRequest setKeepAliveRequest) {
                socket.setKeepAlive(setKeepAliveRequest.isKeepAlive());
            }else {
                throw new IllegalArgumentException("NONE IS A VALID OBJECT");
            }
        }while (socket.getKeepAlive());
    }

    /**
     * Handles a message when it comes in.
     * @param messageTransport the message transport the message comes in.
     * @param socket the socket that gets the sends.
     * @throws IOException gets thrown if the socket closes or cannot finish its task.
     */
    private void handleIncomingMessage(MessageTransport messageTransport, Socket socket) throws IOException {
        System.out.println("New message to add to a message log");
        try {
            NormalMessageLog normalMessageLog = conversationRegister.getConversationByNumber(messageTransport.getMessageLogNumber());
            List<TextMessage> newMessages = messageTransport.getMessages();
            normalMessageLog.addAllMessages(newMessages);
            System.out.println("The message log is now " + normalMessageLog.getMessageList().size() + " messages long.");
            sendObject(messageTransport, socket);
        }catch (CouldNotAddMessageException | CouldNotGetMessageLogException exception){
            String message = "Something went wrong in adding " + messageTransport.getMessages().size() + " with the exception " + exception.getMessage() + " and class " + exception.getClass();
            logEvent(Level.WARNING, message);
            sendObject(exception, socket);
        }
    }

    /**
     * Handles the interaction that comes with the user part of this server.
     * @param userRequest the user request that was received.
     * @param socket the socket this user request is from.
     * @throws IOException gets thrown if the socket closes or cannot finish its task.
     */
    private void handleUserInteraction(UserRequest userRequest, Socket socket) throws IOException{
        try {
            if (userRequest.isLogin()){
                User user = userRegister.login(userRequest.getUsername(), userRequest.getPassword());
                List<NormalMessageLog> normalMessageLogs = conversationRegister.getAllConversationsOfUsername(user.getUsername());
                LoginTransport loginTransport = new LoginTransport(user, normalMessageLogs);
                sendObject(loginTransport, socket);
            }else if (userRequest.isNewUser()){
                User user = new User(userRequest.getUsername(), userRequest.getPassword());
                userRegister.addUser(user);
                sendObject(true, socket);
            } else if (userRequest.isCheckUsername()){
                sendObject(userRegister.checkIfUsernameIsTaken(userRequest.getUsername()), socket);
            }
        }catch (CouldNotLoginToUserException | IllegalArgumentException | CouldNotAddUserException exception){
            String message = "Something went wrong in the " + userRequest + " with the exception " + exception.getMessage() + " and class " + exception.getClass();
            logEvent(Level.WARNING, message);
            sendObject(exception, socket);
        }
    }

    /**
     * Handles the message log request objects.
     * @param messageLogRequest the message log request to handle.
     * @param socket the socket the communication is happening over.
     * @throws IOException gets thrown if the socket closes or cannot finish its task.
     */
    private void handleMessageLogInteraction(MessageLogRequest messageLogRequest, Socket socket) throws IOException {
        try {
            if (messageLogRequest.isNewMessageLog()){
                makeNewMessageLog(messageLogRequest, socket);
            }else if (messageLogRequest.isCheckForMessages()){
                checkForNewMessagesInMessageLog(messageLogRequest, socket);
            }else if (messageLogRequest.isAddMembers()){
                addNewMembersToMessageLog(messageLogRequest, socket);
            }else if (messageLogRequest.isRemoveMembers()){
                
            }
        }catch (CouldNotAddMessageLogException | CouldNotAddMemberException | CouldNotGetMessageLogException exception) {
            String message = "Something went wrong in the " + messageLogRequest + " with the exception " + exception.getMessage() + " and class " + exception.getClass();
            logEvent(Level.WARNING, message);
            sendObject(exception, socket);
        }
    }

    /**
     * Makes a new message log based on the request.
     * @param messageLogRequest the request for making a new message log.
     * @param socket the socket this message log is being made over.
     * @throws IOException gets thrown if the socket closes or cannot finish its task.
     * @throws CouldNotAddMessageLogException gets thrown if the message log could not be added.
     * @throws CouldNotAddMemberException gets thrown if a member could not be added.
     */
    private void makeNewMessageLog(MessageLogRequest messageLogRequest, Socket socket) throws IOException, CouldNotAddMessageLogException, CouldNotAddMemberException {
        List<String> usernames = messageLogRequest.getUsernames();
        String nameOfMessageLog = messageLogRequest.getNameOfMessageLog();
        NormalMessageLog normalMessageLog = conversationRegister.addNewConversationWithUsernames(usernames, nameOfMessageLog);
        sendObject(normalMessageLog, socket);
    }

    private void addNewMembersToMessageLog(MessageLogRequest messageLogRequest, Socket socket){

    }

    /**
     * Checks if a message log has new messages compared to the messagelog request.
     * @param messageLogRequest the request that wants messages for a message log.
     * @param socket socket that the communication is happening over.
     * @throws CouldNotGetMessageLogException gets thrown if the message log could not be found.
     * @throws IOException gets thrown if something goes wrong with the communication.
     */
    private void checkForNewMessagesInMessageLog(MessageLogRequest messageLogRequest, Socket socket) throws CouldNotGetMessageLogException, IOException {
        NormalMessageLog normalMessageLog = conversationRegister.getConversationByNumber(messageLogRequest.getMessageLogNumber());
        List<TextMessage> textMessageList = normalMessageLog.checkForNewMessages(messageLogRequest.getListSize());
        MessageTransport messageTransport = new MessageTransport(textMessageList, normalMessageLog);
        sendObject(messageTransport, socket);
    }

    /**
     * Sends an object through the socket.
     * @param object the object you want to send.
     * @param socket the socket the object should go through.
     */
    private void sendObject(Object object, Socket socket) throws IOException {
        try {
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(object);
        }catch (IOException exception){
            String message = "Object could not be sent. " + object.getClass();
            logEvent(Level.WARNING, message);
            throw exception;
        }
    }

    /**
     * Gets the message that is sent through the socket.
     * @param socket the socket that this message is coming through.
     * @return the message this socket contains.
     * @throws IOException gets thrown if the object could not be received.
     * @throws InvalidResponseException gets thrown if the class could not be found for that object.
     */
    private Object getObject(Socket socket) throws IOException, InvalidResponseException {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            Object object = objectInputStream.readObject();
            checkIfObjectIsNull(object, "object");
            return object;
        }catch (ClassNotFoundException exception){
            throw new InvalidResponseException("The class of the response was invalid.");
        }
    }

    /**
     * Logs an error or event in this server.
     * @param level the level the message should have.
     * @param message the message.
     */
    private synchronized void logEvent(Level level, String message ){
        logger.log(level, message);
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
