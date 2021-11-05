package no.stonedstonar.chatapplication.frontend;

import no.stonedstonar.chatapplication.model.conversation.ObservableConversation;
import no.stonedstonar.chatapplication.model.conversationregister.personal.PersonalConversationRegister;
import no.stonedstonar.chatapplication.model.exception.InvalidResponseException;
import no.stonedstonar.chatapplication.model.exception.conversation.CouldNotAddConversationException;
import no.stonedstonar.chatapplication.model.exception.conversation.CouldNotGetConversationException;
import no.stonedstonar.chatapplication.model.exception.conversation.UsernameNotPartOfConversationException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotAddMemberException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotGetMemberException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotRemoveMemberException;
import no.stonedstonar.chatapplication.model.exception.message.CouldNotAddMessageException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotAddMessageLogException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotGetMessageLogException;
import no.stonedstonar.chatapplication.model.exception.user.CouldNotAddUserException;
import no.stonedstonar.chatapplication.model.exception.user.CouldNotLoginToUserException;
import no.stonedstonar.chatapplication.model.member.ConversationMember;
import no.stonedstonar.chatapplication.model.member.Member;
import no.stonedstonar.chatapplication.model.membersregister.MemberRegister;
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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class that represents the logic that the chat client should hold.
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class ChatClient {

    private User endUser;

    private volatile PersonalConversationRegister personalConversationRegister;

    private final Logger logger;

    private final String host;

    private final int portNumber;

    private long conversationFocus;

    private boolean runBackgroundThread;

    private boolean threadStopped;

    private final String invalidResponse;

    private Thread checkingThread;

    /**
      * Makes an instance of the ChatClient class.
      */
    public ChatClient(){
        logger = Logger.getLogger(getClass().toString());
        host = "localhost";
        portNumber = 1380;
        conversationFocus = 0;
        invalidResponse = "The response from the server was invalid format.";
    }

    /**
     * Logs the user out and clears all the data.
     */
    public void logOutOfUser(){
        stopBackgroundThread();
        while (!checkIfThreadIsStopped()){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                logWaringError(e);
            }
        }
        conversationFocus = 0;
        endUser = null;
        personalConversationRegister = null;
    }

    /**
     * Gets the personal conversation register.
     * @return the personal conversation regsiter.
     */
    public synchronized PersonalConversationRegister getPersonalConversationRegister(){
        return personalConversationRegister;
    }

    /**
     * Sets the conversation that is active in the gui. Starts a listening thread.
     * @param conversationFocus the conversation you want to listen for new messages on.
     */
    public void setConversationFocus(long conversationFocus) {
        checkIfLongIsNegative(conversationFocus, "conversation focus");
        this.conversationFocus = conversationFocus;
    }

    /**
     * Sets the run boolean to false and interrupts the thread.
     */
    public void stopBackgroundThread(){
        runBackgroundThread = false;
    }

    /**
     * Starts the message listening thread.
     */
    public void startBackgroundThread(){
        runBackgroundThread = true;
        threadStopped = false;
        checkingThread = new Thread(() -> {
            try {
                int count = 0;
                long currentMessageLogForThread = conversationFocus;
                ObservableConversation observableConversation = null;
                if (currentMessageLogForThread > 0){
                     observableConversation = getConversationByNumber(currentMessageLogForThread);
                }
                do {
                    if (currentMessageLogForThread != this.conversationFocus){
                        currentMessageLogForThread = this.conversationFocus;
                        observableConversation = getConversationByNumber(currentMessageLogForThread);
                    }
                    if (count == 4){
                        checkForNewMessages();
                        checkForNewMembers();
                        checkForNewConversations();
                        checkConversationsForNewNames();
                        count = 0;
                    }else {
                        if (currentMessageLogForThread != 0 && observableConversation != null){
                            checkIFCurrentConversationHasNewMessages(observableConversation);
                        }
                        count += 1;
                    }
                    Thread.sleep(1500);
                } while(runBackgroundThread);
            }catch (CouldNotAddMessageException | IOException | InvalidResponseException | CouldNotGetMessageLogException | InterruptedException | UsernameNotPartOfConversationException | CouldNotGetConversationException | CouldNotGetMemberException | CouldNotRemoveMemberException | CouldNotAddMemberException | CouldNotAddConversationException exception) {
                logWaringError(exception);
                stopBackgroundThread();
            }
            threadStopped = true;
        });
        checkingThread.start();
    }

    /**
     * Stops all the threads.
     */
    public void stopAllThreads(){
        runBackgroundThread = false;
    }

    /**
     * Checks if the listening thread is active.
     * @return <code>true</code> if the thread is running.
     *         <code>false</code> if the thread is not running.
     */
    public boolean checkIfListeningThreadIsActive(){
        return runBackgroundThread;
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
     * Checks if the current conversation has new messages.
     * @param observableConversation the observable conversation that this is about.
     * @throws UsernameNotPartOfConversationException gets thrown if the username is not a part of this conversation.
     * @throws IOException gets thrown if the socket closes and the communication cannot be continued.
     * @throws CouldNotAddMessageException gets thrown if the message could not be added.
     * @throws InvalidResponseException gets thrown if the response from the server is invalid.
     * @throws CouldNotGetMessageLogException gets thrown if the conversation for that date could not be found.
     */
    private void checkIFCurrentConversationHasNewMessages(ObservableConversation observableConversation) throws UsernameNotPartOfConversationException, IOException, CouldNotAddMessageException, InvalidResponseException, CouldNotGetMessageLogException {
        Socket socket = new Socket(host, portNumber);
        checkConversationForNewMessages(observableConversation, socket);
    }

    /**
     * Returns the iterator of all the conversation of the user.
     * @return the iterator to get the personal conversations of the user.
     */
    public Iterator<ObservableConversation> getConversationsIterator(){
        return getPersonalConversationRegister().getIterator();
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
     * @throws CouldNotAddMessageLogException gets thrown if the conversation could not be added.
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
            if (!(object instanceof ObservableConversation)){
                if (object instanceof CouldNotAddMessageLogException exception){
                    throw exception;
                }else if (object instanceof CouldNotAddMemberException exception){
                    throw exception;
                }else {
                    throw new IllegalArgumentException("The returned object is not what excepted.");
                }
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
     * @throws CouldNotGetMessageLogException gets thrown if the server could not get the conversation.
     * @throws IOException gets thrown if something fails in the socket.
     * @throws InvalidResponseException gets thrown if the class could not be found for that object or the response is a different object than expected.
     */
    public void sendMessage(String messageContents, ObservableConversation observableConversation) throws IOException, CouldNotAddMessageException, CouldNotGetMessageLogException, InvalidResponseException {
        checkString(messageContents, "message");
        checkIfObjectIsNull(observableConversation, "observable conversation");
        try (Socket socket = new Socket(host, portNumber)){
            TextMessage textMessage = new TextMessage(messageContents, endUser.getUsername());
            List<MessageTransport> messageTransportList = new ArrayList<>();
            messageTransportList.add(new MessageTransport(textMessage, true));
            MessageRequest messageRequest = new MessageRequestBuilder().addMessageTransportList(messageTransportList).addConversationNumber(observableConversation.getConversationNumber()).build();
            sendObject(messageRequest, socket);
            Object object = getObject(socket);
            if (!(object instanceof  MessageRequest)){
                if (object instanceof CouldNotAddMessageException exception){
                    throw exception;
                }else if (object instanceof CouldNotGetMessageLogException exception){
                    throw exception;
                }else {
                    throw new InvalidResponseException(invalidResponse);
                }
            }
        } catch (IOException | CouldNotAddMessageException | InvalidResponseException | CouldNotGetMessageLogException exception){
            logWaringError(exception);
            throw exception;
        }
    }

    /**
     * Gets the conversation that matches that long number.
     * @param messageLogNumber the number that conversation has.
     * @return the conversation that matches that number.
     * @throws CouldNotGetConversationException gets thrown if the conversation could not be found.
     */
    public ObservableConversation getConversationByNumber(long messageLogNumber) throws CouldNotGetConversationException {
        return getPersonalConversationRegister().getConversationByNumber(messageLogNumber);
    }

    /**
     * Logs the client in as a user.
     * @param username the username of the user.
     * @param password the password that the user has.
     * @throws IOException gets thrown if the socket failed to be made.
     * @throws CouldNotLoginToUserException gets thrown if the password or username is incorrect.
     * @throws InvalidResponseException gets thrown if the class could not be found for that object or the response is a different object than expected.
     */
    public void loginToUser(String username, String password) throws IOException, CouldNotLoginToUserException, InvalidResponseException {
        checkString(username, "username ");
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
     * Makes it possible to edit the conversation.
     * @param namesToAdd a list with all the names to add.
     * @param namesToRemove a list with all the names to remove.
     * @param conversationName the new conversation name.
     * @param observableConversation the observable conversation to edit.
     * @throws CouldNotGetConversationException gets thrown if the conversation could not be found.
     * @throws IOException gets thrown if the socket closes before the updates can be sent.
     * @throws InvalidResponseException gets thrown if the response from the server is invalid.
     * @throws CouldNotAddMemberException gets thrown if a member could not be added.
     * @throws CouldNotRemoveMemberException gets thrown if a member could not be removed.
     */
    public void editConversation(List<String> namesToAdd, List<String> namesToRemove, String conversationName, ObservableConversation observableConversation) throws CouldNotGetConversationException, IOException, InvalidResponseException, CouldNotAddMemberException, CouldNotRemoveMemberException {
        checkIfObjectIsNull(conversationName, "conversation name");
        checkIfObjectIsNull(namesToAdd, "names to add");
        checkIfObjectIsNull(namesToRemove, "names to remove");
        checkIfObjectIsNull(observableConversation, "personal conversation");
        try (Socket socket = new Socket(host, portNumber)){
            SetKeepAliveRequest setKeepAliveRequest = new SetKeepAliveRequest(true);
            sendObject(setKeepAliveRequest, socket);
            if ((!conversationName.isEmpty()) && (!conversationName.equals(observableConversation.getConversationName()))){
                editConversationName(observableConversation, conversationName, socket);
            }
            addOrRemoveMembers(observableConversation, namesToAdd, namesToRemove, socket);
            SetKeepAliveRequest setStopAlive = new SetKeepAliveRequest(false);
            sendObject(setStopAlive, socket);
        }catch (Exception object){
            logWaringError(object);
            if (object instanceof CouldNotAddMemberException exception){
                throw exception;
            }else if (object instanceof CouldNotGetConversationException exception){
                throw exception;
            }else if (object instanceof CouldNotRemoveMemberException exception){
                throw exception;
            }else if (object instanceof IOException exception){
                throw exception;
            }else {
                throw new InvalidResponseException(invalidResponse);
            }
        }
    }

    /**
     * Makes it possible to edit a conversations name.
     * @param observableConversation the observable conversation to be edited.
     * @param newName the new name of the conversation.
     * @param socket the socket this communication should happen over.
     * @throws Exception gets thrown if something goes wrong
     */
    private void editConversationName(ObservableConversation observableConversation, String newName, Socket socket) throws Exception {
        List<Long> conversationNumberList = new ArrayList<>();
        conversationNumberList.add(observableConversation.getConversationNumber());
        ConversationRequest conversationRequest = new ConversationRequestBuilder().addConversationName(newName).addUsername(endUser.getUsername()).addConversationNumberList(conversationNumberList).build();
        sendObject(conversationRequest, socket);
        Object object = getObject(socket);
        if (object instanceof ConversationRequest){
            observableConversation.setConversationName(newName);
        }else if (object instanceof Exception exception){
            throw exception;
        }else {
            throw new InvalidResponseException(invalidResponse);
        }
    }


    /**
     * Adds or removes members from a conversation.
     * @param observableConversation the observable conversation that this is about.
     * @param addNames the names to add as a list.
     * @param removeNames  the names to remove as a list.
     * @throws Exception gets thrown if the response from the server is an exception or the socket closed unexpected.
     */
    private void addOrRemoveMembers(ObservableConversation observableConversation, List<String> addNames, List<String> removeNames, Socket socket) throws Exception {
        if ((!addNames.isEmpty()) || (!removeNames.isEmpty())){
            List<MemberTransport> memberTransportList = new ArrayList<>();
            if (!addNames.isEmpty()){
                addNames.forEach(name -> memberTransportList.add(new MemberTransport(new ConversationMember(name), true)));
            }
            if(!removeNames.isEmpty()){
                removeNames.forEach(name -> memberTransportList.add(new MemberTransport(new ConversationMember(name), false)));
            }
            MembersRequest membersRequest = new MembersRequestBuilder().addMemberTransports(memberTransportList).addConversationNumber(observableConversation.getConversationNumber()).addUsername(getUsername()).build();
            sendObject(membersRequest, socket);
            Object object = getObject(socket);
            if (!(object instanceof MembersRequest)){
                if (object instanceof Exception exception){
                    throw exception;
                }else {
                    throw new InvalidResponseException(invalidResponse);
                }
            }
        }
    }

    /**
     * Checks all the conversations for new messages.
     * @throws IOException gets thrown if the socket failed to be made.
     * @throws InvalidResponseException gets thrown if the class could not be found for that object or the response is a different object than expected.
     * @throws CouldNotAddMessageException gets thrown if the text message could not be added.
     * @throws CouldNotGetMessageLogException gets thrown if the server can't find the conversation.
     * @throws UsernameNotPartOfConversationException gets thrown if the user is not a part of the specified conversation.
     */
    private void checkForNewMessages() throws CouldNotAddMessageException, IOException, InvalidResponseException, CouldNotGetMessageLogException, UsernameNotPartOfConversationException {
        Iterator<ObservableConversation> it = getPersonalConversationRegister().getIterator();
        if (it.hasNext()){
            try (Socket socket = new Socket(host, portNumber)){
                socket.setKeepAlive(true);
                SetKeepAliveRequest setKeepAliveRequest = new SetKeepAliveRequest(true);
                sendObject(setKeepAliveRequest, socket);
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
    }

    /**
     * Checks for new messages and adds them if need be.
     * @param observableConversation the conversation you want to check.
     * @param socket the socket the communication happens over.
     * @throws IOException gets thrown if the socket failed to be made.
     * @throws InvalidResponseException gets thrown if the class could not be found for that object or the response is a different object than expected.
     * @throws CouldNotAddMessageException gets thrown if the text message could not be added to the local message log.
     * @throws CouldNotGetMessageLogException gets thrown if the server can't find the conversation.
     * @throws UsernameNotPartOfConversationException gets thrown if the user is not a part of the specified conversation.
     */
    public synchronized void checkConversationForNewMessages(ObservableConversation observableConversation, Socket socket) throws IOException, CouldNotAddMessageException, CouldNotGetMessageLogException, InvalidResponseException, UsernameNotPartOfConversationException {
        try {
            LocalDate localDate = LocalDate.now();
            long lastMessageNumber = observableConversation.getMessageLogForDate(localDate, endUser.getUsername()).getLastMessageNumber();
            long conversationNumber = observableConversation.getConversationNumber();
            ArrayList<String> name = new ArrayList<>();
            name.add(endUser.getUsername());
            MessageRequest messageRequest = new MessageRequestBuilder().addLastMessage(lastMessageNumber).addDate(LocalDate.now()).setUsername(getUsername()).addConversationNumber(conversationNumber).setCheckForMessages(true).build();
            sendObject(messageRequest, socket);
            Object object = getObject(socket);
            if (object instanceof MessageRequest response) {
                List<Message> textMessageList = response.getMessageTransportList().stream().map(MessageTransport::getMessage).toList();
                if (!textMessageList.isEmpty()) {
                    observableConversation.addAllMessagesWithSameDate(textMessageList);
                }
            } else if (object instanceof CouldNotGetMessageLogException exception) {
                throw exception;
            } else if (object instanceof IllegalArgumentException exception) {
                throw exception;
            } else {
                throw new InvalidResponseException(invalidResponse);
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
            List<Long> conversationNumbers = getPersonalConversationRegister().getAllConversationNumbers();
            usernames.add(endUser.getUsername());
            ConversationRequest conversationRequest = new ConversationRequestBuilder().setCheckForNewConversations(true).addUsername(getUsername()).addConversationNumberList(conversationNumbers).build();
            sendObject(conversationRequest, socket);
            Object object = getObject(socket);
            if(object instanceof PersonalConversationTransport personalConversationTransport){
                Iterator<ObservableConversation> it = personalConversationTransport.getPersonalConversationList().iterator();
                while (it.hasNext()){
                    ObservableConversation observableConversation = it.next();
                    getPersonalConversationRegister().addConversation(observableConversation);
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
                throw new InvalidResponseException(invalidResponse);
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
                    throw new InvalidResponseException(invalidResponse);
                }
            }
        }catch (IOException | IllegalArgumentException | CouldNotAddUserException | InvalidResponseException exception){
            logWaringError(exception);
            throw exception;
        }
    }

    /**
     * Checks a conversation for new names.
     * @throws CouldNotGetConversationException gets thrown if the conversation could not be located.
     * @throws IOException gets thrown if the socket closes before the task is done.
     * @throws InvalidResponseException gets thrown if the response is invalid.
     */
    public void checkConversationsForNewNames() throws CouldNotGetConversationException, IOException, InvalidResponseException {
        Iterator<ObservableConversation> it = getPersonalConversationRegister().getIterator();
        if (it.hasNext()){
            try (Socket socket = new Socket(host, portNumber)){

                Map<Long, String> namesOfConversations = new HashMap<>();
                it.forEachRemaining(convo -> namesOfConversations.put(convo.getConversationNumber(), convo.getConversationName()));
                ConversationRequest conversationRequest = new ConversationRequestBuilder().addConversationNamesMap(namesOfConversations).addUsername(getUsername()).setCheckForConversationNames(true).build();
                sendObject(conversationRequest,socket);
                Object object = getObject(socket);
                if(object instanceof ConversationRequest response){
                    Map<Long, String> newNames = response.getNewConversationNamesMap();
                    if (!newNames.isEmpty()){
                        Set<Long> keySet = newNames.keySet();
                        Iterator<Long> keyIt = keySet.iterator();
                        while(keyIt.hasNext()){
                            long key = keyIt.next();
                            ObservableConversation observableConversation = getConversationByNumber(key);
                            String newName = newNames.get(key);
                            if (!newName.equals(observableConversation.getConversationName())){
                                observableConversation.setConversationName(newName);
                            }
                        }
                    }
                }else if(object instanceof CouldNotGetConversationException exception){
                    throw exception;
                }else {
                    throw new InvalidResponseException(invalidResponse);
                }
            } catch (IOException | InvalidResponseException | CouldNotGetConversationException exception) {
                logWaringError(exception);
                throw exception;
            }
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
     * Checks all the conversations for new members.
     * @throws UsernameNotPartOfConversationException gets thrown if this user is not a part of that conversation.
     * @throws CouldNotGetConversationException gets thrown if the conversation could not be found.
     * @throws IOException gets thrown if the socket closes before everything was done.
     * @throws InvalidResponseException gets thrown if the server responds with wrong object.
     * @throws CouldNotGetMemberException gets thrown if a member could not be found.
     * @throws CouldNotRemoveMemberException gets thrown if a member could not be removed.
     * @throws CouldNotAddMemberException gets thrown if a member could not be added.
     */
    public void checkForNewMembers() throws UsernameNotPartOfConversationException, CouldNotGetConversationException, IOException, InvalidResponseException, CouldNotGetMemberException, CouldNotRemoveMemberException, CouldNotAddMemberException {
        Iterator<ObservableConversation> it = getPersonalConversationRegister().getIterator();
        while (it.hasNext()){
            ObservableConversation observableConversation = it.next();
            checkConversationForNewOrDeletedMembers(observableConversation);
        }
    }

    /**
     * Checks if the input conversation has new members.
     * @param observableConversation the conversation to check for new members.
     * @throws UsernameNotPartOfConversationException gets thrown if this user is not a part of that conversation.
     * @throws CouldNotGetConversationException gets thrown if the conversation could not be found.
     * @throws IOException gets thrown if the socket closes before everything was done.
     * @throws InvalidResponseException gets thrown if the server responds with wrong object.
     * @throws CouldNotGetMemberException gets thrown if a member could not be found.
     * @throws CouldNotRemoveMemberException gets thrown if a member could not be removed.
     * @throws CouldNotAddMemberException gets thrown if a member could not be added.
     */
    private synchronized void checkConversationForNewOrDeletedMembers(ObservableConversation observableConversation) throws UsernameNotPartOfConversationException, CouldNotGetConversationException, IOException, InvalidResponseException, CouldNotGetMemberException, CouldNotRemoveMemberException, CouldNotAddMemberException {
        try (Socket socket = new Socket(host, portNumber)){
            MembersRequest membersRequest = new MembersRequestBuilder().setCheckForNewMembers(true).addConversationNumber(observableConversation.getConversationNumber()).addUsername(getUsername()).addLastMember(observableConversation.getMembers().getLastMemberNumber()).setLastDeletedMember(observableConversation.getMembers().getLastDeletedMember()).build();
            sendObject(membersRequest, socket);
            Object object = getObject(socket);
            if (object instanceof MembersRequest response){
                List<MemberTransport> membersTransport = response.getMembers();
                if (!membersTransport.isEmpty()){
                    List<Member> membersToRemove = membersTransport.stream().filter(member -> !member.isAddMember()).map(MemberTransport::getMember).toList();
                    List<Member> membersToAdd = membersTransport.stream().filter(MemberTransport::isAddMember).map(MemberTransport::getMember).toList();
                    MemberRegister memberRegister = observableConversation.getMembers();
                    if (!membersToRemove.isEmpty()){
                        memberRegister.removeAllMembers(membersToRemove, getUsername());
                    }
                    if (!membersToAdd.isEmpty()){
                        memberRegister.addAllMembers(membersToAdd, getUsername());
                    }
                }
            }else if (object instanceof UsernameNotPartOfConversationException exception){
                throw exception;
            }else if (object instanceof CouldNotGetConversationException exception){
                throw exception;
            }else if(object instanceof IllegalArgumentException exception){
                throw exception;
            }else {
                throw new InvalidResponseException(invalidResponse);
            }
        }catch (IOException | CouldNotGetMemberException | CouldNotRemoveMemberException | InvalidResponseException | UsernameNotPartOfConversationException | CouldNotGetConversationException | CouldNotAddMemberException exception){
            logWaringError(exception);
            throw exception;
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
     * Checks if a long is negative or equal to zero.
     * @param number the number to check.
     * @param prefix the prefix the error should have.
     */
    private synchronized void checkIfLongIsNegative(long number, String prefix) {
        if (number < 0) {
            throw new IllegalArgumentException("Expected the " + prefix + " to be larger than zero.");
        }
    }
}
