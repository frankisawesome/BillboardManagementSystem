package BillboardAssignment.BillboardServer.BusinessLogic.AuthAndUserDatabaseTesting;

import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.*;
import BillboardAssignment.BillboardServer.BusinessLogic.User.InsufficentPrivilegeException;
import BillboardAssignment.BillboardServer.BusinessLogic.User.User;
import BillboardAssignment.BillboardServer.BusinessLogic.User.UserDataInput;
import BillboardAssignment.BillboardServer.BusinessLogic.User.UserManager;
import BillboardAssignment.BillboardServer.Database.*;
import org.junit.jupiter.api.BeforeEach;

/**
 * Class defines universal setup and common data elements for each test class, to keep things consistent
 * This testing class specifically uses in-memory databases.
 */
public class FatherTester {

    private static String adminPasssword = UserManager.defaultHashedAdminPasssword;
    protected PasswordManager passwordManager;
    protected UserManager userManager;
    protected UserSessionKey adminKey;
    protected UserDataInput adminUser = new UserDataInput(UserManager.defaultAdminUserID, adminPasssword);
    protected SessionKeyManager sessionKeyManager;

    @BeforeEach
    void setUp() throws DatabaseNotAccessibleException, InsufficentPrivilegeException, IncorrectSessionKeyException, OutOfDateSessionKeyException, IncorrectPasswordException, DatabaseLogicException, DatabaseObjectNotFoundException {
        Queryable<User> database = new DatabaseArray<User>();

        database.initialiseDatabase("Users");

        Queryable<UserSessionKey> database2 = new DatabaseArray<UserSessionKey>();
        database2.initialiseDatabase("SessionKeys");

        passwordManager = new PasswordManager(database);
        sessionKeyManager = new SessionKeyManager(database2);
        userManager = new UserManager(passwordManager, sessionKeyManager, database);
        userManager.createFirstUser();

        String sessionKey = userManager.login(new UserDataInput(UserManager.defaultAdminUserID, adminPasssword)).sessionKey;
        adminKey = new UserSessionKey(UserManager.defaultAdminUserID, sessionKey);
    }
}
