package BillboardAssignment.BillboardServer.BusinessLogic.AuthAndUserDatabaseTesting;

import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.*;
import BillboardAssignment.BillboardServer.Database.*;
import BillboardAssignment.BillboardServer.BusinessLogic.User.*;
import org.junit.jupiter.api.BeforeEach;

/**
 * Class defines universal setup and common data elements for each test class, to keep things consistent
 */
public class FatherTester {

        protected PasswordManager passwordManager;

        protected UserManager userManager;

        protected UserSessionKey adminKey;

        protected UserDataInput adminUser = new UserDataInput(69420, "pwd");

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

            String sessionKey = userManager.login(new UserDataInput(69420, "pwd")).sessionKey;
            adminKey = new UserSessionKey(69420, sessionKey);
        }
}
