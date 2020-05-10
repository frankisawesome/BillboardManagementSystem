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
     * @param adminUserPerms The user session key with edit user permissions
     * @return The user that was just added to the database
     * @throws DatabaseNotAccessibleException
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

        UserDataInput userToAdd = new UserDataInput(69420, "pwd".getBytes(), new UserPrivilege[]{UserPrivilege.CreateBillboards, UserPrivilege.EditAllBillboards, UserPrivilege.ScheduleBillboards, UserPrivilege.EditUsers}, "");
        User userWithNewPassword = passwords.hashNewPassword(userToAdd);

        userDatabase.addObject(userWithNewPassword);

        return userWithNewPassword;
    }


    /**
     * Not for the API, just used interally and for debugging
     * @param ID
     * @return
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseNotAccessibleException
     */
    protected User getUser(int ID) throws DatabaseObjectNotFoundException, DatabaseNotAccessibleException {
        return userDatabase.getObject(ID);
    }

    /**
     * List all users, given a session key with the edit users perms. Can get any data from these objects you need.
     * @param adminUserPerms The admin user session key.
     * @return An array of Users that the database holds.
     * @throws DatabaseNotAccessibleException
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
     * @param userID
     * @param userSessionKey
     * @return List of privledges
     * @throws InsufficentPrivilegeException
     * @throws OutOfDateSessionKeyException
     * @throws DatabaseNotAccessibleException
     * @throws IncorrectSessionKeyException
     * @throws DatabaseObjectNotFoundException
     */
    public UserPrivilege[] getPermissions(int userID, UserSessionKey userSessionKey) throws InsufficentPrivilegeException, OutOfDateSessionKeyException, DatabaseNotAccessibleException, IncorrectSessionKeyException, DatabaseObjectNotFoundException {
        User adminUser = null;

        if (sessionKeys.checkSessionKeyStatus(userSessionKey)) {
            adminUser = getUser(userSessionKey.getID());
        }
        // We only get to this section if the password and permissions are correct, will throw error above if they aren't

        if (adminUser.getID() == userID){
            return userDatabase.getObject(userID).getPrivileges();
        }

        adminUser.checkUserHasPriv(new UserPrivilege[]{UserPrivilege.EditUsers});

        return userDatabase.getObject(userID).getPrivileges();
    }

    /**
     * Set the privileges of a user. Needs edit users permission
     * @param userToChange
     * @param privilegesToSet
     * @param adminKey
     * @throws OutOfDateSessionKeyException
     * @throws DatabaseNotAccessibleException
     * @throws InsufficentPrivilegeException
     * @throws IncorrectSessionKeyException
     * @throws DatabaseObjectNotFoundException
     * @throws RemoveOwnEditUsersPrivilegeException Throws if you try to remove your own admin privileges
     * @throws DatabaseLogicException
     */
    public void setPermissions(UserDataInput userToChange, UserPrivilege[] privilegesToSet, UserSessionKey adminKey) throws OutOfDateSessionKeyException, DatabaseNotAccessibleException, InsufficentPrivilegeException, IncorrectSessionKeyException, DatabaseObjectNotFoundException, RemoveOwnEditUsersPrivilegeException, DatabaseLogicException {

        User adminUser = checkSessionKeyPrivileges(adminKey, new UserPrivilege[]{UserPrivilege.EditUsers});

        if (adminKey.getID() == userToChange.getID()){
            boolean gettingRidOfEditUsers = true;
            for (int i =0; i < privilegesToSet.length; i++){
                if (privilegesToSet[i] == UserPrivilege.EditUsers){
                    gettingRidOfEditUsers = false;
                }
            }
            if (gettingRidOfEditUsers){
                throw new RemoveOwnEditUsersPrivilegeException();
            }
        }

        User userToChangeInDatabase = userDatabase.getObject(userToChange.getID());

        userDatabase.removeObject(userToChange.getID());

        userDatabase.addObject(new User(userToChangeInDatabase.getID(), userToChangeInDatabase.twiceHashedPassword, userToChangeInDatabase.salt, privilegesToSet, userToChangeInDatabase.getUsername()));

    }

    private User checkSessionKeyPrivileges(UserSessionKey key, UserPrivilege[] privileges) throws InsufficentPrivilegeException, DatabaseObjectNotFoundException, DatabaseNotAccessibleException, OutOfDateSessionKeyException, IncorrectSessionKeyException {
        User adminUser = null;

        if (sessionKeys.checkSessionKeyStatus(key)) {
            adminUser = getUser(key.getID());
        }

        adminUser.checkUserHasPriv(privileges);
        // We only get to this section if the password and permissions are correct, will throw error above if they aren't
        return adminUser;
    }
}
