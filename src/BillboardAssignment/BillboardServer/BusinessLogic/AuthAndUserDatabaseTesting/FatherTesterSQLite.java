package BillboardAssignment.BillboardServer.BusinessLogic.AuthAndUserDatabaseTesting;

import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.*;
import BillboardAssignment.BillboardServer.Database.*;
import BillboardAssignment.BillboardServer.BusinessLogic.User.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

/**
 * Class defines universal setup and common data elements for each test class, to keep things consistent
 */
public class FatherTesterSQLite {

    protected PasswordManager passwordManager;

    protected UserManager userManager;

    protected UserSessionKey adminKey;

    protected UserDataInput adminUser = new UserDataInput(69420, "pwd");

    protected SessionKeyManager sessionKeyManager;

    Queryable<User> userDatabase;

    @BeforeEach
    void setUp() throws DatabaseNotAccessibleException, InsufficentPrivilegeException, IncorrectSessionKeyException, OutOfDateSessionKeyException, IncorrectPasswordException, DatabaseLogicException, DatabaseObjectNotFoundException {
        userDatabase = new UserSQLiteDatabase();
        userDatabase.initialiseDatabase("Users");

        Queryable<UserSessionKey> database2 = new DatabaseArray<UserSessionKey>();
        database2.initialiseDatabase("SessionKeys");

        passwordManager = new PasswordManager(userDatabase);
        sessionKeyManager = new SessionKeyManager(database2);
        userManager = new UserManager(passwordManager, sessionKeyManager, userDatabase);
        userManager.createFirstUser();

        String sessionKey = userManager.login(new UserDataInput(69420, "pwd")).sessionKey;
        adminKey = new UserSessionKey(69420, sessionKey);
    }

    @AfterEach
    void cleanup() throws DatabaseNotAccessibleException {
        userDatabase.removeAllData();
    }
}