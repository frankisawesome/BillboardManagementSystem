package BillboardAssignment.User;

import BillboardAssignment.AuthAndUserDatabaseTesting.FatherTester;
import BillboardAssignment.Authentication.*;
import BillboardAssignment.Database.*;
import BillboardAssignment.User.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestUserManager extends FatherTester {

    @AfterEach
    void tearDown() {
    }

    @Test
    void login() throws IncorrectPasswordException, DatabaseNotAccessibleException, DatabaseObjectNotFoundException, DatabaseLogicException {

        String sessionKey = userManager.login(adminUser).sessionKey;

        assertTrue(sessionKey instanceof String);

        UserDataInput badCreds = new UserDataInput(69420, "the_wrong_password".getBytes());

        assertThrows(IncorrectPasswordException.class, () -> {
            userManager.login(badCreds);
        });
    }

    @Test
    void createUser() throws OutOfDateSessionKeyException, IncorrectSessionKeyException, IncorrectPasswordException, DatabaseLogicException, DatabaseNotAccessibleException, DatabaseObjectNotFoundException, InsufficentPrivilegeException {
        UserDataInput user = new UserDataInput(1, "This_is_the_hashed_password".getBytes(), new UserPrivilege[]{UserPrivilege.EditUsers, UserPrivilege.EditAllBillboards, UserPrivilege.ScheduleBillboards, UserPrivilege.CreateBillboards}, "");

        userManager.createUser(user, adminKey);

        UserDataInput userIn = new UserDataInput(1, "This_is_the_hashed_password".getBytes());
        String sessionKey = userManager.login(userIn).sessionKey;

        assertTrue(sessionKey instanceof String);

        assertThrows(IncorrectPasswordException.class, () -> {
            userManager.login(new UserDataInput(1, "This_is_the_wrong_hashed_password".getBytes()));
        });
        assertThrows(DatabaseObjectNotFoundException.class, () -> {
            userManager.login(new UserDataInput(2, "This_is_the_hashed_password".getBytes()));
        });

        UserDataInput user2 = new UserDataInput(123, "This_is_the_hashed_password".getBytes(), new UserPrivilege[]{}, "");

        userManager.createUser(user2, adminKey);

        UserSessionKey sessionKey2 = userManager.login(user2);

        assertThrows(InsufficentPrivilegeException.class, () -> {
            userManager.createUser(new UserDataInput(1234, "This_is_the_hashed_password".getBytes()), sessionKey2);
        });
    }

    @Test
    void listUsers() throws InsufficentPrivilegeException, IncorrectSessionKeyException, OutOfDateSessionKeyException, DatabaseNotAccessibleException, IncorrectPasswordException, DatabaseLogicException, DatabaseObjectNotFoundException {
        User adminUser = userManager.getUser(69420);
        assertArrayEquals(new User[]{ adminUser}, userManager.listUsers(adminKey));

        UserDataInput user1 = new UserDataInput(1, "This_is_the_hashed_password".getBytes(), new UserPrivilege[]{}, "");
        UserDataInput user2 = new UserDataInput(2, "This_is_the_hashed_password".getBytes(), new UserPrivilege[]{UserPrivilege.EditUsers, UserPrivilege.EditAllBillboards, UserPrivilege.ScheduleBillboards, UserPrivilege.CreateBillboards}, "");
        UserDataInput user3 = new UserDataInput(3, "This_is_the_hashed_password".getBytes(), new UserPrivilege[]{UserPrivilege.EditUsers, UserPrivilege.EditAllBillboards, UserPrivilege.ScheduleBillboards, UserPrivilege.CreateBillboards}, "");

        UserDataInput[] userArray = new UserDataInput[]{user1, user2, user3};
        User[] userOut = new User[4];
        userOut[0] = adminUser;

        for (int i = 1; i < userArray.length +1; i++) {
            userManager.createUser(userArray[i-1],adminKey);
            userOut[i] = userManager.getUser(i );
        }

        User[] outputUsers = userManager.listUsers(adminKey);

        assertArrayEquals(userOut, outputUsers);

        UserSessionKey nonAdminKey = userManager.login(new UserDataInput(1, "This_is_the_hashed_password".getBytes()));
        assertThrows(InsufficentPrivilegeException.class, () -> {
            userManager.listUsers(nonAdminKey);
        });

    }

}