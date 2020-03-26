package BillboardAssignment.AuthenticationTester;

import BillboardAssignment.Authentication.IncorrectPasswordException;
import BillboardAssignment.Authentication.PasswordManager;
import BillboardAssignment.Authentication.UserAuthDataInput;
import BillboardAssignment.Authentication.UserAuthDataOutput;
import BillboardAssignment.Database.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordManagerTest {

    private PasswordManager passwords;
    @BeforeEach
    void setUp() throws DatabaseNotAccessibleException {
        Queryable<UserAuthDataOutput> database = new DatabaseArray<UserAuthDataOutput>();
        database.initialiseDatabase();

        passwords = new PasswordManager(database);
    }

    @Test
    void checkPasswordMatch() throws DatabaseNotAccessibleException, DatabaseLogicException, DatabaseObjectNotFoundException, IncorrectPasswordException {
        byte[] realPassword = {1,2,3,4};

        byte[] fakePassword = {1,2,3,4,5};

        UserAuthDataInput input1 = new UserAuthDataInput(1, realPassword);

        passwords.addPasswordData(input1);

        assertEquals(true, passwords.checkPasswordMatch(input1));

        input1.setOnceHashedPassword(fakePassword);

        assertThrows(IncorrectPasswordException.class, () -> {passwords.checkPasswordMatch(input1);});
    }

    @Test
    void addPasswordData() throws DatabaseNotAccessibleException, DatabaseLogicException, DatabaseObjectNotFoundException, IncorrectPasswordException {
        byte[] realPassword = {1,2,3,4};

        byte[] fakePassword = {1,2,3,4,5};

        UserAuthDataInput input1 = new UserAuthDataInput(1, realPassword);

        UserAuthDataInput input4 = new UserAuthDataInput(1, fakePassword);

        UserAuthDataInput input2 = new UserAuthDataInput(3, realPassword);


        UserAuthDataInput input3 = new UserAuthDataInput(3, realPassword);

        passwords.addPasswordData(input1);

        assertThrows(DatabaseLogicException.class, () -> {passwords.addPasswordData(input4);});

        passwords.addPasswordData(input3);

        assertEquals(true, passwords.checkPasswordMatch(input2));
    }

    @Test
    void changePassword() throws DatabaseNotAccessibleException, IncorrectPasswordException, DatabaseObjectNotFoundException, DatabaseLogicException {
        byte[] realPassword = {1,2,3,4};

        byte[] fakePassword = {1,2,3,4,5};

        UserAuthDataInput input1 = new UserAuthDataInput(1, realPassword);

        passwords.addPasswordData(input1);

        passwords.changePassword(input1, fakePassword);
        input1.setOnceHashedPassword(fakePassword);

        assertEquals(true, passwords.checkPasswordMatch(input1));
    }
}