package BillboardAssignment.BillboardServer.BusinessLogic.AuthAndUserDatabaseTesting;

import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.*;
import BillboardAssignment.BillboardServer.BusinessLogic.User.InsufficentPrivilegeException;
import BillboardAssignment.BillboardServer.BusinessLogic.User.User;
import BillboardAssignment.BillboardServer.BusinessLogic.User.UserDataInput;
import BillboardAssignment.BillboardServer.BusinessLogic.User.UserManager;
import BillboardAssignment.BillboardServer.Database.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

/**
 * Class defines universal setup and common data elements for each test class, to keep things consistent
 * This test class uses SQLite Databases, which should be how the classes are used in production
 */
public class FatherTesterSQLite {

    private static String adminPasssword = UserManager.defaultHashedAdminPasssword;
    protected PasswordManager passwordManager;
    protected UserManager userManager;
    protected UserSessionKey adminKey;
    protected UserDataInput adminUser = new UserDataInput(UserManager.defaultAdminUserID, adminPasssword);
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

        String sessionKey = userManager.login(new UserDataInput(UserManager.defaultAdminUserID, adminPasssword)).sessionKey;
        adminKey = new UserSessionKey(UserManager.defaultAdminUserID, sessionKey);
    }

    @AfterEach
    void cleanup() throws DatabaseNotAccessibleException {
        userDatabase.removeAllData();
    }
}