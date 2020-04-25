package BillboardAssignment.User;

import BillboardAssignment.Database.Identifiable;

/**
 * Utility class essentially so we can have instances of the Identifiable class for testing. Don't try to store this in a database in production please.
 */
public class BaseUser implements Identifiable {

    private int userID;

    public BaseUser(int userID) {
        this.userID = userID;
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
        this.userID = newID;
    }
}
