package BillboardAssignment.Database;

import java.util.ArrayList;

public class DatabaseArray<E extends Identifiable> implements Queryable<E> {

    /**
     * The actual "Database" that stores all of our objects in memory
     */
    private ArrayList<E> data;

    private boolean arrayInitialised = false;

    /**
     * Do a simple linear search to figure out the index of the object with the given ID, if it exists
     * @param ID The integer ID of the object we want to search for.
     * @return The index of the object if we found it, -1 if we didn't.
     */
    private int linearIDSearch(int ID){
        for (int i = 0; i < data.size(); i++){
            if (data.get(i).getID() == ID){
                return i;
            }
        }
        return -1;
    }

    private boolean contains(int ID){
        return linearIDSearch(ID) != -1;
    }


    /**
     * Add the given object to the database
     *
     * @param object The Identifiable object that we want to add to the database
     * @return A boolean indication whether or not the object was added successfully
     * @throws DatabaseNotAccessibleException Throws an exception if we can't access the database.
     *                                        Can be inherited to provide more reason about the error (I.E. Database connection failed)
     * @throws DatabaseLogicException Throws an exception if we try to do an illegal operation
     */
    @Override
    public boolean addObject(E object) throws DatabaseNotAccessibleException, DatabaseLogicException {
        if (!arrayInitialised){
            throw new DatabaseNotAccessibleException(E.getEntityName(), "The database array hasn't been initialised yet.");
        }

        if (contains(object.getID())){
            throw new DatabaseLogicException(E.getEntityName(), "The primary-key constraint of the object's ID was violated");
        }

        return data.add(object);
    }

    /**
     * Remove the given object with the given ID from the database
     *
     * @param ID The unique integer identifier of the object that we want to remove
     * @return A boolean indication whether or not the object was removed successfully
     * @throws DatabaseNotAccessibleException Throws an exception if we can't access the database.
     *                                        Can be inherited to provide more reason about the error (I.E. Database connection failed)
     */
    @Override
    public boolean removeObject(int ID) throws DatabaseNotAccessibleException {
        if (!arrayInitialised){
            throw new DatabaseNotAccessibleException(E.getEntityName(), "The database array hasn't been initialised yet!");
        }

        if (!contains(ID)){
            return false; /* The object was never in the database */
        }

        int index = linearIDSearch(ID);

        data.remove(index);
        return true;
    }

    /**
     * Get the given object with the given ID from the database
     *
     * @param ID The unique integer identifier of the object that we want to get
     * @return The object that we wanted to get from the database.
     * @throws DatabaseObjectNotFoundException Throws exception if the ID isn't in the database. Please check for it before querying.
     * @throws DatabaseNotAccessibleException  Throws an exception if we can't access the database.
     *                                         Can be inherited to provide more reason about the error (I.E. Database connection failed)
     */
    @Override
    public E getObject(int ID) throws DatabaseObjectNotFoundException, DatabaseNotAccessibleException {
        if (!arrayInitialised){
            throw new DatabaseNotAccessibleException(E.getEntityName(), "The database array hasn't been initialised yet!");
        }

        if (!contains(ID)){
            throw new DatabaseObjectNotFoundException(E.getEntityName(), ID);
        }

        int index = linearIDSearch(ID);


        return data.get(index);
    }

    /**
     * Check if an object with the given ID is in the database
     *
     * @param ID The unique integer identifier of the object that we want to check exists
     * @return A boolean that is True iff there exists an object in the database with the given ID
     * @throws DatabaseNotAccessibleException Throws an exception if we can't access the database.
     *                                        Can be inherited to provide more reason about the error (I.E. Database connection failed)
     */
    @Override
    public boolean objectInDatabase(E object) throws DatabaseNotAccessibleException {
        if (!arrayInitialised){
            throw new DatabaseNotAccessibleException(E.getEntityName(), "The database array hasn't been initialised yet!");
        }
        return linearIDSearch(object.getID()) != -1;
    }

    /**
     * Simply initialise the array
     */
    @Override
    public void initialiseDatabase() throws DatabaseNotAccessibleException {
        data = new ArrayList<E>();
        arrayInitialised = true;
    }
}
