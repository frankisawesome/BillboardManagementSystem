package BillboardAssignment.User;

import BillboardAssignment.Authentication.*;
import BillboardAssignment.Database.DatabaseLogicException;
import BillboardAssignment.Database.DatabaseNotAccessibleException;
import BillboardAssignment.Database.DatabaseObjectNotFoundException;
import BillboardAssignment.Database.Queryable;

public class UserManager {

    private PasswordManager passwords;
    private SessionKeyManager sessionKeys;
    public Queryable<User> userDatabase;

    public UserManager(PasswordManager passwords, SessionKeyManager keyManager, Queryable<User> userDatabase) {
        this.passwords = passwords;
        this.sessionKeys = keyManager;
        this.userDatabase = userDatabase;
    }

    /**
     * Testing only, if we don't care about session keys
     * @param passwords
     */
    public UserManager(PasswordManager passwords, Queryable<User> userDatabase) {
        this.passwords = passwords;
        this.sessionKeys = null;
        this.userDatabase = userDatabase;
    }

    /**
     * Tries to login the given user, using their user ID and once hashed password
     * @param userCreds
     * @return Session key in string form
     * @throws IncorrectPasswordException
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseNotAccessibleException
     * @throws DatabaseLogicException
     */
    public String login(UserDataInput userCreds) throws IncorrectPasswordException, DatabaseObjectNotFoundException, DatabaseNotAccessibleException, DatabaseLogicException {
        passwords.checkPasswordMatch(userCreds);

        // We only get to this section if the password is correct, will throw error above if it isn't

        // Remove old session key, if it exists
        sessionKeys.removeSessionKey(userCreds);

        SessionKeyDataOutput sessionKey = sessionKeys.addSessionKeyData(userCreds);

        return sessionKey.sessionKey;
    }

    public User createUser(UserDataInput user) throws DatabaseNotAccessibleException, DatabaseLogicException {

        User userWithNewPassword = passwords.hashNewPassword(user);

        userDatabase.addObject(userWithNewPassword);

        return userWithNewPassword;
    }
}
