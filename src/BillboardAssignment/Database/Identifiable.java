package BillboardAssignment.Database;

/**
 * Identifiable is an interface used to ensure that any objects stored in a database can be identified via a primary key.
 * Objects stored in a Queryable Collection must implement this interface.
 * Make sure you override getEntityName!! The IDE won't tell you to but you really need to!!
 * @author Sebastian Muir-Smith
 */
/*



 */
public interface Identifiable {

    /**
     * Get the unique identifier of the Identifiable object
     * @return The integer identifier associated with the given object
     */
    public int getID();

    /**
     * Change the given Identifiable object's ID to the supplied Integer ID
     * @param newID The integer ID that we want to replace the old ID with
     */
    public void changeID(int newID);


    /**
     * Get the name of the object entity. E.G. If user data is being stored, then this should return "User".
     * This will be used to choose what database the object will be stored in.
     * THIS MUST BE OVERRIDDEN IN YOUR INHERITED CLASS!!
     * @return A descriptor of the entity that is being stored.
     */
    public static String getEntityName(){
        return "ERROR: Please override me!!";
    };

}
