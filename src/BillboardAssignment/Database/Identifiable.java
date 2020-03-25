package BillboardAssignment.Database;

/**
 * Identifiable is an interface used to ensure that any objects stored in a database can be identified via a primary key.
 * Objects stored in a Queryable Collection must implement this interface.
 *
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
     * @return
     */
    public boolean changeID(int newID);

}
