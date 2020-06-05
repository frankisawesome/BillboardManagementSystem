package BillboardAssignment.BillboardServer.MockClient;

import BillboardAssignment.BillboardServer.Server.RequestType;
import BillboardAssignment.BillboardServer.Server.ServerRequest;
import BillboardAssignment.BillboardServer.Server.ServerResponse;
import BillboardAssignment.BillboardServer.Services.Authentication.UserSessionKey;

import java.util.HashMap;

public class LoginMockClient {
    public static void main(String[] args) {
        HashMap<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("id", "69420");
        requestBody.put("password", "pwd");
        ServerRequest<UserSessionKey> request = new ServerRequest<UserSessionKey>(RequestType.USER, "login", requestBody);
        try {
            ServerResponse<UserSessionKey> response = request.getResponse();
            if (response.status().equals("ok")) {
                System.out.println("The session key is: " + response.body().sessionKey);
            } else {
                System.out.println("Request failed, message: " + response.status());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
