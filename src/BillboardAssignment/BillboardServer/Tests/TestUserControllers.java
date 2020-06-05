package BillboardAssignment.BillboardServer.Tests;

import BillboardAssignment.BillboardServer.Server.RequestType;
import BillboardAssignment.BillboardServer.Server.ServerRequest;
import BillboardAssignment.BillboardServer.Server.ServerResponse;
import BillboardAssignment.BillboardServer.Server.UserData;
import BillboardAssignment.BillboardServer.Services.Authentication.UserSessionKey;
import BillboardAssignment.BillboardServer.Services.User.UserPrivilege;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.HashMap;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for all user related controllers, i.e RequestType.USER
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestUserControllers {
    /*
    Helper method to get key with admin privileges
     */
    public static UserSessionKey loginWithAdmin() throws Exception {
        HashMap<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("username", "admin");
        requestBody.put("password", "b\u0083¤$L\u0005\u0017SÉ(ÿÏ5\u008A!¬\u009E¡¥Î?ÊM½Òë9góa¯¯R¬ÊÀ\u0007\u001F\u0005\u0019ÛíG\u0086û\u0011Õ^úÔÃ.¸\u0086\u0088Çd_I\u00819Kwæ");
        ServerRequest<UserSessionKey> request = new ServerRequest<UserSessionKey>(RequestType.USER, "login", requestBody);
        ServerResponse<UserSessionKey> response = request.getResponse();
        return response.body();
    }

    public static HashMap<String, String> requestBodyWithKey() throws Exception {
        UserSessionKey key = loginWithAdmin();
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("keyId", Integer.toString(key.getID()));
        requestBody.put("key", key.sessionKey);
        return requestBody;
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
        requestBody.put("newUserName", "myuser");
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
    @Order(8)
    void checkChangePassword() throws Exception {
        UserSessionKey key = loginWithAdmin();
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("idToFind", "69420");
        requestBody.put("newPassword", "b\u0083¤$L\u0005\u0017SÉ(ÿÏ5\u008A!¬\u009E¡¥Î?ÊM½Òë9góa¯¯R¬ÊÀ\u0007\u001F\u0005\u0019ÛíG\u0086û\u0011Õ^úÔÃ.¸\u0086\u0088Çd_I\u00819Kwæ");
        requestBody.put("key", key.sessionKey);
        requestBody.put("keyId", Integer.toString(key.getID()));
        ServerRequest<String> request = new ServerRequest<>(RequestType.USER, "change password", requestBody);
        ServerResponse<String> response = request.getResponse();

        assertEquals("ok", response.status());

    }

    @Test
    @Order(9)
    void checkDeleteUser() throws Exception {
        UserSessionKey key = loginWithAdmin();
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("idToFind", "0");
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
        ServerRequest<UserData[]> request = new ServerRequest<>(RequestType.USER, "list users", requestBody);
        ServerResponse<UserData[]> response = request.getResponse();
        assertEquals("ok", response.status());
        assertEquals(2, response.body().length);
        System.out.println(response.body()[0].id);
        System.out.println(response.body()[1].id);


    }
}
