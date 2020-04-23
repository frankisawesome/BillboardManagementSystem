package BillboardAssignment.UserTester;

import BillboardAssignment.Authentication.*;
import BillboardAssignment.Database.*;
import BillboardAssignment.User.User;
import BillboardAssignment.User.UserDataInput;
import BillboardAssignment.User.UserManager;
import BillboardAssignment.User.UserPrivilege;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class UserManagerTest {

    PasswordManager passwords;

    SessionKeyManager keyManager;

    UserDataInput userCreds;

    UserManager userManager;

    @BeforeEach
    void setUp() throws DatabaseNotAccessibleException, DatabaseLogicException, DatabaseObjectNotFoundException {
        Queryable<User> userDatabase = new DatabaseArray<User>();
        userDatabase.initialiseDatabase();
        passwords = new PasswordManager(userDatabase);

        Queryable<SessionKeyDataOutput> database2 = new DatabaseArray<SessionKeyDataOutput>();
        database2.initialiseDatabase();
        keyManager = new SessionKeyManager(database2);

        userCreds = new UserDataInput(100, "123".getBytes(), new UserPrivilege[]{UserPrivilege.CreateBillboards});

        userManager = new UserManager(passwords, keyManager, userDatabase);

        userManager.createUser(userCreds);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void login() throws IncorrectPasswordException, DatabaseNotAccessibleException, DatabaseObjectNotFoundException, DatabaseLogicException {

        String sessionKey = userManager.login(userCreds);

        assertTrue(sessionKey instanceof String);

        UserDataInput badCreds = new UserDataInput(100, "the_wrong_password".getBytes());

        assertThrows(IncorrectPasswordException.class, ()->{userManager.login(badCreds);});
    }

    @Test
    void createUser() throws OutOfDateSessionKeyException, IncorrectSessionKeyException, IncorrectPasswordException, DatabaseLogicException, DatabaseNotAccessibleException, DatabaseObjectNotFoundException {
        UserDataInput user = new UserDataInput(1, "This_is_the_hashed_password".getBytes(), new UserPrivilege[]{UserPrivilege.EditUsers, UserPrivilege.EditAllBillboards, UserPrivilege.ScheduleBillboards, UserPrivilege.CreateBillboards});

        userManager.createUser(user);

        UserDataInput userIn = new UserDataInput(1, "This_is_the_hashed_password".getBytes());
        String sessionKey = userManager.login(userIn);

        assertTrue(sessionKey instanceof String);

        assertThrows(IncorrectPasswordException.class, ()->{userManager.login(new UserDataInput(1, "This_is_the_wrong_hashed_password".getBytes()));});
        assertThrows(DatabaseObjectNotFoundException.class, ()->{userManager.login(new UserDataInput(2, "This_is_the_hashed_password".getBytes()));});
    }

}