package BillboardAssignment.BillboardServer.Database;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Testing class for the in-memory "Database" array.
 */
class TestDatabaseArray {

    Queryable<DummyDatabaseObject> database;

    @BeforeEach
    void init() throws DatabaseNotAccessibleException {

        database = new DatabaseArray<DummyDatabaseObject>();
        database.initialiseDatabase("TestDatabase");
    }

    @org.junit.jupiter.api.Test
    void addObject() throws DatabaseNotAccessibleException, DatabaseLogicException, DatabaseObjectNotFoundException {
        database.addObject(new DummyDatabaseObject(1));
        database.addObject(new DummyDatabaseObject(2));
        assertEquals(1, database.getObject(1).getID()); /* Make sure it exists*/

        assertThrows(DatabaseLogicException.class, () -> {
            database.addObject(new DummyDatabaseObject(1));
        }); /* Make sure this errors out */
    }

    @org.junit.jupiter.api.Test
    void removeObject() throws DatabaseNotAccessibleException, DatabaseLogicException {
        database.addObject(new DummyDatabaseObject(1));
        database.removeObject(1);

        assertThrows(DatabaseObjectNotFoundException.class, () -> {
            database.getObject(1);
        }); /* Make sure this errors out */
    }

    @org.junit.jupiter.api.Test
    void getObject() throws DatabaseNotAccessibleException, DatabaseLogicException {
        database.addObject(new DummyDatabaseObject(1));
        database.addObject(new DummyDatabaseObject(2));
        assertThrows(DatabaseObjectNotFoundException.class, () -> {
            database.getObject(3);
        }); /* Make sure this errors out */
    }

    @org.junit.jupiter.api.Test
    void objectInDatabase() throws DatabaseNotAccessibleException, DatabaseLogicException {
        database.addObject(new DummyDatabaseObject(1));
        database.addObject(new DummyDatabaseObject(2));
        assertEquals(true, database.objectInDatabase(new DummyDatabaseObject(1)));
        assertEquals(false, database.objectInDatabase(new DummyDatabaseObject(3)));
    }

    @org.junit.jupiter.api.Test
    void removeAllData() throws DatabaseNotAccessibleException, DatabaseLogicException {
        database.addObject(new DummyDatabaseObject(1));

        database.removeAllData();


        assertThrows(DatabaseObjectNotFoundException.class, () -> {
            database.getObject(1);
        }); /* Make sure this errors out */

    }

}