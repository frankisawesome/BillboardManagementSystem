package BillboardAssignment.User;

import BillboardAssignment.Database.Identifiable;

/**
 * Class for handling the input of user authentication data from the front end.
 * Doesn't override getEntityName as this data should never be stored in a database.
 */
public class UserDataInput implements Identifiable {

    /**
     * ID for each user
     */
    private int userID;

    /**
     * Enum of privileges for each years
     */
    private UserPrivilege[] privileges;
    /**
     * We don't want this stored in the database, the input password that was hashed by the frontend
     */
    private byte[] onceHashedPassword;

    /**
     *  The username of the user, unique, but we don't use it as the primary key as that would be inefficient.
     */
    private String username;

    public UserDataInput(int userID, byte[] onceHashedPassword, UserPrivilege[] privileges, String username) {
        this.userID = userID;

        this.onceHashedPassword = onceHashedPassword;

        this.privileges = privileges;

        this.username = username;
    }

    /**
     * For when we don't care about the original password (Just going to store the double hashed version only)
     *
     * @param userID
     */
    public UserDataInput(int userID) {
        this.userID = userID;
        this.privileges = new UserPrivilege[]{};
        this.onceHashedPassword = "".getBytes();
        this.username = null;
    }

    /**
     * For when we don't care about the original priviledges (Just going to store the double hashed version only)
     *
     * @param userID
     */
    public UserDataInput(int userID, byte[] onceHashedPassword) {
        this.userID = userID;
        this.privileges = new UserPrivilege[]{};
        this.onceHashedPassword = onceHashedPassword;
        this.username = null;
    }

    public UserPrivilege[] getPrivileges() {
        return privileges;
    }

    /**
     * Make sure to overwrite this in any sub-class as it contains senstive data that shouldn't be stored/accessed except for hashing
     *
     * @return
     */
    public byte[] getOnceHashedPassword() {
        return onceHashedPassword;
    }

    public void setOnceHashedPassword(byte[] onceHashedPassword) {
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
     * @return user's username
     */
    public String getUsername(){return username;}



}
