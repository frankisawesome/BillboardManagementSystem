package BillboardAssignment.Authentication;

/**
 * Simple Exception for when a password doesn't match the one on file
 */
public class OutOfDateSessionKeyException extends Exception {

    /**
     * Constructor of the Exception, user ID and session key to shout about (!!DO NOT SUPPLY THE REAL SESSION KEY)).
     *
     * @param UserID The ID of the user who has an expired session key
     */
    public OutOfDateSessionKeyException(int UserID) {
        super(String.format("The session key on file is out of date. The old session key must be removed, and a new session key generated using password authentication.\nUser: %s", UserID));
    }


}