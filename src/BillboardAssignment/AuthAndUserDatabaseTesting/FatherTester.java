package BillboardAssignment.AuthAndUserDatabaseTesting;

import BillboardAssignment.Authentication.*;
import BillboardAssignment.Database.*;
import BillboardAssignment.User.*;
import org.junit.jupiter.api.BeforeEach;

/**
 * Class defines universal setup and common data elements for each test class, to keep things consistent
 */
public class FatherTester {

        protected PasswordManager passwordManager;

        protected UserManager userManager;

        protected UserSessionKey adminKey;

        protected UserDataInput adminUser = new UserDataInput(69420, "pwd".getBytes());

        protected SessionKeyManager sessionKeyManager;

        @BeforeEach
        void setUp() throws DatabaseNotAccessibleException, InsufficentPrivilegeException, IncorrectSessionKeyException, OutOfDateSessionKeyException, IncorrectPasswordException, DatabaseLogicException, DatabaseObjectNotFoundException {
            Queryable<User> database = new DatabaseArray<User>();
            database.initialiseDatabase();

            Queryable<UserSessionKey> database2 = new DatabaseArray<UserSessionKey>();
            database2.initialiseDatabase();

            passwordManager = new PasswordManager(database);
            sessionKeyManager = new SessionKeyManager(database2);
            userManager = new UserManager(passwordManager, sessionKeyManager, database);
            userManager.createFirstUser();

            String sessionKey = userManager.login(new UserDataInput(69420, "pwd".getBytes())).sessionKey;
            adminKey = new UserSessionKey(69420, sessionKey);
        }
}
