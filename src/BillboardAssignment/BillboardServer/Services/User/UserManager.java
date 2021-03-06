package BillboardAssignment.BillboardServer.Services.User;

import BillboardAssignment.BillboardServer.Database.*;
import BillboardAssignment.BillboardServer.Services.Authentication.*;

import java.util.ArrayList;

public class UserManager {

    public static String defaultHashedAdminPasssword = "b\u0083¤$L\u0005\u0017SÉ(ÿÏ5\u008A!¬\u009E¡¥Î?ÊM½Òë9góa¯¯R¬ÊÀ\u0007\u001F\u0005\u0019ÛíG\u0086û\u0011Õ^úÔÃ.¸\u0086\u0088Çd_I\u00819Kwæ";
    public static int defaultAdminUserID = 69420;
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
    protected UserManager(PasswordManager passwords, Queryable<User> userDatabase) {
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
     * @throws DatabaseNotAccessibleException  If the database can't be connected to
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
     * @param adminUserPerms The user session key with edit user permissions
     * @return The user that was just added to the database
     * @throws DatabaseNotAccessibleException  If the database can't be connected to
     * @throws DatabaseLogicException
     * @throws DatabaseObjectNotFoundException
     * @throws IncorrectPasswordException
     * @throws InsufficentPrivilegeException
     */
    public User createUser(UserDataInput userToAdd, UserSessionKey adminUserPerms) throws DatabaseNotAccessibleException, DatabaseLogicException, DatabaseObjectNotFoundException, InsufficentPrivilegeException, OutOfDateSessionKeyException, IncorrectSessionKeyException {

        checkSessionKeyPrivileges(adminUserPerms, new UserPrivilege[]{UserPrivilege.EditUsers});

        User userWithNewPassword = passwords.hashNewPassword(userToAdd);

        userDatabase.addObject(userWithNewPassword);

        return userWithNewPassword;
    }


    /**
     * Creates the first, default admin user, with ID UserManager.defaultAdminUserID, and password "pwd". This default password will likely have to change.
     *
     * @return User that was just added
     * @throws DatabaseNotAccessibleException If the database can't be connected to
     * @throws DatabaseLogicException
     */
    public void createFirstUser() throws DatabaseNotAccessibleException, DatabaseLogicException, DatabaseObjectNotFoundException {
        try {
            User user = getUser(69420);
        } catch (DatabaseObjectNotFoundException e) {
            String onceHashedPassword = "b\u0083¤$L\u0005\u0017SÉ(ÿÏ5\u008A!¬\u009E¡¥Î?ÊM½Òë9góa¯¯R¬ÊÀ\u0007\u001F\u0005\u0019ÛíG\u0086û\u0011Õ^úÔÃ.¸\u0086\u0088Çd_I\u00819Kwæ";
            UserDataInput userToAdd = new UserDataInput(UserManager.defaultAdminUserID, onceHashedPassword, new UserPrivilege[]{UserPrivilege.CreateBillboards, UserPrivilege.EditAllBillboards, UserPrivilege.ScheduleBillboards, UserPrivilege.EditUsers}, "admin");
            User userWithNewPassword = passwords.hashNewPassword(userToAdd);


            userDatabase.addObject(userWithNewPassword);
        }
    }

    /**
     * Only ever used for tests, removes first user without checking session key or handling errror
     */
    public void deleteFirstUser() {
        try {
            userDatabase.removeObject(69420);
        } catch (Exception e) {

        }
    }


    /**
     * Not for the API, just used interally and for debugging
     *
     * @param ID
     * @return
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseNotAccessibleException  If the database can't be connected to
     */
    protected User getUser(int ID) throws DatabaseObjectNotFoundException, DatabaseNotAccessibleException {
        return userDatabase.getObject(ID);
    }

    /**
     * List all users, given a session key with the edit users perms. Can get any data from these objects you need.
     *
     * @param adminUserPerms The admin user session key.
     * @return An array of Users that the database holds.
     * @throws DatabaseNotAccessibleException  If the database can't be connected to
     * @throws InsufficentPrivilegeException
     * @throws OutOfDateSessionKeyException
     * @throws DatabaseObjectNotFoundException
     * @throws IncorrectSessionKeyException
     */
    public User[] listUsers(UserSessionKey adminUserPerms) throws DatabaseNotAccessibleException, InsufficentPrivilegeException, OutOfDateSessionKeyException, DatabaseObjectNotFoundException, IncorrectSessionKeyException {

        checkSessionKeyPrivileges(adminUserPerms, new UserPrivilege[]{UserPrivilege.EditUsers});

        return userDatabase.getAllObjects().toArray(new User[0]);
    }

    /**
     * Get the permissions of a user. If you're grabbing permissions for yourself you need no perms, but for others you need edit users.
     *
     * @param userToGet
     * @param userSessionKey
     * @return List of privledges
     * @throws InsufficentPrivilegeException
     * @throws OutOfDateSessionKeyException
     * @throws DatabaseNotAccessibleException  If the database can't be connected to
     * @throws IncorrectSessionKeyException
     * @throws DatabaseObjectNotFoundException
     */
    public UserPrivilege[] getPermissions(UserDataInput userToGet, UserSessionKey userSessionKey) throws InsufficentPrivilegeException, OutOfDateSessionKeyException, DatabaseNotAccessibleException, IncorrectSessionKeyException, DatabaseObjectNotFoundException {
        User adminUser = null;

        if (sessionKeys.checkSessionKeyStatus(userSessionKey)) {
            adminUser = getUser(userSessionKey.getID());
        }
        // We only get to this section if the password and permissions are correct, will throw error above if they aren't

        if (adminUser.getID() == userToGet.getID()) {
            return userDatabase.getObject(userToGet.getID()).getPrivileges();
        }

        adminUser.checkUserHasPriv(new UserPrivilege[]{UserPrivilege.EditUsers});

        return userDatabase.getObject(userToGet.getID()).getPrivileges();
    }

    /**
     * Set the privileges of a user. Needs edit users permission
     *
     * @param userToChange
     * @param privilegesToSet
     * @param adminKey
     * @throws OutOfDateSessionKeyException
     * @throws DatabaseNotAccessibleException       If the database can't be connected to
     * @throws InsufficentPrivilegeException
     * @throws IncorrectSessionKeyException
     * @throws DatabaseObjectNotFoundException
     * @throws RemoveOwnEditUsersPrivilegeException Throws if you try to remove your own admin privileges
     * @throws DatabaseLogicException
     */
    public void setPermissions(UserDataInput userToChange, UserPrivilege[] privilegesToSet, UserSessionKey adminKey) throws OutOfDateSessionKeyException, DatabaseNotAccessibleException, InsufficentPrivilegeException, IncorrectSessionKeyException, DatabaseObjectNotFoundException, RemoveOwnEditUsersPrivilegeException, DatabaseLogicException {

        User adminUser = checkSessionKeyPrivileges(adminKey, new UserPrivilege[]{UserPrivilege.EditUsers});

        if (adminKey.getID() == userToChange.getID()) {
            boolean gettingRidOfEditUsers = true;
            for (int i = 0; i < privilegesToSet.length; i++) {
                if (privilegesToSet[i] == UserPrivilege.EditUsers) {
                    gettingRidOfEditUsers = false;
                }
            }
            if (gettingRidOfEditUsers) {
                throw new RemoveOwnEditUsersPrivilegeException();
            }
        }

        User userToChangeInDatabase = userDatabase.getObject(userToChange.getID());

        userDatabase.removeObject(userToChange.getID());

        userDatabase.addObject(new User(userToChangeInDatabase.getID(), userToChangeInDatabase.twiceHashedPassword, userToChangeInDatabase.salt, privilegesToSet, userToChangeInDatabase.getUsername()));

    }

    /**
     * Sets the password, given the input object (only really need user ID), new password, and a session key.
     *
     * @param userToChange
     * @param newPassword
     * @param userSessionKey
     * @throws OutOfDateSessionKeyException
     * @throws DatabaseNotAccessibleException  If the database can't be connected to
     * @throws IncorrectSessionKeyException
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseLogicException
     * @throws InsufficentPrivilegeException
     */
    public void setPassword(UserDataInput userToChange, String newPassword, UserSessionKey userSessionKey) throws OutOfDateSessionKeyException, DatabaseNotAccessibleException, IncorrectSessionKeyException, DatabaseObjectNotFoundException, DatabaseLogicException, InsufficentPrivilegeException {
        User adminUser = null;

        if (sessionKeys.checkSessionKeyStatus(userSessionKey)) {
            adminUser = getUser(userSessionKey.getID());
        }
        // We only get to this section if the password and permissions are correct, will throw error above if they aren't

        if (adminUser.getID() == userToChange.getID()) {
            passwords.changePassword(userToChange, newPassword);
        } else {
            adminUser.checkUserHasPriv(new UserPrivilege[]{UserPrivilege.EditUsers});
            passwords.changePassword(userToChange, newPassword);
        }
    }

    /**
     * Remove a user from both user and session key databases. Requires edit users perm
     *
     * @param userToDelete
     * @param adminKey
     * @throws OutOfDateSessionKeyException
     * @throws DatabaseNotAccessibleException  If the database can't be connected to
     * @throws InsufficentPrivilegeException
     * @throws IncorrectSessionKeyException
     * @throws DatabaseObjectNotFoundException
     * @throws RemoveOwnUserException
     */
    public void deleteUser(UserDataInput userToDelete, UserSessionKey adminKey) throws OutOfDateSessionKeyException, DatabaseNotAccessibleException, InsufficentPrivilegeException, IncorrectSessionKeyException, DatabaseObjectNotFoundException, RemoveOwnUserException {
        User admin = checkSessionKeyPrivileges(adminKey, new UserPrivilege[]{UserPrivilege.EditUsers});

        if (userToDelete.getID() == admin.getID()) {
            throw new RemoveOwnUserException();
        }
        userDatabase.removeObject(userToDelete.getID());
        sessionKeys.removeSessionKey(userToDelete);
    }

    public User checkSessionKeyPrivileges(UserSessionKey key, UserPrivilege[] privileges) throws InsufficentPrivilegeException, DatabaseObjectNotFoundException, DatabaseNotAccessibleException, OutOfDateSessionKeyException, IncorrectSessionKeyException {
        User adminUser = null;

        if (sessionKeys.checkSessionKeyStatus(key)) {
            adminUser = getUser(key.getID());
        }

        adminUser.checkUserHasPriv(privileges);
        // We only get to this section if the password and permissions are correct, will throw error above if they aren't
        return adminUser;
    }

    public User checkSessionKeyPrivileges(UserSessionKey key, UserPrivilege privileges) throws InsufficentPrivilegeException, DatabaseObjectNotFoundException, DatabaseNotAccessibleException, OutOfDateSessionKeyException, IncorrectSessionKeyException {
        User adminUser = null;

        if (sessionKeys.checkSessionKeyStatus(key)) {
            adminUser = getUser(key.getID());
        }

        adminUser.checkUserHasPriv(privileges);
        // We only get to this section if the password and permissions are correct, will throw error above if they aren't
        return adminUser;
    }

    /**
     * Log out the given user, essentially just delete their session key from the database. Returns true iff session key was in the database in the first place
     *
     * @param key
     * @return
     * @throws DatabaseNotAccessibleException If the database can't be connected to
     */
    public boolean logout(UserSessionKey key) throws DatabaseNotAccessibleException {

        return sessionKeys.removeSessionKey(new UserDataInput(key.getID(), "", new UserPrivilege[0], ""));

    }

    /**
     * Map a given username to an integer ID in the database. Will error out if nobody has a given username
     *
     * @param username
     * @return the ID of the user with said username
     * @throws DatabaseObjectNotFoundException
     * @throws NoSuchFieldException
     * @throws DatabaseNotAccessibleException  If the database can't be connected to
     * @throws DatabaseMultipleMatchException
     */
    public int mapUsernameToID(String username) throws DatabaseObjectNotFoundException, DatabaseNotAccessibleException, DatabaseMultipleMatchException {
        ArrayList<User> result;
        try {
            result = userDatabase.getWhere("username", username, new User(1, "", "", new UserPrivilege[0], ""));
        } catch (NoSuchFieldException e) {
            // This will never happen
            throw new DatabaseNotAccessibleException("user");
        }

        if (result.size() > 1) {
            throw new DatabaseMultipleMatchException();
        }

        return result.get(0).getID();
    }

    /**
     * Get the next integer ID to be added, for making new users
     *
     * @return the next ID
     * @throws DatabaseNotAccessibleException If the database can't be connected to
     */
    public int getNextID() throws DatabaseNotAccessibleException {
        return userDatabase.getMaxID() + 1;
    }
}
