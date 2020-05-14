package BillboardAssignment.BillboardServer.Database;

import BillboardAssignment.BillboardServer.BusinessLogic.User.User;
import BillboardAssignment.BillboardServer.BusinessLogic.User.UserPrivilege;

import java.io.UnsupportedEncodingException;
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
                "username TEXT UNIQUE NOT NULL," +
                "twiceHashedPassword TEXT NOT NULL," +
                "salt TEXT NOT NULL," +
                "editUsers TEXT NOT NULL," +
                "editAllBillboards TEXT NOT NULL," +
                "scheduleBillboards TEXT NOT NULL," +
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

        statement.setString(3, object.twiceHashedPassword);
        statement.setString(4, object.salt);

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
        String username = results.getString(2).toString();
        String twiceHashedPwd = results.getString(3);
        String salt = results.getString(4);
        UserPrivilege[] priv = new UserPrivilege[4];
        int privCounter = 0;

        if (results.getString(5).toString().equals("true")){
            priv[0] = UserPrivilege.EditUsers;
            privCounter++;
        }
        if (results.getString(6).toString().equals("true")){
            priv[1] = UserPrivilege.EditAllBillboards;
            privCounter++;
        }
        if (results.getString(7).toString().equals("true")){
            priv[2] = UserPrivilege.ScheduleBillboards;
            privCounter++;
        }
        if (results.getString(8).toString().equals("true")){
            priv[3] = UserPrivilege.CreateBillboards;
            privCounter++;
        }

        UserPrivilege[] privilegesNoNulls =  new UserPrivilege[privCounter];

        for (UserPrivilege p:
             priv) {
            if (p != null){
                privilegesNoNulls[privCounter-1] = p;
                privCounter--;
            }

        }
        return new User(ID, twiceHashedPwd, salt, privilegesNoNulls, username);
    }

    /**
     * Get the question mark string needed to insert parameters. E.G. if we want to add UserID and username, return "?, ?"
     *
     * @return
     */
    @Override
    public String getQuestionMarks() {
        return "?, ?, ?, ?, ?, ?, ?, ?";
    }


}
