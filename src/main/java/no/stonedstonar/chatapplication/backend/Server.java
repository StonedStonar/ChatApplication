package no.stonedstonar.chatapplication.backend;

import javafx.application.Platform;
import no.stonedstonar.chatapplication.model.conversation.*;
import no.stonedstonar.chatapplication.model.conversationregister.server.NormalConversationRegister;
import no.stonedstonar.chatapplication.model.conversationregister.personal.NormalPersonalConversationRegister;
import no.stonedstonar.chatapplication.model.exception.InvalidResponseException;
import no.stonedstonar.chatapplication.model.exception.conversation.CouldNotAddConversationException;
import no.stonedstonar.chatapplication.model.exception.conversation.CouldNotGetConversationException;
import no.stonedstonar.chatapplication.model.exception.conversation.UsernameNotPartOfConversationException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotAddMemberException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotGetMemberException;
import no.stonedstonar.chatapplication.model.exception.member.CouldNotRemoveMemberException;
import no.stonedstonar.chatapplication.model.exception.message.CouldNotRemoveMessageException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotGetMessageLogException;
import no.stonedstonar.chatapplication.model.exception.message.CouldNotAddMessageException;
import no.stonedstonar.chatapplication.model.exception.user.CouldNotAddUserException;
import no.stonedstonar.chatapplication.model.exception.user.CouldNotLoginToUserException;
import no.stonedstonar.chatapplication.model.member.ConversationMember;
import no.stonedstonar.chatapplication.model.member.Member;
import no.stonedstonar.chatapplication.model.message.Message;
import no.stonedstonar.chatapplication.model.message.TextMessage;
import no.stonedstonar.chatapplication.model.user.User;
import no.stonedstonar.chatapplication.model.user.EndUser;
import no.stonedstonar.chatapplication.model.userregister.NormalUserRegister;
import no.stonedstonar.chatapplication.network.requests.*;
import no.stonedstonar.chatapplication.network.requests.builder.MembersRequestBuilder;
import no.stonedstonar.chatapplication.network.requests.builder.MessageRequestBuilder;
import no.stonedstonar.chatapplication.network.transport.LoginTransport;
import no.stonedstonar.chatapplication.network.transport.MemberTransport;
import no.stonedstonar.chatapplication.network.transport.MessageTransport;
import no.stonedstonar.chatapplication.network.transport.PersonalConversationTransport;

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
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public class Server{

    private ServerSocket welcomeSocket;

    private volatile NormalUserRegister normalUserRegister;

    private volatile NormalConversationRegister normalConversationRegister;

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
        normalUserRegister = new NormalUserRegister();
        normalConversationRegister = new NormalConversationRegister();
        run = true;
        executors = Executors.newFixedThreadPool(12);
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
            EndUser endUser = new EndUser("bjarne22", "passr");
            EndUser endUser1 = new EndUser("fjell", "passord");
            EndUser endUser3 = new EndUser("bass", "thepass");
            normalUserRegister.addUser(endUser);
            normalUserRegister.addUser(endUser1);
            normalUserRegister.addUser(endUser3);
            List<Member> twoMembers = new ArrayList<>();
            twoMembers.add(new ConversationMember(endUser.getUsername()));
            twoMembers.add(new ConversationMember(endUser1.getUsername()));
            List<Member> threeMembers = new ArrayList<>();
            threeMembers.add(new ConversationMember(endUser.getUsername()));
            threeMembers.add(new ConversationMember(endUser1.getUsername()));
            threeMembers.add(new ConversationMember(endUser3.getUsername()));
            normalConversationRegister.addNewConversationWithUsernames(threeMembers, "");
            normalConversationRegister.addNewConversationWithUsernames(twoMembers, "");
            List<ServerConversation> normalMessageLogs = normalConversationRegister.getAllConversationsOfUsername("bjarne22");
            normalMessageLogs.get(0).addNewMessage(new TextMessage("Haha", "bjarne22"));
            normalMessageLogs.get(0).addNewMessage(new TextMessage("Nope", "fjell"));
            normalMessageLogs.get(0).addNewMessage(new TextMessage("So funny bjarne", "bass"));
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
            if(object instanceof MessageRequest messageRequest){
                handleIncomingMessage(messageRequest, socket);
            }else if (object instanceof UserRequest userRequest){
                handleUserInteraction(userRequest, socket);
            }else if (object instanceof ConversationRequest conversationRequest){
                handleConversationInteraction(conversationRequest, socket);
            }else if (object instanceof MembersRequest membersRequest){
                handleMembersRequest(membersRequest, socket);
            }else if (object instanceof SetKeepAliveRequest setKeepAliveRequest) {
                socket.setKeepAlive(setKeepAliveRequest.isKeepAlive());
            }else {
                throw new IllegalArgumentException("NONE IS A VALID OBJECT");
            }
        }while ((socket.getKeepAlive()) && (!socket.isClosed()));
    }

    /**
     * Handles a message when it comes in.
     * @param messageRequest the message transport the message comes in.
     * @param socket the socket that gets the sends.
     * @throws IOException gets thrown if the socket closes or cannot finish its task.
     */
    private void handleIncomingMessage(MessageRequest messageRequest, Socket socket) throws IOException {
        try {
            if (messageRequest.isCheckForMessages()){
                checkForNewMessages(messageRequest, socket);
            }else {
                List<Message> addMessages = messageRequest.getMessageTransportList().stream().filter(MessageTransport::isAddMessage).map(MessageTransport::getMessage).toList();
                List<Message> removeMessages = messageRequest.getMessageTransportList().stream().filter(mess -> !mess.isAddMessage()).map(MessageTransport::getMessage).toList();
                ServerConversation conversation = normalConversationRegister.getConversationByNumber(messageRequest.getConversationNumber());
                if (!addMessages.isEmpty()){
                    addNewMessages(addMessages, conversation);
                }
                if (!removeMessages.isEmpty()){
                    removeMessages(removeMessages, conversation);
                }
                sendObject(messageRequest, socket);
            }
        } catch (UsernameNotPartOfConversationException | CouldNotGetConversationException | CouldNotGetMessageLogException | CouldNotAddMessageException | CouldNotRemoveMessageException exception) {
            String message = "Something went wrong in adding " + messageRequest.getMessageTransportList().size() + " with the exception " + exception.getMessage() + " and class " + exception.getClass();
            logEvent(Level.WARNING, message);
            sendObject(exception, socket);
        }
    }

    /**
     * Adds new messages to the conversations they match.
     * @param messages the message request.
     * @throws CouldNotGetConversationException gets thrown if the conversation could not be located.
     * @throws UsernameNotPartOfConversationException gets thrown if the user is not a part of that conversation.
     * @throws CouldNotAddMessageException gets thrown if the messages is already in the conversation.
     * @throws CouldNotGetMessageLogException gets thrown if the message log for that date could not be located.
     */
    private void addNewMessages(List<Message> messages, ServerConversation conversation) throws IOException, CouldNotGetConversationException, UsernameNotPartOfConversationException, CouldNotAddMessageException, CouldNotGetMessageLogException {
        conversation.addAllMessagesWithSameDate(messages);
    }

    /**
     * Removes all the messages if every single one is in the conversation-
     * @param messageList the message list you want to remove.
     * @param conversation the conversation these messages are in.
     * @throws UsernameNotPartOfConversationException gets thrown if the user is not a part of that conversation.
     * @throws CouldNotRemoveMessageException gets thrown if one or more of the messages are not in the conversation.
     * @throws CouldNotGetMessageLogException  gets thrown if the date of these messages don't have a message log.
     */
    private void removeMessages(List<Message> messageList, ServerConversation conversation) throws UsernameNotPartOfConversationException, CouldNotRemoveMessageException, CouldNotGetMessageLogException {
        conversation.removeAllMessagesWithSameDate(messageList);
    }

    /**
     * Checks if a conversation has new messages compared to the conversation request.
     * @param messageRequest the request that wants messages for a conversation.
     * @param socket socket that the communication is happening over.
     * @throws CouldNotGetMessageLogException gets thrown if the conversation could not be found.
     * @throws IOException gets thrown if something goes wrong with the communication.
     * @throws CouldNotGetConversationException gets thrown if the conversation could not be found.
     * @throws UsernameNotPartOfConversationException gets thrown if the username is not a part of the conversation.
     */
    private void checkForNewMessages(MessageRequest messageRequest, Socket socket) throws IOException, CouldNotGetConversationException, CouldNotGetMessageLogException, UsernameNotPartOfConversationException {
        ServerConversation conversation = normalConversationRegister.getConversationByNumber(messageRequest.getConversationNumber());
        List<Message> newMessages = conversation.checkForNewMessagesOnDate(messageRequest.getCheckMessageDate(),messageRequest.getLastMessage(), messageRequest.getUsername());
        List<MessageTransport> messageTransportList = new ArrayList<>();
        newMessages.forEach(message -> messageTransportList.add(new MessageTransport(message, true)));
        MessageRequest responseRequest = new MessageRequestBuilder().addMessageTransportList(messageTransportList).build();
        sendObject(responseRequest, socket);
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
                User endUser = normalUserRegister.login(userRequest.getUsername(), userRequest.getPassword());
                NormalPersonalConversationRegister personalConversationRegister = normalConversationRegister.getAllConversationsUserHasAndMakePersonalRegister(userRequest.getUsername());
                LoginTransport loginTransport = new LoginTransport(endUser, personalConversationRegister);
                sendObject(loginTransport, socket);
            }else if (userRequest.isNewUser()){
                EndUser endUser = new EndUser(userRequest.getUsername(), userRequest.getPassword());
                normalUserRegister.addUser(endUser);
                sendObject(true, socket);
            } else if (userRequest.isCheckUsername()){
                sendObject(normalUserRegister.checkIfUsernameIsTaken(userRequest.getUsername()), socket);
            }
        }catch (CouldNotLoginToUserException | IllegalArgumentException | CouldNotAddUserException exception){
            String message = "Something went wrong in the " + userRequest + " with the exception " + exception.getMessage() + " and class " + exception.getClass();
            logEvent(Level.WARNING, message);
            sendObject(exception, socket);
        }
    }

    /**
     * Handles the conversation request objects.
     * @param conversationRequest the conversation request to handle.
     * @param socket the socket the communication is happening over.
     * @throws IOException gets thrown if the socket closes or cannot finish its task.
     */
    private void handleConversationInteraction(ConversationRequest conversationRequest, Socket socket) throws IOException {
        try {
            if (conversationRequest.isNewConversation()){
                makeNewConversation(conversationRequest, socket);
            }else if (conversationRequest.isCheckForNewConversation()){
                handleCheckForNewConversations(conversationRequest, socket);
            }
        }catch (CouldNotAddMemberException | CouldNotAddConversationException exception) {
            String message = "Something went wrong in the " + conversationRequest + " with the exception " + exception.getMessage() + " and class " + exception.getClass();
            logEvent(Level.WARNING, message);
            sendObject(exception, socket);
        }
    }

    /**
     * Checks for new conversations by a user.
     * @param conversationRequest the conversation request that wants to check for new messages.
     * @param socket the socket this communication is happening over.
     * @throws IOException gets thrown if the socket closes or cannot finish its task.
     */
    private void handleCheckForNewConversations(ConversationRequest conversationRequest, Socket socket) throws IOException {
        List<Long> conversationNumbers = conversationRequest.getConversationNumberList();
        String username = conversationRequest.getUsername();
        List<ServerConversation> list = normalConversationRegister.getAllConversationsOfUsername(username);
        List<ObservableConversation> observableConversations = new ArrayList<>();
        if (conversationNumbers.size() < list.size()){
            List<Long> notMatchingNumbers = list.stream().map(convo -> convo.getConversationNumber()).filter(number -> {
                boolean valid = false;
                if (!conversationNumbers.contains(number)){
                    valid = true;
                }
                return valid;
            }).toList();
            notMatchingNumbers.forEach(number -> {
                observableConversations.add(new NormalObservableConversation(list.stream().filter(convo -> convo.getConversationNumber() == number).findFirst().get(), username));
            });
        }
        PersonalConversationTransport personalConversationTransport = new PersonalConversationTransport(observableConversations);
        sendObject(personalConversationTransport, socket);
    }

    /**
     * Makes a new conversation based on the request.
     * @param conversationRequest the request for making a new conversation.
     * @param socket the socket this conversation is being made over.
     * @throws IOException gets thrown if the socket closes or cannot finish its task.
     * @throws CouldNotAddConversationException gets thrown if the conversation could not be added.
     * @throws CouldNotAddMemberException gets thrown if a member could not be added.
     */
    private void makeNewConversation(ConversationRequest conversationRequest, Socket socket) throws IOException, CouldNotAddMemberException, CouldNotAddConversationException {
        List<Member> usernames = conversationRequest.getMemberList();
        String nameOfMessageLog = conversationRequest.getNameOfConversation();
        ServerConversation conversation = normalConversationRegister.addNewConversationWithUsernames(usernames, nameOfMessageLog);
        String username = usernames.get(0).getUsername();
        ObservableConversation observableConversation = new NormalObservableConversation(conversation, username);
        sendObject(observableConversation, socket);
    }

    /**
     * Handles incoming members request.
     * @param membersRequest the member request that was sent form the client side.
     * @param socket the socket that sent this request.
     * @throws IOException gets thrown if the socket closes or cannot finish its task.
     */
    private void handleMembersRequest(MembersRequest membersRequest, Socket socket) throws IOException {
        try {
            if (membersRequest.isCheckForNewMembers()){
                checkForNewMembers(membersRequest, socket);
            }else {
                ServerConversation serverConversation = normalConversationRegister.getConversationByNumber(membersRequest.getConversationNumber());
                String username = membersRequest.getUsername();
                List<Member> membersToAdd = membersRequest.getMembers().stream().filter(MemberTransport::isAddMember).map(MemberTransport::getMember).toList();
                List<Member> membersToRemove = membersRequest.getMembers().stream().filter(mem -> !mem.isAddMember()).map(MemberTransport::getMember).toList();
                addNewMembersToConversation(membersToAdd, serverConversation, username);
                removeMembers(membersToRemove, serverConversation, username);
                sendObject(membersRequest, socket);
            }
        }catch (CouldNotAddMemberException | CouldNotGetMemberException | CouldNotRemoveMemberException | CouldNotGetConversationException | UsernameNotPartOfConversationException exception){
            String message = "Something went wrong in the " + membersRequest + " with the exception " + exception.getMessage() + " and class " + exception.getClass();
            logEvent(Level.WARNING, message);
            sendObject(exception, socket);
        }
    }

    /**
     * Checks if there are any new members in a conversation.
     * @param membersRequest the conversation you want to check.
     * @param socket the socket this conversation request came from.
     * @throws IOException gets thrown if the socket closes or cannot finish its task.
     * @throws UsernameNotPartOfConversationException gets thrown if the username is not a part of this conversation.
     * @throws CouldNotGetConversationException gets thrown if the conversation could not be found.
     */
    private void checkForNewMembers(MembersRequest membersRequest, Socket socket) throws IOException, UsernameNotPartOfConversationException, CouldNotGetConversationException {
        long lastMember = membersRequest.getLastMember();
        long conversationNumber = membersRequest.getConversationNumber();
        String username = membersRequest.getUsername();
        ServerConversation serverConversation = normalConversationRegister.getConversationByNumber(conversationNumber);
        List<Member> newMembers= serverConversation.getMembers().checkForNewUsers(lastMember, username);
        List<MemberTransport> memberTransportList = new ArrayList<>();
        newMembers.forEach(mem -> memberTransportList.add(new MemberTransport(mem, true)));
        MembersRequest response = new MembersRequestBuilder().addMemberTransports(memberTransportList).build();
        sendObject(response, socket);
    }

    /**
     * Adds new members to conversation if they are not in the conversation.
     * @param members the list with the new members.
     * @param conversation the conversation these members are going to be added to.
     * @param username the username of the end user who wants to add new members.
     * @throws CouldNotGetConversationException gets thrown if the conversation could not be located.
     * @throws CouldNotAddMemberException gets thrown if one person in the request is already in the conversation.
     * @throws UsernameNotPartOfConversationException gets thrown if the username is not a part of this conversation.
     */
    private void addNewMembersToConversation(List<Member> members, ServerConversation conversation, String username) throws CouldNotGetConversationException, CouldNotAddMemberException, UsernameNotPartOfConversationException {
        conversation.getMembers().addAllMembers(members, username);
    }

    /**
     * Removes all members if they are in the conversation.
     * @param members the list with the members to remove.
     * @param conversation the conversation these members are going to be removed from.
     * @param username the username of the end user who wants to remove members.
     * @throws UsernameNotPartOfConversationException gets thrown if the username is not a part of this conversation.
     * @throws CouldNotGetMemberException gets thrown if a member could not be located.
     * @throws CouldNotRemoveMemberException gets thrown if one or more members are missing form this object.
     */
    private void removeMembers(List<Member> members, ServerConversation conversation, String username) throws UsernameNotPartOfConversationException, CouldNotGetMemberException, CouldNotRemoveMemberException {
        conversation.getMembers().removeAllMembers(members, username);
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
