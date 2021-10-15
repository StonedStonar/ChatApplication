package no.stonedstonar.chatapplication.frontend;

import no.stonedstonar.chatapplication.model.conversation.PersonalConversationRegister;
import no.stonedstonar.chatapplication.model.exception.InvalidResponseException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotAddMemberException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotAddMessageLogException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotGetMessageLogException;
import no.stonedstonar.chatapplication.model.exception.textmessage.CouldNotAddTextMessageException;
import no.stonedstonar.chatapplication.model.exception.textmessage.CouldNotGetTextMessageException;
import no.stonedstonar.chatapplication.model.exception.user.CouldNotAddUserException;
import no.stonedstonar.chatapplication.model.exception.user.CouldNotLoginToUserException;
import no.stonedstonar.chatapplication.model.message.MessageLog;
import no.stonedstonar.chatapplication.model.message.PersonalMessageLog;
import no.stonedstonar.chatapplication.model.message.TextMessage;
import no.stonedstonar.chatapplication.model.networktransport.*;
import no.stonedstonar.chatapplication.model.networktransport.builder.MessageLogRequestBuilder;
import no.stonedstonar.chatapplication.model.networktransport.builder.UserRequestBuilder;
import no.stonedstonar.chatapplication.model.User;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class that represents the logic that the chat client should hold.
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class ChatClient {

    private User user;

    private volatile PersonalConversationRegister personalConversationRegister;

    private Logger logger;

    private String localHost;

    private int portNumber;

    private long messageLogFocus;

    private Thread checkForMessagesThread;

    private boolean runThread;

    private boolean threadStopped;

    /**
      * Makes an instance of the ChatClient class.
      */
    public ChatClient(){
        logger = Logger.getLogger(getClass().toString());
        //"46.212.111.100"
        localHost = "localhost";
        portNumber = 1380;
        messageLogFocus = 0;
        threadStopped = true;
    }

    /**
     * Logs the user out and clears all the data.
     */
    public void logOutOfUser(){
        stopThread();
        messageLogFocus = 0;
        while (!threadStopped){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                logWaringError(e);
            }
        }
        user = null;
        personalConversationRegister = null;
    }

    /**
     * Sets the message log that is active in the gui. Starts a listening thread.
     * @param messageLogNumber the message log you want to listen for new messages on.
     */
    public void setMessageLogFocus(long messageLogNumber){
        System.out.println("Setting log number.");
        stopThread();
        while(!threadStopped){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        messageLogFocus = messageLogNumber;
        try {
            threadStopped = false;
            runThread = true;
            PersonalMessageLog personalMessageLog = getMessageLogByLongNumber(messageLogFocus);
            checkForMessagesThread =  new Thread(() ->{
                checkCurrentMessageLogForUpdates(personalMessageLog);
            });
            checkForMessagesThread.start();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the run boolean to false and interrupts the thread.
     */
    public void stopThread(){
        runThread = false;
    }

    /**
     * Checks if the listening thread is stopped.
     * @return <code>true</code> if the thread is stopped.
     *         <code>false</code> if the thread is still running.
     */
    public boolean checkIfThreadIsStopped(){
        return threadStopped;
    }

    /**
     * Checks the current message log for new messages.
     * @param personalMessageLog the active message log.
     */
    public void checkCurrentMessageLogForUpdates(PersonalMessageLog personalMessageLog){
        //Todo: sette opp slik at hvis tiden går så og så mye skal alle loggene sjekkes etter oppdateringer.
        int count = 0;
        do {
            try {
                if (count > 2){
                    checkForNewMessages();
                    count = 0;
                }else {
                    Socket socket = new Socket(localHost, portNumber);
                    checkMessageLogForNewMessages(personalMessageLog, socket);
                    count += 1;
                }
                Thread.sleep(5000);
            }catch (CouldNotAddTextMessageException | IOException | InvalidResponseException | CouldNotGetMessageLogException | InterruptedException exception){
                logWaringError(exception);
                stopThread();
            }
        } while(runThread);
        threadStopped = true;
    }

    /**
     * Returns the message log of the user.
     * @return the personal message log of the user.
     */
    public List<PersonalMessageLog> getMessageLogs(){
        System.out.println(personalConversationRegister.getMessageLogList().size());
        return personalConversationRegister.getMessageLogList();
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
     * Logs the warning errors and throws the exception.
     * @param exception the exception you want to log.
     */
    private synchronized void logWaringError(Exception exception) {
        String message = "Something has gone wrong on the serverside " + exception.getClass() + " exception content: " + exception.getMessage();
        logger.log(Level.WARNING, message);
    }

    /**
     * Makes a new conversation.
     * @param usernames the names of all the members of the new conversation.
     * @return the message log that this conversation is now.
     * @throws CouldNotAddMessageLogException gets thrown if the message log could not be added.
     * @throws CouldNotAddMemberException gets thrown if the members could not be added.
     * @throws IOException gets thrown if something goes wrong with the socket.
     * @throws InvalidResponseException gets thrown if the class could not be found for that object or the response is a different object than expected.
     */
    public void makeNewConversation(List<String> usernames) throws CouldNotAddMessageLogException, CouldNotAddMemberException, IOException, InvalidResponseException {
        checkIfListIsEmptyOrNull(usernames, "usernames");
        try (Socket socket = new Socket("localhost", 1380)){
            MessageLogRequest messageLogRequest = new MessageLogRequestBuilder().setNewMessageLog(true).addUsernames(usernames).build();
            sendObject(messageLogRequest, socket);
            Object object = getObject(socket);
            if (object instanceof MessageLog messageLog){
                personalConversationRegister.addMessageLog(messageLog);
            }else if (object instanceof CouldNotAddMessageLogException exception){
                throw exception;
            }else if (object instanceof CouldNotAddMemberException exception){
                throw exception;
            }else {
                throw new IllegalArgumentException("The returned object is not what excepted.");
            }
        }catch (CouldNotAddMemberException | CouldNotAddMessageLogException | InvalidResponseException exception){
            logWaringError(exception);
            throw exception;
        }
    }

    /**
     * Sends a message to a person if this client is logged in.
     * @param messageContents the contents of the message.
     * @param messageLog the message log that holds the message log number.
     * @throws CouldNotGetTextMessageException gets thrown if the server could not add the message.
     * @throws CouldNotGetMessageLogException gets thrown if the server could not get the message log.
     * @throws IOException gets thrown if something fails in the socket.
     * @throws InvalidResponseException gets thrown if the class could not be found for that object or the response is a different object than expected.
     */
    public void sendMessage(String messageContents, MessageLog messageLog) throws IOException, CouldNotAddTextMessageException, CouldNotGetMessageLogException, InvalidResponseException {
        checkString(messageContents, "message");
        checkIfObjectIsNull(messageLog, "message log");
        try (Socket socket = new Socket(localHost, portNumber)){
            TextMessage textMessage = new TextMessage(messageContents, user.getUsername());
            ArrayList<TextMessage> textMessageList = new ArrayList<>();
            textMessageList.add(textMessage);
            MessageTransport messageTransport = new MessageTransport(textMessageList, messageLog);
            sendObject(messageTransport, socket);
            Object object = getObject(socket);
            if (object instanceof  MessageTransport){
                messageLog.addMessage(textMessage);
            }else if (object instanceof CouldNotAddTextMessageException exception){
                throw exception;
            }else if (object instanceof CouldNotGetMessageLogException exception){
                throw exception;
            }else {
                throw new InvalidResponseException("The object is of a invalid format.");
            }
        } catch (IOException | CouldNotAddTextMessageException | InvalidResponseException | CouldNotGetMessageLogException exception){
            logWaringError(exception);
            throw exception;
        }
    }

    /**
     * Gets the message log that matches that long number.
     * @param messageLogNumber the number that message log has.
     * @return the message log that matches that number.
     * @throws CouldNotGetMessageLogException gets thrown if the message log could not be found.
     */
    public PersonalMessageLog getMessageLogByLongNumber(long messageLogNumber) throws CouldNotGetMessageLogException {
        return personalConversationRegister.getPersonalMessageLogByNumber(messageLogNumber);
    }

    /**
     * Logs the client in as a user.
     * @param username the username of the user.
     * @param password the password that the user has.
     * @throws IOException gets thrown if the socket failed to be made.
     * @throws CouldNotLoginToUserException gets thrown if the password or username is incorrect.
     * @throws InvalidResponseException gets thrown if the class could not be found for that object or the response is a different object than expected.
     */
    public void loginToUser(String username, String password) throws IOException, CouldNotLoginToUserException, InvalidResponseException {;
        checkString(username, "username");
        checkString(password, "password");
        try (Socket socket = new Socket(localHost, portNumber)){
            UserRequest userRequest = new UserRequestBuilder().setLogin(true).setUsername(username).setPassword(password).build();
            sendObject(userRequest, socket);
            Object object = getObject(socket);
            if (object instanceof LoginTransport loginTransport){
                this.user = loginTransport.getUser();
                personalConversationRegister = new PersonalConversationRegister(loginTransport.getMessageLogList());
            }else if (object instanceof CouldNotLoginToUserException exception){
                throw exception;
            }else if (object instanceof IllegalArgumentException exception){
                throw exception;
            }
        } catch (CouldNotLoginToUserException | IllegalArgumentException | IOException | InvalidResponseException exception) {
            String message = "Something has gone wrong on the serverside " + exception.getClass() + " exception content: " + exception.getMessage();
            logger.log(Level.WARNING, message);
            throw exception;
        }
    }

    /**
     * Checks all the message logs for new messages.
     * @throws IOException gets thrown if the socket failed to be made.
     * @throws InvalidResponseException gets thrown if the class could not be found for that object or the response is a different object than expected.
     * @throws CouldNotAddTextMessageException gets thrown if the text message could not be added.
     * @throws CouldNotGetMessageLogException gets thrown if the server can't find the message log.
     */
    //Todo: Finn en berdre måte å synkronisere meldinger på. De kan sende meldinger likt og da burde man kanskje ta basis i den siste meldingen som ble sendt fra lista?
    // Det som kan funke er å ha en liste for hver person i samtalen. Så de er separate.
    // Da er det dermed umulig at to meldinger kommer likt og ingen av dem får oppdateringen. 
    public void checkForNewMessages() throws CouldNotAddTextMessageException, IOException, InvalidResponseException, CouldNotGetMessageLogException {
        try (Socket socket = new Socket(localHost, portNumber)){
            socket.setKeepAlive(true);
            SetKeepAliveRequest setKeepAliveRequest = new SetKeepAliveRequest(true);
            sendObject(setKeepAliveRequest, socket);
            List<PersonalMessageLog> messageLogList = personalConversationRegister.getMessageLogList();
            Iterator<PersonalMessageLog> it = messageLogList.iterator();
            while(it.hasNext()) {
                PersonalMessageLog log = it.next();
                checkMessageLogForNewMessages(log, socket);
                System.out.println("While");
            }
            SetKeepAliveRequest notKeepAlive = new SetKeepAliveRequest(false);
            sendObject(notKeepAlive, socket);
            System.out.println("Closing socket");
        } catch (IOException | InvalidResponseException | CouldNotAddTextMessageException | CouldNotGetMessageLogException exception) {
            logWaringError(exception);
            throw exception;
        }
    }

    /**
     * Checks for new messages and adds them if need be.
     * @param log the message log you want to check.
     * @param socket the socket the communication happens over.
     * @throws IOException gets thrown if the socket failed to be made.
     * @throws InvalidResponseException gets thrown if the class could not be found for that object or the response is a different object than expected.
     * @throws CouldNotAddTextMessageException gets thrown if the text message could not be added to the local message log.
     * @throws CouldNotGetMessageLogException gets thrown if the server cant find the message log.
     */
    public synchronized void checkMessageLogForNewMessages(PersonalMessageLog log, Socket socket) throws IOException, CouldNotAddTextMessageException, CouldNotGetMessageLogException, InvalidResponseException {
        try {
            int size = log.getMessageList().size();
            long messageLogNumber = log.getMessageLogNumber();
            MessageLogRequest messageLogRequest = new MessageLogRequestBuilder().setCheckForMessages(true).addListSize(size).addMessageLogNumber(messageLogNumber).build();
            System.out.println(messageLogNumber);
            sendObject(messageLogRequest, socket);
            Object object = getObject(socket);
            if(object instanceof MessageTransport messageTransport){
                List<TextMessage> textMessageList = messageTransport.getMessages();
                if (!textMessageList.isEmpty()){
                    log.addAllMessages(textMessageList);
                }
            }else if(object instanceof CouldNotGetMessageLogException exception){
                throw exception;
            }else if(object instanceof IllegalArgumentException exception){
                throw exception;
            }else{
                throw new InvalidResponseException("The response from the server was invalid format.");
            }
        }catch (CouldNotGetMessageLogException | IllegalArgumentException | CouldNotAddTextMessageException | IOException | InvalidResponseException exception){
            logWaringError(exception);
            throw exception;
        }
    }

    /**
     * Checks if the username is taken on the sersver.
     * @param username the username of the person you want to check.
     * @return <code>true</code> if the username is taken by another user.
     *         <code>false</code> if the username is not taken by another user.
     * @throws IOException gets thrown if the socket failed to be made
     * @throws InvalidResponseException gets thrown if the class could not be found for that object or the response is a different object than expected.
     */
    public boolean checkUsername(String username) throws IOException, InvalidResponseException{
        checkString(username, "username");
        try (Socket socket = new Socket(localHost, portNumber)){
            UserRequest userRequest = new UserRequestBuilder().setUsername(username).setCheckUsername(true).build();
            sendObject(userRequest, socket);
            Object object = getObject(socket);
            if (object instanceof Boolean valid){
                return valid;
            }else if (object instanceof IllegalArgumentException exception){
                throw exception;
            }else {
                //Todo: Vurder en methode som sjekker flere ganger at responsen er responsen. Det Arne forklarte
                throw new InvalidResponseException("The response form the server is of a invalid format.");
            }
        }catch (IOException | IllegalArgumentException | InvalidResponseException exception){
            logWaringError(exception);
            throw exception;
        }
    }

    /**
     * Contacts the server and makes a new user if the username is not taken.
     * @param username the username the user wants.
     * @param password the password of the user.
     * @throws IOException gets thrown if the socket failed to be made
     * @throws InvalidResponseException gets thrown if the class could not be found for that object or the response is a different object than expected.
     */
    public void makeNewUser(String username, String password) throws IOException, CouldNotAddUserException, InvalidResponseException {
        try (Socket socket = new Socket(localHost, 1380)){
            checkString(username, "username");
            checkString(password, "password");
            UserRequest userRequest = new UserRequestBuilder().setNewUser(true).setUsername(username).setPassword(password).build();
            sendObject(userRequest, socket);
            Object object = getObject(socket);
            if (!(object instanceof Boolean)){
                if (object instanceof IllegalArgumentException exception){
                    throw exception;
                }else if (object instanceof CouldNotAddUserException exception) {
                    throw exception;
                }else {
                    throw new InvalidResponseException("The response from the sever was of the wrong class.");
                }
            }
        }catch (IOException | IllegalArgumentException | CouldNotAddUserException | InvalidResponseException exception){
            logWaringError(exception);
            throw exception;
        }
    }



    /**
     * Sends a object through the socket.
     * @param object the object you want to send.
     * @param socket the socket the object should go through.
     * @throws IOException gets thrown if the object could not be sent.
     */
    private void sendObject(Object object, Socket socket) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(object);
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
     * Checks if the list is empty or null.
     * @param list the list you want to check.
     * @param prefix the prefix the exception should have.
     */
    private void checkIfListIsEmptyOrNull(List list, String prefix){
        checkIfObjectIsNull(list, prefix);
        if (list.isEmpty()){
            throw new IllegalArgumentException("The " + prefix + " cannot be zero in size.");
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
