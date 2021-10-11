package no.stonedstonar.chatapplication.frontend;

import no.stonedstonar.chatapplication.model.*;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotAddMemberException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotAddMessageLogException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotGetMessageLogException;
import no.stonedstonar.chatapplication.model.exception.textmessage.CouldNotAddTextMessageException;
import no.stonedstonar.chatapplication.model.exception.textmessage.CouldNotGetTextMessageException;
import no.stonedstonar.chatapplication.model.exception.user.CouldNotAddUserException;
import no.stonedstonar.chatapplication.model.exception.user.CouldNotLoginToUserException;
import no.stonedstonar.chatapplication.model.networktransport.LoginTransport;
import no.stonedstonar.chatapplication.model.networktransport.MessageLogRequest;
import no.stonedstonar.chatapplication.model.networktransport.MessageTransport;
import no.stonedstonar.chatapplication.model.networktransport.UserRequest;
import no.stonedstonar.chatapplication.model.networktransport.builder.MessageLogRequestBuilder;
import no.stonedstonar.chatapplication.model.networktransport.builder.UserRequestBuilder;

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

    private PersonalConversationRegister personalConversationRegister;

    private Logger logger;

    private String localHost;

    private int portNumber;

    /**
      * Makes an instance of the ChatClient class.
      */
    public ChatClient(){
        logger = Logger.getLogger(getClass().toString());
        localHost = "localhost";
        portNumber = 1380;

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
     * Makes a new conversation.
     * @param usernames the names of all the members of the new conversation.
     * @return the message log that this conversation is now.
     * @throws CouldNotAddMessageLogException gets thrown if the message log could not be added.
     * @throws CouldNotAddMemberException gets thrown if the members could not be added.
     * @throws IOException gets thrown if something goes wrong with the socket.
     * @throws ClassNotFoundException gets thrown if the class could not be found for that object.
     */
    public void makeNewConversation(List<String> usernames) throws CouldNotAddMessageLogException, CouldNotAddMemberException, IOException, ClassNotFoundException {
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
        }catch (CouldNotAddMemberException | CouldNotAddMessageLogException | ClassNotFoundException exception){
            String message = "Something has gone wrong on the serverside " + exception.getClass() + " exception content: " + exception.getMessage();
            logger.log(Level.WARNING, message);
            throw exception;
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
     * @throws IOException gets thrown if something fails in the socket.
     * @throws ClassNotFoundException gets thrown if the class could not be found for that object.
     */
    public TextMessage sendMessage(String messageContents, MessageLog messageLog) throws IOException, CouldNotAddTextMessageException, CouldNotGetMessageLogException, ClassNotFoundException {
        checkString(messageContents, "message");
        checkIfObjectIsNull(messageLog, "message log");
        //Todo: Add a function to check if the message was added successfully.And alter the documentation.
        try (Socket socket = new Socket(localHost, portNumber)){
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
            throw e;
        }catch (CouldNotAddTextMessageException | ClassNotFoundException | CouldNotGetMessageLogException exception){
            String message = "Something has gone wrong on the serverside " + exception.getClass() + " exception content: " + exception.getMessage();
            logger.log(Level.WARNING, message);
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
     * @throws ClassNotFoundException gets thrown if the class could not be found for that object.
     */
    public void loginToUser(String username, String password) throws IOException, CouldNotLoginToUserException, ClassNotFoundException, CouldNotAddMessageLogException {;
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
            }else if (object instanceof CouldNotAddMessageLogException exception){
                throw exception;
            }
        } catch (CouldNotLoginToUserException | IllegalArgumentException | IOException | ClassNotFoundException | CouldNotAddMessageLogException exception) {
            String message = "Something has gone wrong on the serverside " + exception.getClass() + " exception content: " + exception.getMessage();
            logger.log(Level.WARNING, message);
            throw exception;
        }
    }

    public void logOutOfUser(){

    }

    /**
     * Checks if the username is taken on the sersver.
     * @param username the username of the person you want to check.
     * @return <code>true</code> if the username is taken by another user.
     *         <code>false</code> if the username is not taken by another user.
     * @throws IOException gets thrown if the socket failed to be made
     * @throws ClassNotFoundException gets thrown if the class could not be found for that object.
     */
    public boolean checkUsername(String username) throws IOException, ClassNotFoundException {
        checkString(username, "username");
        try (Socket socket = new Socket(localHost, portNumber)){
            UserRequest userRequest = new UserRequestBuilder().setUsername(username).setCheckUsername(true).build();
            sendObject(userRequest, socket);
            Object object = getObject(socket);
            if (object instanceof Boolean valid){
                return valid;
            }else if (object instanceof IllegalArgumentException exception){
                throw exception;
            }else{
                throw new IllegalArgumentException("The response form the server is of a invalid format.");
            }
        }catch (IOException | IllegalArgumentException | ClassNotFoundException exception){
            String message = "Something has gone wrong on the serverside " + exception.getClass() + " exception content: " + exception.getMessage();
            logger.log(Level.WARNING, message);
            throw exception;
        }
    }

    /**
     * Contacts the server and makes a new user if the username is not taken.
     * @param username the username the user wants.
     * @param password the password of the user.
     * @throws IOException gets thrown if the socket failed to be made
     * @throws ClassNotFoundException gets thrown if the class could not be found for that object.
     */
    public void makeNewUser(String username, String password) throws IOException, ClassNotFoundException, CouldNotLoginToUserException, CouldNotAddUserException {
        try (Socket socket = new Socket(localHost, 1380)){
            checkString(username, "username");
            checkString(password, "password");
            UserRequest userRequest = new UserRequestBuilder().setNewUser(true).setUsername(username).setPassword(password).build();
            sendObject(userRequest, socket);
            Object object = getObject(socket);
            if (object instanceof IllegalArgumentException exception){
                throw exception;
            }else if (object instanceof CouldNotAddUserException exception) {
                throw exception;
            }
        }catch (IOException | IllegalArgumentException | ClassNotFoundException | CouldNotAddUserException exception){
            //Todo: lag en handler
            String message = "Something has gone wrong on the serverside " + exception.getClass() + " exception content: " + exception.getMessage();
            logger.log(Level.WARNING, message);
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
     * @throws ClassNotFoundException gets thrown if the class could not be found for that object.
     */
    private Object getObject(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
        Object object = objectInputStream.readObject();
        checkIfObjectIsNull(object, "object");
        return object;
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
