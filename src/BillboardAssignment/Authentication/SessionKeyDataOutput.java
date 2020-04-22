package BillboardAssignment.Authentication;

import BillboardAssignment.Database.Identifiable;

import java.time.LocalDateTime;

public class SessionKeyDataOutput implements Identifiable {

    public String sessionKey;

    public LocalDateTime expiryDateTime;

    private int userID;
    /**
     * Just creating a basic object to store user ID, just for consistency of the API this class inherits UserAuthDataInput
     * @param userID
     */
    public SessionKeyDataOutput(int userID, String sessionKey, LocalDateTime expiryDateTime) {
        this.userID = userID;

        this.sessionKey = sessionKey;

        this.expiryDateTime = expiryDateTime;
    }

    /**
     * Constructor for when we don't care about the expiry data (e.g. we just want to check on the current datetime). Don't store objects created like this.
     * @param userID
     * @param sessionKey
     */
    public SessionKeyDataOutput(int userID, String sessionKey) {
        this.userID = userID;

        this.sessionKey = sessionKey;
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

    /**
     * Get the name of the object entity. E.G. If user data is being stored, then this should return "User".
     * This will be used to choose what database the object will be stored in.
     * @return A descriptor of the entity that is being stored.
     */
    public static String getEntityName(){
        return "SessionKeyDataOutput";
    }
}
