package BillboardAssignment.User;

import BillboardAssignment.Authentication.*;
import BillboardAssignment.Database.DatabaseLogicException;
import BillboardAssignment.Database.DatabaseNotAccessibleException;
import BillboardAssignment.Database.DatabaseObjectNotFoundException;
import BillboardAssignment.Database.Queryable;

public class UserManager {

    public Queryable<User> userDatabase;
    private PasswordManager passwords;
    private SessionKeyManager sessionKeys;

    public UserManager(PasswordManager passwords, SessionKeyManager keyManager, Queryable<User> userDatabase) {
        this.passwords = passwords;
        this.sessionKeys = keyManager;
        this.userDatabase = userDatabase;
    }

    /**
     * Testing only, if we don't care about session keys
     *
     * @param passwords
     */
    public UserManager(PasswordManager passwords, Queryable<User> userDatabase) {
        this.passwords = passwords;
        this.sessionKeys = null;
        this.userDatabase = userDatabase;
    }

    /**
     * Tries to login the given user, using their user ID and once hashed password
     *
     * @param userCreds
     * @return Session key in string form
     * @throws IncorrectPasswordException
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseNotAccessibleException
     * @throws DatabaseLogicException
     */
    public UserSessionKey login(UserDataInput userCreds) throws IncorrectPasswordException, DatabaseObjectNotFoundException, DatabaseNotAccessibleException, DatabaseLogicException {
        passwords.checkPasswordMatch(userCreds);

        // We only get to this section if the password is correct, will throw error above if it isn't

        // Remove old session key, if it exists
        sessionKeys.removeSessionKey(userCreds);

        UserSessionKey sessionKey = sessionKeys.addSessionKeyData(userCreds);

        return sessionKey;
    }

    /**
     * Creates a user, if the second argument user has correct password and permissions
     *
     * @param userToAdd      The user we want to add, with password, ID and permissions
     * @param adminUserPerms The user with edit user permissions, needs
     * @return The user that was just added to the database
     * @throws DatabaseNotAccessibleException
     * @throws DatabaseLogicException
     * @throws DatabaseObjectNotFoundException
     * @throws IncorrectPasswordException
     * @throws InsufficentPrivilegeException
     */
    public User createUser(UserDataInput userToAdd, UserSessionKey adminUserPerms) throws DatabaseNotAccessibleException, DatabaseLogicException, DatabaseObjectNotFoundException, IncorrectPasswordException, InsufficentPrivilegeException, OutOfDateSessionKeyException, IncorrectSessionKeyException {

        User adminUser = null;

        if (sessionKeys.checkSessionKeyStatus(adminUserPerms)) {
            adminUser = getUser(adminUserPerms.getID());
        }

        adminUser.checkUserHasPriv(new UserPrivilege[]{UserPrivilege.EditUsers});

        // We only get to this section if the password and permissions are correct, will throw error above if they aren't

        User userWithNewPassword = passwords.hashNewPassword(userToAdd);

        userDatabase.addObject(userWithNewPassword);

        return userWithNewPassword;
    }


    /**
     * Creates the first, default admin user, with ID 69420, and password "pwd". This default password will likely have to change.
     * @return User that was just added
     * @throws DatabaseNotAccessibleException
     * @throws DatabaseLogicException
     * @throws DatabaseObjectNotFoundException
     * @throws IncorrectPasswordException
     * @throws InsufficentPrivilegeException
     * @throws OutOfDateSessionKeyException
     * @throws IncorrectSessionKeyException
     */
    public User createFirstUser() throws DatabaseNotAccessibleException, DatabaseLogicException, DatabaseObjectNotFoundException, IncorrectPasswordException, InsufficentPrivilegeException, OutOfDateSessionKeyException, IncorrectSessionKeyException {

        // We only get to this section if the password and permissions are correct, will throw error above if they aren't
        UserDataInput userToAdd = new UserDataInput(69420, "pwd".getBytes(), new UserPrivilege[]{UserPrivilege.CreateBillboards, UserPrivilege.EditAllBillboards, UserPrivilege.ScheduleBillboards, UserPrivilege.EditUsers}, "");
        User userWithNewPassword = passwords.hashNewPassword(userToAdd);

        userDatabase.addObject(userWithNewPassword);

        return userWithNewPassword;
    }

    protected User getUser(int ID) throws DatabaseObjectNotFoundException, DatabaseNotAccessibleException {
        return userDatabase.getObject(ID);
    }

    public User[] listUsers() throws DatabaseNotAccessibleException {
        return userDatabase.getAllObjects().toArray(new User[0]);
    }
}
