package no.stonedstonar.chatapplication.model.member;
/**
 *
 * @version 0.2
 * @author Steinar Hjelle Midthus
 */
public interface Member {

    /**
     * Gets the username of the member.
     * @return the username.
     */
    String getUsername();

    /**
     * Gets the members number.
     * @return the members number.
     */
    long getMemberNumber();
}
