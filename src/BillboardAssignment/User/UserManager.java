package BillboardAssignment.User;

import BillboardAssignment.Authentication.*;
import BillboardAssignment.Database.DatabaseLogicException;
import BillboardAssignment.Database.DatabaseNotAccessibleException;
import BillboardAssignment.Database.DatabaseObjectNotFoundException;

public class UserManager {

    private PasswordManager passwords;
    private SessionKeyManager sessionKeys;

    public UserManager(PasswordManager passwords, SessionKeyManager keyManager) {
        this.passwords = passwords;
        this.sessionKeys = keyManager;
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
    public String login(UserAuthDataInput userCreds) throws IncorrectPasswordException, DatabaseObjectNotFoundException, DatabaseNotAccessibleException, DatabaseLogicException {
        passwords.checkPasswordMatch(userCreds);

        // We only get to this section if the password is correct, will throw error above if it isn't

        // Remove old session key, if it exists
        sessionKeys.removeSessionKey(userCreds);

        SessionKeyDataOutput sessionKey = sessionKeys.addSessionKeyData(userCreds);

        return sessionKey.sessionKey;
    }

    public PrivilegedUser createUser(PrivilegedUser user) {
    }
}
