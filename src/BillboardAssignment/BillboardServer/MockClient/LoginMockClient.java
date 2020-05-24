package BillboardAssignment.BillboardServer.MockClient;

import BillboardAssignment.BillboardServer.BillboardServer.RequestType;
import BillboardAssignment.BillboardServer.BillboardServer.ServerRequest;
import BillboardAssignment.BillboardServer.BillboardServer.ServerResponse;
import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.UserSessionKey;

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
