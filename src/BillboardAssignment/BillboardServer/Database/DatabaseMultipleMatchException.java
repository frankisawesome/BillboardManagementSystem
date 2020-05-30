package BillboardAssignment.BillboardServer.Database;

/**
 * Exception for when a query that is meant to return a single object returns more than 1.
 */
public class DatabaseMultipleMatchException extends Exception {

    public DatabaseMultipleMatchException() {
        super("ERROR: A where query on a unique parameter returned more than one row, there is likely a problem with your database or query");
    }
}
