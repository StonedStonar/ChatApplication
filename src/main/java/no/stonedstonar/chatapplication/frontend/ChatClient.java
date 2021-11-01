package no.stonedstonar.chatapplication.frontend;

import no.stonedstonar.chatapplication.model.conversation.ObservableConversation;
import no.stonedstonar.chatapplication.model.conversationregister.personal.NormalPersonalConversationRegister;
import no.stonedstonar.chatapplication.model.conversationregister.personal.PersonalConversationRegister;
import no.stonedstonar.chatapplication.model.exception.InvalidResponseException;
import no.stonedstonar.chatapplication.model.exception.conversation.CouldNotAddConversationException;
import no.stonedstonar.chatapplication.model.exception.conversation.CouldNotGetConversationException;
import no.stonedstonar.chatapplication.model.exception.conversation.UsernameNotPartOfConversationException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotAddMemberException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotAddMessageLogException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotGetMessageLogException;
import no.stonedstonar.chatapplication.model.exception.message.CouldNotAddMessageException;
import no.stonedstonar.chatapplication.model.exception.message.CouldNotGetMessageException;
import no.stonedstonar.chatapplication.model.exception.user.CouldNotAddUserException;
import no.stonedstonar.chatapplication.model.exception.user.CouldNotLoginToUserException;
import no.stonedstonar.chatapplication.model.member.ConversationMember;
import no.stonedstonar.chatapplication.model.member.Member;
import no.stonedstonar.chatapplication.model.message.Message;
import no.stonedstonar.chatapplication.model.message.TextMessage;
import no.stonedstonar.chatapplication.model.user.User;
import no.stonedstonar.chatapplication.network.requests.*;
import no.stonedstonar.chatapplication.network.requests.builder.ConversationRequestBuilder;
import no.stonedstonar.chatapplication.network.requests.builder.MembersRequestBuilder;
import no.stonedstonar.chatapplication.network.requests.builder.MessageRequestBuilder;
import no.stonedstonar.chatapplication.network.requests.builder.UserRequestBuilder;
import no.stonedstonar.chatapplication.network.transport.LoginTransport;
import no.stonedstonar.chatapplication.network.transport.MemberTransport;
import no.stonedstonar.chatapplication.network.transport.MessageTransport;
import no.stonedstonar.chatapplication.network.transport.PersonalConversationTransport;

import java.io.*;
import java.net.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class that represents the logic that the chat client should hold.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class ChatClient {

    private User endUser;

    private volatile NormalPersonalConversationRegister personalConversationRegister;

    private Logger logger;

    private String host;

    private int portNumber;

    private long messageLogFocus;

    private boolean runCheckForMessageThread;

    private ExecutorService executors;

    /**
      * Makes an instance of the ChatClient class.
      */
    public ChatClient(){
        executors = Executors.newFixedThreadPool(3);
        logger = Logger.getLogger(getClass().toString());
        host = "localhost";
        portNumber = 1380;
        messageLogFocus = 0;
    }

    /**
     * Logs the user out and clears all the data.
     */
    public void logOutOfUser(){
        stopCheckingForMessages();
        messageLogFocus = 0;
        while (!executors.isTerminated()){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                logWaringError(e);
            }
        }
        endUser = null;
        personalConversationRegister = null;
    }

    /**
     * Gets the personal conversation register.
     * @return the personal conversation regsiter.
     */
    public PersonalConversationRegister getPersonalConversationRegister(){
        return personalConversationRegister;
    }

    /**
     * Sets the message log that is active in the gui. Starts a listening thread.
     * @param messageLogNumber the message log you want to listen for new messages on.
     */
    public void setMessageLogFocus(long messageLogNumber) {
        stopCheckingForMessages();
        messageLogFocus = messageLogNumber;

        runCheckForMessageThread = true;
        try {
            ObservableConversation personalMessageLog = getConversationByNumber(messageLogFocus);
            executors.submit(() -> {
                checkCurrentMessageLogForUpdates(personalMessageLog);
            });
        } catch (CouldNotGetConversationException e) {
            logWaringError(e);
        }
    }

    /**
     * Sets the run boolean to false and interrupts the thread.
     */
    public void stopCheckingForMessages(){
        runCheckForMessageThread = false;
    }

    /**
     * Stops all the threads.
     */
    public void stopAllThreads(){
        runCheckForMessageThread = false;
        executors.shutdown();
    }

    /**
     * Checks if the listening thread is stopped.
     * @return <code>true</code> if the thread is stopped.
     *         <code>false</code> if the thread is still running.
     */
    public boolean checkIfThreadIsStopped(){
        return executors.isShutdown();
    }

    /**
     * Checks the current message log for new messages.
     * @param observableConversation the active message log.
     */
    public void checkCurrentMessageLogForUpdates(ObservableConversation observableConversation){
        int count = 0;
        do {
            try {
                if (count > 8){
                    checkForNewMessages();
                    checkForNewConversations();
                    count = 0;
                }else {
                    checkIfCurrentMessageLogHasNewMessages(observableConversation);
                    count += 1;
                }
                Thread.sleep(2000);
            }catch (CouldNotAddMessageException | IOException | InvalidResponseException | CouldNotGetMessageLogException | InterruptedException | UsernameNotPartOfConversationException | CouldNotAddConversationException exception){
                logWaringError(exception);
                stopCheckingForMessages();
            }
        } while(runCheckForMessageThread);
    }


    private void checkIfCurrentMessageLogHasNewMessages(ObservableConversation observableConversation) throws UsernameNotPartOfConversationException, IOException, CouldNotAddMessageException, InvalidResponseException, CouldNotGetMessageLogException {
        Socket socket = new Socket(host, portNumber);
        checkConversationForNewMessages(observableConversation, socket);
    }

    /**
     * Returns the message log of the user.
     * @return the personal message log of the user.
     */
    public List<ObservableConversation> getMessageLogs(){
        return personalConversationRegister.getConversationList();
    }

    /**
     * Gets the username of the user.
     * @return the username of the logged in user.
     */
    public String getUsername(){
        if (endUser != null){
            return endUser.getUsername();
        }else {
            throw new IllegalArgumentException("The user cannot be null.");
        }
    }

    /**
     * Gets the user of the application.
     * @return the user of this application.
     */
    public User getUser(){
        return endUser;
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
     * @param nameOfConversation the name that the conversation should have.
     * @return the message log that this conversation is now.
     * @throws CouldNotAddMessageLogException gets thrown if the message log could not be added.
     * @throws CouldNotAddMemberException gets thrown if the members could not be added.
     * @throws IOException gets thrown if something goes wrong with the socket.
     * @throws InvalidResponseException gets thrown if the class could not be found for that object or the response is a different object than expected.
     * @throws CouldNotAddConversationException gets thrown if the conversation could not be added.
     */
    public void makeNewConversation(List<String> usernames, String nameOfConversation) throws CouldNotAddMessageLogException, CouldNotAddMemberException, IOException, InvalidResponseException, CouldNotAddConversationException {
        checkIfListIsEmptyOrNull(usernames, "usernames");
        checkIfObjectIsNull(nameOfConversation, "name of conversation");
        List<Member> members = new ArrayList<>();
        usernames.forEach(name -> members.add(new ConversationMember(name)));
        try (Socket socket = new Socket("localhost", 1380)){
            ConversationRequestBuilder conversationRequestBuilder = new ConversationRequestBuilder().setNewConversation(true).addMembers(members);
            if (!nameOfConversation.isEmpty()){
                conversationRequestBuilder.addConversationName(nameOfConversation);
            }
            ConversationRequest conversationRequest = conversationRequestBuilder.build();
            sendObject(conversationRequest, socket);
            Object object = getObject(socket);
            if (object instanceof ObservableConversation observableConversation){

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
     * @param observableConversation the conversation that holds the conversation number.
     * @throws CouldNotGetMessageException gets thrown if the server could not add the message.
     * @throws CouldNotGetMessageLogException gets thrown if the server could not get the message log.
     * @throws IOException gets thrown if something fails in the socket.
     * @throws InvalidResponseException gets thrown if the class could not be found for that object or the response is a different object than expected.
     */
    public void sendMessage(String messageContents, ObservableConversation observableConversation) throws IOException, CouldNotAddMessageException, CouldNotGetMessageLogException, InvalidResponseException {
        checkString(messageContents, "message");
        checkIfObjectIsNull(observableConversation, "message log");
        try (Socket socket = new Socket(host, portNumber)){
            TextMessage textMessage = new TextMessage(messageContents, endUser.getUsername());
            List<MessageTransport> messageTransportList = new ArrayList<>();
            messageTransportList.add(new MessageTransport(textMessage, true));
            MessageRequest messageRequest = new MessageRequestBuilder().addMessageTransportList(messageTransportList).addConversationNumber(observableConversation.getConversationNumber()).build();
            sendObject(messageRequest, socket);
            Object object = getObject(socket);
            if (object instanceof  MessageRequest response){
                System.out.println("Message is now added.");
            }else if (object instanceof CouldNotAddMessageException exception){
                throw exception;
            }else if (object instanceof CouldNotGetMessageLogException exception){
                throw exception;
            }else {
                throw new InvalidResponseException("The object is of a invalid format.");
            }
        } catch (IOException | CouldNotAddMessageException | InvalidResponseException | CouldNotGetMessageLogException exception){
            logWaringError(exception);
            throw exception;
        }
    }

    /**
     * Gets the message log that matches that long number.
     * @param messageLogNumber the number that message log has.
     * @return the message log that matches that number.
     * @throws CouldNotGetConversationException gets thrown if the conversation could not be found.
     */
    public ObservableConversation getConversationByNumber(long messageLogNumber) throws CouldNotGetConversationException {
        return personalConversationRegister.getConversationByNumber(messageLogNumber);
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
        try (Socket socket = new Socket(host, portNumber)){
            UserRequest userRequest = new UserRequestBuilder().setLogin(true).setUsername(username).setPassword(password).build();
            sendObject(userRequest, socket);
            Object object = getObject(socket);
            if (object instanceof LoginTransport loginTransport){
                this.endUser = loginTransport.getUser();
                personalConversationRegister = loginTransport.getPersonalConversationRegister();
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
     *
     * @param namesToAdd
     * @param namesToRemove
     * @param conversationName
     */
    public void editConversation(List<String> namesToAdd, List<String> namesToRemove, String conversationName, ObservableConversation observableConversation){
        checkIfObjectIsNull(conversationName, "conversation name");
        checkIfObjectIsNull(namesToAdd, "names to add");
        checkIfObjectIsNull(namesToRemove, "names to remove");
        checkIfObjectIsNull(observableConversation, "personal conversation");
        try {
            if (!conversationName.isEmpty()){
                editConversationName(observableConversation, conversationName);
            }
            if (!namesToAdd.isEmpty() || !namesToRemove.isEmpty()){
                addOrRemoveMembers(observableConversation, namesToAdd, namesToRemove);
            }
        } catch (CouldNotGetConversationException exception) {
            exception.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (CouldNotAddMemberException exception) {
            exception.printStackTrace();
        }
    }

    private void editConversationName(ObservableConversation observableConversation, String newName){

    }

    /**
     * Sends a request to the server for adding and removing people from a conversation.
     * @param observableConversation
     * @param addNames
     * @param removeNames
     * @throws CouldNotGetConversationException
     * @throws IOException
     * @throws InvalidResponseException
     * @throws CouldNotAddMemberException
     */
    private void addOrRemoveMembers(ObservableConversation observableConversation, List<String> addNames, List<String> removeNames) throws CouldNotGetConversationException, IOException, InvalidResponseException, CouldNotAddMemberException {
        try (Socket socket = new Socket(host, portNumber)){
            List<MemberTransport> memberTransportList = new ArrayList<>();
            addNames.forEach(name -> memberTransportList.add(new MemberTransport(new ConversationMember(name), true)));
            removeNames.forEach(name -> memberTransportList.add(new MemberTransport(new ConversationMember(name), false)));
            MembersRequest membersRequest = new MembersRequestBuilder().addMemberTransports(memberTransportList).addConversationNumber(observableConversation.getConversationNumber()).addUsername(getUsername()).build();
            sendObject(membersRequest, socket);
            Object object = getObject(socket);
            if (object instanceof MembersRequest response){

            }else if (object instanceof CouldNotAddMemberException exception){
                throw exception;
            }else if (object instanceof CouldNotGetConversationException exception){
                throw exception;
            }else {
                throw new InvalidResponseException("The response from the server was invalid format.");
            }
        }catch (IOException | InvalidResponseException | CouldNotGetConversationException | CouldNotAddMemberException exception){
            logWaringError(exception);
            throw exception;
        }
    }

    /**
     * Checks all the message logs for new messages.
     * @throws IOException gets thrown if the socket failed to be made.
     * @throws InvalidResponseException gets thrown if the class could not be found for that object or the response is a different object than expected.
     * @throws CouldNotAddMessageException gets thrown if the text message could not be added.
     * @throws CouldNotGetMessageLogException gets thrown if the server can't find the message log.
     * @throws UsernameNotPartOfConversationException gets thrown if the user is not a part of the specified conversation.
     */
    private void checkForNewMessages() throws CouldNotAddMessageException, IOException, InvalidResponseException, CouldNotGetMessageLogException, UsernameNotPartOfConversationException {
        try (Socket socket = new Socket(host, portNumber)){
            socket.setKeepAlive(true);
            SetKeepAliveRequest setKeepAliveRequest = new SetKeepAliveRequest(true);
            sendObject(setKeepAliveRequest, socket);
            List<ObservableConversation> messageLogList = personalConversationRegister.getConversationList();
            Iterator<ObservableConversation> it = messageLogList.iterator();
            while(it.hasNext()) {
                ObservableConversation log = it.next();
                checkConversationForNewMessages(log, socket);
            }
            SetKeepAliveRequest notKeepAlive = new SetKeepAliveRequest(false);
            sendObject(notKeepAlive, socket);
        } catch (IOException | InvalidResponseException | CouldNotAddMessageException | CouldNotGetMessageLogException | UsernameNotPartOfConversationException exception) {
            logWaringError(exception);
            throw exception;
        }
    }

    /**
     * Checks for new messages and adds them if need be.
     * @param observableConversation the message log you want to check.
     * @param socket the socket the communication happens over.
     * @throws IOException gets thrown if the socket failed to be made.
     * @throws InvalidResponseException gets thrown if the class could not be found for that object or the response is a different object than expected.
     * @throws CouldNotAddMessageException gets thrown if the text message could not be added to the local message log.
     * @throws CouldNotGetMessageLogException gets thrown if the server can't find the message log.
     * @throws UsernameNotPartOfConversationException gets thrown if the user is not a part of the specified conversation.
     */
    public synchronized void checkConversationForNewMessages(ObservableConversation observableConversation, Socket socket) throws IOException, CouldNotAddMessageException, CouldNotGetMessageLogException, InvalidResponseException, UsernameNotPartOfConversationException {
        try {
            logger.log(Level.INFO, "Syncing message log " + observableConversation.getConversationNumber());
            LocalDate localDate = LocalDate.now();
            long lastMessageNumber = observableConversation.getMessageLogForDate(localDate, endUser.getUsername()).getLastMessageNumber();
            long conversationNumber = observableConversation.getConversationNumber();
            ArrayList<String> name = new ArrayList<>();
            name.add(endUser.getUsername());
            MessageRequest messageRequest = new MessageRequestBuilder().addLastMessage(lastMessageNumber).addDate(LocalDate.now()).setUsername(getUsername()).addConversationNumber(conversationNumber).setCheckForMessages(true).build();
            sendObject(messageRequest, socket);
            Object object = getObject(socket);
            if (object instanceof MessageRequest response) {
                List<Message> textMessageList = response.getMessageTransportList().stream().map(trans -> trans.getMessage()).toList();
                if (!textMessageList.isEmpty()) {
                    //Todo: Se om denne kan endres slik at flere meldinger kan legges til fra forksjellig dato.
                    observableConversation.addAllMessagesWithSameDate(textMessageList);
                }
            } else if (object instanceof CouldNotGetMessageLogException exception) {
                throw exception;
            } else if (object instanceof IllegalArgumentException exception) {
                throw exception;
            } else {
                throw new InvalidResponseException("The response from the server was invalid format.");
            }
        } catch (CouldNotGetMessageLogException | IllegalArgumentException | CouldNotAddMessageException | IOException | InvalidResponseException | UsernameNotPartOfConversationException exception) {
            logWaringError(exception);
            throw exception;
        }
    }

    /**
     * Checks if there are any new conversations on the server.
     * @throws IOException gets thrown if the socket failed to be made.
     * @throws InvalidResponseException gets thrown if the response from the server is invalid.
     * @throws CouldNotAddConversationException gets thrown if the conversation could not be added.
     */
    public void checkForNewConversations() throws IOException, InvalidResponseException, CouldNotAddConversationException {
        try (Socket socket = new Socket(host, portNumber)){
            List<String> usernames = new ArrayList<>();
            List<Long> conversationNumbers = personalConversationRegister.getAllConversationNumbers();
            usernames.add(endUser.getUsername());
            ConversationRequest conversationRequest = new ConversationRequestBuilder().setCheckForNewConversations(true).addUsername(getUsername()).addConversationNumberList(conversationNumbers).build();
            sendObject(conversationRequest, socket);
            Object object = getObject(socket);
            if(object instanceof PersonalConversationTransport personalConversationTransport){
                Iterator<ObservableConversation> it = personalConversationTransport.getPersonalConversationList().iterator();
                while (it.hasNext()){
                    ObservableConversation observableConversation = it.next();
                    personalConversationRegister.addConversation(observableConversation);
                }
            }else if (object instanceof IllegalArgumentException exception){
                throw exception;
            }
        } catch (IOException | InvalidResponseException | IllegalArgumentException |  CouldNotAddConversationException exception) {
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
        try (Socket socket = new Socket(host, portNumber)){
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
        try (Socket socket = new Socket(host, 1380)){
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
    private synchronized void sendObject(Object object, Socket socket) throws IOException {
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
    private synchronized Object getObject(Socket socket) throws IOException, InvalidResponseException {
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
    private synchronized void checkIfListIsEmptyOrNull(List list, String prefix){
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
    private synchronized void checkString(String stringToCheck, String errorPrefix){
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
    private synchronized void checkIfObjectIsNull(Object object, String error){
        if (object == null){
            throw new IllegalArgumentException("The " + error + " cannot be null.");
        }
    }

    /**
     *
     */
    public void checkForNewMembers() throws UsernameNotPartOfConversationException, CouldNotGetConversationException, IOException, InvalidResponseException {
        List<ObservableConversation> observableConversations = personalConversationRegister.getConversationList();
        Iterator<ObservableConversation> it = observableConversations.iterator();
        while (it.hasNext()){
            ObservableConversation observableConversation = it.next();
            checkConversationForNewOrDeletedMembers(observableConversation);
        }
    }

    private void checkConversationForNewOrDeletedMembers(ObservableConversation observableConversation) throws UsernameNotPartOfConversationException, CouldNotGetConversationException, IOException, InvalidResponseException {
        try (Socket socket = new Socket(host, portNumber)){
            MembersRequest membersRequest = new MembersRequestBuilder().addConversationNumber(observableConversation.getConversationNumber()).addUsername(getUsername()).addLastMember(observableConversation.getMembers().getLastMemberNumber()).build();
            sendObject(membersRequest, socket);
            Object object = getObject(socket);
            if (object instanceof MembersRequest response){

            }else if (object instanceof UsernameNotPartOfConversationException exception){
                throw exception;
            }else if (object instanceof CouldNotGetConversationException exception){
                throw exception;
            }else if(object instanceof IllegalArgumentException exception){
                throw exception;
            }else {
                throw new InvalidResponseException("The response from the server is invalid.");
            }

        }catch (IOException | InvalidResponseException | UsernameNotPartOfConversationException | CouldNotGetConversationException exception){
            logWaringError(exception);
            throw exception;
        }
    }
}
