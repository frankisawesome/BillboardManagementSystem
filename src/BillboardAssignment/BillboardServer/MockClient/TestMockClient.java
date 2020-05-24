package BillboardAssignment.BillboardServer.MockClient;

import BillboardAssignment.BillboardServer.BillboardServer.RequestType;
import BillboardAssignment.BillboardServer.BillboardServer.ServerRequest;
import BillboardAssignment.BillboardServer.BillboardServer.ServerResponse;

/**
 * A collection of mains that demonstrate the use of the server
 */
public class TestMockClient {
    /**
     * simple demo of sending a test request to the server, try this out, and take a look at the ServerRequest and ServerResponse classes
     * Make sure you have the server running - run the main method in BillboardServer
     * @param args
     */
    public static void main(String[] args) {
        //this would be your requests, for example ServerRequest<User> request = new ...(RequestType.USER, "Login");
        ServerRequest<String> myRequest = new ServerRequest<String>(RequestType.TEST, "Test Message");
        //you should always do a try catch when running the getResponse() method
        try {
            ServerResponse<String> response = myRequest.getResponse();
            //this would catch errors such as bad request - basically something wrong in your request
            //the response.status() string should always be "ok" if the request was successful
            if (response.status().equals("ok")) {
                System.out.println(response.body());
            } else {
                System.out.println("Request failed - status: " + response.status());
            }
        }
        //this would catch errors such as server not online - basically server side failures
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
