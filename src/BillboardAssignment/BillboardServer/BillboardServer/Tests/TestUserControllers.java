package BillboardAssignment.BillboardServer.BillboardServer.Tests;

import BillboardAssignment.BillboardServer.BillboardServer.BillboardServer;
import BillboardAssignment.BillboardServer.BillboardServer.RequestType;
import BillboardAssignment.BillboardServer.BillboardServer.ServerRequest;
import BillboardAssignment.BillboardServer.BillboardServer.ServerResponse;
import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.UserSessionKey;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;

public class TestUserControllers {
    UserSessionKey loginWithAdmin() throws Exception {
        HashMap<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("id", "69420");
        requestBody.put("password", "pwd");
        ServerRequest<UserSessionKey> request = new ServerRequest<UserSessionKey>(RequestType.USER, "login", requestBody);
        ServerResponse<UserSessionKey> response = request.getResponse();
        return response.body();
    }

    Boolean logout(UserSessionKey key) throws Exception {
        HashMap<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("key", key.sessionKey);
        requestBody.put("id", Integer.toString(key.getID()));
        ServerRequest<Boolean> request = new ServerRequest<Boolean>(RequestType.USER, "logout", requestBody);
        ServerResponse<Boolean> response = request.getResponse();
        return response.body();
    }

    @Test
    void checkLogin() throws Exception {
        UserSessionKey key = loginWithAdmin();
        assertNotEquals("",key.sessionKey);
    }

    @Test
    void checkLogOut() throws Exception {
        UserSessionKey key = loginWithAdmin();
        boolean logoutSuccess = logout(key);
        assertEquals(true, logoutSuccess);
    }

    @Test
    void checkCreateUser() throws Exception {
        UserSessionKey key = loginWithAdmin();
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("currentUserId", Integer.toString(key.getID()));
        requestBody.put("newUserId", "12345");
        requestBody.put("password", "newpass");
        requestBody.put("key", key.sessionKey);
        ServerRequest<UserSessionKey> request = new ServerRequest<UserSessionKey>(RequestType.USER, "create", requestBody);
        ServerResponse response = request.getResponse();
        assertEquals("ok", response.status());
    }
}
