package BillboardAssignment.Authentication;

/**
 * Simple Exception for when a password doesn't match the one on file
 */
public class IncorrectSessionKeyException extends Exception {

    /**
     * Constructor of the Exception, user ID and session key to shout about (!!DO NOT SUPPLY THE REAL SESSION KEY)).
     *
     * @param UserID          The ID of the user that got their password wrong
     * @param inputSessionKey The input session key that was wrong
     */
    public IncorrectSessionKeyException(int UserID, String inputSessionKey) {
        super(String.format("The given session key does not match the one in the database.\nUser: %s, sessionKey: %s", UserID, inputSessionKey));
    }


}