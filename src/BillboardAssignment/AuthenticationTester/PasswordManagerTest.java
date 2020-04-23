package BillboardAssignment.AuthenticationTester;

import BillboardAssignment.Authentication.IncorrectPasswordException;
import BillboardAssignment.Authentication.PasswordManager;
import BillboardAssignment.Database.*;
import BillboardAssignment.User.User;
import BillboardAssignment.User.UserDataInput;
import BillboardAssignment.User.UserManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordManagerTest {

    private PasswordManager passwords;

    private UserManager userManager;

    @BeforeEach
    void setUp() throws DatabaseNotAccessibleException {
        Queryable<User> database = new DatabaseArray<User>();
        database.initialiseDatabase();

        passwords = new PasswordManager(database);
        userManager = new UserManager(passwords, database);
    }


    @Test
    void checkPasswordMatch() throws DatabaseNotAccessibleException, DatabaseLogicException, DatabaseObjectNotFoundException, IncorrectPasswordException {
        byte[] realPassword = {1,2,3,4};

        byte[] fakePassword = {1,2,3,4,5};

        UserDataInput input1 = new UserDataInput(1, realPassword);

        userManager.createUser(input1);

        byte[] realP = input1.getOnceHashedPassword();

        assertEquals(true, passwords.checkPasswordMatch(input1));

        input1.setOnceHashedPassword(fakePassword);

        assertThrows(IncorrectPasswordException.class, () -> {passwords.checkPasswordMatch(input1);});
    }


    @Test
    void changePassword() throws DatabaseNotAccessibleException, IncorrectPasswordException, DatabaseObjectNotFoundException, DatabaseLogicException {
        byte[] realPassword = {1,2,3,4};

        byte[] fakePassword = {1,2,3,4,5};

        UserDataInput input1 = new UserDataInput(1, realPassword);

        userManager.createUser(input1);

        passwords.changePassword(input1, fakePassword);
        input1.setOnceHashedPassword(fakePassword);

        assertEquals(true, passwords.checkPasswordMatch(input1));
    }
}