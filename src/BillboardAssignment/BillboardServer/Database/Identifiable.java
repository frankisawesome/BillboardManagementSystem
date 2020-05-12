package BillboardAssignment.BillboardServer.Database;

/**
 * Identifiable is an interface used to ensure that any objects stored in a database can be identified via a primary key.
 * Objects stored in a Queryable Collection must implement this interface.
 * Make sure you override getEntityName!! The IDE won't tell you to but you really need to!!
 *
 * @author Sebastian Muir-Smith
 */
/*



 */
public interface Identifiable {

    /**
     * Get the unique identifier of the Identifiable object
     *
     * @return The integer identifier associated with the given object
     */
    int getID();

    /**
     * Change the given Identifiable object's ID to the supplied Integer ID
     *
     * @param newID The integer ID that we want to replace the old ID with
     */
    void changeID(int newID);

}
