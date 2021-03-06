package BillboardAssignment.BillboardServer.Services.Authentication;

import BillboardAssignment.BillboardServer.Database.DatabaseLogicException;
import BillboardAssignment.BillboardServer.Database.DatabaseNotAccessibleException;
import BillboardAssignment.BillboardServer.Database.DatabaseObjectNotFoundException;
import BillboardAssignment.BillboardServer.Services.AuthAndUserDatabaseTesting.FatherTester;
import BillboardAssignment.BillboardServer.Services.User.InsufficentPrivilegeException;
import BillboardAssignment.BillboardServer.Services.User.User;
import BillboardAssignment.BillboardServer.Services.User.UserDataInput;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestPasswordManager extends FatherTester {


    @Test
    void checkPasswordMatch() throws DatabaseNotAccessibleException, DatabaseLogicException, DatabaseObjectNotFoundException, IncorrectPasswordException, OutOfDateSessionKeyException, InsufficentPrivilegeException, IncorrectSessionKeyException {
        String realPassword = "nice";

        String fakePassword = "meme";

        UserDataInput input1 = new UserDataInput(1, realPassword);

        userManager.createUser(input1, adminKey);

        String realP = input1.getOnceHashedPassword();

        passwordManager.checkPasswordMatch(input1); /* This shouldn't error */

        input1.setOnceHashedPassword(fakePassword);

        assertThrows(IncorrectPasswordException.class, () -> {
            passwordManager.checkPasswordMatch(input1);
        });
    }


    @Test
    void changePassword() throws DatabaseNotAccessibleException, IncorrectPasswordException, DatabaseObjectNotFoundException, DatabaseLogicException, OutOfDateSessionKeyException, InsufficentPrivilegeException, IncorrectSessionKeyException {
        String realPassword = "nice";

        String fakePassword = "meme";

        UserDataInput input1 = new UserDataInput(1, realPassword);

        userManager.createUser(input1, adminKey);

        passwordManager.changePassword(input1, fakePassword);
        input1.setOnceHashedPassword(fakePassword);

        assertTrue(passwordManager.checkPasswordMatch(input1) instanceof User);
    }
}