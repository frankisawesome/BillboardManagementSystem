package BillboardAssignment.BillboardServer.Database;

/**
 * Simple Exception for when a database logical error occurs
 */
public class DatabaseLogicException extends Exception {

    /**
     * Constructor of the Exception, supply the ID that you want the error message to shout about.
     *
     * @param DatabaseName The name of the database that cannot be accessed
     */
    public DatabaseLogicException(String DatabaseName) {
        super("A logical error has occured in database \"" + DatabaseName + "\".");
    }

    /**
     * Constructor of the Exception, supply the Database name that you want the error message to shout about.
     *
     * @param DatabaseName The name of the database that made a logical error.
     * @param Reason       The reason why the error occured.
     */
    public DatabaseLogicException(String DatabaseName, String Reason) {
        super("A logical error has occured in database \"" + DatabaseName + "\".Reason:" + Reason);
    }

}
