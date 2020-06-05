package BillboardAssignment.BillboardServer.Database;

import BillboardAssignment.BillboardServer.Services.Billboard.Schedule;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ScheduleSQLiteDatabase extends SQLiteDatabase<Schedule> {
    /**
     * Get the string required to create the database, e.g. CREATE TABLE IF NOT EXISTS users ....
     *
     * @return said string
     */
    @Override
    public String getDBCreationString() {
        return "CREATE TABLE IF NOT EXISTS Schedules (" +
                "id INTEGER PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "day TEXT NOT NULL," +
                "start TEXT NOT NULL," +
                "end TEST NOT NULL);";
    }

    /**
     * Map the object to a string that can be used to add a database. e.g. User -> 'userID, userName ....'
     *
     * @return said string
     */
    @Override
    public String getAttributeNames() {
        return "id, name, day, start, end";
    }

    /**
     * Map the object to a string that is the primary key of the database e.g. User -> 'userID'
     *
     * @return said string
     */
    @Override
    public String getPrimaryKey() {
        return "id";
    }

    /**
     * Add the values to a preparedStatement e.g statement.setInt(1, 20), statement.setString(2, "The UserName")
     *
     * @param statement The PreparedStatement to add the values to
     * @param object    The object containing the values we want to add
     * @throws SQLException If the method isn't implemented properly or invalid data is attempted to be stored
     */
    @Override
    public void addValues(PreparedStatement statement, Schedule object) throws SQLException {
            statement.setInt(1, object.getID());
            statement.setString(2, object.name);
            statement.setString(3, object.day);
            statement.setString(4, object.start.format(DateTimeFormatter.ISO_LOCAL_TIME));
            statement.setString(5, object.end.format(DateTimeFormatter.ISO_LOCAL_TIME));
    }

    /**
     * Map the result of a sql query to the generic object
     *
     * @param results a single row of the results
     * @return the object constructed from the results
     */
    @Override
    public Schedule mapResultSetToObject(ResultSet results) throws SQLException {
        int id = results.getInt(1);
        String name = results.getString(2);
        String day = results.getString(3);
        String start = results.getString(4);
        String end = results.getString(5);

        return new Schedule(id, name, day, LocalTime.parse(start), LocalTime.parse(end));
    }

    /**
     * Get the question mark string needed to insert parameters. E.G. if we want to add UserID and username, return "?, ?"
     *
     * @return
     */
    @Override
    public String getQuestionMarks() {
        return "?, ?, ?, ?, ?";
    }
}
