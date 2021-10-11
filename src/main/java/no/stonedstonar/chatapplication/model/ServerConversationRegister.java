package no.stonedstonar.chatapplication.model;

import no.stonedstonar.chatapplication.model.exception.member.CouldNotAddMemberException;
import no.stonedstonar.chatapplication.model.exception.messagelog.CouldNotAddMessageLogException;

import java.util.List;

/**
 *
 * @version 0.1
 * @author Steinar Hjelle Midthus
 */
public class ServerConversationRegister extends ConversationRegister {

    /**
      * Makes an instance of the ServerConversationalRegister class.
      */
    public ServerConversationRegister(){
        super();
    }

    /**
     * Gets the message logs that matches this username.
     * @param username the username that the message log is for.
     * @return the message logs that belongs to this username in a list.
     */
    public List<MessageLog> getAllMessageLogsOfUsername(String username){
        checkString(username, "username");
        return getMessageLogList().stream().filter(messageLog -> {
            return messageLog.getMembersOfConversation().checkIfUsernameIsMember(username);
        }).toList();
    }

    /**
     * Adds a new message log based on a list of names that are in it.
     * @param usernames a list with all the usernames that are part of this message log.
     * @return the message log that was just added to the system.
     * @throws CouldNotAddMessageLogException gets thrown if the message log is already in the register.
     * @throws CouldNotAddMemberException gets thrown if a member could not be added.
     */
    public MessageLog addNewMessageLogWithUsernames(List<String> usernames) throws CouldNotAddMessageLogException, CouldNotAddMemberException {
        checkIfObjectIsNull(usernames, "usernames");
        if (!usernames.isEmpty()){
            MessageLog messageLog = new MessageLog(makeNewMessageLogNumber());
            messageLog.getMembersOfConversation().addAllMembers(usernames);
            addMessageLog(messageLog);
            return messageLog;
        }else {
            throw new IllegalArgumentException("The size of the usernames must be larger than 0.");
        }
    }

    /**
     * Makes a new log number for each log that is in the list.
     * @return the number that the new message log can have.
     */
    private long makeNewMessageLogNumber(){
        long number = 1;
        List<MessageLog> messageLogList = getMessageLogList();
        if (messageLogList.size() > 0){
            number = (messageLogList.get(messageLogList.size() - 1).getMessageLogNumber() + 1);
        }
        return number;
    }
}
