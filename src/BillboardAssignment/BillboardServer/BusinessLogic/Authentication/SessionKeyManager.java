package BillboardAssignment.BillboardServer.BusinessLogic.Authentication;

import BillboardAssignment.BillboardServer.Database.*;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

public class SessionKeyManager {

    private Queryable<UserSessionKey> sessionKeyDatabase;

    public SessionKeyManager(Queryable<UserSessionKey> database) throws DatabaseNotAccessibleException {
        this.sessionKeyDatabase = database;
        database.initialiseDatabase("SessionKeys");
    }

    /**
     * Create a random 25-length string session key.
     *
     * @return a random 25-length string session key.
     */
    private String createSessionKey() {
        SecureRandom random = new SecureRandom();
        Base64.Encoder byteToStringGenerator = Base64.getEncoder(); /* Turns bytes into url-safe strings */

        // Let's use strings with length 25
        byte[] bytes = new byte[25];

        random.nextBytes(bytes);

        return byteToStringGenerator.encodeToString(bytes);

    }

    /**
     * Takes a given input userID (Enclosed in an object for API consistency), and generates a new session key and expiry date, and then stores it in the database.
     * Assumes that any old session keys are not in the database, so please remove them before adding new session keys.
     *
     * @param user
     * @return The user that was stored, with session key and valid time, etc.
     * @throws DatabaseNotAccessibleException
     * @throws DatabaseLogicException
     */
    public UserSessionKey addSessionKeyData(Identifiable user) throws DatabaseNotAccessibleException, DatabaseLogicException {
        String randomSessionKey = createSessionKey();
        int userID = user.getID();


        LocalDateTime sessionKeyExpiryTime = LocalDateTime.now().plusHours(24);

        UserSessionKey dataToStore = new UserSessionKey(userID, randomSessionKey, sessionKeyExpiryTime);

        sessionKeyDatabase.addObject(dataToStore);

        return dataToStore;
    }

    /**
     * Check the given user's ID and session key input in a UserSessionKey object to see if they match the data on file.
     * Throws one of 4 exceptions if there is a problem (And the problem should be handled differently depending on the exception), and true anytime else.
     *
     * @param user
     * @return Allways errors out or returns true
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseNotAccessibleException
     * @throws IncorrectSessionKeyException
     * @throws OutOfDateSessionKeyException
     */
    public boolean checkSessionKeyStatus(UserSessionKey user) throws DatabaseObjectNotFoundException, DatabaseNotAccessibleException, IncorrectSessionKeyException, OutOfDateSessionKeyException {
        UserSessionKey dataInDatabase = sessionKeyDatabase.getObject(user.getID());

        if (dataInDatabase.expiryDateTime.isBefore(LocalDateTime.now())) {
            throw new OutOfDateSessionKeyException(user.getID());
        }

        if (dataInDatabase.sessionKey != user.sessionKey) {
            throw new IncorrectSessionKeyException(user.getID(), user.sessionKey);
        }


        return true; /* If we made it this far without erroring out, then the session key is correct. */
    }

    /**
     * Check the given user's ID and session key input in a UserSessionKey object to see if they match the data on file.
     * DO NOT USE THIS METHOD SIGNATURE, IT IS ONLY FOR TESTING.
     *
     * @param user
     * @param debugTime The time to check the session key in the database against. Don't use this at all, except for testing.
     * @return Allways errors out or returns true
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseNotAccessibleException
     * @throws IncorrectSessionKeyException
     * @throws OutOfDateSessionKeyException
     */
    protected boolean checkSessionKeyStatus(UserSessionKey user, LocalDateTime debugTime) throws DatabaseObjectNotFoundException, DatabaseNotAccessibleException, IncorrectSessionKeyException, OutOfDateSessionKeyException {
        UserSessionKey dataInDatabase = sessionKeyDatabase.getObject(user.getID());

        if (dataInDatabase.expiryDateTime.isBefore(debugTime)) {
            throw new OutOfDateSessionKeyException(user.getID());
        }

        if (dataInDatabase.sessionKey != user.sessionKey) {
            throw new IncorrectSessionKeyException(user.getID(), user.sessionKey);
        }


        return true; /* If we made it this far without erroring out, then the session key is correct. */
    }

    /**
     * Remove a given session key
     *
     * @param user
     * @return boolean if the session key existed in the first place or not
     * @throws DatabaseNotAccessibleException
     */
    public boolean removeSessionKey(Identifiable user) throws DatabaseNotAccessibleException {
        return sessionKeyDatabase.removeObject(user.getID());
    }

}
