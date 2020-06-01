package BillboardAssignment.BillboardServer.BusinessLogic.Authentication;

/**
 * Simple Exception for when a password doesn't match the one on file
 */
public class IncorrectPasswordException extends Exception {

    /**
     * Constructor of the Exception, user ID and onceHashedPassword to shout about (!!DO NOT SUPPLY THE TWICE HASHED PASSWORD)).
     *
     * @param UserID                   The ID of the user that got their password wrong
     * @param onceHashedPasswordString The input password that was wrong
     */
    public IncorrectPasswordException(int UserID, String onceHashedPasswordString) {
        super(String.format("The given password does not match.\nUser: %s, HashedPassword: %s", UserID, new String(onceHashedPasswordString)));
    }


}
