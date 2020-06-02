package BillboardAssignment.BillboardServer.BusinessLogic.User;

import BillboardAssignment.BillboardServer.BusinessLogic.AuthAndUserDatabaseTesting.FatherTesterSQLite;
import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.IncorrectPasswordException;
import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.IncorrectSessionKeyException;
import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.OutOfDateSessionKeyException;
import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.UserSessionKey;
import BillboardAssignment.BillboardServer.Database.DatabaseLogicException;
import BillboardAssignment.BillboardServer.Database.DatabaseNotAccessibleException;
import BillboardAssignment.BillboardServer.Database.DatabaseObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


public class TestUserManagerWithSQLiteDatabase extends FatherTesterSQLite {

    @AfterEach
    void tearDown() {
    }

    @Test
    void login() throws IncorrectPasswordException, DatabaseNotAccessibleException, DatabaseObjectNotFoundException, DatabaseLogicException {

        String sessionKey = userManager.login(adminUser).sessionKey;

        assertTrue(sessionKey instanceof String);

        UserDataInput badCreds = new UserDataInput(UserManager.defaultAdminUserID, "the_wrong_password");

        assertThrows(IncorrectPasswordException.class, () -> {
            userManager.login(badCreds);
        });
    }

    @Test
    void createUser() throws OutOfDateSessionKeyException, IncorrectSessionKeyException, IncorrectPasswordException, DatabaseLogicException, DatabaseNotAccessibleException, DatabaseObjectNotFoundException, InsufficentPrivilegeException {
        UserDataInput user = new UserDataInput(1, "This_is_the_hashed_password", new UserPrivilege[]{UserPrivilege.EditUsers, UserPrivilege.EditAllBillboards, UserPrivilege.ScheduleBillboards, UserPrivilege.CreateBillboards}, "22");

        userManager.createUser(user, adminKey);

        UserDataInput userIn = new UserDataInput(1, "This_is_the_hashed_password");
        String sessionKey = userManager.login(userIn).sessionKey;

        assertTrue(sessionKey instanceof String);

        assertThrows(IncorrectPasswordException.class, () -> {
            userManager.login(new UserDataInput(1, "This_is_the_wrong_hashed_password"));
        });
        assertThrows(DatabaseObjectNotFoundException.class, () -> {
            userManager.login(new UserDataInput(2, "This_is_the_hashed_password"));
        });

        UserDataInput user2 = new UserDataInput(123, "This_is_the_hashed_password", new UserPrivilege[]{}, "33");

        userManager.createUser(user2, adminKey);

        UserSessionKey sessionKey2 = userManager.login(user2);

        assertThrows(InsufficentPrivilegeException.class, () -> {
            userManager.createUser(new UserDataInput(1234, "This_is_the_hashed_password"), sessionKey2);
        });
    }

    @Test
    void listUsers() throws InsufficentPrivilegeException, IncorrectSessionKeyException, OutOfDateSessionKeyException, DatabaseNotAccessibleException, IncorrectPasswordException, DatabaseLogicException, DatabaseObjectNotFoundException {
        User adminUser = userManager.getUser(UserManager.defaultAdminUserID);
        assertArrayEquals(new User[]{adminUser}, userManager.listUsers(adminKey));

        UserDataInput user1 = new UserDataInput(1, "This_is_the_hashed_password", new UserPrivilege[]{}, "1");
        UserDataInput user2 = new UserDataInput(2, "This_is_the_hashed_password", new UserPrivilege[]{UserPrivilege.EditUsers, UserPrivilege.EditAllBillboards, UserPrivilege.ScheduleBillboards, UserPrivilege.CreateBillboards}, "2");
        UserDataInput user3 = new UserDataInput(3, "This_is_the_hashed_password", new UserPrivilege[]{UserPrivilege.EditUsers, UserPrivilege.EditAllBillboards, UserPrivilege.ScheduleBillboards, UserPrivilege.CreateBillboards}, "3");

        UserDataInput[] userArray = new UserDataInput[]{user1, user2, user3};
        User[] userOut = new User[4];
        userOut[0] = adminUser;

        for (int i = 1; i < userArray.length + 1; i++) {
            userManager.createUser(userArray[i - 1], adminKey);
            userOut[i] = userManager.getUser(i);
        }

        User[] outputUsers = userManager.listUsers(adminKey);
        Arrays.sort(outputUsers);
        Arrays.sort(userOut);
        assertArrayEquals(userOut, outputUsers);

        UserSessionKey nonAdminKey = userManager.login(new UserDataInput(1, "This_is_the_hashed_password"));
        assertThrows(InsufficentPrivilegeException.class, () -> {
            userManager.listUsers(nonAdminKey);
        });

    }

    @Test
    void getPermissions() throws Exception, InsufficentPrivilegeException {
        UserPrivilege[] adminPerms = userManager.getPermissions(adminUser, adminKey);

        assertSetEquals(new UserPrivilege[]{UserPrivilege.EditUsers, UserPrivilege.EditAllBillboards, UserPrivilege.ScheduleBillboards, UserPrivilege.CreateBillboards}, adminPerms);

        UserDataInput user1 = new UserDataInput(1, "This_is_the_hashed_password", new UserPrivilege[]{}, "");
        userManager.createUser(user1, adminKey);
        UserSessionKey user1Key = userManager.login(user1);

        UserPrivilege[] noPerms = userManager.getPermissions(user1, user1Key);
        UserPrivilege[] noPerms2 = userManager.getPermissions(adminUser, adminKey);
        assertSetEquals(new UserPrivilege[]{}, noPerms);
        assertSetEquals(new UserPrivilege[]{}, noPerms2);

        assertThrows(InsufficentPrivilegeException.class, () -> {
            userManager.getPermissions(adminUser, user1Key);
        });

    }

    @Test
    void setPermissions() throws InsufficentPrivilegeException, IncorrectSessionKeyException, OutOfDateSessionKeyException, DatabaseNotAccessibleException, IncorrectPasswordException, DatabaseLogicException, DatabaseObjectNotFoundException, RemoveOwnEditUsersPrivilegeException {
        assertThrows(RemoveOwnEditUsersPrivilegeException.class, () -> {
            userManager.setPermissions(adminUser, new UserPrivilege[]{}, adminKey);
        });

        userManager.setPermissions(adminUser, new UserPrivilege[]{UserPrivilege.EditUsers}, adminKey); /* Nothrow */
        userManager.setPermissions(adminUser, new UserPrivilege[]{UserPrivilege.EditUsers, UserPrivilege.EditAllBillboards}, adminKey); /* Nothrow */

        UserDataInput user1 = new UserDataInput(1, "This_is_the_hashed_password", new UserPrivilege[]{}, "aslkdjf");
        userManager.createUser(user1, adminKey);
        UserSessionKey user1Key = userManager.login(user1);

        assertThrows(InsufficentPrivilegeException.class, () -> {
            userManager.setPermissions(user1, new UserPrivilege[]{}, user1Key);
        });

        userManager.setPermissions(user1, new UserPrivilege[]{UserPrivilege.EditUsers}, adminKey);
        assertArrayEquals(new UserPrivilege[]{UserPrivilege.EditUsers}, userManager.getPermissions(user1, adminKey));
        userManager.setPermissions(user1, new UserPrivilege[]{}, adminKey);
        assertArrayEquals(new UserPrivilege[]{}, userManager.getPermissions(user1, adminKey));
    }

    @Test
    void setPassword() throws InsufficentPrivilegeException, IncorrectSessionKeyException, OutOfDateSessionKeyException, DatabaseNotAccessibleException, DatabaseLogicException, DatabaseObjectNotFoundException, IncorrectPasswordException {
        userManager.setPassword(adminUser, "pwd123", adminKey); /* Nothrow */

        UserDataInput user1 = new UserDataInput(1, "This_is_the_hashed_password", new UserPrivilege[]{}, "1234");
        userManager.createUser(user1, adminKey);
        UserSessionKey user1Key = userManager.login(user1);

        assertThrows(InsufficentPrivilegeException.class, () -> {
            userManager.setPassword(adminUser, "asdf", user1Key);
        });

        userManager.setPassword(user1, "asdf", user1Key);
        userManager.login(new UserDataInput(user1.getID(), "asdf")); /* no throw */
        userManager.setPassword(user1, "asdf123", adminKey);
        userManager.login(new UserDataInput(user1.getID(), "asdf123")); /* no throw */
        assertThrows(IncorrectPasswordException.class, () -> {
            userManager.login(new UserDataInput(user1.getID(), "asdf")); /* no throw */
        });
    }

    @Test
    void deleteUser() throws InsufficentPrivilegeException, IncorrectSessionKeyException, OutOfDateSessionKeyException, DatabaseNotAccessibleException, DatabaseLogicException, DatabaseObjectNotFoundException, IncorrectPasswordException, RemoveOwnEditUsersPrivilegeException, RemoveOwnUserException {
        assertThrows(RemoveOwnUserException.class, () -> {
            userManager.deleteUser(adminUser, adminKey);
        });

        UserDataInput user1 = new UserDataInput(1, "This_is_the_hashed_password", new UserPrivilege[]{}, "");
        userManager.createUser(user1, adminKey);
        UserSessionKey user1Key = userManager.login(user1);

        assertThrows(InsufficentPrivilegeException.class, () -> {
            userManager.deleteUser(adminUser, user1Key);
        });
        assertThrows(InsufficentPrivilegeException.class, () -> {
            userManager.deleteUser(user1, user1Key);
        });
        userManager.deleteUser(user1, adminKey);
        assertThrows(DatabaseObjectNotFoundException.class, () -> {
            userManager.getUser(1);
        });
        assertThrows(DatabaseObjectNotFoundException.class, () -> {
            sessionKeyManager.checkSessionKeyStatus(user1Key);
        });
    }

    @Test
    void logout() throws IncorrectSessionKeyException, OutOfDateSessionKeyException, DatabaseNotAccessibleException, DatabaseObjectNotFoundException {
        assertTrue(userManager.logout(adminKey));
        assertThrows(DatabaseObjectNotFoundException.class, () -> {
            userManager.listUsers(adminKey);
        });
        assertFalse(userManager.logout(adminKey)); /* No throw? */
    }

    /**
     * Assert that two arrays are equal (using the concept of set equality). Not a test, just a helper function
     *
     * @param set1
     * @param set2
     * @throws Exception
     */
    void assertSetEquals(UserPrivilege[] set1, UserPrivilege[] set2) throws Exception {
        boolean match;
        for (int i = 0; i < set1.length; i++) {
            match = false;
            for (int j = 0; j < set2.length; j++) {
                if (set1[i] == set2[j]) {
                    match = true;
                }
            }
            if (!match) {
                throw new Exception("The two enums aren't the same!");
            }

        }
    }

}
