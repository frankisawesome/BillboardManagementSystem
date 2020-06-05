package BillboardAssignment.BillboardServer.Database;

import BillboardAssignment.BillboardServer.Services.Billboard.Billboard;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BillboardSQLiteDatabase extends SQLiteDatabase<Billboard> {
    @Override
    public String getDBCreationString() {
        return "CREATE TABLE IF NOT EXISTS Billboards (" +
                "id INTEGER PRIMARY KEY," +
                "name TEXT UNIQUE NOT NULL," +
                "content TEXT NOT NULL," +
                "createdBy INTEGER NOT NULL);";
    }

    @Override
    public String getAttributeNames() {
        return "id, name, content, createdBy";
    }

    @Override
    public String getPrimaryKey() {
        return "id";
    }

    @Override
    public void addValues(PreparedStatement statement, Billboard object) throws SQLException {
        statement.setInt(1, object.getID());
        statement.setString(2, object.name);
        statement.setString(3, object.xml);
        statement.setInt(4, object.creatorId);
    }

    @Override
    public Billboard mapResultSetToObject(ResultSet results) throws SQLException {
        int id = results.getInt(1);
        String name = results.getString(2);
        String xml = results.getString(3);
        int creatorId = results.getInt(4);
        return new Billboard(id, name, xml, creatorId);
    }

    @Override
    public String getQuestionMarks() {
        return "?, ?, ?, ?";
    }
}
