package BillboardAssignment.BillboardServer.Database;

/**
 * Simple Exception for when an object doesn't exist in a database
 */
public class DatabaseObjectNotFoundException extends Exception {

    /**
     * Constructor of the Exception, supply the ID that you want the error message to shout about.
     *
     * @param ID The ID that doesn't exist in the database.
     */
    public DatabaseObjectNotFoundException(String databaseName, int ID) {
        super("Object in database " + databaseName + " with given ID \"" + ID + "\" not found in the database.");
    }

    /**
     * Constructor of the Exception, supply the ID that you want the error message to shout about.
     * This overload is for where statements.
     */
    public DatabaseObjectNotFoundException(String databaseName, int value, String parameterName) {
        super("Object in database " + databaseName + " with given parameter \"" + parameterName + "\" value \"" + value + "\" not found in the database.");
    }

    /**
     * Constructor of the Exception, supply the ID that you want the error message to shout about.
     * This overload is for where statements.
     */
    public DatabaseObjectNotFoundException(String databaseName, String value, String parameterName) {
        super("Object in database " + databaseName + " with given parameter \"" + parameterName + "\" value \"" + value + "\" not found in the database.");
    }

}
