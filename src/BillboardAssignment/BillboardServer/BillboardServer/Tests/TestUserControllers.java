package BillboardAssignment.BillboardServer.BillboardServer.Tests;

import BillboardAssignment.BillboardServer.BillboardServer.BillboardServer;
import BillboardAssignment.BillboardServer.BillboardServer.RequestType;
import BillboardAssignment.BillboardServer.BillboardServer.ServerRequest;
import BillboardAssignment.BillboardServer.BillboardServer.ServerResponse;
import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.UserSessionKey;
import BillboardAssignment.BillboardServer.BusinessLogic.User.User;
import BillboardAssignment.BillboardServer.BusinessLogic.User.UserPrivilege;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Random;

/**
 * Test suite for all user related controllers, i.e RequestType.USER
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestUserControllers {
    /*
    Helper method to get key with admin privileges
     */
    UserSessionKey loginWithAdmin() throws Exception {
        HashMap<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("id", "69420");
        requestBody.put("password", "b\u0083¤$L\u0005\u0017SÉ(ÿÏ5\u008A!¬\u009E¡¥Î?ÊM½Òë9góa¯¯R¬ÊÀ\u0007\u001F\u0005\u0019ÛíG\u0086û\u0011Õ^úÔÃ.¸\u0086\u0088Çd_I\u00819Kwæ");
        ServerRequest<UserSessionKey> request = new ServerRequest<UserSessionKey>(RequestType.USER, "login", requestBody);
        ServerResponse<UserSessionKey> response = request.getResponse();
        return response.body();
    }

    /*
    Helper method to log out the admin user
     */
    Boolean logout(UserSessionKey key) throws Exception {
        HashMap<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("key", key.sessionKey);
        requestBody.put("keyId", Integer.toString(key.getID()));
        ServerRequest<Boolean> request = new ServerRequest<Boolean>(RequestType.USER, "logout", requestBody);
        ServerResponse<Boolean> response = request.getResponse();
        return response.body();
    }

    @Test
    @Order(1)
    void checkLogin() throws Exception {
        UserSessionKey key = loginWithAdmin();
        assertNotEquals("", key.sessionKey);
    }

    @Test
    @Order(2)
    void checkLogOut() throws Exception {
        UserSessionKey key = loginWithAdmin();
        boolean logoutSuccess = logout(key);
        assertTrue(logoutSuccess);
    }

    @Test
    @Order(3)
    void checkCreateUser() throws Exception {
        UserSessionKey key = loginWithAdmin();
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("keyId", Integer.toString(key.getID()));
        Random random = new Random();
        requestBody.put("newUserId", "555");
        requestBody.put("password", "newpass");
        requestBody.put("key", key.sessionKey);
        ServerRequest<UserSessionKey> request = new ServerRequest<UserSessionKey>(RequestType.USER, "create", requestBody);
        ServerResponse response = request.getResponse();
        assertEquals("ok", response.status());
    }

    @Test
    @Order(4)
    void checkGetUserPerms() throws Exception {
        UserSessionKey key = loginWithAdmin();
        HashMap<String, String> requestBody = new HashMap<>();
        //Here I'm checking the admin user's own
        requestBody.put("idToFind", Integer.toString(key.getID()));
        requestBody.put("key", key.sessionKey);
        requestBody.put("keyId", Integer.toString(key.getID()));
        ServerRequest<UserPrivilege[]> request = new ServerRequest<>(RequestType.USER, "get privileges", requestBody);
        ServerResponse<UserPrivilege[]> response = request.getResponse();
        assertEquals("ok", response.status());
        //privileges can't be empty
        assertNotEquals(new UserPrivilege[]{}, response.body());
    }

    @Test
    @Order(6)
    void checkAddPermission() throws Exception {
        UserSessionKey key = loginWithAdmin();
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("newPrivilege", "EditAllBillboards");
        requestBody.put("idToFind", Integer.toString(key.getID()));
        requestBody.put("key", key.sessionKey);
        requestBody.put("keyId", Integer.toString(key.getID()));
        ServerRequest<String> request = new ServerRequest<>(RequestType.USER, "add privilege", requestBody);
        ServerResponse<String> response = request.getResponse();
        assertEquals("ok", response.status());
    }

    @Test
    @Order(5)
    void checkRemovePermission() throws Exception {
        UserSessionKey key = loginWithAdmin();
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("privilegeToRemove", "EditAllBillboards");
        requestBody.put("idToFind", Integer.toString(key.getID()));
        requestBody.put("key", key.sessionKey);
        requestBody.put("keyId", Integer.toString(key.getID()));
        ServerRequest<String> request = new ServerRequest<>(RequestType.USER, "remove privilege", requestBody);
        ServerResponse<String> response = request.getResponse();
        assertEquals("ok", response.status());
    }

    //Always have this test as the last one, since it changes the password of admin user
    @Test
    @Order(9)
    void checkChangePassword() throws Exception {
        UserSessionKey key = loginWithAdmin();
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("idToFind", Integer.toString(key.getID()));
        requestBody.put("newPassword", "b\u0083¤$L\u0005\u0017SÉ(ÿÏ5\u008A!¬\u009E¡¥Î?ÊM½Òë9góa¯¯R¬ÊÀ\u0007\u001F\u0005\u0019ÛíG\u0086û\u0011Õ^úÔÃ.¸\u0086\u0088Çd_I\u00819Kwæ");
        requestBody.put("key", key.sessionKey);
        requestBody.put("keyId", Integer.toString(key.getID()));
        ServerRequest<String> request = new ServerRequest<>(RequestType.USER, "change password", requestBody);
        ServerResponse<String> response = request.getResponse();

        assertEquals("ok", response.status());

    }

    @Test
    @Order(8)
    void checkDeleteUser() throws Exception {
        UserSessionKey key = loginWithAdmin();
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("idToFind", "555");
        requestBody.put("key", key.sessionKey);
        requestBody.put("keyId", Integer.toString(key.getID()));
        ServerRequest<String> request = new ServerRequest<>(RequestType.USER, "delete user", requestBody);
        ServerResponse<String> response = request.getResponse();
        assertEquals("ok", response.status());
    }

    @Test
    @Order(7)
    void checkGetAllUsers() throws Exception {
        UserSessionKey key = loginWithAdmin();
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("key", key.sessionKey);
        requestBody.put("keyId", Integer.toString(key.getID()));
        ServerRequest<User[]> request = new ServerRequest<>(RequestType.USER, "list users", requestBody);
        ServerResponse<User[]> response = request.getResponse();
        assertEquals("ok", response.status());
        assertEquals(2, response.body().length);
        System.out.println(response.body()[0].getID());
        System.out.println(response.body()[1].getID());


    }
}
