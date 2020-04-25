package BillboardAssignment.Database;

/**
 * This generic interface is used as a parent class for any sort of data storage method (Anything that can be queried using an unique ID).
 * It exists mainly so that code can be re-used whether the underlying storage is simply an array or an actual database (Useful for unit tests)
 *
 * @param <E> The generic contains one object that must implement the Identifiable interface. This simply means that the object must have some kind of primary integer key
 *            (as all things you store in a database should), and a method to access and change said ID, at the object level.
 * @author Sebastian Muir-Smith
 * @see Identifiable
 * @see DatabaseObjectNotFoundException
 * @see DatabaseNotAccessibleException
 * @see DatabaseLogicException
 */
public interface Queryable<E extends Identifiable> {

    /**
     * Add the given object to the database
     *
     * @param object The Identifiable object that we want to add to the database
     * @return A boolean indication whether or not the object was added successfully
     * @throws DatabaseNotAccessibleException Throws an exception if we can't access the database.
     *                                        Can be inherited to provide more reason about the error (I.E. Database connection failed)
     * @throws DatabaseLogicException         Throws an error if you make an invalid operation (Adding a object that allready exists)
     */
    boolean addObject(E object) throws DatabaseNotAccessibleException, DatabaseLogicException;

    /**
     * Remove the given object with the given ID from the database
     *
     * @param ID The unique integer identifier of the object that we want to remove
     * @return A boolean indication whether or not the object was removed successfully
     * @throws DatabaseNotAccessibleException Throws an exception if we can't access the database.
     *                                        Can be inherited to provide more reason about the error (I.E. Database connection failed)
     */
    boolean removeObject(int ID) throws DatabaseNotAccessibleException;

    /**
     * Get the given object with the given ID from the database
     *
     * @param ID The unique integer identifier of the object that we want to get
     * @return The object that we wanted to get from the database.
     * @throws DatabaseObjectNotFoundException Throws exception if the ID isn't in the database. Please check for it before querying.
     * @throws DatabaseNotAccessibleException  Throws an exception if we can't access the database.
     *                                         Can be inherited to provide more reason about the error (I.E. Database connection failed)
     */
    E getObject(int ID) throws DatabaseObjectNotFoundException, DatabaseNotAccessibleException;

    /**
     * Check if an object with the given ID is in the database
     *
     * @param object The object that we want to check exists
     * @return A boolean that is True iff there exists an object in the database with the given ID
     * @throws DatabaseNotAccessibleException Throws an exception if we can't access the database.
     *                                        Can be inherited to provide more reason about the error (I.E. Database connection failed)
     */
    boolean objectInDatabase(E object) throws DatabaseNotAccessibleException;

    /**
     * Run any necessary setup to get the database ready for storage and querying. The database will be created depended on
     * the getEntityName value of the object being stored. I.E. If the entity name is a sessionToken, then the sessionToken database will be created/accessed.
     *
     * @throws DatabaseNotAccessibleException Errors out if the database didn't get created successfully
     */
    void initialiseDatabase() throws DatabaseNotAccessibleException;
}
