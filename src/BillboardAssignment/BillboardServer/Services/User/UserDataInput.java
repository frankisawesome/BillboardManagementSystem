package BillboardAssignment.BillboardServer.Services.User;

import BillboardAssignment.BillboardServer.Database.Identifiable;

/**
 * Class for handling the input of user authentication data from the front end.
 * Doesn't override getEntityName as this data should never be stored in a database.
 */
public class UserDataInput implements Identifiable {

    /**
     * The username of the user, unique, but we don't use it as the primary key as that would be inefficient and can cause problems with string comparison, etc.
     */
    public String username;
    /**
     * ID for each user
     */
    private int userID;
    /**
     * Enum array of privileges for each user
     */
    private UserPrivilege[] privileges;
    /**
     * We don't want this stored in the database, the input password that was hashed by the frontend
     */
    private String onceHashedPassword;

    /**
     * Default User Data input constructor
     * @param userID Unique integer identifier of the user
     * @param onceHashedPassword Password from the front end that is only hashed once, will not be stored in the DB
     * @param privileges Enum array of privileges for each user
     * @param username The username of the user
     */
    public UserDataInput(int userID, String onceHashedPassword, UserPrivilege[] privileges, String username) {
        this.userID = userID;

        this.onceHashedPassword = onceHashedPassword;

        this.privileges = privileges;

        this.username = username;
    }

    /**
     * User Data input constructor for when we don't care about privs or usernames
     * @param userID Unique integer identifier of the user
     * @param onceHashedPassword Password from the front end that is only hashed once, will not be stored in the DB
     */
    public UserDataInput(int userID, String onceHashedPassword) {
        this.userID = userID;

        this.onceHashedPassword = onceHashedPassword;

        this.privileges = new UserPrivilege[0];

        this.username = "";
    }

    public UserDataInput(int id, String password, String newUserName) {
        this.userID = id;
        this.privileges = new UserPrivilege[]{};
        this.onceHashedPassword = password;
        this.username = newUserName;
    }

    public UserPrivilege[] getPrivileges() {
        return privileges;
    }

    /**
     * Make sure that you don't store the password in the database, just use null in the inheritance constructor
     *
     * @return
     */
    public String getOnceHashedPassword() {
        return onceHashedPassword;
    }

    public void setOnceHashedPassword(String onceHashedPassword) {
        this.onceHashedPassword = onceHashedPassword;
    }

    /**
     * Get the unique identifier of the Identifiable object
     *
     * @return The integer identifier associated with the given object
     */
    @Override
    public int getID() {
        return userID;
    }

    /**
     * Change the given Identifiable object's ID to the supplied Integer ID
     *
     * @param newID The integer ID that we want to replace the old ID with
     */
    @Override
    public void changeID(int newID) {
        userID = newID;
    }

    /**
     * Username getter
     *
     * @return user's username
     */
    public String getUsername() {
        return username;
    }


}
