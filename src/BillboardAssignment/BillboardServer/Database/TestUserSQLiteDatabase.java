package BillboardAssignment.BillboardServer.Database;

import BillboardAssignment.BillboardServer.BusinessLogic.AuthAndUserDatabaseTesting.FatherTester;
import BillboardAssignment.BillboardServer.BusinessLogic.User.User;
import BillboardAssignment.BillboardServer.BusinessLogic.User.UserPrivilege;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing class for the in-memory "Database" array.
 */
class TestUserSQLiteDatabase extends FatherTester {

    UserSQLiteDatabase database;

    @BeforeEach
    void init() throws DatabaseNotAccessibleException {

        database = new UserSQLiteDatabase();
        database.initialiseDatabase("Users");

    }

    @AfterEach
    void cleanup() throws DatabaseNotAccessibleException {
        database.removeAllData();
    }

    @org.junit.jupiter.api.Test
    void addObject() throws DatabaseNotAccessibleException, DatabaseLogicException, DatabaseObjectNotFoundException {
        database.addObject(new User(1, "pwd", "1234", new UserPrivilege[]{UserPrivilege.EditUsers}, "test"));
        database.addObject(new User(2, "pwd", "1234", new UserPrivilege[]{UserPrivilege.EditUsers}, "test2"));
        assertEquals(1, database.getObject(1).getID()); /* Make sure it exists*/

        assertThrows(DatabaseLogicException.class, () -> {
            database.addObject(new User(1, "pwd", "1234", new UserPrivilege[]{UserPrivilege.EditUsers}, "test123"));
        }); /* Make sure this errors out */

        assertThrows(DatabaseLogicException.class, () -> {
            database.addObject(new User(3, "pwd", "1234", new UserPrivilege[]{UserPrivilege.EditUsers}, "test"));
        }); /* Make sure this errors out */
    }

    @org.junit.jupiter.api.Test
    void removeObject() throws DatabaseNotAccessibleException, DatabaseLogicException {
        database.addObject(new User(1, "pwd", "1234", new UserPrivilege[]{UserPrivilege.EditUsers}, "test"));
        database.removeObject(1);

        assertThrows(DatabaseObjectNotFoundException.class, () -> {
            database.getObject(1);
        }); /* Make sure this errors out */
    }

    @org.junit.jupiter.api.Test
    void getObject() throws DatabaseNotAccessibleException, DatabaseLogicException, DatabaseObjectNotFoundException {
        database.addObject(new User(1, "pwd", "1234", new UserPrivilege[]{UserPrivilege.EditUsers}, "test"));
        database.addObject(new User(2, "pwd", "1234", new UserPrivilege[]{UserPrivilege.EditUsers}, "test2"));
        assertThrows(DatabaseObjectNotFoundException.class, () -> {
            database.getObject(3);
        }); /* Make sure this errors out */

        User userToBeStored = new User(1, "pwd", "1234", new UserPrivilege[]{UserPrivilege.EditUsers}, "test");
        User storedUser = database.getObject(1);
        String a = userToBeStored.getUsername();
        String b = storedUser.getUsername();
        Boolean c = a == b;
        Boolean d = a.toString() == b.toString();
        assertTrue(userValuesEqual(userToBeStored, storedUser));
    }

    @org.junit.jupiter.api.Test
    void objectInDatabase() throws DatabaseNotAccessibleException, DatabaseLogicException {
        database.addObject(new User(1, "pwd", "1234", new UserPrivilege[]{UserPrivilege.EditUsers}, "test"));
        database.addObject(new User(2, "pwd", "1234", new UserPrivilege[]{UserPrivilege.EditUsers}, "test2"));
        assertEquals(true, database.objectInDatabase(new User(1, "pwd", "1234", new UserPrivilege[]{UserPrivilege.EditUsers}, "test")));
        assertEquals(false, database.objectInDatabase(new User(3, "pwd", "1234", new UserPrivilege[]{UserPrivilege.EditUsers}, "test")));
    }

    @org.junit.jupiter.api.Test
    void getWhere() throws DatabaseObjectNotFoundException, NoSuchFieldException, DatabaseNotAccessibleException, DatabaseLogicException {

        database.addObject(new User(1, "nice", "salt", new UserPrivilege[0], "that guy"));
        User firstobs = database.getWhere("salt", "salt", new User(1, "nice", "salt", new UserPrivilege[0], "that guy")).get(0);

        assertEquals(firstobs.getID(), 1);

        assertThrows((DatabaseObjectNotFoundException.class), () -> {
            database.getWhere("salt", 2,new User(1, "nice", "123", new UserPrivilege[0], "that guy"));
        });

        assertThrows((NoSuchFieldException.class), () -> {
            database.getWhere("notinOBJ", 2, new User(1, "nice", "salt", new UserPrivilege[0], "that guy"));
        });

        database.addObject(new User(3, "nice", "salt", new UserPrivilege[0], "that guy2"));

        assertEquals(database.getWhere("twiceHashedPassword", "nice", new User(1, "nice", "salt", new UserPrivilege[0], "that guy")).size(), 2);

    }




    boolean userValuesEqual(User a, User b){
        return a.salt.equals(b.salt) && a.getID() == b.getID() && a.twiceHashedPassword.equals(b.twiceHashedPassword) && a.getUsername().equals(b.getUsername()) && assertSetEquals(a.getPrivileges(), b.getPrivileges());
    }

    /**
     * Assert that two arrays are equal (using the concept of set equality). Not a test, just a helper function
     * @param set1
     * @param set2
     * @throws Exception
     */
    boolean assertSetEquals(UserPrivilege[] set1, UserPrivilege[] set2) {
        boolean match;
        boolean allMatch = true;
        for (int i = 0; i < set1.length; i ++){
            match = false;
            for (int j = 0; j < set2.length; j++) {
                if (set1[i] == set2[j]) {
                    match = true;
                }
            }

            if (!match){
                allMatch =false;
            }

        }
        return allMatch;
    }

}