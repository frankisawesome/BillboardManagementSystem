package BillboardAssignment.BillboardServer.Database;

import BillboardAssignment.BillboardServer.BusinessLogic.User.User;
import BillboardAssignment.BillboardServer.BusinessLogic.User.UserPrivilege;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserSQLiteDatabase extends SQLiteDatabase<User> {
    /**
     * Get the string required to create the database, e.g. CREATE TABLE IF NOT EXISTS users ....
     *
     * @return said string
     */
    @Override
    public String getDBCreationString() {
        return "CREATE TABLE IF NOT EXISTS Users (" +
                "userID INTEGER PRIMARY KEY," +
                "username TEXT NOT NULL," +
                "twiceHashedPassword TEXT NOT NULL" +
                "salt TEXT NOT NULL" +
                "editUsers TEXT NOT NULL" +
                "editAllBillboards TEXT NOT NULL" +
                "scheduleBillboards TEXT NOT NULL" +
                "createBillboards TEXT NOT NULL);";
    }

    /**
     * Map the object to a string that can be used to add a database. e.g. User -> 'userID, userName ....'
     *
     * @return said string
     */
    @Override
    public String getAttributeNames() {
        return "userID, username, twiceHashedPassword, salt, editUsers, editAllBillboards, scheduleBillboards, createBillboards";
    }

    /**
     * Map the object to a string that is the primary key of the database e.g. User -> 'userID'
     *
     * @return said string
     */
    @Override
    public String getPrimaryKey() {
        return "userID";
    }

    /**
     * Add the values to a preparedStatement e.g statement.setInt(1, 20), statement.setString(2, "The UserName")
     *
     * @param statement
     * @param object
     */
    @Override
    public void addValues(PreparedStatement statement, User object) throws SQLException {
        /* String.format("%d, %s, %s, %s, %s, %s, %s, %s, %s", object.getID(), object.getUsername(),
                object.twiceHashedPassword, object.salt, object.checkUserHasPriv(UserPrivilege.EditUsers),
                object.checkUserHasPriv(UserPrivilege.EditAllBillboards),object.checkUserHasPriv(UserPrivilege.ScheduleBillboards),
                object.checkUserHasPriv(UserPrivilege.CreateBillboards))  <- old code that may be useful later, TODO: REMOVE*/
        statement.setInt(1, object.getID());
        statement.setString(2, object.getUsername());
        statement.setString(3, object.twiceHashedPassword.toString());
        statement.setString(4, object.salt.toString());
        statement.setString(5, Boolean.toString(object.checkUserHasPriv(UserPrivilege.EditUsers)));
        statement.setString(6, Boolean.toString(object.checkUserHasPriv(UserPrivilege.EditAllBillboards)));
        statement.setString(7, Boolean.toString(object.checkUserHasPriv(UserPrivilege.ScheduleBillboards)));
        statement.setString(8, Boolean.toString(object.checkUserHasPriv(UserPrivilege.CreateBillboards)));
    }

    /**
     * Map the result of a sql query to the generic object
     *
     * @param results a single row of the results
     * @return the object constructed from the results
     */
    @Override
    public User mapResultSetToObject(ResultSet results) throws SQLException {
        int ID = results.getInt(1);
        String username = results.getString(2);
        byte[] twiceHashedPwd = results.getString(3).getBytes();
        byte[] salt = results.getString(4).getBytes();
        UserPrivilege[] priv = new UserPrivilege[4];

        if (results.getString(5) == "true"){
            priv[0] = UserPrivilege.EditUsers;
        }
        if (results.getString(6) == "true"){
            priv[1] = UserPrivilege.EditAllBillboards;
        }
        if (results.getString(7) == "true"){
            priv[2] = UserPrivilege.ScheduleBillboards;
        }
        if (results.getString(8) == "true"){
            priv[4] = UserPrivilege.CreateBillboards;
        }

        return new User(ID, twiceHashedPwd, salt, priv, username);
    }
}
