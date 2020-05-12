package BillboardAssignment.BillboardServer.Database;

/**
 * Simple Exception for when a database isn't accessible for whatever reason
 */
public class DatabaseNotAccessibleException extends Exception {

    /**
     * Constructor of the Exception, supply the Database name that you want the error message to shout about.
     *
     * @param DatabaseName The name of the database that cannot be accessed
     */
    public DatabaseNotAccessibleException(String DatabaseName) {
        super("The database with name\"" + DatabaseName + "\" could not be accessed.");
    }

    /**
     * Constructor of the Exception, supply the Database name that you want the error message to shout about.
     *
     * @param DatabaseName The name of the database that cannot be accessed
     * @param Reason       The reason why the database can't be accessed.
     */
    public DatabaseNotAccessibleException(String DatabaseName, String Reason) {
        super("The database with name\"" + DatabaseName + "\" could not be accessed.\nReason:" + Reason);
    }

}
