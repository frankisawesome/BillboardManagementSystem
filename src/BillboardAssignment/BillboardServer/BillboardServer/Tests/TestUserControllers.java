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
    UserSessionKey login() throws Exception {
        HashMap<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("id", "69420");
        requestBody.put("password", "pwd");
        ServerRequest<UserSessionKey> request = new ServerRequest<UserSessionKey>(RequestType.USER, "login", requestBody);
        ServerResponse<UserSessionKey> response = request.getResponse();
        return response.body();
    }

    boolean logout() throws Exception {
        UserSessionKey key = login();
        HashMap<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("key", key);
        requestBody.put("password", "pwd");
    }
    @Test
    void checkLogin() throws Exception {
        UserSessionKey key = login();
        //response should be ok, and the body should be a non empty string (token)
        assertNotEquals("",key.sessionKey);
    }

    @Test
    void checkLogOut() throws Exception {
        UserSessionKey key = login();
        HashMap<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("key", );
        requestBody.put("password", "pwd");
        ServerRequest<UserSessionKey> request = new ServerRequest<UserSessionKey>(RequestType.USER, "login", requestBody);
        ServerResponse<UserSessionKey> response = request.getResponse();
    }

    @Test
    void checkCreateUser() throws Exception {
        HashMap<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("id", "69420");
        requestBody.put("password", "pwd");
        ServerRequest<UserSessionKey> request = new ServerRequest<UserSessionKey>(RequestType.USER, "login", requestBody);
        ServerResponse<UserSessionKey> response = request.getResponse();
        UserSessionKey sessionKey = response.body();
        ServerRequest<UserSessionKey> request = new ServerRequest<UserSessionKey>(RequestType.USER, "login", requestBody);
    }
}
