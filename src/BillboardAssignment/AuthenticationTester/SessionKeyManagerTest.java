package BillboardAssignment.AuthenticationTester;

import BillboardAssignment.Authentication.IncorrectSessionKeyException;
import BillboardAssignment.Authentication.OutOfDateSessionKeyException;
import BillboardAssignment.Authentication.SessionKeyDataOutput;
import BillboardAssignment.Authentication.SessionKeyManager;
import BillboardAssignment.Database.*;
import BillboardAssignment.User.BaseUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SessionKeyManagerTest {

    private SessionKeyManager keyManager;

    @BeforeEach
    void setUp() throws DatabaseNotAccessibleException {
        Queryable<SessionKeyDataOutput> database = new DatabaseArray<SessionKeyDataOutput>();
        keyManager = new SessionKeyManager(database);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addSessionKeyData() throws DatabaseNotAccessibleException, DatabaseLogicException {
        BaseUser newUser = new BaseUser(1);

        keyManager.addSessionKeyData(newUser);

        assertThrows(DatabaseLogicException.class, () -> {keyManager.addSessionKeyData(newUser);}); /* no dups */
    }

    @Test
    void checkSessionKeyStatus() throws DatabaseNotAccessibleException, DatabaseLogicException, OutOfDateSessionKeyException, DatabaseObjectNotFoundException, IncorrectSessionKeyException {
        BaseUser newUser = new BaseUser(1);

        SessionKeyDataOutput goodUser = keyManager.addSessionKeyData(newUser);

        assertTrue(keyManager.checkSessionKeyStatus(goodUser));

        SessionKeyDataOutput badUser = new SessionKeyDataOutput(goodUser.getID(), "This is the incorrect session key, jaslkdfjalskdjfasdlkfjadslfkasjdflaksjfaslkfjaslkfjasdf!", goodUser.expiryDateTime);

        assertThrows(IncorrectSessionKeyException.class, () -> {keyManager.checkSessionKeyStatus(badUser);});

        assertThrows(OutOfDateSessionKeyException.class, () -> {keyManager.checkSessionKeyStatus(goodUser, LocalDateTime.now().plusHours(24));});

        assertTrue(keyManager.checkSessionKeyStatus(goodUser, LocalDateTime.now().plusHours(23)));

    }

    @Test
    void removeSessionKey() throws OutOfDateSessionKeyException, DatabaseNotAccessibleException, IncorrectSessionKeyException, DatabaseObjectNotFoundException, DatabaseLogicException {
        BaseUser newUser = new BaseUser(1);
        BaseUser otherUser = new BaseUser(2);

        SessionKeyDataOutput goodUser = keyManager.addSessionKeyData(newUser);

        assertTrue(keyManager.checkSessionKeyStatus(goodUser));

        assertFalse(keyManager.removeSessionKey(otherUser));

        keyManager.removeSessionKey(newUser);

        assertThrows(DatabaseObjectNotFoundException.class, ()->{keyManager.checkSessionKeyStatus(goodUser);});

        keyManager.addSessionKeyData(newUser);
    }
}