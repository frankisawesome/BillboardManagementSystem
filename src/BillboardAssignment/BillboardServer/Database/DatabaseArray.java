package BillboardAssignment.BillboardServer.Database;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class DatabaseArray<E extends Identifiable> implements Queryable<E> {

    /**
     * The actual "Database" that stores all of our objects in memory
     */
    private ArrayList<E> data;

    private boolean arrayInitialised = false;

    private String entityName;

    /**
     * Do a simple linear search to figure out the index of the object with the given ID, if it exists
     *
     * @param ID The integer ID of the object we want to search for.
     * @return The index of the object if we found it, -1 if we didn't.
     */
    private int linearIDSearch(int ID) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getID() == ID) {
                return i;
            }
        }
        return -1;
    }

    private boolean contains(int ID) {
        return linearIDSearch(ID) != -1;
    }


    /**
     * Add the given object to the database
     *
     * @param object The Identifiable object that we want to add to the database
     * @return A boolean indication whether or not the object was added successfully
     * @throws DatabaseNotAccessibleException Throws an exception if we can't access the database.
     *                                        Can be inherited to provide more reason about the error (I.E. Database connection failed)
     * @throws DatabaseLogicException         Throws an exception if we try to do an illegal operation
     */
    @Override
    public boolean addObject(E object) throws DatabaseNotAccessibleException, DatabaseLogicException {
        if (!arrayInitialised) {
            throw new DatabaseNotAccessibleException(entityName, "The database array hasn't been initialised yet.");
        }

        if (contains(object.getID())) {
            throw new DatabaseLogicException(entityName, "The primary-key constraint of the object's ID was violated");
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
        if (!arrayInitialised) {
            throw new DatabaseNotAccessibleException(entityName, "The database array hasn't been initialised yet!");
        }

        if (!contains(ID)) {
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
        if (!arrayInitialised) {
            throw new DatabaseNotAccessibleException(entityName, "The database array hasn't been initialised yet!");
        }

        if (!contains(ID)) {
            throw new DatabaseObjectNotFoundException(entityName, ID);
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
        if (!arrayInitialised) {
            throw new DatabaseNotAccessibleException(entityName, "The database array hasn't been initialised yet!");
        }
        return linearIDSearch(object.getID()) != -1;
    }


    @Override
    public void initialiseDatabase(String entityName) {
        data = new ArrayList<E>();
        arrayInitialised = true;
        this.entityName = entityName;
    }

    @Override
    public ArrayList<E> getAllObjects() throws DatabaseNotAccessibleException {
        if (!arrayInitialised) {
            throw new DatabaseNotAccessibleException(entityName, "The database array hasn't been initialised yet!");
        }

        return data;
    }


    @Override
    public int getMaxID() throws DatabaseNotAccessibleException {
        if (!arrayInitialised) {
            throw new DatabaseNotAccessibleException(entityName, "The database array hasn't been initialised yet!");
        }
        int max = 0;

        for (int i = 0; i < data.size(); i++ ){
            max = Math.max(max, data.get(i).getID());
        }

        return max;
    }

    /**
     * Get name of database, for debugging
     */
    @Override
    public String getEntityName(){
        return this.entityName;
    }

    /**
     * Remove all observations in dataset
     *
     * @throws DatabaseNotAccessibleException
     */
    @Override
    public void removeAllData() throws DatabaseNotAccessibleException {
        if (!arrayInitialised) {
            throw new DatabaseNotAccessibleException(entityName, "The database array hasn't been initialised yet!");
        }
        data = new ArrayList<E>();
    }

    /**
     * Gets the given object, which's parameterName parameter's value matched parameterValue.
     * E.G. select * from db where parameterName = parameterValue
     * @param parameterName The name of the object's parameter
     * @param parameterValue The value of said parameter we want to search for
     * @param dummyObject Any object of type E, we need it to check if a parameter exists
     * @return The object(s) that satisfy the condition
     * @throws DatabaseNotAccessibleException
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseMultipleMatchException
     */
    @Override
    public ArrayList<E> getWhere(String parameterName, int parameterValue, E dummyObject) throws DatabaseNotAccessibleException, DatabaseObjectNotFoundException, NoSuchFieldException {
        if (!arrayInitialised) {
            throw new DatabaseNotAccessibleException(entityName, "The database array hasn't been initialised yet!");
        }

        /* This will error if the field doesn't exist */
        Field field = dummyObject.getClass().getField(parameterName);

        E obj;
        ArrayList<E> output = new ArrayList<E>();

        for (int i = 0; i < data.size(); i++ ){
            obj = data.get(i);
            try {
                if ((int) field.get(data.get(i)) == parameterValue) {
                    output.add(obj);
                }
            }
            catch (IllegalAccessException e) {
                    throw new NoSuchFieldException();
                }
        }

        if (output.size() == 0){
            throw new DatabaseObjectNotFoundException(entityName, parameterValue, parameterName);
        }

        return output;

    }

    /**
     * Gets the given object, which's parameterName parameter's value matched parameterValue.
     * E.G. select * from db where parameterName = parameterValue
     * @param parameterName The name of the object's parameter
     * @param parameterValue The value of said parameter we want to search for
     * @param dummyObject Any object of type E, we need it to check if a parameter exists
     * @return The object(s) that satisfy the condition
     * @throws DatabaseNotAccessibleException
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseMultipleMatchException
     */
    @Override
    public ArrayList<E> getWhere(String parameterName, String parameterValue, E dummyObject) throws DatabaseNotAccessibleException, DatabaseObjectNotFoundException, NoSuchFieldException {
        if (!arrayInitialised) {
            throw new DatabaseNotAccessibleException(entityName, "The database array hasn't been initialised yet!");
        }

        /* This will error if the field doesn't exist */
        Field field = dummyObject.getClass().getField(parameterName);

        E obj;
        ArrayList<E> output = new ArrayList<E>();

        for (int i = 0; i < data.size(); i++ ){
            obj = data.get(i);
            try {
                if (((String) field.get(data.get(i))).equals(parameterValue)) {
                    output.add(obj);
                }
            }
            catch (IllegalAccessException e) {
                throw new NoSuchFieldException();
            }
        }

        if (output.size() == 0){
            throw new DatabaseObjectNotFoundException(entityName, parameterValue, parameterName);
        }

        return output;
    }

    ;
}
