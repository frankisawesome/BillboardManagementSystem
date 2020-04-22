package BillboardAssignment.UserTester;

import BillboardAssignment.Authentication.*;
import BillboardAssignment.Database.*;
import BillboardAssignment.User.UserManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class UserManagerTest {

    PasswordManager passwords;

    SessionKeyManager keyManager;

    UserAuthDataInput userCreds;


    UserManager userManager;

    @BeforeEach
    void setUp() throws DatabaseNotAccessibleException, DatabaseLogicException, DatabaseObjectNotFoundException {
        Queryable<UserAuthDataOutput> database = new DatabaseArray<UserAuthDataOutput>();
        database.initialiseDatabase();
        passwords = new PasswordManager(database);

        Queryable<SessionKeyDataOutput> database2 = new DatabaseArray<SessionKeyDataOutput>();
        database.initialiseDatabase();
        keyManager = new SessionKeyManager(database2);

        userCreds = new UserAuthDataInput(100, "123".getBytes());

        passwords.addPasswordData(userCreds);

        userManager = new UserManager(passwords, keyManager);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void login() throws IncorrectPasswordException, DatabaseNotAccessibleException, DatabaseObjectNotFoundException, DatabaseLogicException {

        String sessionKey = userManager.login(userCreds);

        assertTrue(sessionKey instanceof String);

        UserAuthDataInput badCreds = new UserAuthDataInput(1, "the_wrong_password".getBytes());

        assertThrows(IncorrectPasswordException.class, ()->{userManager.login(badCreds);});
    }

    @Test
    void createUser() throws OutOfDateSessionKeyException, IncorrectSessionKeyException, IncorrectPasswordException, DatabaseLogicException, DatabaseNotAccessibleException, DatabaseObjectNotFoundException {
        PrivilegedUser user = new PrivilegedUser(1, "This_is_the_hashed_password".getBytes(), new String[]{"Edit Users", "Edit All Billboards", "Schedule Billboards", "Create Billboards"});

        userManager.createUser(user);

        UserAuthDataInput userIn = new UserAuthDataInput(1, "This_is_the_hashed_password".getBytes());
        String sessionKey = userManager.login(userIn);

        assertTrue(sessionKey instanceof String);

        assertThrows(IncorrectPasswordException.class, ()->{userManager.login(new UserAuthDataInput(1, "This_is_the_wrong_hashed_password".getBytes()));});
        assertThrows(IncorrectPasswordException.class, ()->{userManager.login(new UserAuthDataInput(2, "This_is_the_hashed_password".getBytes()));});
    }

}