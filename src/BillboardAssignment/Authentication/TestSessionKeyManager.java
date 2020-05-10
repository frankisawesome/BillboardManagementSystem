package BillboardAssignment.Authentication;

import BillboardAssignment.Database.*;
import BillboardAssignment.User.UserDataInput;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TestSessionKeyManager {

    private SessionKeyManager keyManager;

    @BeforeEach
    void setUp() throws DatabaseNotAccessibleException {
        Queryable<UserSessionKey> database = new DatabaseArray<UserSessionKey>();
        keyManager = new SessionKeyManager(database);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addSessionKeyData() throws DatabaseNotAccessibleException, DatabaseLogicException {
        UserDataInput newUser = new UserDataInput(1);

        keyManager.addSessionKeyData(newUser);

        assertThrows(DatabaseLogicException.class, () -> {
            keyManager.addSessionKeyData(newUser);
        }); /* no dups */
    }

    @Test
    void checkSessionKeyStatus() throws DatabaseNotAccessibleException, DatabaseLogicException, OutOfDateSessionKeyException, DatabaseObjectNotFoundException, IncorrectSessionKeyException {
        UserDataInput newUser = new UserDataInput(1);

        UserSessionKey goodUser = keyManager.addSessionKeyData(newUser);

        assertTrue(keyManager.checkSessionKeyStatus(goodUser));

        UserSessionKey badUser = new UserSessionKey(goodUser.getID(), "This is the incorrect session key, jaslkdfjalskdjfasdlkfjadslfkasjdflaksjfaslkfjaslkfjasdf!", goodUser.expiryDateTime);

        assertThrows(IncorrectSessionKeyException.class, () -> {
            keyManager.checkSessionKeyStatus(badUser);
        });

        assertThrows(OutOfDateSessionKeyException.class, () -> {
            keyManager.checkSessionKeyStatus(goodUser, LocalDateTime.now().plusHours(24));
        });

        assertTrue(keyManager.checkSessionKeyStatus(goodUser, LocalDateTime.now().plusHours(23)));

    }

    @Test
    void removeSessionKey() throws OutOfDateSessionKeyException, DatabaseNotAccessibleException, IncorrectSessionKeyException, DatabaseObjectNotFoundException, DatabaseLogicException {
        UserDataInput newUser = new UserDataInput(1);
        UserDataInput otherUser = new UserDataInput(2);

        UserSessionKey goodUser = keyManager.addSessionKeyData(newUser);

        assertTrue(keyManager.checkSessionKeyStatus(goodUser));

        assertFalse(keyManager.removeSessionKey(otherUser));

        keyManager.removeSessionKey(newUser);

        assertThrows(DatabaseObjectNotFoundException.class, () -> {
            keyManager.checkSessionKeyStatus(goodUser);
        });

        keyManager.addSessionKeyData(newUser);
    }
}