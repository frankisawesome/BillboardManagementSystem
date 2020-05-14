package BillboardAssignment.BillboardServer.Database;

import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.ArrayList;

public abstract class SQLiteDatabase<E extends Identifiable> implements Queryable<E> {
    private String entityName;
    private Connection connection;
    private boolean databaseInitialised = false;

    /**
     * Add the given object to the database
     *
     * @param object The Identifiable object that we want to add to the database
     * @return A boolean indication whether or not the object was added successfully
     * @throws DatabaseNotAccessibleException Throws an exception if we can't access the database.
     *                                        Can be inherited to provide more reason about the error (I.E. Database connection failed)
     * @throws DatabaseLogicException         Throws an error if you make an invalid operation (Adding a object that allready exists)
     */
    @Override
    public boolean addObject(E object) throws DatabaseNotAccessibleException, DatabaseLogicException {
        if (!databaseInitialised) {
            throw new DatabaseNotAccessibleException(getEntityName());
        }

        String stringifiedNames = getAttributeNames();

        try {
            PreparedStatement add = connection.prepareStatement(String.format("INSERT INTO %s (%s) VALUES (%s);", getEntityName(), stringifiedNames, getQuestionMarks()));
            addValues(add, object);
            add.executeUpdate();
            add.close();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            throw new DatabaseLogicException(getEntityName());
        }

        return true;
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
        if (!databaseInitialised) {
            throw new DatabaseNotAccessibleException(getEntityName());
        }

        try {
            PreparedStatement remove = connection.prepareStatement(String.format("DELETE FROM %s WHERE %s = %d", getEntityName(), getPrimaryKey(), ID)); /* No need for sql injection projection when just using ID */
            remove.executeUpdate();
            remove.close();
        } catch (java.sql.SQLException e) {
            /* This should never happen, deleting a record should never error if the database is set up correctly. */
            e.printStackTrace();
            System.err.println("ERROR, A DELETE OPERATION FAILED! SOMETHING IS WRONG!");
            throw new DatabaseNotAccessibleException(getEntityName()); /* This isn't database accessibility problem most likely, but we still want to throw an error */
        }

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
        E outputObject;

        if (!databaseInitialised) {
            throw new DatabaseNotAccessibleException(getEntityName());
        }

        try {
            PreparedStatement get = connection.prepareStatement(String.format("SELECT * FROM %s WHERE %s = %d", getEntityName(), getPrimaryKey(), ID)); /* No need for sql injection projection when just using ID */
            ResultSet singleRow = get.executeQuery();
            singleRow.next();
            outputObject = mapResultSetToObject(singleRow);
            get.close();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            throw new DatabaseObjectNotFoundException(getEntityName(), ID);
        }

        return outputObject;
    }

    /**
     * Check if an object with the given ID is in the database
     *
     * @param object The object that we want to check exists
     * @return A boolean that is True iff there exists an object in the database with the given ID
     * @throws DatabaseNotAccessibleException Throws an exception if we can't access the database.
     *                                        Can be inherited to provide more reason about the error (I.E. Database connection failed)
     */
    @Override
    public boolean objectInDatabase(E object) throws DatabaseNotAccessibleException {
        boolean inDatabase;

        if (!databaseInitialised) {
            throw new DatabaseNotAccessibleException(getEntityName());
        }

        try {
            PreparedStatement get = connection.prepareStatement(String.format("SELECT * FROM %s WHERE %s = %d", getEntityName(), getPrimaryKey(), object.getID())); /* No need for sql injection projection when just using ID */
            ResultSet rs = get.executeQuery();
            inDatabase = rs.next();
            get.close();
        } catch (java.sql.SQLException e) {
            /* This should never happen, deleting a record should never error if the database is set up correctly. */
            e.printStackTrace();
            System.err.println("ERROR, A DELETE OPERATION FAILED! SOMETHING IS WRONG!");
            throw new DatabaseNotAccessibleException(getEntityName()); /* This isn't database accessibility problem most likely, but we still want to throw an error */
        }

        return inDatabase;
    }

    /**
     * Run any necessary setup to get the database ready for storage and querying. The database will be created depended on
     * the getEntityName value of the object being stored. I.E. If the entity name is a sessionToken, then the sessionToken database will be created/accessed.
     *
     * @throws DatabaseNotAccessibleException Errors out if the database didn't get created successfully
     */
    @Override
    public void initialiseDatabase(String entityName) throws DatabaseNotAccessibleException {
        connection = DBConnection.getInstance();

        try {
            Statement createDatabase = connection.createStatement();
            createDatabase.execute(getDBCreationString());
            createDatabase.close();
            databaseInitialised = true;
            this.entityName = entityName;
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            throw new DatabaseNotAccessibleException(entityName);
        }

    }

    /**
     * Return an array of every single object in the database.
     *
     * @return A list of the database generic type, containing every entry in the database
     * @throws DatabaseNotAccessibleException Throws an exception if we can't access the database.
     */
    @Override
    public ArrayList<E> getAllObjects() throws DatabaseNotAccessibleException {
        ArrayList<E> outputArray = new ArrayList<E>();

        if (!databaseInitialised) {
            throw new DatabaseNotAccessibleException(getEntityName());
        }

        try {
            PreparedStatement get = connection.prepareStatement(String.format("SELECT * FROM %s", getEntityName())); /* No need for sql injection projection when just using ID */
            ResultSet rows = get.executeQuery();
            while (rows.next()) {
                outputArray.add(mapResultSetToObject(rows));
            }
            get.close();
        } catch (java.sql.SQLException e) {
            /* This should never happen, deleting a record should never error if the database is set up correctly. */
            e.printStackTrace();
            System.err.println("ERROR, A DELETE OPERATION FAILED! SOMETHING IS WRONG!");
            throw new DatabaseNotAccessibleException(getEntityName()); /* This isn't database accessibility problem most likely, but we still want to throw an error */
        }

        return outputArray;
    }

    /**
     * Get the maximum ID of any object in the database
     *
     * @return An integer of the maximum ID. Returns 0 if no records in database
     * @throws DatabaseNotAccessibleException
     */
    @Override
    public int getMaxID() throws DatabaseNotAccessibleException {
        int maxID = 0;
        if (!databaseInitialised) {
            throw new DatabaseNotAccessibleException(getEntityName());
        }

        try {
            PreparedStatement get = connection.prepareStatement(String.format("SELECT % FROM %s", getEntityName())); /* No need for sql injection projection when just using ID */
            ResultSet rows = get.executeQuery(); /* Yes I'm aware that I could just use a select max here, but this is simpler and produces more consistent errors imo*/
            while (rows.next()) {
                maxID = Math.max(mapResultSetToObject(rows).getID(), maxID);
            }
            get.close();
        } catch (java.sql.SQLException e) {
            /* This should never happen, deleting a record should never error if the database is set up correctly. */
            e.printStackTrace();
            System.err.println("ERROR, A DELETE OPERATION FAILED! SOMETHING IS WRONG!");
            throw new DatabaseNotAccessibleException(getEntityName()); /* This isn't database accessibility problem most likely, but we still want to throw an error */
        }

        return maxID;
    }

    /**
     * Remove all observations in dataset
     *
     * @throws DatabaseNotAccessibleException
     */
    @Override
    public void removeAllData() throws DatabaseNotAccessibleException {
        if (!databaseInitialised) {
            throw new DatabaseNotAccessibleException(getEntityName());
        }

        try {
            PreparedStatement del = connection.prepareStatement(String.format("DELETE FROM %s", getEntityName())); /* No need for sql injection projection when just using ID */
            del.executeUpdate(); /* Yes I'm aware that I could just use a select max here, but this is simpler and produces more consistent errors imo*/
            del.close();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            throw new DatabaseNotAccessibleException(getEntityName());
        }

    }

    /**
     * Get name of database, for debugging
     */
    @Override
    public String getEntityName() {
        return this.entityName;
    }


    /**
     * Get the string required to create the database, e.g. CREATE TABLE IF NOT EXISTS users ....
     *
     * @return said string
     */
    public abstract String getDBCreationString();

    /**
     * Map the object to a string that can be used to add a database. e.g. User -> 'userID, userName ....'
     *
     * @return said string
     */
    public abstract String getAttributeNames();

    /**
     * Map the object to a string that is the primary key of the database e.g. User -> 'userID'
     *
     * @return said string
     */
    public abstract String getPrimaryKey();

    /**
     * Add the values to a preparedStatement e.g statement.setInt(1, 20), statement.setString(2, "The UserName")
     */
    public abstract void addValues(PreparedStatement statement, E object) throws SQLException;

    /**
     * Map the result of a sql query to the generic object
     *
     * @param results a single row of the results
     * @return the object constructed from the results
     */
    public abstract E mapResultSetToObject(ResultSet results) throws SQLException;

    /**
     * Get the question mark string needed to insert parameters. E.G. if we want to add UserID and username, return "?, ?"
     * @return
     */
    public abstract String getQuestionMarks();
}
