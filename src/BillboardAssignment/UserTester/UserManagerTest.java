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

    UserAuthDataOutput userCredsHashed;

    UserManager userManager;

    @BeforeEach
    void setUp() throws DatabaseNotAccessibleException, DatabaseLogicException, DatabaseObjectNotFoundException {
        Queryable<UserAuthDataOutput> database = new DatabaseArray<UserAuthDataOutput>();
        database.initialiseDatabase();
        passwords = new PasswordManager(database);

        Queryable<SessionKeyDataOutput> database2 = new DatabaseArray<SessionKeyDataOutput>();
        database.initialiseDatabase();
        keyManager = new SessionKeyManager(database2);

        userCreds = new UserAuthDataInput(1, "123".getBytes());

        passwords.addPasswordData(userCreds);

        userCredsHashed = passwords.passwordDatabase.getObject(1);

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

}